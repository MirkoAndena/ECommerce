package ecommerce.utils;

public enum ClientPages {
	Login("Login"),
	Home("Home"),
	Risultati("Search"),
	Carrello("Cart"),
	Ordini("Order");
	
	public String servlet;
	private ClientPages(String servlet) {
		this.servlet = servlet;
	}
}
