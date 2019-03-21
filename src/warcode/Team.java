package warcode;

public enum Team {
	RED, BLUE;
	
	@Override
	public String toString() {
		switch(this) {
		case RED:
			return "RED";
		case BLUE:
			return "BLUE";
		default:
			return "NO_TEAM";
		}
	}
	
	public static Team fromString(String string) {
		switch(string) {
		case "RED":
			return RED;
		case "BLUE":
			return BLUE;
		default:
			throw new RuntimeException("Team can only be \"RED\" or \"BLUE\"");
		}
	}
}
