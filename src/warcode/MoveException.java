package warcode;

public class MoveException extends GameException {

	private static final long serialVersionUID = -8906704528017876875L;

	public MoveException() {

	}

	public MoveException(String message) {
		super(message);
	}

	public MoveException(Throwable cause) {
		super(cause);
	}

	public MoveException(String message, Throwable cause) {
		super(message, cause);
	}

}
