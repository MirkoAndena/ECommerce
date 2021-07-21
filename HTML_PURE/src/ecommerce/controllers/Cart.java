package ecommerce.controllers;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ecommerce.SessionContext;
import ecommerce.controllers.support.AuthenticatedServlet;
import ecommerce.controllers.support.FatalException;

@WebServlet("/Cart")
public class Cart extends AuthenticatedServlet {
	private static final long serialVersionUID = 1L;
       
    public Cart() {
        super();
    }
    
	@Override
	protected void OnInit() {

	}	

	@Override
	public void Get(HttpServletRequest request, HttpServletResponse response, int user) throws ServletException, IOException, FatalException {
		
		ecommerce.database.dto.Cart cart = SessionContext.getInstance(user).getCart();
		
		// Visualizzazione del carattere euro (€)
		response.setCharacterEncoding("UTF-8");
		
		super.getThymeleaf().init(request, response)
		.setVariable("cart", cart)
		.process("/cart.html");
	}

	@Override
	public void Post(HttpServletRequest request, HttpServletResponse response, int user) throws ServletException, IOException, FatalException {
		// TODO Auto-generated method stub
	}
}
