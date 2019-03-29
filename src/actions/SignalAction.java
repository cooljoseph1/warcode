package actions;

public class SignalAction extends Action {
	public final int id;
	public final int signal;
	public final int oldSignal;

	public SignalAction(int id, int signal, int oldSignal) {
		super(ActionType.SIGNAL);
		this.id = id;
		this.signal = signal;
		this.oldSignal = oldSignal;
	}

	public SignalAction(String string) {
		this(string.split(", "));
	}

	public SignalAction(String[] parts) {
		this(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
	}

	@Override
	public String toString() {
		return String.join(", ", actionType.toString(), Integer.toString(id), Integer.toString(signal), Integer.toString(oldSignal));
	}

}
