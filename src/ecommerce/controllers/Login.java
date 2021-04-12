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
public class Login extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private Connection connection;
	private TemplateEngine templateEngine;
	private UserDao userDao;
       
    public Login() {
        super();
    }
    
    @Override
	public void init() throws ServletException {
    	ServletContext context = getServletContext();
		connection = ConnectionBuilder.create(context);
		templateEngine = Utils.initTemplateEngine(context);
		userDao = new UserDao(connection, new SHA());
	}
    
    @Override
    public void destroy() {
    	ConnectionBuilder.close(connection);
    }
	
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
