package warcode;

import java.lang.reflect.Constructor;

import exceptions.GameException;

public class SandboxThread extends Thread {

	private final Constructor<WCRobot> constructor;
	private final Unit unit;
	private final Engine engine;

	public final Object pauseLock = new Object();
	private boolean paused;

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
			paused = true;

			// Wait for engine to let us know it is this robot's turn
			while (paused) {
				try {
					synchronized (pauseLock) {
						System.out.println("paused");
						pauseLock.wait();
					}
				} catch (InterruptedException e) {
					return;
				}
			}

			// run the turn

			robot._do_turn();

		}

	}

	public void setPaused(boolean bool) {
		paused = bool;
	}

	public synchronized void awaken() {
		System.out.println(paused);
		paused = false;
		pauseLock.notifyAll();
	}

}
