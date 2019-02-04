package warcode;

public class Signal {
	public final int value;
	public final Team team;
	public final int id;
	public final UnitType unitType;
	public Signal(int value, Team team, int id, UnitType unitType) {
		this.value = value;
		this.team = team;
		this.id = id;
		this.unitType = unitType;
	}
}
