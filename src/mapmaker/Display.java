package mapmaker;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import warcode.Tile;

public class Display extends JPanel implements MouseMotionListener, MouseListener {

	private static final long serialVersionUID = 5268301898468656990L;
	private static final Color FOREST_GREEN = 	new Color(0, 153, 0);

	private Window window;

	private int displayWidth;
	private int displayHeight;
	private int mapWidth;
	private int mapHeight;
	private double scaleSize;
	private int top;
	private int left;

	private int[] previousTileChanged = new int[] { -1, -1 };
	private Tile[][] tileMap;
	private Tile currentTileType = Tile.IMPASSABLE;

	public Display(int width, int height, int mapWidth, int mapHeight) {
		super();

		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;

		tileMap = new Tile[mapHeight][mapWidth];
		// initialize tileMap to empty tiles
		for (int y = 0; y < tileMap.length; y++) {
			for (int x = 0; x < tileMap[0].length; x++) {
				tileMap[y][x] = Tile.PASSABLE;
			}
		}

		window = new Window(this);

		window.setPreferredSize(new Dimension(width, height));
		window.setLayout(new BorderLayout());
		window.add(this, BorderLayout.CENTER);
		window.pack();

		window.setVisible(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		addMouseMotionListener(this);
		addMouseListener(this);

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
					g2d.setColor(Color.BLACK);
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

	private void setTile(int mouseX, int mouseY, Tile tileType) {
		window.setStatus(Status.UNSAVED);

		int[] gridPosition = calculateGridPosition(mouseX, mouseY);
		if (Arrays.equals(gridPosition, previousTileChanged)) {
			return;
		}
		if (gridPosition[0] < 0 || gridPosition[0] >= mapWidth || gridPosition[1] < 0 || gridPosition[1] >= mapHeight) {
			return;
		}

		tileMap[gridPosition[1]][gridPosition[0]] = tileType;
		previousTileChanged = gridPosition;
		repaint();

	}

	public void setTool(Tile tool) {
		currentTileType = tool;
		window.setCurrentTool(tool);
	}

	public String mapToString() {
		StringBuilder stringBuilder = new StringBuilder();

		for (Tile[] row : tileMap) {
			for (Tile tile : row) {
				stringBuilder.append(tile);
			}
			stringBuilder.append("\n");
		}
		return stringBuilder.substring(0, stringBuilder.length() - 1); // get rid of the last newline
	}

	public void openMap(String fileLocation) {
		try {

			LinkedList<Tile[]> temporaryMap = new LinkedList<Tile[]>();
			int height = 0;
			int width = 0;

			BufferedReader reader = new BufferedReader(new FileReader(fileLocation));
			for (String line = reader.readLine(); line != null; line = reader.readLine()) {
				height += 1;
				width = line.length();
			}
			reader.close();

			tileMap = new Tile[height][width];

			reader = new BufferedReader(new FileReader(fileLocation));
			int y = 0;
			for (String line = reader.readLine(); line != null; line = reader.readLine(), y++) {
				for (int x = 0; x < line.length(); x++) {
					tileMap[y][x] = Tile.fromChar(line.charAt(x));
				}
			}
			reader.close();

		} catch (IOException ex) {
			throw new RuntimeException(ex);
		} finally {
			repaint();
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (!SwingUtilities.isLeftMouseButton(e)) {
			return;
		}
		setTile(e.getX(), e.getY(), currentTileType);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		switch (e.getButton()) {
		case MouseEvent.BUTTON1:
			setTile(e.getX(), e.getY(), currentTileType);
			break;
		case MouseEvent.BUTTON2:
			break;
		case MouseEvent.BUTTON3:
			currentTileType = Tile.next(currentTileType);
			window.setCurrentTool(currentTileType);
			break;
		default:
			break;
		}

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}
}
