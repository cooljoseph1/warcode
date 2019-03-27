package warcode;

public enum Winner {
	RED, BLUE, TIE;

	@Override
	public String toString() {
		switch (this) {
		case RED:
			return "RED";
		case BLUE:
			return "BLUE";
		case TIE:
			return "TIE";
		default:
			throw new IllegalArgumentException("Winner must be RED, BLUE, or TIE");
		}
	}

	public static Winner fromString(String string) {
		switch (string) {
		case "RED":
			return RED;
		case "BLUE":
			return BLUE;
		case "TIE":
			return TIE;
		default:
			throw new IllegalArgumentException("Winner must be \"RED\", \"BLUE\", or \"TIE\"");
		}
	}
}
