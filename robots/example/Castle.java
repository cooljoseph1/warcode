package example;

import warcode.SPECS;
import warcode.WCRobot;

public class Castle extends RobotHelper {
	private final static int[][] directions = { { 1, 0 }, { 1, 1 }, { 0, 1 }, { -1, 1 }, { -1, 0 }, { -1, -1 }, { 0, -1 },
			{ 1, -1 } };

	private int peasantsBuilt = 0;

	public Castle(WCRobot robot) {
		super(robot);
		// initialize castle

	}

	@Override
	public void turn() {

		// If the castle has built fewer than 6 peasants, first try to build a peasant
		if (peasantsBuilt < 0 && robot.getGold() >= SPECS.Peasant.CONSTRUCTION_GOLD
				&& robot.getWood() >= SPECS.Peasant.CONSTRUCTION_WOOD) {
			// Check all directions to see if they are open
			for (int[] direction : directions) {
				int x = robot.me.getX() + direction[0];
				int y = robot.me.getY() + direction[1];

				// The robot must first check if a location is on the map, otherwise they may
				// throw an error
				// when checking if the location is open.
				if (robot.isOnMap(x, y) && robot.isOpen(x, y)) {
					// If the location is open, build a peasant
					robot.buildUnit(x, y, SPECS.Peasant);
					peasantsBuilt++;
					return;
				}
			}
		}

		System.out.println("I have " + robot.getGold() + " gold and " + robot.getWood() + " wood.");
		
		// Build a knight if it can
		if (robot.getGold() >= SPECS.Archer.CONSTRUCTION_GOLD && robot.getWood() >= SPECS.Archer.CONSTRUCTION_WOOD) {
			// Check all directions to see if they are open
			for (int[] direction : directions) {
				int x = robot.me.getX() + direction[0];
				int y = robot.me.getY() + direction[1];

				// The robot must first check if a location is on the map, otherwise they may
				// throw an error
				// when checking if the location is open.
				if (robot.isOnMap(x, y) && robot.isOpen(x, y)) {
					// If the location is open, build a knight
					robot.buildUnit(x, y, SPECS.Archer);
					return;
				}
			}
		}

	}

}
