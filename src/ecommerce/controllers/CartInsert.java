package ecommerce.controllers;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ecommerce.SessionContext;
import ecommerce.database.dao.ArticleDao;
import ecommerce.database.dto.Article;
import ecommerce.database.dto.Cart;
import ecommerce.database.dto.Seller;
import ecommerce.utils.Pair;

@WebServlet("/CartInsert")
public class CartInsert extends BaseServlet {
	private static final long serialVersionUID = 1L;
	private ArticleDao articleDao;
       
    public CartInsert() {
        super();
    }
    
	@Override
	protected void OnInit() {

	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (super.redirectIfNotLogged(request, response)) return;

		if (this.articleDao == null)
			this.articleDao = new ArticleDao(connection, super.getUserId(request));
		
		int articleId = -1, sellerId = -1, quantity = -1;
		float price = -1;
		try {
			articleId = Integer.parseInt(request.getParameter("article"));
			sellerId = Integer.parseInt(request.getParameter("seller"));
			price = Float.parseFloat(request.getParameter("price"));
			quantity = Integer.parseInt(request.getParameter("quantity"));
		} catch (NumberFormatException e) {
			System.err.println("Integer value not parsable");
		}
	
		Pair<Article, Seller> elements = articleDao.getArticleAndSellerById(articleId, sellerId);
		if (elements == null) System.err.println("Articolo e/o Seller non trovati nel DB");
		Cart cart = SessionContext.getInstance(super.getUserId(request)).getCart();
		cart.add(elements.second, elements.first, quantity, price);
		
		// FORWARD TO CART PAGE
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/Cart");
	    dispatcher.forward(request, response);
	}
}
