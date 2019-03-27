package actions;

public class GiveAction extends Action {
	public final int id;
	public final int x;
	public final int y;
	public final int wood;
	public final int gold;

	public GiveAction(int id, int x, int y, int wood, int gold) {
		super(ActionType.GIVE);
		this.id = id;
		this.x = x;
		this.y = y;
		this.wood = wood;
		this.gold = gold;
	}

	public GiveAction(String string) {
		this(string.split(", "));
	}

	public GiveAction(String[] parts) {
		this(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]),
				Integer.parseInt(parts[3]), Integer.parseInt(parts[4]));
	}

	@Override
	public String toString() {
		return String.join(", ", actionType.toString(), Integer.toString(id), Integer.toString(x), Integer.toString(y));
	}

}
