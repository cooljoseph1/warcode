package sample;

import java.util.Random;

import exceptions.GameException;
import warcode.Engine;
import warcode.SPECS;
import warcode.Unit;
import warcode.WCRobot;

public class Robot extends WCRobot {
	private final int[][] directions = new int[][] { { 1, 0 }, { 1, 1 }, { 0, 1 }, { -1, 1 }, { -1, 0 }, { -1, -1 },
			{ 0, -1 }, { 1, -1 } };

	private final Random random = new Random();

	public Robot(Unit unit, Engine engine) {
		super(unit, engine);
		// Do anything you want to on initialization.
	}

	// You must override the turn method in your bot.
	@Override
	public void turn() throws GameException {
		// System.out.println("I am a " + me.unitType);
		if (me.unitType == SPECS.Castle) {
			if (getGold() >= SPECS.Peasant.CONSTRUCTION_GOLD && getWood() >= SPECS.Peasant.CONSTRUCTION_WOOD) {

				int[] direction = directions[random.nextInt(8)];
				int x = me.getX() + direction[0];
				int y = me.getY() + direction[1];

				if (isOnMap(x, y) && isOpen(x, y)) {
					buildUnit(x, y, SPECS.Peasant);
					System.out.println("Just built a peasant!");
				}
			}
		}
	}
}
