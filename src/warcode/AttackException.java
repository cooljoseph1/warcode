package warcode;

public class AttackException extends GameException {

	private static final long serialVersionUID = -8906704528017876875L;

	public AttackException() {

	}

	public AttackException(String message) {
		super(message);
	}

	public AttackException(Throwable cause) {
		super(cause);
	}

	public AttackException(String message, Throwable cause) {
		super(message, cause);
	}

}
