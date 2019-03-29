package actions;

public class MoveAction extends Action {
	public final int id;
	public final int x;
	public final int y;
	public final int startX;
	public final int startY;

	public MoveAction(int id, int x, int y, int startX, int startY) {
		super(ActionType.MOVE);
		this.id = id;
		this.x = x;
		this.y = y;
		this.startX = startX;
		this.startY = startY;
	}

	public MoveAction(String string) {
		this(string.split(", "));
	}

	public MoveAction(String[] parts) {
		this(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]),
				Integer.parseInt(parts[3]), Integer.parseInt(parts[4]));
	}

	@Override
	public String toString() {
		return String.join(", ", actionType.toString(), Integer.toString(id), Integer.toString(x), Integer.toString(y), Integer.toString(startX), Integer.toString(startY));
	}

}
