package warcode;

public enum OperationType {
	MOVE, ATTACK, MINE, COLLECT, GIVE, BUILD, SIGNAL, DO_NOTHING;
	
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
		case "B":
			return BUILD;
		case "S":
			return SIGNAL;
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
		case BUILD:
			return "B";
		case SIGNAL:
			return "S";
		default:
			return "I";
		}
	}
}
