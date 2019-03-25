package actions;

import warcode.Team;
import warcode.UnitType;

public class BuildAction extends Action {
	public final int id;
	public final Team team;
	public final UnitType unitType;
	public final int x;
	public final int y;

	public BuildAction(int id, Team team, UnitType unitType, int x, int y) {
		super(ActionType.BUILD);
		this.id = id;
		this.team = team;
		this.unitType = unitType;
		this.x = x;
		this.y = y;
	}

	public BuildAction(String string) {
		super(ActionType.BUILD);
		String[] parts = string.split(", ");
		id = Integer.parseInt(parts[0]);
		team = Team.fromString(parts[1]);
		unitType = UnitType.fromString(parts[2]);
		x = Integer.parseInt(parts[3]);
		y = Integer.parseInt(parts[4]);
	}
	
	public BuildAction(String[] parts) {
		super(ActionType.BUILD);
		id = Integer.parseInt(parts[0]);
		team = Team.fromString(parts[1]);
		unitType = UnitType.fromString(parts[2]);
		x = Integer.parseInt(parts[3]);
		y = Integer.parseInt(parts[4]);
	}

	@Override
	public String toString() {
		return String.join(", ", actionType.toString(), Integer.toString(id), team.toString(), unitType.toString(),
				Integer.toString(x), Integer.toString(y));
	}

}
