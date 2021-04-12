package ecommerce.controllers;

import javax.servlet.ServletContext;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

public class Utils {
	public static TemplateEngine initTemplateEngine(ServletContext servletContext) {
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		TemplateEngine engine = new TemplateEngine();
		engine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
		return engine;
	}
}
