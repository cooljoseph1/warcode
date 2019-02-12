package warcode;

public enum Tile {
	PASSABLE, IMPASSABLE, GOLD, WOOD, INVALID;

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
		default:
			return "X";
		}
	}

	public static Tile fromChar(char c) {
		switch (c) {
		case 'P':
			return Tile.PASSABLE;
		case 'I':
			return Tile.IMPASSABLE;
		case 'G':
			return Tile.GOLD;
		case 'W':
			return Tile.WOOD;
		default:
			return Tile.INVALID;
		}
	}
	
	public static Tile fromString(String c) {
		switch (c) {
		case "P":
			return Tile.PASSABLE;
		case "I":
			return Tile.IMPASSABLE;
		case "G":
			return Tile.GOLD;
		case "W":
			return Tile.WOOD;
		default:
			return Tile.INVALID;
		}
	}
}
