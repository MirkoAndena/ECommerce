package ecommerce.controllers.support;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

public class Thymeleaf {
	private ServletContext servletContext;
	private TemplateEngine templateEngine;
	private WebContext webContext;
	private PrintWriter printWriter;
	
	public Thymeleaf(ServletContext servletContext, TemplateEngine templateEngine) {
		this.servletContext = servletContext;
		this.templateEngine = templateEngine;
	}
	
	public Thymeleaf init(HttpServletRequest request, HttpServletResponse response) throws IOException {
		this.printWriter = response.getWriter();
		this.webContext = new WebContext(request, response, servletContext, request.getLocale());
		return this;
	}
	
	public Thymeleaf setVariable(String name, Object value) {
		this.webContext.setVariable(name, value);
		return this;
	}
	
	public void process(String path) {
		this.templateEngine.process(path, webContext, printWriter);
	}
}