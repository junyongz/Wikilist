package com.tombyong.wikilist.project.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.tombyong.wikilist.project.Item;
import com.tombyong.wikilist.project.Project;
import com.tombyong.wikilist.project.ProjectService;

@Controller
@RequestMapping("/items")
@SessionAttributes(value = "project")
public class ItemController {

	@InitBinder("item")
	protected void initBinder(WebDataBinder binder) {
		binder.setRequiredFields("name");
		binder.setMessageCodesResolver(this.messageCodesResolver);
	}

	@Autowired
	private ProjectService projectService;

	@Autowired
	private MessageCodesResolver messageCodesResolver;

	@RequestMapping("/new")
	public String newEmptyItem(Model model) {
		model.addAttribute("item", new Item());
		return "/item/create";
	}

	@RequestMapping("/edit/{itemId}")
	public String editItem(@PathVariable String itemId, Model model) {
		Item editing = null;
		Project project = (Project) model.asMap().get("project");
		for (Item item : project.getItems()) {
			if (itemId.equals(item.getId())) {
				editing = item;
				break;
			}
		}

		model.addAttribute("item", editing);

		return "/item/create";
	}

	@RequestMapping("/complete/{itemId}")
	public String completeItem(@PathVariable String itemId, Model model) {
		Item completing = null;
		Project project = (Project) model.asMap().get("project");
		for (Item item : project.getItems()) {
			if (itemId.equals(item.getId())) {
				completing = item;
				break;
			}
		}
		completing.setDone(true);
		projectService.addOrUpdateItem(project, completing);

		model.addAttribute("items", project.getItems());

		return "redirect:/items/";
	}

	@RequestMapping("/save")
	public String saveItem(@ModelAttribute("item") Item item, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "/item/create";
		}

		Project project = (Project) model.asMap().get("project");

		projectService.addOrUpdateItem(project, item);
		model.addAttribute("items", project.getItems());

		return "redirect:/items/";
	}

	@RequestMapping("/remove/{itemId}")
	public String removeItem(@PathVariable String itemId, Model model) {
		Project project = (Project) model.asMap().get("project");

		for (Item item : project.getItems()) {
			if (itemId.equals(item.getId())) {
				projectService.removeItem(project, item);
				break;
			}
		}

		return "redirect:/items/";
	}

	@RequestMapping("/p")
	public String backToProject(SessionStatus sessionStatus) {
		sessionStatus.setComplete();
		return "redirect:/projects/";
	}

	@RequestMapping("/")
	public String listItems(Model model) {
		Project project = (Project) model.asMap().get("project");
		if (project != null) {
			model.addAttribute("items", project.getItems());
			return "/item/list";
		}

		return "redirect:/projects/";
	}

	@RequestMapping("/{projectId}")
	public String listItems(Model model, @PathVariable String projectId) {
		Project project = projectService.getProjectByPrimaryKey(projectId);
		model.addAttribute("project", project);
		model.addAttribute("items", project.getItems());

		return "/item/list";
	}

	@ExceptionHandler(HttpSessionRequiredException.class)
	public String handleNoSessionProjectException() {
		return "redirect:/projects/";
	}

}
