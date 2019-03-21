package warcode;

public class MineAction implements Action {
	public final int id;

	public MineAction(int id) {
		this.id = id;
	}

	public MineAction(String string) {
		String parts[] = string.split(", ");
		id = Integer.parseInt(parts[0]);
	}

	@Override
	public String toString() {
		return String.join(", ", "MINE", Integer.toString(id));
	}

}
