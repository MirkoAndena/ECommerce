package ecommerce.controllers.support;

import ecommerce.utils.ClientPages;

public class FatalException extends Exception {
	private static final long serialVersionUID = 1L;

	public ClientPages page;
	public String message;
	public FatalException(ClientPages page, String message) {
		super();
		this.page = page;
		this.message = message;
	}

}
