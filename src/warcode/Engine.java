package warcode;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

public class Engine {
	public final Constructor<WCRobot> redConstructor;
	public final Constructor<WCRobot> blueConstructor;

	private Map map;

	
	Set<Integer> ids = new HashSet<Integer>();
	private HashMap<Integer, WCRobot> idRobotMap = new HashMap<Integer, WCRobot>();

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

	public int[][] getPassableMap() {
		return map.getPassableMapCopy();
	}

	public int[][] getGoldMap() {
		return map.getGoldMapCopy();
	}

	public int[][] getWoodMap() {
		return map.getWoodMapCopy();
	}


	public boolean isOpen(int x, int y) {
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

	public boolean isOnMine(int x, int y) {
		return (map.get(x, y) == 2); // 2 means it is on a mine.
	}

	public void decreaseGold(int x, int y, int amount) {
		map.decreaseGold(x, y, amount);
	}

	public boolean isOnTree(int x, int y) {
		return (map.get(x, y) == 3); // 3 means it is a tree location.
	}

	public void decreaseWood(int x, int y, int amount) {
		map.decreaseWood(x, y, amount);
	}
	
	private void addRobot(Unit unit, Team team) {
		WCRobot robot;
		try {
			if (team == Team.RED) {
				robot = redConstructor.newInstance(unit);
			} else {
				robot = blueConstructor.newInstance(unit);
			}

			idRobotMap.put(robot.me.id, robot);
		} catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
			if (team == Team.RED) {
				System.out.println("Red robot failed to initialize");
			} else {
				System.out.println("Blue robot failed to initialize");
			}
		}
	}
	
	private void removeRobot(int id) {
		idRobotMap.remove(id);
	}

	public Winner playGame(int seed) {

		map = new Map(seed);

		// add initial castle locations, alternating red then blue
		Team currentTeam = Team.RED;
		for (int[] location : map.getAlternatingCastleLocations()) {
			int id = (int) (Math.random() * (Math.pow(2, 16) - 1) + 1);
			while (ids.contains(id)) {
				id = (int) (Math.random() * (Math.pow(2, 16) - 1) + 1);
			}
			Unit castle = new Unit(id, SPECS.Castle, currentTeam, location[0], location[1]);
			addRobot(castle, currentTeam);
			ids.add(id);
			if (currentTeam == Team.RED) {
				currentTeam = Team.BLUE;
			} else {
				currentTeam = Team.RED;
			}
		}

		// Run game until one wins or turn reaches 1000.
		boolean redWon = false;
		boolean blueWon = false;
		int turn = 0;
		while (!redWon && !blueWon && turn < 1000) {
			redWon = true;
			blueWon = true;

			for (Iterator<WCRobot> robotIterator = wcrobots.iterator(); robotIterator.hasNext();) {
				WCRobot robot = robotIterator.next();
				if (robot == null) { // A null robot is one that was killed since its last turn.
					//remove the robot from the hashmap and the iterator.
					idRobotMap.remove(robot.me.id);
					robotIterator.remove();
					
				}
				if (robot.me.team == Team.RED) {
					blueWon = false;
				} else {
					redWon = false;
				}
				robot._do_turn();
			}

			turn++;
		}
		
		if(redWon) {
			return Winner.RED;
		} else if(blueWon) {
			return Winner.BLUE;
		} else {
			return Winner.TIE;
		}
	}
}
