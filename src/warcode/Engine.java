package warcode;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class Engine {
	public final Constructor<WCRobot> redConstructor;
	public final Constructor<WCRobot> blueConstructor;
	
	private boolean redWon = false;
	private boolean blueWon = false;
	private Map map;
	
	
	private LinkedList<WCRobot> wcrobots = new LinkedList<WCRobot>();
	Set<Integer> ids = new HashSet<Integer>();
	
	public Engine(Class<WCRobot> red, Class<WCRobot> blue) throws NoSuchMethodException {
		try {
			this.redConstructor = red.getDeclaredConstructor(new Class[] {Unit.class});
		} catch (NoSuchMethodException e) {
			System.out.println("Red failed to initialize due to a bad constructor.");
			throw e;
		}
		try {
			this.blueConstructor = blue.getDeclaredConstructor(new Class[] {Unit.class});
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
	
	private void addRobot(Unit unit, Team team) {
		try {
			if(team == Team.RED) {
				wcrobots.add(redConstructor.newInstance(unit));
			} else {
				wcrobots.add(blueConstructor.newInstance(unit));
			}
		} catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
			if(team == Team.RED) {
				System.out.println("Red robot failedto initialize");
			} else {
				System.out.println("Blue robot failedto initialize");
			}
		}
	}
	
	public boolean isOpen(int x, int y) {
		if(!map.isOpen(x, y)) {
			return false;
		}
		for(WCRobot robot : wcrobots) {
			if(x == robot.me.getX() && y == robot.me.getY()) {
				return false;
			}
		}
		return true;
	}
	public boolean isOnMine(int x, int y) {
		return (map.get(x,y) == 2); //2 means it is on a mine.
	}
	public void decreaseGold(int x, int y, int amount) {
		map.decreaseGold(x, y, amount);
	}
	public boolean isOnTree(int x, int y) {
		return (map.get(x, y) == 3); //3 means it is a tree location.
	}
	public void decreaseWood(int x, int y, int amount) {
		map.decreaseWood(x, y, amount);
	}
	
	public void playGame(int seed) {
		map = new Map(seed);
		
		//add initial castle locations, alternating red then blue
		Team currentTeam = Team.RED;
		for(int[] location : map.getAlternatingCastleLocations()) {
			int id = (int) (Math.random()*(Math.pow(2, 16) - 1) + 1);
			while(ids.contains(id)) {
				id = (int) (Math.random()*(Math.pow(2, 16) - 1) + 1);
			}
			Unit castle = new Unit(id, SPECS.Castle, currentTeam, location[0], location[1]);
			addRobot(castle, currentTeam);
			ids.add(id);
			if(currentTeam == Team.RED) {
				currentTeam = Team.BLUE;
			} else {
				currentTeam = Team.RED;
			}
		}
		
		while(!redWon && !blueWon) {
			for(WCRobot robot: wcrobots) {
				robot._do_turn();
			}
		}
	}
}
