package ecommerce.controllers;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ecommerce.SessionContext;
import ecommerce.controllers.support.AuthenticatedServlet;
import ecommerce.controllers.support.FatalException;
import ecommerce.database.dao.UserDao;
import ecommerce.database.dao.OrderDao;
import ecommerce.database.dto.Order;
import ecommerce.database.dto.User;
import ecommerce.frontendDto.SellerCart;
import ecommerce.utils.ClientPages;
import ecommerce.utils.ListUtils;

@WebServlet("/OrderInsert")
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
		
		// Ottenimento id del carrello ordinato
		int cartId = getCartId(request);		
		
		// Recupero dei dati del carrello ordinato
		SellerCart sellerCart = ListUtils.find(SessionContext.getInstance(user).getCart().sellerCarts, cart -> cart.id == cartId);
		User userDto = userDao.getUserById(user);
		
		if (userDto == null) throw new FatalException(ClientPages.Ordini, "Nessun utente trovato con id " + user);
		
		// Creazione ordine
		Order order = new Order(userDto, sellerCart);
		boolean stored = orderDao.storeOrder(order, sellerCart.purchases);
		
		// Eliminazione dal carrello
		if (stored) SessionContext.getInstance(user).getCart().sellerCarts.remove(sellerCart);
		else throw new FatalException(ClientPages.Ordini, "Impossibile salvare l'ordine");
		
		// REDIRECT TO CART PAGE
		response.sendRedirect(getServletContext().getContextPath() + "/Cart");
	}
	
	private int getCartId(HttpServletRequest request) throws FatalException {
		int cartId = -1;
		try {
			cartId = Integer.parseInt(request.getParameter("cartId"));
		} catch (NumberFormatException e) {
			throw new FatalException(ClientPages.Ordini, "Non è stato passato un valore corrispondente ad un id");
		}
		return cartId;
	}
}
