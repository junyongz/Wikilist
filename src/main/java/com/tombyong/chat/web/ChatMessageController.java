package com.tombyong.chat.web;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.tombyong.chat.Message;
import com.tombyong.database.mongodb.DomainObject;

@Controller
@RequestMapping("/chat")
public class ChatMessageController {

	@Autowired
	private Mongo mongoDb;

	private String dbName = "chat";

	@SuppressWarnings("unchecked")
	@RequestMapping("/")
	public String main(Principal user, Model model, HttpServletRequest req) {
		Set<Principal> principals = (Set<Principal>) req.getSession().getServletContext().getAttribute("users");
		List<Principal> users = new ArrayList<Principal>(principals);
		users.remove(user);
		model.addAttribute("users", users);
		return "/chat/room";
	}

	@RequestMapping(value = "/post", headers = "Accept=application/json")
	@ResponseBody
	public String postMessage(Principal user, @RequestParam String toUser, @RequestParam String contents, Locale locale) {
		DomainObject<Message> o = DomainObject.forDomain(new Message(user.getName(), toUser, contents, Calendar
				.getInstance(locale).getTimeInMillis()), this.mongoDb.getDB(dbName));
		o.save().fillId("id");

		return o.id();
	}

	@RequestMapping(value = "/list", headers = "Accept=application/json")
	@ResponseBody
	public List<Message> retrieveMessagesSince(Principal user, @RequestParam String fromUser, @RequestParam long since) {
		DBObject conditions = new BasicDBObject();
		conditions.put("timestamp", new BasicDBObject("$gt", since));
		conditions.put("fromUser", fromUser);
		conditions.put("toUser", user.getName());

		List<DomainObject<Message>> messages = DomainObject.list("message", Message.class, conditions,
				this.mongoDb.getDB(dbName));
		List<Message> messageList = new ArrayList<Message>();
		for (DomainObject<Message> message : messages) {
			messageList.add(message.fillId("id").get());
		}
		return messageList;
	}
}
