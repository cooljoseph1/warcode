package warcode;

public class SignalException extends GameException {

	private static final long serialVersionUID = -8906704528017876875L;

	public SignalException() {

	}

	public SignalException(String message) {
		super(message);
	}

	public SignalException(Throwable cause) {
		super(cause);
	}

	public SignalException(String message, Throwable cause) {
		super(message, cause);
	}

}
