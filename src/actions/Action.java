package actions;

import java.util.Arrays;

public abstract class Action {
	public final ActionType actionType;
	
	public Action(ActionType actionType) {
		this.actionType = actionType;
	}
	
	public static Action fromString(String string) {
		String[] parts = string.split(", ");

		String[] infoParts = Arrays.copyOfRange(parts, 1, parts.length);
		switch (ActionType.fromString(parts[0])) {
		case ATTACK:
			return new AttackAction(infoParts);
		case BUILD:
			return new BuildAction(infoParts);
		case COLLECT:
			return new CollectAction(infoParts);
		case GIVE:
			return new GiveAction(infoParts);
		case MINE:
			return new MineAction(infoParts);
		case MOVE:
			return new MoveAction(infoParts);
		case SIGNAL:
			return new SignalAction(infoParts);
		default:
			throw new IllegalArgumentException("Invalid type for Action");
		}

	}
}
