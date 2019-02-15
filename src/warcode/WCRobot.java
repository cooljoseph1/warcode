package warcode;

public abstract class WCRobot {
	public Unit me;
	public Tile[][] map;
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
	private boolean given = false;
	private boolean built = false;
	private boolean signalled = false;
	
	public WCRobot() {
		
	}
	
	public WCRobot(Unit me, Engine engine) {
		this(me, engine, 200);
	}

	public WCRobot(Unit me, Engine engine, int time) {
		this.me = me;
		this.engine = engine;
		this.time = time;
	}

	void _do_turn() {
		moved = false;
		attacked = false;
		gathered = false;
		given = false;
		built = false;
		
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
		if (me.unitType == SPECS.Castle) {
			throw new MoveException("Castles cannot move");
		} else if (moved) {
			throw new MoveException("Robot can only move once per turn");
		} else if (Engine.distanceSquared(me.getX(), me.getY(), x, y) > me.unitType.MOVEMENT_SPEED) {// distance squared
																										// is too far
			throw new MoveException(String.format("%d, %d is too far to move to", x, y));
		} else if (engine.isOpen(x, y)) {
			this.me.setX(x);
			this.me.setY(y);
			moved = true;
		} else {
			throw new MoveException("Robot cannot move onto impassable terrain");
		}
	}

	public final void mine() throws MineException {
		if (me.unitType != SPECS.Peasant) {
			throw new MineException("Only peasants can mine");
		} else if (gathered) {
			throw new MineException("Robot can only mine/collect once per turn");
		} else if (engine.isOnMine(me.getX(), me.getY())) {
			this.me.addGold(SPECS.MINE_AMOUNT);
			engine.decreaseGold(me.getX(), me.getY(), SPECS.MINE_AMOUNT);
			gathered = true;
		} else {
			throw new MineException("Robot is not on a mine");
		}

	}

	public final void collect(int x, int y) throws CollectException {
		if (me.unitType != SPECS.Peasant) {
			throw new CollectException("Only peasants can collect wood");
		} else if (gathered) {
			throw new CollectException("Peasant can only collect/mine once per turn");
		} else if (Engine.distanceSquared(x, y, me.getX(), me.getY()) > 2) // a distance of two is the adjacent tiles
																			// and itself
		{
			throw new CollectException("Peasant can only collect wood from adjacent squares");
		} else if (engine.isOnTree(x, y)) {
			this.me.addWood(SPECS.WOOD_AMOUNT);
			engine.decreaseWood(x, y, SPECS.WOOD_AMOUNT);
			gathered = true;
		} else {
			throw new CollectException(String.format("%d %d does not contain any wood", x, y));
		}
	}

	public final void give(int x, int y, int gold, int wood) throws GiveException {
		if (me.unitType != SPECS.Peasant) {
			throw new GiveException("Only peasants can give resources");
		} else if (given) {
			throw new GiveException("Peasant can only give resources once per turn");
		} else if (gold > me.getGold()) {
			throw new GiveException(String.format("Peasant does not have enough gold to give %d gold", gold));
		} else if (wood > me.getWood()) {
			throw new GiveException(String.format("Peasant does not have enough wood to give %d wood", wood));
		} else if (Engine.distanceSquared(x, y, me.getX(), me.getY()) > 2) // a distance of two is the adjacent tiles
																			// and itself
		{
			throw new GiveException("Peasant can only give to adjacent tiles");
		} else if (!engine.isOnCastle(x, y)) {
			throw new GiveException("Peasant can only give resources to a castle");
		} else {
			me.decreaseWood(wood);
			me.decreaseGold(gold);

			engine.addResources(x, y, gold, wood);
			given = true;
		}

	}

	public final void attack(int x, int y) throws AttackException {
		if (attacked) {
			throw new AttackException("Robot can only attack once per turn");
		} else if (Engine.distanceSquared(x, y, me.getX(), me.getY()) > me.unitType.ATTACK_RADIUS) {
			throw new AttackException("Robot cannot attack outside of attack radius");
		} else {
			engine.attack(x, y, me.unitType);
			attacked = true;
		}
	}

	public final void buildUnit(int x, int y, UnitType unitType) throws BuildException {
		if(built) {
			throw new BuildException("Robot can only build one unit per turn");
		} else if (me.unitType != SPECS.Castle && me.unitType != SPECS.Peasant) {
			throw new BuildException("Only peasants and castles can build units");
		} else if (me.unitType == SPECS.Peasant && unitType != SPECS.Castle) {
			throw new BuildException("Peasants can only build castles");
		} else if (me.unitType == SPECS.Castle && unitType == SPECS.Castle) {
			throw new BuildException("Castles cannot build other castles");
		} else if (Engine.distanceSquared(x, y, me.getX(), me.getY()) > 2) {
			throw new BuildException("Robot can only build on adjacent squares");
		} else {
			engine.makeRobot(x, y, me.team, unitType);
			built = true;
		}

	}

	public final void signal(int message) throws SignalException {
		if(signalled) {
			throw new SignalException("Robot can only signal once per turn");
		} else {
			this.me.setSignal(message);
			signalled = true;
		}
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
		return (distSquared <= this.me.unitType.VISION_RADIUS);
	}

	public final Signal getSignal(Unit unit) {
		return unit.getSignal();
	}
}
