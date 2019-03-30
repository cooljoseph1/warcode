package example;

import warcode.WCRobot;

public abstract class Robot {
	final WCRobot robot;

	public Robot(WCRobot robot) {
		this.robot = robot;
	}

	public abstract void turn();

}
