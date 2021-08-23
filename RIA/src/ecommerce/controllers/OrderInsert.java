package ecommerce.controllers;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import ecommerce.controllers.support.AuthenticatedServlet;
import ecommerce.controllers.support.FatalException;
import ecommerce.database.dao.UserDao;
import ecommerce.database.dao.OrderDao;
import ecommerce.database.dto.Order;
import ecommerce.database.dto.User;
import ecommerce.frontendDto.SellerCart;
import ecommerce.utils.Json;

@WebServlet("/OrderInsert")
@MultipartConfig
public class OrderInsert extends AuthenticatedServlet {
	private static final long serialVersionUID = 1L;
	private UserDao userDao;
	private OrderDao orderDao;
       
    public OrderInsert() {
        super();
    }
	
	 @Override
	protected void OnInit() {
    	this.userDao = new UserDao(super.connection);
		this.orderDao = new OrderDao(connection);
	}

	@Override
	public void Get(HttpServletRequest request, HttpServletResponse response, int user) throws ServletException, IOException, FatalException {
		// TODO Auto-generated method stub
	}

	@Override
	public void Post(HttpServletRequest request, HttpServletResponse response, int user) throws ServletException, IOException, FatalException {	
		
		User userDto = userDao.getUserById(user);
		
		// Recupero dei dati del carrello ordinato
		SellerCart sellerCart = buildSellerCartFromRequest(request);
		
		if (userDto == null) throw new FatalException("Nessun utente trovato con id " + user);
		
		// Creazione ordine
		Order order = new Order(userDto, sellerCart);
		boolean stored = orderDao.storeOrder(order, sellerCart.purchases);
		
		Json json = Json.build()
				.add("stored", stored);
			
		super.sendResult(response, json);
	}
	
	private SellerCart buildSellerCartFromRequest(HttpServletRequest request) throws FatalException {
		String json = request.getParameter("sellerCart");
		Gson gson = new Gson();
		return gson.fromJson(json, SellerCart.class);
	}
}
