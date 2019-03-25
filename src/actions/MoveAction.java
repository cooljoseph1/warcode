package actions;


public class MoveAction extends Action {
	public final int id;
	public final int x;
	public final int y;

	public MoveAction(int id, int x, int y) {
		super(ActionType.MOVE);
		this.id = id;
		this.x = x;
		this.y = y;
	}

	public MoveAction(String string) {
		super(ActionType.MOVE);
		String[] parts = string.split(", ");
		id = Integer.parseInt(parts[0]);
		x = Integer.parseInt(parts[1]);
		y = Integer.parseInt(parts[2]);
	}
	
	public MoveAction(String[] parts) {
		super(ActionType.MOVE);
		id = Integer.parseInt(parts[0]);
		x = Integer.parseInt(parts[1]);
		y = Integer.parseInt(parts[2]);
	}

	@Override
	public String toString() {
		return String.join(", ", actionType.toString(), Integer.toString(id), Integer.toString(x), Integer.toString(y));
	}

}
