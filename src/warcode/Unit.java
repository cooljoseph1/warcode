package warcode;

public class Unit {
	public final int id;
	public final UnitType unitType;
	public final Team team;
	
	private int x;
	private int y;
	private int health;
	private int wood;
	private int gold;
	private Signal signal;
	
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
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	protected void setWood(int wood) {
		this.wood = wood;
	}
	protected void setGold(int gold) {
		this.gold = gold;
	}
	protected void addWood(int wood) {
		this.wood+=wood;
		if(this.gold+this.wood > SPECS.MAX_RESOURCES) {
			this.wood = SPECS.MAX_RESOURCES - this.gold;
		}
	}
	protected void addGold(int gold) {
		this.gold+=gold;
		if(this.gold+this.wood > SPECS.MAX_RESOURCES) {
			this.gold = SPECS.MAX_RESOURCES - this.wood;
		}
	}
	protected void setHealth(int health) {
		this.health = health;
	}
	protected void hurtUnit(int amount) {
		health -= amount;
		if (health<0) {
			health = 0;
		}
	}
	public int getGold() {
		return gold;
	}
	public int getWood() {
		return wood;
	}
	protected void setSignal(int value) {
		this.signal = new Signal(value, team, id, unitType);
	}
	public Signal getSignal() {
		return signal;
	}
	public int getHealth() {
		return health;
	}
}
