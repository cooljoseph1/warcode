package actions;

import warcode.Team;
import warcode.UnitType;

public class DieAction extends Action {
	public final int id;

	public DieAction(int id, Team team, UnitType unitType, int x, int y) {
		super(ActionType.DIE);
		this.id = id;
	}

	public DieAction(String string) {
		super(ActionType.DIE);
		String[] parts = string.split(", ");
		id = Integer.parseInt(parts[0]);
	}
	
	public DieAction(String[] parts) {
		super(ActionType.DIE);
		id = Integer.parseInt(parts[0]);
	}

	@Override
	public String toString() {
		return String.join(", ", actionType.toString(), Integer.toString(id));
	}

}
