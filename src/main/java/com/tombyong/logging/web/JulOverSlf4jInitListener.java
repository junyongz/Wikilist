package com.tombyong.logging.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.bridge.SLF4JBridgeHandler;

/**
 * A servlet context listener to install {@link SLF4JBridgeHandler} upon startup
 * and uninstall after shutdown.
 * 
 * @author Chong Jun Yong
 * @see org.slf4j.bridge.SLF4JBridgeHandler
 */
public class JulOverSlf4jInitListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		sce.getServletContext().log("Installing SLF4J Bridge Handler");
		SLF4JBridgeHandler.install();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		sce.getServletContext().log("Uninstalling SLF4J Bridge Handler");
		SLF4JBridgeHandler.uninstall();
	}

}
