package ecommerce_ria.controllers;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ecommerce_ria.controllers.support.AuthenticatedServlet;
import ecommerce_ria.controllers.support.FatalException;

@WebServlet("/Index")
@MultipartConfig
public class Index extends AuthenticatedServlet {
	private static final long serialVersionUID = 1L;
       
    public Index() {
        super();
    }
    
	@Override
	protected void OnInit() {
	}

	@Override
	public void Get(HttpServletRequest request, HttpServletResponse response, int user) throws ServletException, IOException, FatalException {		
		
		// Visualizzazione del carattere euro (�)
		response.setCharacterEncoding("UTF-8");
		
		super.getThymeleaf().init(request, response)
		.process("/index.html");
	}

	@Override
	public void Post(HttpServletRequest request, HttpServletResponse response, int user) throws ServletException, IOException, FatalException {
		// TODO Auto-generated method stub
	}
}
