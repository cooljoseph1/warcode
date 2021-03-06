package warcode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;

public class Map {
	private Tile[][] passableMap;
	private int[][] goldMap;
	private int[][] woodMap;
	private LinkedList<InitialCastle> initialCastleLocations = new LinkedList<InitialCastle>();

	private final String origMap;
	public final int width;
	public final int height;

	public Map(String mapName) {
		try {
			origMap = new String(Files.readAllBytes(Paths.get(mapName)));

			int height = 0;
			int width = 0;

			BufferedReader reader = new BufferedReader(new FileReader(mapName));
			for (String line = reader.readLine(); line != null; line = reader.readLine()) {
				height += 1;
				width = line.length();
			}
			reader.close();

			this.width = width;
			this.height = height;

			passableMap = new Tile[height][width];

			reader = new BufferedReader(new FileReader(mapName));
			int y = 0;
			for (String line = reader.readLine(); line != null; line = reader.readLine(), y++) {
				for (int x = 0; x < line.length(); x++) {
					passableMap[y][x] = Tile.fromChar(line.charAt(x));
				}
			}
			reader.close();

		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}

		goldMap = new int[this.height][this.width];
		woodMap = new int[this.height][this.width];

		for (int y = 0; y < this.height; y++) {
			for (int x = 0; x < this.width; x++) {
				switch (passableMap[y][x]) {
				case RED_CASTLE:
					initialCastleLocations.add(new InitialCastle(Team.RED, x, y));
					passableMap[y][x] = Tile.PASSABLE;
					break;
				case BLUE_CASTLE:
					initialCastleLocations.add(new InitialCastle(Team.BLUE, x, y));
					passableMap[y][x] = Tile.PASSABLE;
					break;
				case GOLD:
					goldMap[y][x] = SPECS.GOLD_MINE_AMOUNT;
					break;
				case WOOD:
					woodMap[y][x] = SPECS.TREE_AMOUNT;
					break;
				default:
					break;
				}
			}
		}

	}

	public Map(String[] mapRows, int width, int height) {

		origMap = String.join("\n", mapRows);

		this.width = width;
		this.height = height;

		passableMap = new Tile[height][width];

		int y = 0;
		for (String line : mapRows) {
			for (int x = 0; x < line.length(); x++) {
				passableMap[y][x] = Tile.fromChar(line.charAt(x));
			}
			y++;
		}

		goldMap = new int[this.height][this.width];
		woodMap = new int[this.height][this.width];

		for (y = 0; y < this.height; y++) {
			for (int x = 0; x < this.width; x++) {
				switch (passableMap[y][x]) {
				case RED_CASTLE:
					initialCastleLocations.add(new InitialCastle(Team.RED, x, y));
					passableMap[y][x] = Tile.PASSABLE;
					break;
				case BLUE_CASTLE:
					initialCastleLocations.add(new InitialCastle(Team.BLUE, x, y));
					passableMap[y][x] = Tile.PASSABLE;
					break;
				case GOLD:
					goldMap[y][x] = SPECS.GOLD_MINE_AMOUNT;
					break;
				case WOOD:
					woodMap[y][x] = SPECS.TREE_AMOUNT;
					break;
				default:
					break;
				}
			}
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Tile[][] getPassableMapCopy() {
		Tile[][] copy = new Tile[passableMap.length][passableMap[0].length];
		for (int i = 0; i < passableMap.length; i++) {
			copy[i] = Arrays.copyOf(passableMap[i], passableMap[i].length);
		}
		return copy;
	}

	public int[][] getGoldMapCopy() {
		int[][] copy = new int[goldMap.length][goldMap[0].length];
		for (int i = 0; i < goldMap.length; i++) {
			copy[i] = Arrays.copyOf(goldMap[i], goldMap[i].length);
		}
		return copy;
	}

	public int[][] getWoodMapCopy() {
		int[][] copy = new int[woodMap.length][woodMap[0].length];
		for (int i = 0; i < woodMap.length; i++) {
			copy[i] = Arrays.copyOf(woodMap[i], woodMap[i].length);
		}
		return copy;
	}

	public boolean isOpen(int x, int y) {
		return (passableMap[y][x] == Tile.PASSABLE);
	}

	public boolean isOpenPeasant(int x, int y) {
		return (passableMap[y][x] == Tile.PASSABLE || passableMap[y][x] == Tile.GOLD);
	}

	void decreaseGold(int x, int y, int amount) {
		goldMap[y][x] -= amount;
		if (goldMap[y][x] <= 0) {
			goldMap[y][x] = 0;
		}
		if (goldMap[y][x] == 0) {
			passableMap[y][x] = Tile.IMPASSABLE; // turn the mine into an impassable square.
		}
	}

	void decreaseWood(int x, int y, int amount) {
		woodMap[y][x] -= amount;
		if (woodMap[y][x] <= 0) {
			woodMap[y][x] = 0;
		}
		if (woodMap[y][x] == 0) {
			passableMap[y][x] = Tile.PASSABLE; // turn the tree into a passable square.
		}
	}

	public Tile get(int x, int y) {
		return passableMap[y][x];
	}

	LinkedList<InitialCastle> getCastleLocations() {
		return (LinkedList<InitialCastle>) initialCastleLocations.clone();

	}

	@Override
	public String toString() {
		return origMap;
	}
}
