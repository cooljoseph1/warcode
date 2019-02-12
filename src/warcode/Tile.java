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
		switch(this) {
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
}
