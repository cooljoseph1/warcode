package example;

import warcode.WCRobot;

public abstract class RobotHelper {
	final WCRobot robot;

	public RobotHelper(WCRobot robot) {
		this.robot = robot;
	}

	public abstract void turn();

}
