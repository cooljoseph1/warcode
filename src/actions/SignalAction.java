package actions;

public class SignalAction extends Action {
	public final int id;
	public final int signal;

	public SignalAction(int id, int signal) {
		super(ActionType.SIGNAL);
		this.id = id;
		this.signal = signal;
	}

	public SignalAction(String string) {
		super(ActionType.SIGNAL);
		String parts[] = string.split(", ");
		id = Integer.parseInt(parts[0]);
		signal = Integer.parseInt(parts[1]);
	}

	public SignalAction(String[] parts) {
		super(ActionType.SIGNAL);
		id = Integer.parseInt(parts[0]);
		signal = Integer.parseInt(parts[1]);
	}

	@Override
	public String toString() {
		return String.join(", ", actionType.toString(), Integer.toString(id), Integer.toString(signal));
	}

}
