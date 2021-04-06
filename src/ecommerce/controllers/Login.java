package ecommerce.controllers;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ecommerce.SessionKeys;
import ecommerce.database.ConnectionBuilder;
import ecommerce.database.dao.UserDao;
import ecommerce.hashing.SHA;

@WebServlet("/Login")
public class Login extends HttpServlet {
	

	private static final long serialVersionUID = 1L;
	
	private Connection connection;
	private UserDao userDao;
       
    public Login() {
        super();
    }
    
    @Override
	public void init() throws ServletException {
		connection = ConnectionBuilder.create(getServletContext());
		userDao = new UserDao(connection, new SHA());
	}
    
    @Override
    public void destroy() {
    	ConnectionBuilder.close(connection);
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO devo mandare giu la pagine login.html
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		if (authenticate(username, password)) {
			HttpSession session = request.getSession(true);
			session.setAttribute(SessionKeys.LOGGED.toString(), true);
			response.sendRedirect("/Home");
		} else {
			response.sendError(0, "Username o password errati");
		}
	}
	
	private boolean authenticate(String username, String password) {
		if (username == null || password == null)
			return false;
		return userDao.checkEmailAndPassword(username, password);
	}
}
