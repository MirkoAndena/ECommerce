package ecommerce.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ecommerce.SessionContext;
import ecommerce.database.dao.ArticleDao;
import ecommerce.frontendDto.ExposedArticle;

@WebServlet("/Cart")
public class Cart extends BaseServlet {
	private static final long serialVersionUID = 1L;
       
    public Cart() {
        super();
    }
    
	@Override
	protected void OnInit() {

	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (super.redirectIfNotLogged(request, response)) return;
		
		ecommerce.database.dto.Cart cart = SessionContext.getInstance(super.getUserId(request)).getCart();
		
		// Visualizzazione del carattere euro (€)
		response.setCharacterEncoding("UTF-8");
		
		super.getThymeleaf().init(request, response)
		.setVariable("cart", cart)
		.process("/cart.html");
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Unused
	}
}
