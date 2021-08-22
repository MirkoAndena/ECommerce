package ecommerce.controllers;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ecommerce.SessionContext;
import ecommerce.SessionKeys;
import ecommerce.controllers.support.BaseServlet;
import ecommerce.controllers.support.FatalException;
import ecommerce.database.dao.UserDao;
import ecommerce.hashing.SHA;
import ecommerce.utils.Json;

@WebServlet("/Login")
@MultipartConfig
public class Login extends BaseServlet {
	private static final long serialVersionUID = 1L;
	
	private UserDao userDao;
       
    public Login() {
        super();
    }
    
    @Override
	protected void OnInit() {
    	this.userDao = new UserDao(super.connection);
	}
	
	// Autenticazione, in caso positivo ritorno dell id utente
	private Integer authenticate(String username, String password) {
		if (username == null || username == "" || password == null || password == "") return null;
		return userDao.getUserIdFromLogin(username, password, new SHA());
	}

	@Override
	public void get(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, FatalException {
		if (super.getUserId(request) != null) {
			// Utente già autenticato
			response.sendRedirect(getServletContext().getContextPath() + "/Index");
    	}
		else
			request.getRequestDispatcher("/login.html").forward(request, response);
	}

	@Override
	public void post(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, FatalException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		Integer userId = authenticate(username, password);
	
		if (userId != null) {
			SessionContext.removeSessionContext(userId);
			HttpSession session = request.getSession(true);
			session.setAttribute(SessionKeys.User.toString(), userId);
		} 
		
		Json json = Json.build().add("logged", userId != null);
		super.sendResult(response, json);
	}
}
