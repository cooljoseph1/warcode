package viewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import actions.Action;
import warcode.Tile;
import warcode.Unit;

public class Display extends JPanel {

	private static final long serialVersionUID = 5268301898468656990L;
	private static final Color FOREST_GREEN = new Color(0, 153, 0);

	private Window window;

	private int displayWidth;
	private int displayHeight;
	private int mapWidth;
	private int mapHeight;
	private double scaleSize;
	private int top;
	private int left;

	private Tile[][] tileMap;

	private ViewerEngine engine;

	public Display(int width, int height) {
		super();

		window = new Window(this);

		window.setPreferredSize(new Dimension(width, height));
		window.setLayout(new BorderLayout());
		window.add(this, BorderLayout.CENTER);
		window.pack();

		grabFocus();

	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		displayWidth = getWidth();
		displayHeight = getHeight();
		setDefaultScale();

		if (tileMap != null) {
			drawMap(g);
		}
		if (engine != null) {
			drawUnits(g);
		}
	}

	protected void drawMap(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, getWidth(), getHeight());

		for (int y = 0; y < tileMap.length; y++) {
			for (int x = 0; x < tileMap[0].length; x++) {
				switch (tileMap[y][x]) {
				case PASSABLE:
					g2d.setColor(Color.WHITE);
					break;
				case IMPASSABLE:
					g2d.setColor(Color.DARK_GRAY);
					break;
				case GOLD:
					g2d.setColor(Color.YELLOW);
					break;
				case WOOD:
					g2d.setColor(FOREST_GREEN);
					break;
				case RED_CASTLE:
					g2d.setColor(Color.RED);
					break;
				case BLUE_CASTLE:
					g2d.setColor(Color.BLUE);
					break;
				default:
					break;
				}
				g2d.fillRect(calculateDrawX(x), calculateDrawY(y), (int) scaleSize + 1, (int) scaleSize + 1);
			}
		}

		g2d.setColor(Color.BLACK);
		for (int y = 0; y < mapHeight + 1; y++) {
			g2d.drawLine(left, calculateDrawY(y), calculateDrawX(mapWidth), calculateDrawY(y));
		}
		for (int x = 0; x < mapWidth + 1; x++) {
			g2d.drawLine(calculateDrawX(x), top, calculateDrawX(x), calculateDrawY(mapHeight));
		}
	}

	protected void drawUnits(Graphics g) {
		
	}

	private void setDefaultScale() {
		scaleSize = Math.min(((double) displayWidth) / mapWidth, ((double) displayHeight) / mapHeight) - 1;
		top = (int) ((displayHeight - (scaleSize * mapHeight)) / 2);
		left = (int) ((displayWidth - (scaleSize * mapWidth)) / 2);
	}

	private int[] calculateDrawPosition(int x, int y) {
		return new int[] { left + (int) (x * scaleSize), top + (int) (y * scaleSize) };
	}

	private int calculateDrawX(int x) {
		return left + (int) (x * scaleSize);
	}

	private int calculateDrawY(int y) {
		return top + (int) (y * scaleSize);
	}

	private int[] calculateGridPosition(int x, int y) {
		return new int[] { (int) Math.floor((x - left) / scaleSize), (int) Math.floor((y - top) / scaleSize) };
	}

	public void openGame(String fileLocation) {
		engine = new ViewerEngine(fileLocation);
		tileMap = engine.getMap().getPassableMap();
		mapWidth = tileMap[0].length;
		mapHeight = tileMap.length;
	}

	public static void main(String[] args) {
		Display display = new Display(700, 500);
		display.openGame(args[0]);
		System.out.println(display.tileMap[10][20]);
	}
}
