package ecommerce.controllers;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.sql.Connection;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;

import ecommerce.SessionKeys;
import ecommerce.database.ConnectionBuilder;

public abstract class BaseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected ServletContext context;
	protected Connection connection;
	protected TemplateEngine templateEngine;
    
    public BaseServlet() {
        super();
    }

    @Override
	public void init() throws ServletException {
    	ServletContext context = getServletContext();
		connection = ConnectionBuilder.create(context);
		templateEngine = Utils.initTemplateEngine(context);
		OnInit();
	}
    
    protected abstract void OnInit();
    
    @Override
    public void destroy() {
    	ConnectionBuilder.close(connection);
    }
    
    protected void redirectIfNotLogged(HttpServletRequest request, HttpServletResponse response) {
    	if (!isUserAuthenticated(request)) {
    		try {
				response.sendRedirect(request.getContextPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }
    
    private boolean isUserAuthenticated(HttpServletRequest request) {
    	try {
	    	HttpSession session = request.getSession(false);
	    	Integer userId = (Integer) session.getAttribute(SessionKeys.User.toString());
	    	return userId != null;
    	} catch (Exception e) {
    		return false;
    	}
    }
}
