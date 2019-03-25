package actions;


public class AttackAction extends Action {
	public final int id;
	public final int x;
	public final int y;

	public AttackAction(int id, int x, int y) {
		super(ActionType.ATTACK);
		this.id = id;
		this.x = x;
		this.y = y;
	}

	public AttackAction(String string) {
		super(ActionType.ATTACK);
		String[] parts = string.split(", ");
		id = Integer.parseInt(parts[0]);
		x = Integer.parseInt(parts[1]);
		y = Integer.parseInt(parts[2]);
	}
	
	public AttackAction(String[] parts) {
		super(ActionType.ATTACK);
		id = Integer.parseInt(parts[0]);
		x = Integer.parseInt(parts[1]);
		y = Integer.parseInt(parts[2]);
	}

	@Override
	public String toString() {
		return String.join(", ", actionType.toString(), Integer.toString(id), Integer.toString(x), Integer.toString(y));
	}

}
