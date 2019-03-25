package actions;

public class CollectAction extends Action {
	public final int id;
	public final int x;
	public final int y;

	public CollectAction(int id, int x, int y) {
		super(ActionType.COLLECT);
		this.id = id;
		this.x = x;
		this.y = y;
	}

	public CollectAction(String string) {
		super(ActionType.COLLECT);
		String parts[] = string.split(", ");
		id = Integer.parseInt(parts[0]);
		x = Integer.parseInt(parts[1]);
		y = Integer.parseInt(parts[2]);
	}

	public CollectAction(String[] parts) {
		super(ActionType.COLLECT);
		id = Integer.parseInt(parts[0]);
		x = Integer.parseInt(parts[1]);
		y = Integer.parseInt(parts[2]);
	}

	@Override
	public String toString() {
		return String.join(", ", actionType.toString(), Integer.toString(id), Integer.toString(x), Integer.toString(y));
	}

}
