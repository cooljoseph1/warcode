package warcode;

public class InitialCastle {
	private final int x;
	private final int y;
	private final Team team;
	/**
	 * Container for a starting castle location
	 * @param team
	 * @param x
	 * @param y
	 */
	public InitialCastle(Team team, int x, int y) {
		this.x = x;
		this.y = y;
		this.team = team;
	}
	
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public Team getTeam() {
		return team;
	}
}
