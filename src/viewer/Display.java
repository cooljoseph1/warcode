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
	private Unit[] units;
	private Action[] actions;
	private Action[][] turns;
	private int gameLength = 0;
	private int actionLength = 0;
	private int currentTurn;

	public Display(int width, int height) {
		super();

		window = new Window(this);

		window.setPreferredSize(new Dimension(width, height));
		window.setLayout(new BorderLayout());
		window.add(this, BorderLayout.CENTER);
		window.pack();

		grabFocus();

	}

	public void reset() {
		tileMap = new Tile[mapHeight][mapWidth];
		// initialize tileMap to empty tiles
		for (int y = 0; y < tileMap.length; y++) {
			for (int x = 0; x < tileMap[0].length; x++) {
				tileMap[y][x] = Tile.PASSABLE;
			}
		}
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		displayWidth = getWidth();
		displayHeight = getHeight();
		setDefaultScale();

		super.paintComponent(g);
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
		try {
			
			BufferedReader reader = new BufferedReader(new FileReader(fileLocation));
			
			//Important:  gameLength must be the first line of the save file.
			gameLength = Integer.parseInt(reader.readLine());
			turns = new Action[gameLength][];
			
			//Important:  the next two lines must be mapWidth and mapHeight respectively.
			mapWidth = Integer.parseInt(reader.readLine());
			mapHeight = Integer.parseInt(reader.readLine());
			tileMap = new Tile[mapHeight][mapWidth];
			for(int y = 0; y<mapHeight; y++) {
				String row = reader.readLine();
				for(int x = 0; x < mapWidth; x++) {
					tileMap[y][x] = Tile.fromChar(row.charAt(x));
				}
			}
			
			
			for(int i = 0; i<gameLength; i++) {
				String line = reader.readLine();
				String[] opers = line.split(";");
				Action[] turnOpers = new Action[opers.length];
				for(int j = 0; j<opers.length; j++) {
					turnOpers[j] = Action.fromString(opers[j]);
				}
				turns[i] = turnOpers;
			}
			
			reader.close();

		} catch (IOException ex) {
			throw new RuntimeException(ex);
		} finally {
			repaint();
		}
	}
}
