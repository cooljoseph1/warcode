package exceptions;

public class GameException extends Exception {

	private static final long serialVersionUID = -4309489418536948607L;

	public GameException() {

	}

	public GameException(String message) {
		super(message);
	}

	public GameException(Throwable cause) {
		super(cause);
	}

	public GameException(String message, Throwable cause) {
		super(message, cause);
	}

}
