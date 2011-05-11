package com.tombyong.user.web;

import java.security.Principal;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

public class UserPrincipalsRegistryListener implements HttpSessionAttributeListener {

	private static final Logger logger = LoggerFactory.getLogger(UserPrincipalsRegistryListener.class);

	@SuppressWarnings("unchecked")
	@Override
	public void attributeAdded(HttpSessionBindingEvent event) {
		if (HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY.equals(event.getName())) {
			Set<Principal> principals = (Set<Principal>) event.getSession().getServletContext().getAttribute("users");
			if (principals == null) {
				principals = new TreeSet<Principal>(new Comparator<Principal>() {
					@Override
					public int compare(Principal o1, Principal o2) {
						return o1.getName().compareTo(o2.getName());
					}
				});
				event.getSession().getServletContext().setAttribute("users", principals);
			}
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			event.getSession().setAttribute("binder", new UserPrincipalSessionBinder(auth));
			principals.add(auth);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void attributeRemoved(HttpSessionBindingEvent event) {
		if (HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY.equals(event.getName())) {
			Set<Principal> principals = (Set<Principal>) event.getSession().getServletContext().getAttribute("users");
			if (principals == null) {
				logger.warn("There is no principals found in servlet context attribute name 'users'?!");
				return;
			}
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			principals.remove(auth);
			event.getSession().removeAttribute("binder");
		}

	}

	@Override
	public void attributeReplaced(HttpSessionBindingEvent se) {
	}

}
