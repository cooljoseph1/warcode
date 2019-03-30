package viewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
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

import warcode.Team;
import warcode.Tile;

public class Display extends JPanel implements ChangeListener, MouseWheelListener, MouseMotionListener, MouseListener {

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
	private double top;
	private double left;

	private double centerX;
	private double centerY;

	private int mouseStartX;
	private int mouseStartY;

	private Tile[][] tileMap;

	private ViewerEngine engine;

	// robot images
	private static final BufferedImage originalRedArcher = loadImage("Resources/Images/RedArcher.png");
	private static final BufferedImage originalRedCastle = loadImage("Resources/Images/RedCastle.png");
	private static final BufferedImage originalRedKnight = loadImage("Resources/Images/RedKnight.png");
	private static final BufferedImage originalRedMage = loadImage("Resources/Images/RedMage.png");
	private static final BufferedImage originalRedPeasant = loadImage("Resources/Images/RedPeasant.png");

	private static final BufferedImage originalBlueArcher = loadImage("Resources/Images/BlueArcher.png");
	private static final BufferedImage originalBlueCastle = loadImage("Resources/Images/BlueCastle.png");
	private static final BufferedImage originalBlueKnight = loadImage("Resources/Images/BlueKnight.png");
	private static final BufferedImage originalBlueMage = loadImage("Resources/Images/BlueMage.png");
	private static final BufferedImage originalBluePeasant = loadImage("Resources/Images/BluePeasant.png");

	private BufferedImage redArcher = originalRedArcher;
	private BufferedImage redCastle = originalRedCastle;
	private BufferedImage redKnight = originalRedKnight;
	private BufferedImage redMage = originalRedMage;
	private BufferedImage redPeasant = originalRedPeasant;

	private BufferedImage blueArcher = originalBlueArcher;
	private BufferedImage blueCastle = originalBlueCastle;
	private BufferedImage blueKnight = originalBlueKnight;
	private BufferedImage blueMage = originalBlueMage;
	private BufferedImage bluePeasant = originalBluePeasant;

	private static BufferedImage loadImage(String file) {
		try {
			return ImageIO.read(new File(file));
		} catch (IOException e) {
			return null;
		}
	}

	public Display(int width, int height) {
		super();

		// set up the window
		window = new Window(this);

		window.setPreferredSize(new Dimension(width, height));
		window.setLayout(new BorderLayout());

		// add in mouse listeners
		addMouseWheelListener(this);
		addMouseMotionListener(this);
		addMouseListener(this);

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

		// See if the window has changed size. If it has, resize images.
		checkWindowSize();

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

			if (unit.team == Team.RED) {
				switch (unit.unitType) {
				case ARCHER:
					drawImage(g2d, redArcher, unit.getX(), unit.getY());
					break;
				case CASTLE:
					drawImage(g2d, redCastle, unit.getX(), unit.getY());
					break;
				case KNIGHT:
					drawImage(g2d, redKnight, unit.getX(), unit.getY());
					break;
				case MAGE:
					drawImage(g2d, redMage, unit.getX(), unit.getY());
					break;
				case PEASANT:
					drawImage(g2d, redPeasant, unit.getX(), unit.getY());
					break;
				default:
					break;
				}
			} else {
				switch (unit.unitType) {
				case ARCHER:
					drawImage(g2d, blueArcher, unit.getX(), unit.getY());
					break;
				case CASTLE:
					drawImage(g2d, blueCastle, unit.getX(), unit.getY());
					break;
				case KNIGHT:
					drawImage(g2d, blueKnight, unit.getX(), unit.getY());
					break;
				case MAGE:
					drawImage(g2d, blueMage, unit.getX(), unit.getY());
					break;
				case PEASANT:
					drawImage(g2d, bluePeasant, unit.getX(), unit.getY());
					break;
				default:
					break;
				}
			}

		}
	}

	private void checkWindowSize() {
		setDefaultScale();
		setPosition();
		if (displayWidth != getWidth() || displayHeight != getHeight()) {

			double oldScale = scaleSize;

			displayWidth = getWidth();
			displayHeight = getHeight();
			setDefaultScale();

			if (oldScale > 0) {
				zoomX *= scaleSize / oldScale;
				zoomY *= scaleSize / oldScale;
			}
			if (scaleSize > 0 && scaleSize < 10000000) { // make sure scaleSize is positive and not insanely large
				scaleImages();
				setPosition();
			}
		}
	}

	private void setDefaultScale() {
		scaleSize = (Math.min(((double) displayWidth) / mapWidth, ((double) displayHeight) / mapHeight) - 1) * zoom;

	}

	private void setPosition() {
		centerX = displayWidth / 2d;
		centerY = displayHeight / 2d;

		left = centerX + zoomX - (scaleSize * mapWidth) / 2;
		top = centerY + zoomY - (scaleSize * mapHeight) / 2;
	}

	private void scaleImages() {

		redArcher = scaleImage(originalRedArcher, scaleSize);
		redCastle = scaleImage(originalRedCastle, scaleSize);
		redKnight = scaleImage(originalRedKnight, scaleSize);
		redMage = scaleImage(originalRedMage, scaleSize);
		redPeasant = scaleImage(originalRedPeasant, scaleSize);

		blueArcher = scaleImage(originalBlueArcher, scaleSize);
		blueCastle = scaleImage(originalBlueCastle, scaleSize);
		blueKnight = scaleImage(originalBlueKnight, scaleSize);
		blueMage = scaleImage(originalBlueMage, scaleSize);
		bluePeasant = scaleImage(originalBluePeasant, scaleSize);

	}

	private BufferedImage scaleImage(BufferedImage before, double scaleSize) {

		int w = before.getWidth();
		int h = before.getHeight();

		BufferedImage after = new BufferedImage((int) scaleSize, (int) scaleSize, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = (Graphics2D) after.getGraphics();

		g2d.scale(scaleSize / w, scaleSize / h);
		g2d.drawImage(before, 0, 0, null);
		g2d.dispose();

		return after;

	}

	private void fillRect(Graphics2D g2d, double x, double y, double width, double height) {
		// smart drawing -- it only draws it if it will show up on the screen
		double left = (x * scaleSize + this.left);
		double top = (y * scaleSize + this.top);
		double absWidth = width * scaleSize + 1;
		double absHeight = height * scaleSize + 1;

		if (!(left > getWidth() || top > getHeight() || left + absWidth < 0 || top + absHeight < 0)) {
			g2d.fillRect((int) left, (int) top, (int) absWidth, (int) absHeight);
		}
	}

	private void drawLine(Graphics2D g2d, double startX, double startY, double endX, double endY) {
		// smart drawing -- it only draws it if it will show up on the screen
		int left = (int) (startX * scaleSize + this.left);
		int top = (int) (startY * scaleSize + this.top);
		int right = (int) (endX * scaleSize + this.left);
		int bottom = (int) (endY * scaleSize + this.top);

		if (!(left > getWidth() || top > getHeight() || right < 0 || bottom < 0)) {

			g2d.drawLine(left, top, right, bottom);
		}
	}

	private void drawImage(Graphics2D g2d, BufferedImage img, double x, double y) {
		// smart drawing -- it only draws it if it will show up on the screen

		int left = (int) (x * scaleSize + this.left);
		int top = (int) (y * scaleSize + this.top);

		if (!(left > getWidth() || top > getHeight() || left + img.getWidth() < 0 || top + img.getHeight() < 0)) {
			g2d.drawImage(img, left, top, null);
		}
	}

	public void openGame(String fileLocation) {
		engine = new ViewerEngine(fileLocation);
		tileMap = engine.getMap().getPassableMap();
		mapWidth = tileMap[0].length;
		mapHeight = tileMap.length;

		setDefaultScale();
		zoom = 1;
		zoomX = 0;
		zoomY = 0;
		scaleImages();

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

	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider) e.getSource();
		setTurn(source.getValue());

		turnLabel.setText("Turn: " + engine.getTurn());

	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		double val = Math.exp(-e.getWheelRotation() / 10d);

		if (val * scaleSize > 1000) {
			val = 1000 / scaleSize;
		}

		if (val * scaleSize < 2) {
			val = 2 / scaleSize;
		}

		// Magic - DO NOT TOUCH!
		zoomX += (centerX + zoomX - e.getX()) * (val - 1);
		zoomY += (centerY + zoomY - e.getY()) * (val - 1);

		zoom *= val;

		setDefaultScale();
		scaleImages();

		repaint();

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		zoomX += e.getX() - mouseStartX;
		zoomY += e.getY() - mouseStartY;

		mouseStartX = e.getX();
		mouseStartY = e.getY();
		repaint();

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		mouseStartX = e.getX();
		mouseStartY = e.getY();

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {
		Display display = new Display(700, 500);
		display.openGame(args[0]);
	}
}
