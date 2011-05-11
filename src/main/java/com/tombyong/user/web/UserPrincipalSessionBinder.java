package com.tombyong.user.web;

import java.security.Principal;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpSessionActivationListener;
import javax.servlet.http.HttpSessionEvent;

public class UserPrincipalSessionBinder implements HttpSessionActivationListener, java.io.Serializable {

	private static final long serialVersionUID = -3546614863281385232L;

	private Principal principal;

	UserPrincipalSessionBinder(Principal principal) {
		this.principal = principal;
	}

	public Principal getPrincipal() {
		return this.principal;
	}

	@Override
	public void sessionWillPassivate(HttpSessionEvent se) {
	}

	@SuppressWarnings("unchecked")
	@Override
	public void sessionDidActivate(HttpSessionEvent event) {
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
		principals.add(principal);
	}

}
