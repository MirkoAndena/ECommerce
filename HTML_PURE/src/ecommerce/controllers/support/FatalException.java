package ecommerce.controllers.support;

public class FatalException extends Exception {
	private static final long serialVersionUID = 1L;

	public FatalException(String message) {
		super(message);
	}

}
