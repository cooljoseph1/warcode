package warcode;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class Engine {
	public final Constructor<WCRobot> redConstructor;
	public final Constructor<WCRobot> blueConstructor;
	
	
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
		return map.passableMap.toArray();
	}
	public int[][] getGoldMap() {
		return map.goldMap.toArray();
	}
	public int[][] getWoodMap() {
		return map.woodMap.toArray();
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
		
		
	}
}
