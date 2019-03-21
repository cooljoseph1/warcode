package warcode;

public class SignalAction implements Action {
	public final int id;
	public final int signal;

	public SignalAction(int id, int signal) {
		this.id = id;
		this.signal = signal;
	}

	public SignalAction(String string) {
		String parts[] = string.split(", ");
		id = Integer.parseInt(parts[0]);
		signal = Integer.parseInt(parts[1]);
	}

	@Override
	public String toString() {
		return String.join(", ", "SIGNAL", Integer.toString(id), Integer.toString(signal));
	}

}
