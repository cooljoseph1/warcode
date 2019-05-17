package viewer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import actions.Action;
import actions.ActionType;
import actions.AttackAction;
import actions.BuildAction;
import actions.CollectAction;
import actions.DieAction;
import actions.GiveAction;
import actions.MineAction;
import actions.MoveAction;
import actions.SignalAction;
import warcode.SPECS;
import warcode.Team;
import warcode.UnitType;
import warcode.Winner;

public class ViewerEngine {

	private String saveFile;
	private LinkedList<Action>[] turnActions;
	private int turns;

	private LinkedList<Attack>[] attacks;

	private ViewerMap map;

	// aliveIdQueue stores the list of all alive units
	private LinkedList<Integer> aliveIdQueue = new LinkedList<Integer>();

	// idUnitMap maps the ids to all the units ever made
	private HashMap<Integer, ViewerUnit> idUnitMap = new HashMap<Integer, ViewerUnit>();
	private LinkedList<ViewerUnit> castles = new LinkedList<ViewerUnit>();

	private int redGold;
	private int redWood;
	private int blueGold;
	private int blueWood;

	private int turn = 0;

	private Winner winner;

	public ViewerEngine(String saveFile) {
		this.saveFile = saveFile;

		try {

			BufferedReader reader = new BufferedReader(new FileReader(saveFile));
			winner = Winner.fromString(reader.readLine());
			turns = Integer.parseInt(reader.readLine());
			int width = Integer.parseInt(reader.readLine());
			int height = Integer.parseInt(reader.readLine());

			String[] mapRows = new String[height];
			for (int i = 0; i < height; i++) {
				mapRows[i] = reader.readLine();
			}

			map = new ViewerMap(mapRows, width, height);

			int turn = 0;
			turnActions = new LinkedList[turns + 1];
			for (String line = reader.readLine(); line != null; line = reader.readLine(), turn++) {
				turnActions[turn] = new LinkedList<Action>();
				for (String action : line.split("; ")) {
					if (!"".equals(action)) {
						turnActions[turn].add(Action.fromString(action));
					}
				}
			}
			reader.close();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}

		attacks = new LinkedList[turns + 1];
		for (int i = 0; i <= turns; i++) {
			LinkedList<Action> actions = turnActions[i];
			attacks[i] = new LinkedList<Attack>();

			for (Action action : actions) {
				if (action.actionType == ActionType.BUILD) {
					BuildAction buildAction = (BuildAction) action;

					ViewerUnit unit = new ViewerUnit(buildAction.id, buildAction.unitType, buildAction.team,
							buildAction.x, buildAction.y);
					idUnitMap.put(unit.id, unit);
				} else if (action.actionType == ActionType.ATTACK) {
					AttackAction attackAction = (AttackAction) action;
					ViewerUnit unit = getUnit(attackAction.id);
					attacks[i]
							.add(new Attack(unit.getX(), unit.getY(), attackAction.x, attackAction.y, unit.getTeam()));
				}
			}

		}
	}

	public Winner getWinner() {
		return winner;
	}

	public boolean hasNextTurn() {
		return turn < turns;
	}

	public void moveForwardTurn() {
		for (Action action : turnActions[turn]) {
			switch (action.actionType) {
			case ATTACK:
				doAttackAction((AttackAction) action);
				break;
			case BUILD:
				doBuildAction((BuildAction) action);
				break;
			case COLLECT:
				doCollectAction((CollectAction) action);
				break;
			case DIE:
				doDieAction((DieAction) action);
				break;
			case GIVE:
				doGiveAction((GiveAction) action);
				break;
			case MINE:
				doMineAction((MineAction) action);
				break;
			case MOVE:
				doMoveAction((MoveAction) action);
				break;
			case SIGNAL:
				doSignalAction((SignalAction) action);
				break;
			default:
				break;
			}

		}

		turn++;
	}

	public void moveBackwardTurn() {
		Iterator turnActionsIterator = turnActions[turn - 1].descendingIterator();
		while (turnActionsIterator.hasNext()) {
			Action action = (Action) turnActionsIterator.next();
			switch (action.actionType) {
			case ATTACK:
				undoAttackAction((AttackAction) action);
				break;
			case BUILD:
				undoBuildAction((BuildAction) action);
				break;
			case COLLECT:
				undoCollectAction((CollectAction) action);
				break;
			case DIE:
				undoDieAction((DieAction) action);
				break;
			case GIVE:
				undoGiveAction((GiveAction) action);
				break;
			case MINE:
				undoMineAction((MineAction) action);
				break;
			case MOVE:
				undoMoveAction((MoveAction) action);
				break;
			case SIGNAL:
				undoSignalAction((SignalAction) action);
				break;
			default:
				break;
			}

		}

		turn--;
	}

	public void doAttackAction(AttackAction attackAction) {
		attack(attackAction.x, attackAction.y, getUnit(attackAction.id).getUnitType());
	}

	public void doBuildAction(BuildAction buildAction) {
		spendMoney(buildAction.team, buildAction.unitType);
		aliveIdQueue.add(buildAction.id);
	}

	public void doCollectAction(CollectAction collectAction) {
		ViewerUnit unit = getUnit(collectAction.id);
		unit.addWood(SPECS.COLLECT_AMOUNT);
		decreaseWood(collectAction.x, collectAction.y, SPECS.COLLECT_AMOUNT);
	}

	public void doDieAction(DieAction dieAction) {
		aliveIdQueue.remove(Integer.valueOf(dieAction.id));
	}

	public void doGiveAction(GiveAction giveAction) {
		ViewerUnit unit = getUnit(giveAction.id);
		unit.decreaseWood(giveAction.wood);
		unit.decreaseGold(giveAction.gold);
		giveResources(giveAction.x, giveAction.y, giveAction.wood, giveAction.gold);
	}

	public void doMineAction(MineAction mineAction) {
		ViewerUnit unit = getUnit(mineAction.id);
		unit.addGold(SPECS.MINE_AMOUNT);
		decreaseWood(unit.getX(), unit.getY(), SPECS.MINE_AMOUNT);
	}

	public void doMoveAction(MoveAction moveAction) {
		getUnit(moveAction.id).setPos(moveAction.x, moveAction.y);
	}

	public void doSignalAction(SignalAction signalAction) {
		getUnit(signalAction.id).setSignal(signalAction.oldSignal);
	}

	public void undoAttackAction(AttackAction attackAction) {
		unAttack(attackAction.x, attackAction.y, getUnit(attackAction.id).getUnitType());
	}

	public void undoBuildAction(BuildAction buildAction) {
		unSpendMoney(buildAction.team, buildAction.unitType);
		aliveIdQueue.remove(Integer.valueOf(buildAction.id));
	}

	public void undoCollectAction(CollectAction collectAction) {
		ViewerUnit unit = getUnit(collectAction.id);
		unit.addWood(-SPECS.COLLECT_AMOUNT);
		decreaseWood(collectAction.x, collectAction.y, -SPECS.COLLECT_AMOUNT);
	}

	public void undoDieAction(DieAction dieAction) {
		aliveIdQueue.add(dieAction.id);
	}

	public void undoGiveAction(GiveAction giveAction) {
		ViewerUnit unit = getUnit(giveAction.id);
		unit.decreaseWood(-giveAction.wood);
		unit.decreaseGold(-giveAction.gold);
		giveResources(giveAction.x, giveAction.y, -giveAction.wood, -giveAction.gold);
	}

	public void undoMineAction(MineAction mineAction) {
		ViewerUnit unit = getUnit(mineAction.id);
		unit.addGold(-SPECS.MINE_AMOUNT);
		decreaseWood(unit.getX(), unit.getY(), -SPECS.MINE_AMOUNT);
	}

	public void undoMoveAction(MoveAction moveAction) {
		getUnit(moveAction.id).setPos(moveAction.startX, moveAction.startY);
	}

	public void undoSignalAction(SignalAction signalAction) {
		getUnit(signalAction.id).setSignal(signalAction.signal);
	}

	public LinkedList<Attack> getAttacks() {
		if (turn >= 0 && turn < attacks.length) {
			return attacks[turn];
		}
		return new LinkedList<Attack>();
	}

	public Winner playGame(String mapName) {

		map = new ViewerMap(mapName);

		// Set initial resources
		redGold = SPECS.INITIAL_GOLD;
		redWood = SPECS.INITIAL_WOOD;

		blueGold = SPECS.INITIAL_GOLD;
		blueWood = SPECS.INITIAL_WOOD;

		// Run game until one wins or turn reaches 1000.
		boolean redWon = false;
		boolean blueWon = false;
		turn = 0;

		if (redWon) {
			return Winner.RED;
		} else if (blueWon) {
			return Winner.BLUE;
		} else {
			return Winner.TIE;
		}
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param gold
	 * @param wood
	 */
	protected void giveResources(int x, int y, int wood, int gold) {
		ViewerUnit castleAtLocation = null;
		for (ViewerUnit castle : castles) {
			if (castle.getX() == x && castle.getY() == y) {
				castleAtLocation = castle;
				break;
			}
		}
		if (castleAtLocation == null) {
			return;
		}
		if (castleAtLocation.team == Team.RED) {
			redGold += gold;
			redWood += wood;
		} else {
			blueGold += gold;
			blueWood += wood;
		}
	}

	protected void decreaseGold(int x, int y, int amount) {
		map.decreaseGold(x, y, amount);
	}

	protected void decreaseWood(int x, int y, int amount) {
		map.decreaseWood(x, y, amount);
	}

	protected void addRedGold(int amount) {
		redGold += amount;
	}

	protected void addBlueGold(int amount) {
		blueGold += amount;
	}

	protected void addRedWood(int amount) {
		redWood += amount;
	}

	protected void addBlueWood(int amount) {
		blueWood += amount;
	}

	protected int getRedGold() {
		return redGold;
	}

	protected int getRedWood() {
		return redWood;
	}

	protected int getBlueGold() {
		return blueGold;
	}

	protected int getBlueWood() {
		return blueWood;
	}

	protected void attack(int x, int y, UnitType unitType) {
		for (int id : aliveIdQueue) {
			ViewerUnit viewerUnit = getUnit(id);
			if (distanceSquared(viewerUnit.getX(), viewerUnit.getY(), x, y) <= unitType.SPLASH_RADIUS) {
				viewerUnit.hurtUnit(unitType.ATTACK_DAMAGE);
			}
		}
	}

	protected void unAttack(int x, int y, UnitType unitType) {
		for (int id : aliveIdQueue) {
			ViewerUnit viewerUnit = getUnit(id);
			if (distanceSquared(viewerUnit.getX(), viewerUnit.getY(), x, y) <= unitType.SPLASH_RADIUS) {
				viewerUnit.hurtUnit(-unitType.ATTACK_DAMAGE);
			}
		}
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param team
	 * @param unitType
	 * @return id of new unit
	 */

	protected void spendMoney(Team team, UnitType unitType) {

		if (team == Team.RED) {
			redGold -= unitType.CONSTRUCTION_GOLD;
			redWood -= unitType.CONSTRUCTION_WOOD;
		} else {
			blueGold -= unitType.CONSTRUCTION_GOLD;
			blueWood -= unitType.CONSTRUCTION_WOOD;
		}

	}

	protected void unSpendMoney(Team team, UnitType unitType) {

		if (team == Team.RED) {
			redGold += unitType.CONSTRUCTION_GOLD;
			redWood += unitType.CONSTRUCTION_WOOD;
		} else {
			blueGold += unitType.CONSTRUCTION_GOLD;
			blueWood += unitType.CONSTRUCTION_WOOD;
		}

	}

	protected ViewerUnit getUnit(int id) {
		return idUnitMap.get(id);
	}

	protected LinkedList<Integer> getAliveUnitIds() { // Dangerous! Returns the actual Linked List!
		return aliveIdQueue;
	}

	public ViewerMap getMap() {
		return map;
	}

	public int getTurns() {
		return turns;
	}

	public int getTurn() {
		return turn;
	}

	protected final static int distanceSquared(ViewerUnit unit1, ViewerUnit unit2) {
		int dx = unit1.getX() - unit2.getX();
		int dy = unit1.getY() - unit2.getY();
		return dx * dx + dy * dy;
	}

	protected final static int distanceSquared(int x1, int y1, int x2, int y2) {
		int dx = x1 - x2;
		int dy = y1 - y2;
		return dx * dx + dy * dy;
	}
}