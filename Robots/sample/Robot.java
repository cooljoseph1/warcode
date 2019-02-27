package sample;

import warcode.Engine;
import warcode.GameException;
import warcode.SPECS;
import warcode.Unit;
import warcode.WCRobot;

public class Robot extends WCRobot {
	public Robot(Unit unit, Engine engine) {
		super(unit, engine);
		// Do anything you want to on initialization.
	}

	// You must override the turn method in your bot.
	@Override
	public void turn() throws GameException {
		if (me.unitType == SPECS.Castle) {
			System.out.println(getGold());
			if (getGold() >= SPECS.Peasant.CONSTRUCTION_GOLD && getWood() >= SPECS.Peasant.CONSTRUCTION_WOOD) {
				this.buildUnit(me.getX() + 1, me.getY() + 1, SPECS.Peasant);
				System.out.println("Just built a peasant!");
			}
		}
	}
}
