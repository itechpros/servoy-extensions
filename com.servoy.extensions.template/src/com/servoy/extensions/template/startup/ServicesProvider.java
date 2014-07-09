package com.servoy.extensions.template.startup;

import java.util.Set;

import org.apache.tomcat.starter.ClassServiceFactory;
import org.apache.tomcat.starter.IServicesProvider;

import com.servoy.extensions.template.WebdavServlet;

public class ServicesProvider implements IServicesProvider {

	public void registerServices() {
		Activator.getContext().registerService(WebdavServlet.class.getName(), new ClassServiceFactory(WebdavServlet.class), null);
	}

	public Set<Class<?>> getAnnotatedClasses(String context) {
		return null;
	}

}