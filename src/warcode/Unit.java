package warcode;

public class Unit {
	public final int id;
	public final UnitType unitType;
	public final Team team;
	
	public int x;
	public int y;
	public int health;
	public int wood;
	public int gold;
	public Signal signal;
	
	public Unit(int id, UnitType unitType, Team team) {
		this(id, unitType, team, 0, 0);
	}
	
	public Unit(int id, UnitType unitType, Team team, int x, int y) {
		this.id = id;
		this.unitType = unitType;
		this.team = team;
		this.x = x;
		this.y = y;
	}
	
	protected void setPos(int x, int y) {
		this.x = x;
		this.y = y;
	}
	protected void setX(int x) {
		this.x = x;
	}
	protected void setY(int y) {
		this.y = y;
	}
	protected void setWood(int wood) {
		this.wood = wood;
	}
	protected void setGold(int gold) {
		this.gold = gold;
	}
	protected void setSignal(int value) {
		this.signal = new Signal(value, team, id, unitType);
	}
}
