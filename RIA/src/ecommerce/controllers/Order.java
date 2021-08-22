package ecommerce.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
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
import ecommerce.frontendDto.ExposedOrder;
import ecommerce.utils.FileReader;
import ecommerce.utils.Json;

@WebServlet("/Order")
@MultipartConfig
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
		
		List<ExposedOrder> orderList = orderDao.getOrders(userDto, articleDao, sellerDao);
		System.out.println("orders: " + orderList.size());
		
		// Visualizzazione del carattere euro (€)
		response.setCharacterEncoding("UTF-8");
		
		Json json = Json.build()
				.add("orders", orderList)
				.add("ordersTemplate", FileReader.read(this, "order_template.html"))
				.add("order_purchase_template", FileReader.read(this, "order_purchase_template.html"));
			
		super.sendResult(response, json);
	}

	@Override
	public void Post(HttpServletRequest request, HttpServletResponse response, int user) throws ServletException, IOException, FatalException {
		// TODO Auto-generated method stub
	}
}
