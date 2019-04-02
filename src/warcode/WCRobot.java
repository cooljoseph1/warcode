package warcode;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import actions.AttackAction;
import actions.CollectAction;
import actions.GiveAction;
import actions.MineAction;
import actions.MoveAction;
import actions.SignalAction;
import exceptions.AttackException;
import exceptions.BuildException;
import exceptions.CollectException;
import exceptions.GameException;
import exceptions.GiveException;
import exceptions.MineException;
import exceptions.MoveException;
import exceptions.SignalException;
import exceptions.VisibilityException;

public abstract class WCRobot {
	class RunTurn implements Callable<Void> {
		@Override
		public Void call() throws GameException {
			turn();
			return null;
		}
	}

	public Unit me;
	public Tile[][] map;
	public int[][] goldMap;
	public int[][] woodMap;
	public Unit[] visibleUnits;
	public int[][] visibleUnitMap;
	public Signal[] signals;
	public long time;

	private Engine engine;

	private RunTurn runTurn;

	public WCRobot() {

	}

	public WCRobot(Unit me, Engine engine) {
		this(me, engine, SPECS.INITIAL_TIME);
	}

	/**
	 * 
	 * @param me
	 * @param engine
	 * @param time
	 * 
	 *               Time in nanoseconds
	 */
	WCRobot(Unit me, Engine engine, long time) {
		this.me = me;
		this.engine = engine;
		this.time = time;
		this.runTurn = new RunTurn();

	}

	void _do_turn() {

		// Make sure the robot only takes one turn per round
		if (me.hasTakenTurn()) {
			System.out.println("Robot has been killed for attempting to take multiple turns in one round.");
			engine.kill(me.id);
		}

		me.setTurnTaken(true);

		if (time < 0) {
			System.out.println(String.format("Time overdrawn by %f milliseconds", -time / 1000000f));
			time += SPECS.INCREMENT_TIME; // add time to clock
			return;
		} else {
			time += SPECS.INCREMENT_TIME; // add time to clock
		}

		me.setMoved(false);
		me.setAttacked(false);
		me.setGathered(false);
		me.setGiven(false);
		me.setSignalled(false);
		me.setBuilt(false);

		me.resetSignal();

		map = engine.getPassableMap();
		goldMap = engine.getGoldMap();
		woodMap = engine.getWoodMap();

		visibleUnits = engine.getVisibleUnits(me);
		visibleUnitMap = engine.getVisibleUnitMap(me);

		long startTime = System.nanoTime();

		FutureTask<Void> task = new FutureTask<Void>(runTurn);
		Thread t = new Thread(task);
		t.start();
		try {
			t.join(200); // allow thread to be run for 200 milliseconds before interrupting it
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (t.isAlive()) {
			t.stop();
		}
		try {
			Void result = task.get();
		} catch (ExecutionException e) {
			e.printStackTrace();

			// kill the robot if it throws an error
			engine.kill(me.id);

		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

		time -= System.nanoTime() - startTime; // subtract off time spent
	}

	// Override this method in your subclass.
	public abstract void turn();

	// -------------------------------------------------------------------------------------------------------------
	// actions robots can use
	public final void move(int x, int y) throws MoveException {
		if (me.unitType == SPECS.Castle) {
			throw new MoveException("Castles cannot move");
		} else if (me.hasMoved()) {
			throw new MoveException("Robot can only move once per turn");
		} else if (Engine.distanceSquared(me.getX(), me.getY(), x, y) > me.unitType.MOVEMENT_SPEED) {// distance squared
																										// is too far
			throw new MoveException(String.format("%d, %d is too far to move to", x, y));
		} else if (engine.isOpen(x, y)) {
			// each action adds an operation to the engine for replays.
			engine.addAction(new MoveAction(me.getId(), x, y, me.getX(), me.getY()));

			me.setX(x);
			me.setY(y);
			me.setMoved(true);
			;
		} else {
			throw new MoveException("Robot cannot move onto impassable terrain");
		}
	}

	public final void mine() throws MineException {
		if (me.unitType != SPECS.Peasant) {
			throw new MineException("Only peasants can mine");
		} else if (me.hasGathered()) {
			throw new MineException("Robot can only mine/collect once per turn");
		} else if (engine.isOnMine(me.getX(), me.getY())) {
			engine.addAction(new MineAction(me.getId()));

			me.addGold(SPECS.MINE_AMOUNT);
			engine.decreaseGold(me.getX(), me.getY(), SPECS.MINE_AMOUNT);
			me.setGathered(true);
		} else {
			throw new MineException("Robot is not on a mine");
		}

	}

	public final void collect(int x, int y) throws CollectException {
		if (me.unitType != SPECS.Peasant) {
			throw new CollectException("Only peasants can collect wood");
		} else if (me.hasGathered()) {
			throw new CollectException("Peasant can only collect/mine once per turn");
		} else if (Engine.distanceSquared(x, y, me.getX(), me.getY()) > 2) // a distance of two is the adjacent tiles
																			// and itself
		{
			throw new CollectException("Peasant can only collect wood from adjacent squares");
		} else if (engine.isOnTree(x, y)) {
			engine.addAction(new CollectAction(me.getId(), x, y));

			this.me.addWood(SPECS.COLLECT_AMOUNT);
			engine.decreaseWood(x, y, SPECS.COLLECT_AMOUNT);
			me.setGathered(true);
		} else {
			throw new CollectException(String.format("%d %d does not contain any wood", x, y));
		}
	}

	public final void give(int x, int y, int gold, int wood) throws GiveException {
		if (me.unitType != SPECS.Peasant) {
			throw new GiveException("Only peasants can give resources");
		} else if (me.hasGiven()) {
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
			engine.addAction(new GiveAction(me.getId(), x, y, wood, gold));

			me.decreaseWood(wood);
			me.decreaseGold(gold);

			engine.giveResources(x, y, wood, gold);
			me.setGiven(true);
		}

	}

	public final void attack(int x, int y) throws AttackException {
		if (me.hasAttakced()) {
			throw new AttackException("Robot can only attack once per turn");
		} else if (Engine.distanceSquared(x, y, me.getX(), me.getY()) > me.unitType.ATTACK_RADIUS) {
			throw new AttackException("Robot cannot attack outside of attack radius");
		} else {
			engine.addAction(new AttackAction(me.getId(), x, y));

			engine.attack(x, y, me.unitType);
			me.setAttacked(true);
		}
	}

	public final void buildUnit(int x, int y, UnitType unitType) throws BuildException {
		if (me.hasBuilt()) {
			throw new BuildException("Robot can only build one unit per turn");
		} else if (me.unitType != SPECS.Castle && me.unitType != SPECS.Peasant) {
			throw new BuildException("Only peasants and castles can build units");
		} else if (me.unitType == SPECS.Peasant && unitType != SPECS.Castle) {
			throw new BuildException("Peasants can only build castles");
		} else if (me.unitType == SPECS.Castle && unitType == SPECS.Castle) {
			throw new BuildException("Castles cannot build other castles");
		} else if (Engine.distanceSquared(x, y, me.getX(), me.getY()) > 2) {
			throw new BuildException("Robot can only build on adjacent squares");
		} else if (getGold() < unitType.CONSTRUCTION_GOLD) {
			throw new BuildException("Not enough gold to build unit");
		} else if (getWood() < unitType.CONSTRUCTION_WOOD) {
			throw new BuildException("Not enough wood to build unit");
		} else if (!engine.isOpen(x, y)) {
			throw new BuildException("Robot cannot build onto other robot");
		} else {
			engine.makeRobot(x, y, me.team, unitType);

			// Do not need to add an action because the engine automatically does that when
			// creating a robot
			me.setBuilt(true);
		}

	}

	public final void signal(int message) throws SignalException {
		if (me.hasSignalled()) {
			throw new SignalException("Robot can only signal once per turn");
		} else {
			engine.addAction(new SignalAction(me.getId(), message, me.getSignal().value));
			this.me.setSignal(message);
			me.setSignalled(true);
		}
	}

	// --------------------------------------------------------------------------------------------------
	// Helper methods

	public final Unit[] getVisibleUnits() {
		return visibleUnits;
	}

	public final int[][] getVisibleUnitMap() {
		return visibleUnitMap;
	}

	public final Unit getUnit(int id) throws VisibilityException {
		Unit unit = engine.getUnit(id);

		if (!isVisible(unit)) {
			throw new VisibilityException("Unit is out of range");
		}

		return unit;
	}

	public final Unit getUnitAtLocation(int x, int y) {
		Unit unit = engine.getUnitAtLocation(x, y);

		if (!isVisible(unit)) {
			throw new VisibilityException("Unit is out of range");
		}

		return unit;
	}

	public final boolean isVisible(Unit unit) {
		int distSquared = Engine.distanceSquared(me, unit);
		return (distSquared <= this.me.unitType.VISION_RADIUS);
	}

	public final boolean isVisible(int x, int y) {
		int distSquared = Engine.distanceSquared(x, y, me.getX(), me.getY());
		return (distSquared <= this.me.unitType.VISION_RADIUS);
	}

	public final Signal getSignal(Unit unit) {
		return unit.getSignal();
	}

	public final int getGold() {
		if (me.team == Team.RED) {
			return engine.getRedGold();
		} else {
			return engine.getBlueGold();
		}
	}

	public final int getWood() {
		if (me.team == Team.RED) {
			return engine.getRedWood();
		} else {
			return engine.getBlueWood();
		}
	}

	/**
	 * 
	 * @return time in nanoseconds
	 */
	public final long getTime() {
		return time;
	}

	public final boolean isOpen(int x, int y) throws GameException {
		if (!engine.isOnMap(x, y)) {
			throw new GameException("Robot attempted to check a location that is off the map.");
		}
		return engine.isOpen(x, y);
	}

	public final boolean isOnMap(int x, int y) {
		return engine.isOnMap(x, y);
	}

	/**
	 * Method to be used by the engine to subtract off the time it has
	 * 
	 * @param time
	 */
	
	void subtractTime(long time) {
		this.time -= time;
	}
}
