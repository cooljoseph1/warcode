package exceptions;

public class GiveException extends GameException {

	private static final long serialVersionUID = -8906704528017876875L;

	public GiveException() {

	}

	public GiveException(String message) {
		super(message);
	}

	public GiveException(Throwable cause) {
		super(cause);
	}

	public GiveException(String message, Throwable cause) {
		super(message, cause);
	}

}
