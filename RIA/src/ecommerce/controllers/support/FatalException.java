package ecommerce.controllers.support;

public class FatalException extends Exception {
	private static final long serialVersionUID = 1L;

	public String msg;
	public FatalException(String message) {
		super();
		msg = message;
	}

}
