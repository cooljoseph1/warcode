package warcode;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import actions.AttackAction;
import actions.BuildAction;
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
	public int gold;
	public int wood;
	public long time;

	private Engine engine;

	private boolean moved = true;
	private boolean attacked = true;
	private boolean gathered = true;
	private boolean given = true;
	private boolean built = true;
	private boolean signalled = true;

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
	 *               Time in milliseconds
	 */
	public WCRobot(Unit me, Engine engine, long time) {
		this.me = me;
		this.engine = engine;
		this.time = time;
		this.runTurn = new RunTurn();

	}

	void _do_turn() {

		if (time < 0) {
			System.out.println(String.format("Time overdrawn by %f milliseconds", -time / 1000000f));
			time += SPECS.INCREMENT_TIME; // add time to clock
			return;
		} else {
			time += SPECS.INCREMENT_TIME; // add time to clock
		}

		moved = false;
		attacked = false;
		gathered = false;
		given = false;
		signalled = false;
		built = false;
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
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

		time -= System.nanoTime() - startTime; // subtract off time spent
	}

	// Override this method in your subclass.
	public abstract void turn() throws GameException;

	// ##############################################################################################################
	// types of actions people an perform.
	public final void move(int x, int y) throws MoveException {
		if (me.unitType == SPECS.Castle) {
			throw new MoveException("Castles cannot move");
		} else if (moved) {
			throw new MoveException("Robot can only move once per turn");
		} else if (Engine.distanceSquared(me.getX(), me.getY(), x, y) > me.unitType.MOVEMENT_SPEED) {// distance squared
																										// is too far
			throw new MoveException(String.format("%d, %d is too far to move to", x, y));
		} else if (engine.isOpen(x, y)) {
			// each action adds an operation to the engine for replays.
			engine.addAction(new MoveAction(me.getId(), x, y));

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
			engine.addAction(new MineAction(me.getId()));

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
			engine.addAction(new CollectAction(me.getId(), x, y));

			this.me.addWood(SPECS.COLLECT_AMOUNT);
			engine.decreaseWood(x, y, SPECS.COLLECT_AMOUNT);
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
			engine.addAction(new GiveAction(me.getId(), x, y, wood, gold));

			me.decreaseWood(wood);
			me.decreaseGold(gold);

			engine.giveResources(x, y, wood, gold);
			given = true;
		}

	}

	public final void attack(int x, int y) throws AttackException {
		if (attacked) {
			throw new AttackException("Robot can only attack once per turn");
		} else if (Engine.distanceSquared(x, y, me.getX(), me.getY()) > me.unitType.ATTACK_RADIUS) {
			throw new AttackException("Robot cannot attack outside of attack radius");
		} else {
			engine.addAction(new AttackAction(me.getId(), x, y));

			engine.attack(x, y, me.unitType);
			attacked = true;
		}
	}

	public final void buildUnit(int x, int y, UnitType unitType) throws BuildException {
		if (built) {
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
		} else {
			int id = engine.makeRobot(x, y, me.team, unitType);
			
			//Do not need to add an action because the engine automatically does that
			//engine.addAction(new BuildAction(id, me.getTeam(), unitType, x, y));
			built = true;
		}

	}

	public final void signal(int message) throws SignalException {
		if (signalled) {
			throw new SignalException("Robot can only signal once per turn");
		} else {
			engine.addAction(new SignalAction(me.getId(), message));
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

	protected void subtractTime(long time) {
		this.time -= time;
	}
}
