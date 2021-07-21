package ecommerce;

import java.util.HashMap;
import java.util.Map;

import ecommerce.database.dto.Cart;

// Dati (per utente) salvati sul server, possono essere cancellati con politiche diverse ad esempio quando c'è un nuovo accesso
public class SessionContext {
	private static Map<Integer, SessionContext> instance;
	
	public static SessionContext getInstance(Integer id) {
		if (instance == null)
			instance = new HashMap<Integer, SessionContext>();
		if (!instance.containsKey(id))
			instance.put(id, new SessionContext());
		return instance.get(id);
	}
	
	public static void removeSessionContext(Integer id) {
		if (instance != null && instance.containsKey(id))
			instance.remove(id);
	}
	
	// Context
	
	private Cart cart;
	private int lastCartId;
	
	private SessionContext() {
		cart = new Cart();
		lastCartId = 0;
	}
	
	public Cart getCart() {
		return cart;
	}
	
	public int newCartId() {
		lastCartId++;
		return lastCartId;
	}
}
