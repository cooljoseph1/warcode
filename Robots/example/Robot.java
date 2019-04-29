package example;

import warcode.Engine;
import warcode.Unit;
import warcode.WCRobot;

public class Robot extends WCRobot {

	private final RobotHelper robot;

	private static int robotCount = 1;

	// If you want to do anything on initialization, override the constructor
	public Robot(Unit unit, Engine engine) {
		// The first call must be to use the super constructor
		super(unit, engine);

		System.out.println("I am robot " + robotCount);
		robotCount++;

		switch (unit.unitType) {
		case CASTLE:
			robot = new Castle(this);
			break;
		case PEASANT:
			robot = new Peasant(this);
			break;
		case ARCHER:
			robot = new Archer(this);
			break;
		case KNIGHT:
			robot = new Knight(this);
			break;
		case MAGE:
			robot = new Mage(this);
			break;
		default:
			robot = null;
		}

	}

	// You must override the turn method in your bot.
	@Override
	public void turn() {
		System.out.println("I am going");
		robot.turn();
	}
}
