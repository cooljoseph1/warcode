package actions;

public class MineAction extends Action {
	public final int id;

	public MineAction(int id) {
		super(ActionType.MINE);
		this.id = id;
	}

	public MineAction(String string) {
		super(ActionType.MINE);
		String[] parts = string.split(", ");
		id = Integer.parseInt(parts[0]);
	}

	public MineAction(String[] parts) {
		super(ActionType.MINE);
		id = Integer.parseInt(parts[0]);
	}

	@Override
	public String toString() {
		return String.join(", ", actionType.toString(), Integer.toString(id));
	}

}
