package warcode;

public class MoveAction implements Action {
	public final int id;
	public final int x;
	public final int y;

	public MoveAction(int id, int x, int y) {
		this.id = id;
		this.x = x;
		this.y = y;
	}

	public MoveAction(String string) {
		String parts[] = string.split(", ");
		id = Integer.parseInt(parts[0]);
		x = Integer.parseInt(parts[3]);
		y = Integer.parseInt(parts[4]);
	}

	@Override
	public String toString() {
		return String.join(", ", "MOVE", Integer.toString(id), Integer.toString(x), Integer.toString(y));
	}

}
