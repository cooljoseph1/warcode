package warcode;

public class BuildException extends GameException {

	private static final long serialVersionUID = -8906704528017876875L;

	public BuildException() {

	}

	public BuildException(String message) {
		super(message);
	}

	public BuildException(Throwable cause) {
		super(cause);
	}

	public BuildException(String message, Throwable cause) {
		super(message, cause);
	}

}
