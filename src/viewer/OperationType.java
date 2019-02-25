package viewer;

public enum OperationType {
	MOVE, ATTACK, MINE, COLLECT, GIVE, CREATE, DO_NOTHING;
	
	public static OperationType fromString(String s) {
		switch(s) {
		case "M":
			return MOVE;
		case "A":
			return ATTACK;
		case "N":
			return MINE;
		case "C":
			return COLLECT;
		case "G":
			return GIVE;
		case "T":
			return CREATE;
		default:
			return DO_NOTHING;
		}
	}
	
	public String toString() {
		switch(this) {
		case MOVE:
			return "M";
		case ATTACK:
			return "A";
		case MINE:
			return "N";
		case COLLECT:
			return "C";
		case GIVE:
			return "G";
		case CREATE:
			return "T";
		default:
			return "I";
		}
	}
}
