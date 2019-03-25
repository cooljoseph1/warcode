package exceptions;

public class MineException extends GameException {

	private static final long serialVersionUID = -8906704528017876875L;

	public MineException() {

	}

	public MineException(String message) {
		super(message);
	}

	public MineException(Throwable cause) {
		super(cause);
	}

	public MineException(String message, Throwable cause) {
		super(message, cause);
	}

}
