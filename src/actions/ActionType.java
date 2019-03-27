package actions;

public enum ActionType {
	ATTACK, BUILD, COLLECT, GIVE, MINE, MOVE, SIGNAL, DIE;

	@Override
	public String toString() {
		switch (this) {
		case ATTACK:
			return "ATTACK";
		case BUILD:
			return "BUILD";
		case COLLECT:
			return "COLLECT";
		case GIVE:
			return "GIVE";
		case MINE:
			return "MINE";
		case MOVE:
			return "MOVE";
		case SIGNAL:
			return "SIGNAL";
		case DIE:
			return "DIE";
		default:
			throw new IllegalArgumentException("Invalid enum type for ActionType");
		}
	}

	public static ActionType fromString(String string) {
		switch (string) {
		case "ATTACK":
			return ATTACK;
		case "BUILD":
			return BUILD;
		case "COLLECT":
			return COLLECT;
		case "GIVE":
			return GIVE;
		case "MINE":
			return MINE;
		case "MOVE":
			return MOVE;
		case "SIGNAL":
			return SIGNAL;
		case "DIE":
			return DIE;
		default:
			throw new IllegalArgumentException("Invalid name for ActionType");

		}
	}
}
