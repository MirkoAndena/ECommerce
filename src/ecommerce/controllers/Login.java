package ecommerce.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import ecommerce.SessionKeys;
import ecommerce.database.ConnectionBuilder;
import ecommerce.database.dao.UserDao;
import ecommerce.hashing.SHA;

@WebServlet("/Login")
public class Login extends BaseServlet {
	private static final long serialVersionUID = 1L;
	
	private UserDao userDao;
       
    public Login() {
        super();
    }
    
    @Override
	protected void OnInit() {
    	this.userDao = new UserDao(super.connection, new SHA());
	}
    
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	// Necessario per mettere message null, cosi nella pagina non viene mostrato l'alert
		WebContext context = new WebContext(request, response, getServletContext(), Locale.ITALY);
		templateEngine.process("/login.html", context, response.getWriter());
	}
    
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		Integer userId = authenticate(username, password);
		if (userId != null) {
			HttpSession session = request.getSession(true);
			session.setAttribute(SessionKeys.User.toString(), userId);
			response.sendRedirect(getServletContext().getContextPath() + "/Home");
		} else {
			WebContext context = new WebContext(request, response, getServletContext(), Locale.ITALY);
			context.setVariable("message", "Username o Password errati");
			templateEngine.process("/login.html", context, response.getWriter());
		}
	}
	
	private Integer authenticate(String username, String password) {
		if (username == null || username == "" || password == null || password == "") return null;
		return userDao.getUserIdFromLogin(username, password);
	}
}
