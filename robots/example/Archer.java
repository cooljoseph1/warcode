package example;

import warcode.Unit;
import warcode.WCRobot;

public class Archer extends RobotHelper {

	public Archer(WCRobot robot) {
		super(robot);
		// initialize castle

	}

	@Override
	public void turn() {
		// archer turn

		Unit[] units = robot.getVisibleUnits();
		for (Unit unit : units) {
			if (unit.getTeam() == robot.me.getTeam()) {
				continue;
			}

			if (distanceSquared(robot.me.getX(), robot.me.getY(), unit.getX(),
					unit.getY()) <= robot.me.getUnitType().ATTACK_RADIUS) {
				robot.attack(unit.getX(), unit.getY());
				return;
			}
		}

	}

}
