package ecommerce_ria.controllers;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import ecommerce_ria.controllers.support.AuthenticatedServlet;
import ecommerce_ria.controllers.support.FatalException;
import ecommerce_ria.database.dao.OrderDao;
import ecommerce_ria.database.dao.UserDao;
import ecommerce_ria.database.dto.Order;
import ecommerce_ria.database.dto.User;
import ecommerce_ria.frontendDto.SellerCart;
import ecommerce_ria.utils.Json;

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
