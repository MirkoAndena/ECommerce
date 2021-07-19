package ecommerce.controllers;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ecommerce.controllers.support.AuthenticatedServlet;
import ecommerce.controllers.support.FatalException;
import ecommerce.database.dao.ArticleDao;
import ecommerce.database.dao.OrderDao;
import ecommerce.database.dao.SellerDao;
import ecommerce.database.dao.UserDao;
import ecommerce.database.dto.User;

@WebServlet("/Order")
public class Order extends AuthenticatedServlet {
	private static final long serialVersionUID = 1L;
	private OrderDao orderDao;
	private UserDao userDao;
	private ArticleDao articleDao;
	private SellerDao sellerDao;
       
    public Order() {
        super();
    }
	
	 @Override
	protected void OnInit() {
		 this.orderDao = new OrderDao(connection);
		 this.userDao = new UserDao(connection);
		this.articleDao = new ArticleDao(connection);
		this.sellerDao = new SellerDao(connection);
	}

	@Override
	public void Get(HttpServletRequest request, HttpServletResponse response, int user) throws ServletException, IOException, FatalException {	
		
		User userDto = userDao.getUserById(user);
		
		// Visualizzazione del carattere euro (€)
		response.setCharacterEncoding("UTF-8");
		
		super.getThymeleaf().init(request, response)
		.setVariable("orders", orderDao.getOrders(userDto, articleDao, sellerDao))
		.process("/home.html");
	}

	@Override
	public void Post(HttpServletRequest request, HttpServletResponse response, int user) throws ServletException, IOException, FatalException {
		// TODO Auto-generated method stub
	}
}
