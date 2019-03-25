package actions;


public class GiveAction extends Action {
	public final int id;
	public final int x;
	public final int y;

	public GiveAction(int id, int x, int y) {
		super(ActionType.GIVE);
		this.id = id;
		this.x = x;
		this.y = y;
	}

	public GiveAction(String string) {
		super(ActionType.GIVE);
		String[] parts = string.split(", ");
		id = Integer.parseInt(parts[0]);
		x = Integer.parseInt(parts[1]);
		y = Integer.parseInt(parts[2]);
	}
	
	public GiveAction(String[] parts) {
		super(ActionType.GIVE);
		id = Integer.parseInt(parts[0]);
		x = Integer.parseInt(parts[1]);
		y = Integer.parseInt(parts[2]);
	}

	@Override
	public String toString() {
		return String.join(", ", actionType.toString(), Integer.toString(id), Integer.toString(x), Integer.toString(y));
	}

}
