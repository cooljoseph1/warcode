package warcode;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class Engine {
	public final Constructor<WCRobot> redConstructor;
	public final Constructor<WCRobot> blueConstructor;

	private Map map;

	private LinkedList<Integer> idQueue = new LinkedList<Integer>();
	private HashMap<Integer, WCRobot> idRobotMap = new HashMap<Integer, WCRobot>();
	private LinkedList<Unit> castles = new LinkedList<Unit>();

	public Engine(Class<WCRobot> red, Class<WCRobot> blue) throws NoSuchMethodException {
		try {
			this.redConstructor = red.getDeclaredConstructor(new Class[] { Unit.class });
		} catch (NoSuchMethodException e) {
			System.out.println("Red failed to initialize due to a bad constructor.");
			throw e;
		}
		try {
			this.blueConstructor = blue.getDeclaredConstructor(new Class[] { Unit.class });
		} catch (NoSuchMethodException e) {
			System.out.println("Blue failed to initialize due to a bad constructor.");
			throw e;
		}
	}

	protected Tile[][] getPassableMap() {
		return map.getPassableMapCopy();
	}

	protected int[][] getGoldMap() {
		return map.getGoldMapCopy();
	}

	protected int[][] getWoodMap() {
		return map.getWoodMapCopy();
	}

	protected int[][] getVisibleUnitMap(Unit unit) {
		int[][] visibleUnitMap = new int[map.height][map.width];

		for (int id : idQueue) {
			Unit tempUnit = getUnit(id);
			visibleUnitMap[tempUnit.getY()][tempUnit.getX()] = tempUnit.id;
		}

		for (int y = 0; y < map.height; y++) {
			for (int x = 0; x < map.width; x++) {
				// set values outside vision radius to -1
				if (distanceSquared(x, y, unit.getX(), unit.getY()) > unit.unitType.VISION_RADIUS) {
					visibleUnitMap[y][x] = -1;
				}
			}
		}

		return visibleUnitMap;
	}

	protected Unit[] getVisibleUnits(Unit unit) {
		LinkedList<Unit> units = new LinkedList<Unit>();
		for (int id : idQueue) {
			Unit tempUnit = getUnit(id);
			if (distanceSquared(tempUnit.getX(), tempUnit.getY(), unit.getX(),
					unit.getY()) <= unit.unitType.VISION_RADIUS) {
				units.add(tempUnit);
			}
		}

		return (Unit[]) units.toArray();
	}

	protected boolean isOpen(int x, int y) {
		if (!map.isOpen(x, y)) {
			return false;
		}
		for (WCRobot robot : idRobotMap.values()) {
			if (x == robot.me.getX() && y == robot.me.getY()) {
				return false;
			}
		}
		return true;
	}

	protected boolean isOnMine(int x, int y) {
		return (map.get(x, y) == Tile.GOLD);
	}

	protected boolean isOnTree(int x, int y) {
		return (map.get(x, y) == Tile.WOOD);
	}

	protected boolean isOnCastle(int x, int y) {
		for (Unit castle : castles) {
			if (castle.getX() == x && castle.getY() == y) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 
	 * @param x
	 * @param y
	 * @param gold
	 * @param wood
	 */
	protected void addResources(int x, int y, int gold, int wood) {
		Unit castleAtLocation = null;
		for(Unit castle : castles) {
			if(castle.getX() == x && castle.getY() == y) {
				castleAtLocation = castle;
				break;
			}
		}
		castleAtLocation.addGold(gold);
		castleAtLocation.addWood(wood);
	}
	protected void decreaseGold(int x, int y, int amount) {
		map.decreaseGold(x, y, amount);
	}

	protected void decreaseWood(int x, int y, int amount) {
		map.decreaseWood(x, y, amount);
	}

	protected void attack(int x, int y, UnitType unitType) {
		LinkedList<Integer> idsToRemove = new LinkedList<Integer>();
		for (int id : idQueue) {
			WCRobot robot = getRobot(id);
			if (distanceSquared(robot.me.getX(), robot.me.getY(), x, y) <= unitType.SPLASH_RADIUS) {
				robot.me.hurtUnit(unitType.ATTACK_DAMAGE);
				if (robot.me.getHealth() <= 0) {
					idsToRemove.add(robot.me.id);
				}
			}
		}
		removeAllRobots(idsToRemove);
	}
	
	protected void makeRobot(int x, int y, Team team, UnitType unitType) {
		int id;
		do {
			id = (int) (Math.random() * (Math.pow(2, 16) - 1) + 1);
		} while (idRobotMap.containsKey(id));
		Unit castle = new Unit(id, unitType, team, x, y);
		addRobot(castle, team);
	}

	protected Unit getUnit(int id) {
		return idRobotMap.get(id).me;
	}

	private WCRobot getRobot(int id) {
		return idRobotMap.get(id);
	}

	private void addRobot(Unit unit, Team team) {
		WCRobot robot = null;
		try {
			if (team == Team.RED) {
				robot = redConstructor.newInstance(unit);
			} else {
				robot = blueConstructor.newInstance(unit);
			}

		} catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
			if (team == Team.RED) {
				System.out.println("Red robot failed to initialize");
			} else {
				System.out.println("Blue robot failed to initialize");
			}
		} finally {
			// add robot to the id-robot hashmap
			idRobotMap.put(robot.me.id, robot);
			// add id to the beginning of the robot queue.
			idQueue.addFirst(robot.me.id);
			if (robot.me.unitType == SPECS.Castle) {
				castles.add(robot.me);
			}
		}
	}

	private void removeRobot(int id) {
		// if it is a castle, remove it from castles
		Unit unit = getUnit(id);
		if (unit.unitType == SPECS.Castle) {
			castles.remove(unit);
		}
		// remove from hashmap
		idRobotMap.remove(id);
		// remove from turn queue
		idQueue.remove(Integer.valueOf(id));
	}

	private void removeAllRobots(Collection<Integer> ids) {
		for (int id : ids) {
			removeRobot(id);
		}
	}

	public Winner playGame(int seed) {

		map = new Map(seed);

		// Add initial castle locations
		for (InitialCastle castleInfo : map.getCastleLocations()) {
			
			makeRobot(castleInfo.getX(), castleInfo.getY(), castleInfo.getTeam(), SPECS.Castle);
		}

		// Run game until one wins or turn reaches 1000.
		boolean redWon = false;
		boolean blueWon = false;
		int turn = 0;
		while (!redWon && !blueWon && turn < 1000) {
			redWon = true;
			blueWon = true;

			for (Iterator<Integer> ids = idQueue.iterator(); ids.hasNext();) {
				Integer id = ids.next();
				WCRobot robot = getRobot(id);
				if (robot.me.team == Team.RED) {
					blueWon = false;
				} else {
					redWon = false;
				}

				robot._do_turn();
			}

			turn++;
		}

		if (redWon) {
			return Winner.RED;
		} else if (blueWon) {
			return Winner.BLUE;
		} else {
			return Winner.TIE;
		}
	}

	protected final static int distanceSquared(Unit unit1, Unit unit2) {
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
