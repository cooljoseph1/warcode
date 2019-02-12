package warcode;

import java.util.Arrays;
import java.util.LinkedList;

public class Map {
	private Tile[][] passableMap;
	private int[][] goldMap;
	private int[][] woodMap;
	private LinkedList<InitialCastle> initialCastleLocations;
	public final int width;
	public final int height;

	public Map(int seed) {
		width = 100;
		height = 100;
		// TODO: make map from a file.

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

	protected void decreaseGold(int x, int y, int amount) {
		goldMap[y][x] -= amount;
		if (goldMap[y][x] <= 0) {
			goldMap[y][x] = 0;
		}
		if (goldMap[y][x] == 0) {
			passableMap[y][x] = Tile.IMPASSABLE; // turn the mine into an impassable square.
		}
	}

	protected void decreaseWood(int x, int y, int amount) {
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

	protected LinkedList<InitialCastle> getCastleLocations() {
		return (LinkedList<InitialCastle>) initialCastleLocations.clone();

	}
}
