package warcode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public final class SPECS {
	public static final UnitType Castle = UnitType.CASTLE;
	public static final UnitType Peasant = UnitType.PEASANT;
	public static final UnitType Archer = UnitType.ARCHER;
	public static final UnitType Mage = UnitType.MAGE;
	public static final UnitType Knight = UnitType.KNIGHT;

	public static final int MAX_RESOURCES = 100;
	public static final int MINE_AMOUNT = 10;
	public static final int COLLECT_AMOUNT = 10;

	public static final int GOLD_MINE_AMOUNT = 2000;
	public static final int TREE_AMOUNT = 200;

	public static final int MAX_MAP_SIZE = 100;
	public static final int MIN_MAP_SIZE = 30;

	public static final long INITIAL_TIME = 20000000; // time in nanoseconds - 20 milliseconds
	public static final long INCREMENT_TIME = 5000000; // time in nanoseconds - 5 milliseconds

	public static final int INITIAL_GOLD = 500;
	public static final int INITIAL_WOOD = 0;

	public static final Set<String> DISALLOWED_CLASSES = new HashSet<String>();

	static {
		try {
			BufferedReader reader = new BufferedReader(new FileReader("resources/DisallowedClasses.txt"));
			String line = reader.readLine();
			while (line != null) {
				DISALLOWED_CLASSES.add(line);
				line = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
