package ecommerce.controllers;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ecommerce.SessionContext;
import ecommerce.controllers.support.AuthenticatedServlet;
import ecommerce.controllers.support.FatalException;
import ecommerce.database.dao.ArticleDao;
import ecommerce.database.dao.SellerDao;
import ecommerce.database.dto.Article;
import ecommerce.database.dto.Cart;
import ecommerce.database.dto.Seller;
import ecommerce.utils.ClientPages;
import ecommerce.utils.Json;
import ecommerce.utils.Pair;

@WebServlet("/CartInsert")
@MultipartConfig
public class CartInsert extends AuthenticatedServlet {
	private static final long serialVersionUID = 1L;
	private ArticleDao articleDao;
	private SellerDao sellerDao;
       
    public CartInsert() {
        super();
    }
    
	@Override
	protected void OnInit() {
		this.articleDao = new ArticleDao(connection);
		this.sellerDao = new SellerDao(connection);
	}

	@Override
	public void Get(HttpServletRequest request, HttpServletResponse response, int user) throws ServletException, IOException, FatalException {
		// TODO Auto-generated method stub
	}

	@Override
	public void Post(HttpServletRequest request, HttpServletResponse response, int user) throws ServletException, IOException, FatalException {
		int articleId = -1, sellerId = -1, quantity = -1;
		float price = -1;
		try {
			articleId = Integer.parseInt(request.getParameter("article"));
			sellerId = Integer.parseInt(request.getParameter("seller"));
			price = Float.parseFloat(request.getParameter("price"));
			quantity = Integer.parseInt(request.getParameter("quantity"));
		} catch (NumberFormatException e) {
			throw new FatalException("Valori passati non corretti");
		}
		
		if (quantity <= 0) throw new FatalException("Quantità non corretta");
	
		Pair<Article, Seller> elements = articleDao.getArticleAndSellerById(sellerDao, articleId, sellerId);
		if (elements == null) throw new FatalException("Articolo e/o Seller non trovati nel DB");
		Cart cart = SessionContext.getInstance(super.getUserId(request)).getCart();
		cart.add(SessionContext.getInstance(user), elements.second, elements.first, quantity, price);
		
		Json json = Json.build(ClientPages.Carrello)
				.add("inserted", true);
			
		super.sendResult(response, json);
	}
}
