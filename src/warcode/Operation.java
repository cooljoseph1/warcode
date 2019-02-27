package warcode;

public class Operation {
	private final OperationType operationType;
	public final int unitId;
	public final int x;
	public final int y;
	
	public final int newRobotId;
	
	public Operation(String string) {
		String parts[] = string.split(",");
		operationType = OperationType.fromString(parts[0]);
		unitId = Integer.parseInt(parts[1]);
		x = Integer.parseInt(parts[2]);
		y = Integer.parseInt(parts[3]);
		
		newRobotId = Integer.parseInt(parts[4]);
	}
	
	public Operation(OperationType operationType, int unitId, int x, int y) {
		this(operationType, unitId, x, y, -1);
	}
	public Operation(OperationType operationType, int unitId, int x, int y, int newRobotId) {
		this.operationType = operationType;
		this.unitId = unitId;
		this.x = x;
		this.y = y;
		this.newRobotId = newRobotId;
	}
	
	@Override
	public String toString() {
		return operationType.toString() + "," + unitId + "," + x + "," + y + "," + newRobotId;
	}
}
