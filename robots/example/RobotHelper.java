package example;

import warcode.WCRobot;

public abstract class RobotHelper {
	final WCRobot robot;

	int[][] directions = { { 1, 0 }, { 1, 1 }, { 0, 1 }, { -1, 1 }, { 1, 0 }, { -1, -1 }, { 0, -1 }, { 1, -1 } };

	public RobotHelper(WCRobot robot) {
		this.robot = robot;
	}

	public abstract void turn();

	int distanceSquared(int x1, int y1, int x2, int y2) {
		return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
	}

}
