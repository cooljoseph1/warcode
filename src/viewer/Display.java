package viewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import warcode.Tile;

public class Display extends JPanel implements ChangeListener, MouseWheelListener {

	private static final long serialVersionUID = 5268301898468656990L;
	private static final Color FOREST_GREEN = new Color(0, 153, 0);

	private Window window;
	private JSlider turnSlider = new JSlider(JSlider.HORIZONTAL, 0, 1000, 0);
	private JLabel turnLabel = new JLabel("Turn: 0");
	private JPanel topPanel = new JPanel();

	private int displayWidth;
	private int displayHeight;
	private int mapWidth;
	private int mapHeight;
	private double scaleSize;
	private double zoom = 1;
	private double zoomX = 0;
	private double zoomY = 0;
	private int top;
	private int left;

	private Tile[][] tileMap;

	private ViewerEngine engine;

	// robot images
	private static final Image originalRedCastle;
	static {
		Image tmpRedCastle = null;
		try {
			tmpRedCastle = ImageIO.read(new File("Resources/Images/RedCastle.png"));
		} catch (IOException e) {

		}
		originalRedCastle = tmpRedCastle;
	}

	private Image redCastle = originalRedCastle;

	public Display(int width, int height) {
		super();

		// set up the window
		window = new Window(this);

		window.setPreferredSize(new Dimension(width, height));
		window.setLayout(new BorderLayout());

		// add in mouse listener
		addMouseWheelListener(this);

		// setup and add the turn slider and label
		topPanel.setLayout(new BorderLayout());

		turnSlider.setMinorTickSpacing(10);
		turnSlider.setMajorTickSpacing(100);
		turnSlider.setPaintTicks(true);
		turnSlider.setPaintLabels(true);
		turnSlider.addChangeListener(this);

		topPanel.add(turnSlider, BorderLayout.NORTH);

		turnLabel.setHorizontalAlignment(JLabel.CENTER);
		topPanel.add(turnLabel, BorderLayout.SOUTH);
		window.add(topPanel, BorderLayout.NORTH);

		// add this (the display part) to the window
		window.add(this, BorderLayout.CENTER);
		window.pack();

		grabFocus();

	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;

		displayWidth = getWidth();
		displayHeight = getHeight();
		setDefaultScale();

		if (tileMap != null) {
			drawMap(g2d);
		}
		if (engine != null) {
			drawUnits(g2d);
		}
		drawLines(g2d);

		g2d.dispose();

	}

	protected void drawMap(Graphics2D g2d) {

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
				fillRect(g2d, x, y, 1, 1);
			}
		}
	}

	protected void drawLines(Graphics2D g2d) {
		g2d.setColor(Color.BLACK);
		for (int y = 0; y < mapHeight + 1; y++) {
			drawLine(g2d, 0, y, mapWidth, y);
		}
		for (int x = 0; x < mapWidth + 1; x++) {
			drawLine(g2d, x, 0, x, mapHeight);
		}

	}

	protected void drawUnits(Graphics2D g2d) {
		for (int id : engine.getAliveUnitIds()) {
			ViewerUnit unit = engine.getUnit(id);
			switch (unit.unitType) {
			case CASTLE:
				drawImage(g2d, redCastle, unit.getX(), unit.getY());
				break;
			case PEASANT:
				g2d.setColor(Color.PINK);
				fillRect(g2d, unit.getX(), unit.getY(), 1, 1);
				break;
			default:
				break;
			}

		}
	}

	private void setDefaultScale() {
		scaleSize = (Math.min(((double) displayWidth) / mapWidth, ((double) displayHeight) / mapHeight) - 1) * zoom;
		top = (int) ((displayHeight - (scaleSize * mapHeight)) / 2 + zoomY * scaleSize);
		left = (int) ((displayWidth - (scaleSize * mapWidth)) / 2 + zoomX * scaleSize);

		redCastle = originalRedCastle.getScaledInstance((int) scaleSize, (int) scaleSize, BufferedImage.SCALE_DEFAULT);
	}

	private void fillRect(Graphics2D g2d, double x, double y, double width, double height) {
		g2d.fillRect((int) (x * scaleSize + left), (int) (y * scaleSize + top), (int) (width * scaleSize + 1),
				(int) (height * scaleSize + 1));
	}

	private void drawLine(Graphics2D g2d, double startX, double startY, double endX, double endY) {
		g2d.drawLine((int) (startX * scaleSize + left), (int) (startY * scaleSize + top),
				(int) (endX * scaleSize + left), (int) (endY * scaleSize + top));
	}

	private void drawImage(Graphics2D g2d, Image img, double x, double y) {
		g2d.drawImage(img, (int) (x * scaleSize + left), (int) (y * scaleSize + top), null);
	}

	public void openGame(String fileLocation) {
		engine = new ViewerEngine(fileLocation);
		tileMap = engine.getMap().getPassableMap();
		mapWidth = tileMap[0].length;
		mapHeight = tileMap.length;

		repaint();
	}

	public void setTurn(int turn) {
		if (engine.getTurn() >= turn) {
			while (engine.getTurn() > turn) {
				engine.moveBackwardTurn();
			}
		} else {
			while (engine.getTurn() < turn) {
				engine.moveForwardTurn();
			}
		}

		repaint();
	}

	public static void main(String[] args) {
		Display display = new Display(700, 500);
		display.openGame(args[0]);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider) e.getSource();
		setTurn(source.getValue());

		turnLabel.setText("Turn: " + engine.getTurn());

	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {

		double centerX = getWidth() / 2d;
		double centerY = getHeight() / 2d;
		
		double val = Math.exp(-e.getWheelRotation() / 10d);
		
		zoomX -= (e.getX() - left)/scaleSize/scaleSize*val;
		zoomY -= (e.getY() - top)/scaleSize/scaleSize*val;
		
		zoom *= val;

		repaint();

	}
}
