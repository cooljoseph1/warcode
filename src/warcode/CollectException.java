package warcode;

public class CollectException extends GameException {

	private static final long serialVersionUID = -8906704528017876875L;

	public CollectException() {

	}

	public CollectException(String message) {
		super(message);
	}

	public CollectException(Throwable cause) {
		super(cause);
	}

	public CollectException(String message, Throwable cause) {
		super(message, cause);
	}

}
