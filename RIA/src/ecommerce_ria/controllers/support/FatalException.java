package ecommerce_ria.controllers.support;

public class FatalException extends Exception {
	private static final long serialVersionUID = 1L;

	public String message;
	public FatalException(String message) {
		super();
		this.message = message;
	}

}
