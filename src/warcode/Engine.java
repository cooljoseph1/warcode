package warcode;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.StringJoiner;

import actions.Action;
import actions.BuildAction;
import actions.DieAction;
import exceptions.GameException;

public class Engine {

	private final Constructor<WCRobot> redConstructor;
	private final Constructor<WCRobot> blueConstructor;

	private Map map;

	// stores a list of all alive units, in order of the turn queue
	private LinkedList<Integer> aliveIdQueue = new LinkedList<Integer>();
	// maps the ids to all robots every created
	private HashMap<Integer, WCRobot> idRobotMap = new HashMap<Integer, WCRobot>();

	// List of initial castles on the map
	private LinkedList<Unit> castles = new LinkedList<Unit>();

	// Gold and wood the teams have
	private int redGold;
	private int redWood;
	private int blueGold;
	private int blueWood;

	// Turn the game is on
	private int turn = 0;

	// Save method -- may change this
	private StringJoiner saveInfo = new StringJoiner("\n");
	private StringJoiner turnActions;

	// End winner. Is not set until after a game is played
	private Winner winner;

	/**
	 * Makes an engine class. This engine is what runs a game. Pass the classes for
	 * the red and blue teams to the engine and then use playGame to play a game.
	 * 
	 * @param red
	 * @param blue
	 * @throws NoSuchMethodException
	 */
	public Engine(String pathToRed, String pathToBlue) throws NoSuchMethodException {
		try {
			redConstructor = loadConstructor(pathToRed);
		} catch (GameException e) {
			throw new GameException("Failure loading red class", e);
		}

		try {
			blueConstructor = loadConstructor(pathToBlue);
		} catch (GameException e) {
			throw new GameException("Failure loading blue class", e);
		}
	}

	/**
	 * Plays a game on the given map
	 * 
	 * @param mapName
	 * @return
	 */
	public Winner playGame(String mapName) {

		// setup the game
		map = new Map(mapName);

		// Add initial castle locations
		turnActions = new StringJoiner("; ");
		for (InitialCastle castleInfo : map.getCastleLocations()) {

			makeRobot(castleInfo.getX(), castleInfo.getY(), castleInfo.getTeam(), SPECS.Castle, false);
		}
		saveInfo.add(turnActions.toString());

		// Set initial resources
		redGold = SPECS.INITIAL_GOLD;
		redWood = SPECS.INITIAL_WOOD;

		blueGold = SPECS.INITIAL_GOLD;
		blueWood = SPECS.INITIAL_WOOD;

		// -----------------------------------------------------------------------------------------------------
		// Run the game

		// Run game until one is eliminated or turn reaches 1000.
		boolean redWon = false;
		boolean blueWon = false;
		turn = 0;

		while (!redWon && !blueWon && turn < 1000) {
			// string joiner of the operations that occurred in the turn.
			turnActions = new StringJoiner("; ");

			redWon = true;
			blueWon = true;

			for (int id : new LinkedList<Integer>(aliveIdQueue)) {
				WCRobot robot = getRobot(id);
				if (robot.me.team == Team.RED) {
					blueWon = false;
				} else {
					redWon = false;
				}

				// let the robot take a turn
				robot.me.setTurnTaken(false);
				robot._do_turn();
			}

			turn++;
			// Add turn's operations to save info, so it can be saved
			saveInfo.add(turnActions.toString());
		}

		// TODO: Add in tiebreaking
		if (redWon) {
			winner = Winner.RED;
		} else if (blueWon) {
			winner = Winner.BLUE;
		} else {
			winner = Winner.TIE;
		}
		return winner;
	}

	/**
	 * Returns the winner of the game
	 * 
	 * @return
	 */
	public Winner getWinner() {
		return winner;
	}

	/**
	 * Saves the game to the file given
	 * 
	 * @param fileName
	 */
	public void save(String fileName) {
		try {
			FileWriter writer = new FileWriter(fileName);
			// save turn, with, height, and then all of the operations
			writer.write(winner + "\n");
			writer.write(turn + "\n");
			writer.write(map.getWidth() + "\n");
			writer.write(map.getHeight() + "\n");
			writer.write(map.toString() + "\n");
			writer.write(saveInfo.toString());
			writer.close();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	void addAction(Action operation) {
		turnActions.add(operation.toString());
	}

	Tile[][] getPassableMap() {
		return map.getPassableMapCopy();
	}

	int[][] getGoldMap() {
		return map.getGoldMapCopy();
	}

	int[][] getWoodMap() {
		return map.getWoodMapCopy();
	}

	int[][] getVisibleUnitMap(Unit unit) {
		int[][] visibleUnitMap = new int[map.height][map.width];

		for (int id : aliveIdQueue) {
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

	Unit[] getVisibleUnits(Unit unit) {
		LinkedList<Unit> units = new LinkedList<Unit>();
		for (int id : aliveIdQueue) {
			Unit tempUnit = getUnit(id);
			if (distanceSquared(tempUnit.getX(), tempUnit.getY(), unit.getX(),
					unit.getY()) <= unit.unitType.VISION_RADIUS) {
				units.add(tempUnit);
			}
		}

		return units.toArray(new Unit[units.size()]);
	}

	boolean isOnMap(int x, int y) {
		return (x >= 0 && y >= 0 && x < map.getWidth() && y < map.getHeight());
	}

	boolean isOpen(int x, int y) {
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

	boolean isOnMine(int x, int y) {
		return (map.get(x, y) == Tile.GOLD);
	}

	boolean isOnTree(int x, int y) {
		return (map.get(x, y) == Tile.WOOD);
	}

	boolean isOnCastle(int x, int y) {
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
	void giveResources(int x, int y, int wood, int gold) {
		Unit castleAtLocation = null;
		for (Unit castle : castles) {
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

	void decreaseGold(int x, int y, int amount) {
		map.decreaseGold(x, y, amount);
	}

	void decreaseWood(int x, int y, int amount) {
		map.decreaseWood(x, y, amount);
	}

	void addRedGold(int amount) {
		redGold += amount;
	}

	void addBlueGold(int amount) {
		blueGold += amount;
	}

	void addRedWood(int amount) {
		redWood += amount;
	}

	void addBlueWood(int amount) {
		blueWood += amount;
	}

	int getRedGold() {
		return redGold;
	}

	int getRedWood() {
		return redWood;
	}

	int getBlueGold() {
		return blueGold;
	}

	int getBlueWood() {
		return blueWood;
	}

	void attack(int x, int y, UnitType unitType) {
		LinkedList<Integer> idsToRemove = new LinkedList<Integer>();
		for (int id : aliveIdQueue) {
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

	/**
	 * 
	 * @param x
	 * @param y
	 * @param team
	 * @param unitType
	 * @return id of new unit
	 */
	int makeRobot(int x, int y, Team team, UnitType unitType) {
		return makeRobot(x, y, team, unitType, true);
	}

	int makeRobot(int x, int y, Team team, UnitType unitType, boolean subtractResources) {
		int id;
		do {
			id = (int) (Math.random() * (Math.pow(2, 16) - 1) + 1);
		} while (idRobotMap.containsKey(id));
		Unit unit = new Unit(id, unitType, team, x, y);

		addRobot(unit, team);

		if (subtractResources) {
			if (team == Team.RED) {
				redGold -= unitType.CONSTRUCTION_GOLD;
				redWood -= unitType.CONSTRUCTION_WOOD;
			} else {
				blueGold -= unitType.CONSTRUCTION_GOLD;
				blueWood -= unitType.CONSTRUCTION_WOOD;
			}
		}

		addAction(new BuildAction(unit.getId(), unit.getTeam(), unit.unitType, unit.getX(), unit.getY()));

		return id;

	}

	void kill(int id) {
		removeRobot(id);
		System.out.println("Killed " + id);
	}

	Unit getUnit(int id) {
		return idRobotMap.get(id).me;
	}

	Unit getUnitAtLocation(int x, int y) {
		for (WCRobot robot : idRobotMap.values()) {
			Unit unit = robot.me;
			if (unit.getX() == x && unit.getY() == y) {
				return unit;
			}
		}

		return null;
	}

	private WCRobot getRobot(int id) {
		return idRobotMap.get(id);
	}

	private void addRobot(Unit unit, Team team) {
		WCRobot robot = null;
		try {
			long startTime = System.nanoTime();
			if (team == Team.RED) {
				robot = redConstructor.newInstance(unit, this);
			} else {
				robot = redConstructor.newInstance(unit, this);
			}
			robot.subtractTime(System.nanoTime() - startTime);

		} catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
			System.out.println("caught");
			if (team == Team.RED) {
				System.out.println("Red robot failed to initialize");
			} else {
				System.out.println("Blue robot failed to initialize");
			}
		} finally {
			// add robot to the id-robot hashmap
			idRobotMap.put(robot.me.id, robot);
			// add id to the beginning of the robot queue.
			aliveIdQueue.addFirst(robot.me.id);
			if (robot.me.unitType == SPECS.Castle) {
				castles.add(robot.me);
			}
		}
	}

	private Constructor<WCRobot> loadConstructor(String pathToClass) {
		Class<WCRobot> robotClass = loadRobotClass(pathToClass);

		// Get all variables
		Field[] fields = robotClass.getDeclaredFields();

		// Make sure the robot isn't using static variables
		for (Field field : fields) {
			if (Modifier.isStatic(field.getModifiers()) && !Modifier.isFinal(field.getModifiers())) {
				// check to see if the variable is in the switch table, as these are declared
				// static, but we want
				// switch statements to be allowed
				System.out.println(field);
				
				field.setAccessible(true);
				
				Field modifiers = null;
				try {
					modifiers = Field.class.getDeclaredField("modifiers");
					modifiers.setAccessible(true);
					modifiers.setInt(field, field.getModifiers() | Modifier.FINAL);
				} catch (ReflectiveOperationException e) {
					throw new RuntimeException(e);
				}

				// System.out.println(field.getName());
				if (field.getName().length() < 14 || !field.getName().substring(0, 14).equals("$SWITCH_TABLE$")) {
					System.out.println("WARNING: Static variables will become final");
				}
			}
		}

		Constructor<WCRobot> constructor = null;
		try {
			return (Constructor<WCRobot>) robotClass.getConstructor(Unit.class, Engine.class);
		} catch (NoSuchMethodException e) {
			throw new GameException(e);
		}
	}

	private static Class<WCRobot> loadRobotClass(String pathToPackage) {
		String path = pathToPackage.replace(".", "/").replace("\\", "/");

		int lastPart = path.lastIndexOf("/");
		String packagePart = path.substring(lastPart + 1);
		String pathPart = path.substring(0, lastPart);

		File file = new File(pathPart);

		try {

			URL url = file.toURI().toURL();
			URL[] urls = new URL[] { url };

			// Create a new class loader with the directory

			URLClassLoader cl = new URLClassLoader(urls, new CustomLoader(Engine.class.getClassLoader()));
			// Load the class
			Class<WCRobot> c = (Class<WCRobot>) cl.loadClass(packagePart + "." + "Robot");

			cl.close();

			return c;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private void removeRobot(int id) {
		// if it is a castle, remove it from castles
		Unit unit = getUnit(id);

		addAction(new DieAction(unit.getId(), unit.getTeam(), unit.unitType, unit.getX(), unit.getY()));

		if (unit.unitType == SPECS.Castle) {
			castles.remove(unit);
		}
		// remove from hashmap
		idRobotMap.remove(id);
		// remove from turn queue
		aliveIdQueue.remove(Integer.valueOf(id));
	}

	private void removeAllRobots(Collection<Integer> ids) {
		for (int id : ids) {
			removeRobot(id);
		}
	}

	final static int distanceSquared(Unit unit1, Unit unit2) {
		int dx = unit1.getX() - unit2.getX();
		int dy = unit1.getY() - unit2.getY();
		return dx * dx + dy * dy;
	}

	final static int distanceSquared(int x1, int y1, int x2, int y2) {
		int dx = x1 - x2;
		int dy = y1 - y2;
		return dx * dx + dy * dy;
	}

	public static void main(String[] args) {

		// This is for executing from a jar file
		File jarPath = new File(Engine.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		String path = jarPath.getParentFile().getAbsolutePath() + "\\";

		Engine engine;
		try {
			engine = new Engine(path + args[0], path + args[1]);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}

		Runtime.getRuntime().addShutdownHook(new ShutdownHook(engine, args[3]));
		System.out.println(engine.playGame(args[2]));
	}

}

class ShutdownHook extends Thread {
	private String saveFile;
	private Engine engine;

	public ShutdownHook(Engine engine, String saveFile) {
		super();

		this.engine = engine;
		this.saveFile = saveFile;
	}

	public void run() {
		System.out.println("Saving...");
		engine.save(saveFile);
		System.out.println("Saved game nicely.");
	}
}

class CustomLoader extends ClassLoader {
	public CustomLoader(ClassLoader parent) {
		super(parent);
	}
}