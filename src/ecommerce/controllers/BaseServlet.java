package ecommerce.controllers;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import ecommerce.SessionKeys;
import ecommerce.controllers.support.Thymeleaf;
import ecommerce.database.ConnectionBuilder;

public abstract class BaseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected ServletContext context;
	protected Connection connection;
	protected TemplateEngine templateEngine;
	private Thymeleaf thymeleaf;
    
    public BaseServlet() {
        super();
    }

    @Override
	public void init() throws ServletException {
    	context = getServletContext();
		connection = ConnectionBuilder.create(context);
		templateEngine = initTemplateEngine(context);
		OnInit();
	}
    
    private static TemplateEngine initTemplateEngine(ServletContext servletContext) {
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		templateResolver.setCharacterEncoding("UTF-8");
		TemplateEngine engine = new TemplateEngine();
		engine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
		return engine;
	}
    
    protected abstract void OnInit();
    
    @Override
    public void destroy() {
    	ConnectionBuilder.close(connection);
    }
    
    protected boolean redirectIfNotLogged(HttpServletRequest request, HttpServletResponse response) {
    	if (!isUserAuthenticated(request)) {
    		try {
				response.sendRedirect(request.getContextPath());
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	return false;
    }
    
    protected boolean isUserAuthenticated(HttpServletRequest request) {
    	try {
	    	return getUserId(request) != null;
    	} catch (Exception e) {
    		return false;
    	}
    }
    
    public Thymeleaf getThymeleaf() {
    	this.thymeleaf = new Thymeleaf(context, templateEngine);
    	return this.thymeleaf;
    }
    
    @SuppressWarnings("unchecked")
	public <T> T getAttribute(HttpServletRequest request, SessionKeys key) {	
    	try {
	    	HttpSession session = request.getSession(false);
	    	if (session == null) return null;    
	    	return (T) session.getAttribute(key.toString());
    	} catch (Exception e) {
    		e.printStackTrace();
    		return null;
    	}
    }
    
    public Integer getUserId(HttpServletRequest request) {
    	return getAttribute(request, SessionKeys.User);
    }
}
