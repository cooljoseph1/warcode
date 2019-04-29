package warcode;

import java.lang.reflect.Constructor;

import exceptions.GameException;

public class SandboxThread extends Thread {

	private final Constructor<WCRobot> constructor;
	private final Unit unit;
	private final Engine engine;
	
	public final Object pauseLock = new Object();

	WCRobot robot;

	public SandboxThread(Constructor<WCRobot> constructor, Unit unit, Engine engine) {
		this.constructor = constructor;
		this.unit = unit;
		this.engine = engine;
		
		run();
	}

	public void run() {
		try {
			robot = constructor.newInstance(unit, engine);
		} catch (Exception e) {
			throw new GameException("Robot failed to initialize");
		}
		while (robot.isAlive()) {
			try {
				synchronized (pauseLock) {
					System.out.println("paused");
					wait();
				}
			} catch (InterruptedException e) {
				return;
			}

			robot._do_turn();

		}

	}

}
