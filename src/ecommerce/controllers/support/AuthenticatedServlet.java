package ecommerce.controllers.support;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AuthenticatedServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
	
	public AuthenticatedServlet() {
		super();
	}
    
    public void get(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, FatalException {
		Integer user = getUserId(request);
    	if (user == null) redirectToLogin(request, response);
		else Get(request, response, user);
    }
    
    public abstract void Get(HttpServletRequest request, HttpServletResponse response, int user) throws ServletException, IOException, FatalException;
	
    public void post(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, FatalException {
    	Integer user = getUserId(request);
    	if (user == null) redirectToLogin(request, response);
		else Post(request, response, user);
    }

    public abstract void Post(HttpServletRequest request, HttpServletResponse response, int user) throws ServletException, IOException, FatalException;
}
