package warcode;

public class WCRobot {
	public Unit me;
	public int[][] map;
	public int[][] goldMap;
	public int[][] woodMap;
	public Unit[] visibleUnits;
	public int[][] visibleUnitMap;
	public Signal[] signals;
	public int gold;
	public int wood;
	public int time;

	private Engine engine;
	private boolean moved = false;
	private boolean attacked = false;
	private boolean gathered = false;

	public WCRobot(Unit me) {
		this(me, 200);
	}

	public WCRobot(Unit me, int time) {
		this.me = me;
		this.time = time;
	}

	void _do_turn() {
		moved = false;
		attacked = false;
		gathered = false;
		map = engine.getPassableMap();
		goldMap = engine.getGoldMap();
		woodMap = engine.getWoodMap();
		
		visibleUnits = engine.getVisibleUnits(me);
		visibleUnitMap = engine.getVisibleUnitMap(me);
		
		turn();
	}

	// Override this method in your subclass.
	public void turn() {

	}

	public final void move(int x, int y) throws MoveException {
		if(me.unitType == SPECS.Castle) {
			throw new MoveException("Castles cannot move.");
		} else if (moved) {
			throw new MoveException("Robot can only move once per turn.");
		} else if (Engine.distanceSquared(me.getX(), me.getY(), x, y) > me.unitType.MOVEMENT_SPEED) {//distance squared is too far
			throw new MoveException(String.format("%d, %d is too far to move to.", x, y));
		} else if (engine.isOpen(x, y)) {
			this.me.setX(x);
			this.me.setY(y);
			moved = true;
		} else {
			throw new MoveException("Robot cannot move onto impassable terrain.");
		}
	}

	public final void mine() throws MineException {
		if(me.unitType != SPECS.Peasant) {
			throw new MineException("Only peasants can mine.");
		} else if (gathered) {
			throw new MineException("Robot can only mine/collect once per turn.");
		} else if (engine.isOnMine(me.getX(), me.getY())) {
			this.me.addGold(SPECS.MINE_AMOUNT);
			engine.decreaseGold(me.getX(), me.getY(), SPECS.MINE_AMOUNT);
			gathered = true;
		} else {
			throw new MineException("Robot is not on a mine.");
		}
		
	}

	public final void collect(int x, int y) throws CollectException {
		if(me.unitType != SPECS.Peasant) {
			throw new CollectException("Only peasants can collect wood.");
		} else if (gathered) {
			throw new CollectException("Robot can only collect/mine once per turn.");
		} else if (engine.isOnTree(x, y)) {
			this.me.addWood(SPECS.WOOD_AMOUNT);
			engine.decreaseWood(x, y, SPECS.WOOD_AMOUNT);
			gathered = true;
		} else {
			throw new CollectException(String.format("%d %d does not contain any wood.", x, y));
		}
	}

	public final void give(int x, int y, int gold, int wood) {
		//TODO:  Make this function
		
	}

	public final void attack(int x, int y) throws AttackException {
		if(attacked) {
			throw new AttackException("Robot can only attack once per turn.");
		} else {
			engine.attack(x, y, me.unitType);
			attacked = true;
		}
	}

	
	public final void buildUnit(int x, int y, UnitType unitType) {
		//TODO:  Make this function
		
	}
	

	public final void signal(int message) {
		this.me.setSignal(message);
	}

	public final Unit[] getVisibleUnits() {
		return visibleUnits;
	}

	public final int[][] getVisibleUnitMap() {
		return visibleUnitMap;
	}

	public final Unit getUnit(int id) {
		return engine.getUnit(id);
	}

	public final boolean isVisible(Unit unit) {
		int distSquared = Engine.distanceSquared(me, unit);
		return (distSquared<=this.me.unitType.VISION_RADIUS);
	}
	
	

	public final Signal getSignal(Unit unit) {
		return unit.getSignal();
	}
}
