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

	private boolean moved = true;
	private boolean attacked = true;
	private boolean gathered = true;
	private boolean given = true;
	private boolean built = true;
	private boolean signalled = true;

	private boolean turnTaken = false;

	/**
	 * 
	 * @param id
	 * @param unitType
	 * @param team
	 */
	public Unit(int id, UnitType unitType, Team team) {
		this(id, unitType, team, 0, 0);
	}

	/**
	 * 
	 * @param id
	 * @param unitType
	 * @param team
	 * @param x
	 * @param y
	 */
	public Unit(int id, UnitType unitType, Team team, int x, int y) {
		this.id = id;
		this.unitType = unitType;
		this.team = team;
		this.x = x;
		this.y = y;

		this.health = unitType.INITIAL_HEALTH;
	}

	void setPos(int x, int y) {
		this.x = x;
		this.y = y;
	}

	void setX(int x) {
		this.x = x;
	}

	void setY(int y) {
		this.y = y;
	}

	void setWood(int wood) {
		this.wood = wood;
	}

	void setGold(int gold) {
		this.gold = gold;
	}

	void addWood(int wood) {
		this.wood += wood;
		if (this.gold + this.wood > SPECS.MAX_RESOURCES) {
			this.wood = SPECS.MAX_RESOURCES - this.gold;
		}
	}

	void addGold(int gold) {
		this.gold += gold;
		if (this.gold + this.wood > SPECS.MAX_RESOURCES) {
			this.gold = SPECS.MAX_RESOURCES - this.wood;
		}
	}

	void decreaseWood(int wood) {
		this.wood -= wood;
		if (this.wood < 0) {
			throw new Error("Uh oh, how is your wood below zero?");
		}
	}

	void decreaseGold(int gold) {
		this.gold -= gold;
		if (this.gold < 0) {
			throw new Error("Uh oh, how is your gold below zero?");
		}
	}

	void setHealth(int health) {
		this.health = health;
	}

	void hurtUnit(int amount) {
		health -= amount;
		if (health < 0) {
			health = 0;
		}
	}

	void setSignal(int value) {
		this.signal = new Signal(value, team, id, unitType);
	}

	void resetSignal() {
		this.signal = null;
	}

	int getGold() {
		return gold;
	}

	void setMoved(boolean state) {
		moved = state;
	}

	void setGathered(boolean state) {
		gathered = state;
	}

	void setGiven(boolean state) {
		given = state;
	}

	void setAttacked(boolean state) {
		attacked = state;
	}

	void setSignalled(boolean state) {
		signalled = state;
	}

	void setBuilt(boolean state) {
		built = state;
	}

	void setTurnTaken(boolean state) {
		turnTaken = state;
	}

	boolean hasMoved() {
		return moved;
	}

	boolean hasGathered() {
		return gathered;
	}

	boolean hasGiven() {
		return given;
	}

	boolean hasAttakced() {
		return attacked;
	}

	boolean hasSignalled() {
		return signalled;
	}

	boolean hasBuilt() {
		return built;
	}

	boolean hasTakenTurn() {
		return turnTaken;
	}

	int getWood() {
		return wood;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Signal getSignal() {
		return signal;
	}

	public int getHealth() {
		return health;
	}

	public int getId() {
		return id;
	}

	public UnitType getUnitType() {
		return unitType;
	}

	public Team getTeam() {
		return team;
	}

	public boolean isAlive() {
		return health > 0;
	}
}
