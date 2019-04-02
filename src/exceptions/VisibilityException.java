package exceptions;

public class VisibilityException extends GameException {

	private static final long serialVersionUID = -8906704528017876875L;

	public VisibilityException() {

	}

	public VisibilityException(String message) {
		super(message);
	}

	public VisibilityException(Throwable cause) {
		super(cause);
	}

	public VisibilityException(String message, Throwable cause) {
		super(message, cause);
	}

}
