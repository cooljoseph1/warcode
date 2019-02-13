package warcode;

public enum Tile {
	PASSABLE, IMPASSABLE, GOLD, WOOD, INVALID, RED_CASTLE, BLUE_CASTLE; //only the first four are used by the WCRobot.

	public static Tile next(Tile tile) {
		switch (tile) {
		case PASSABLE:
			return IMPASSABLE;
		case IMPASSABLE:
			return GOLD;
		case GOLD:
			return WOOD;
		case WOOD:
			return PASSABLE;
		case RED_CASTLE:
			return BLUE_CASTLE;
		case BLUE_CASTLE:
			return RED_CASTLE;
		default:
			return INVALID;
		}
	}

	@Override
	public String toString() {
		switch (this) {
		case PASSABLE:
			return "P";
		case IMPASSABLE:
			return "I";
		case GOLD:
			return "G";
		case WOOD:
			return "W";
		case RED_CASTLE:
			return "R";
		case BLUE_CASTLE:
			return "B";
		default:
			return "X";
		}
	}

	public static Tile fromChar(char c) {
		switch (c) {
		case 'P':
			return PASSABLE;
		case 'I':
			return IMPASSABLE;
		case 'G':
			return GOLD;
		case 'W':
			return WOOD;
		case 'R':
			return RED_CASTLE;
		case 'B':
			return BLUE_CASTLE;
		default:
			return INVALID;
		}
	}

	public static Tile fromString(String c) {
		switch (c) {
		case "P":
			return PASSABLE;
		case "I":
			return IMPASSABLE;
		case "G":
			return GOLD;
		case "W":
			return WOOD;
		case "R":
			return RED_CASTLE;
		case "B":
			return BLUE_CASTLE;
		default:
			return INVALID;
		}
	}
}
