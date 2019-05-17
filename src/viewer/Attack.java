package viewer;

import warcode.Team;

public class Attack {
	public final int startX;
	public final int startY;
	public final int endX;
	public final int endY;
	public final Team team;

	public Attack(int startX, int startY, int endX, int endY, Team team) {
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		this.team = team;
	}

}
