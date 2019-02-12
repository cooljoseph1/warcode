package mapmaker;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Display extends JPanel implements MouseMotionListener, MouseListener {

	private static final long serialVersionUID = 5268301898468656990L;

	private Window window;

	private int displayWidth;
	private int displayHeight;
	private int mapWidth;
	private int mapHeight;
	private double scaleSize;
	private int top;
	private int left;

	private int[] previousTileChanged = new int[] { -1, -1 };
	private int[][] gridMap;

	public Display(int width, int height, int mapWidth, int mapHeight) {
		super();

		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;
		gridMap = new int[mapHeight][mapWidth];
		window = new Window();
		// setPreferredSize(new Dimension(width, height));
		
		window.setPreferredSize(new Dimension(width, height));
		window.setLayout(new BorderLayout());
		window.add(this, BorderLayout.CENTER);
		window.pack();

		window.setVisible(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		addMouseMotionListener(this);

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

		for (int y = 0; y < gridMap.length; y++) {
			for (int x = 0; x < gridMap[0].length; x++) {
				switch (gridMap[y][x]) {
				case 0:
					g2d.setColor(Color.WHITE);
					break;
				case 1:
					g2d.setColor(Color.RED);
					break;
				case 2:
					g2d.setColor(Color.YELLOW);
					break;
				case 3:
					g2d.setColor(Color.GREEN);
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
	
	private void setTile(int mouseX, int mouseY, int tileType) {
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		int[] gridPosition = calculateGridPosition(e.getX(), e.getY());
		if (Arrays.equals(gridPosition, previousTileChanged)) {
			return;
		}
		if (gridPosition[0] < 0 || gridPosition[0] >= mapWidth || gridPosition[1] < 0 || gridPosition[1] >= mapHeight) {
			return;
		}
		gridMap[gridPosition[1]][gridPosition[0]] += 1;
		gridMap[gridPosition[1]][gridPosition[0]] %= 4;
		previousTileChanged = gridPosition;
		repaint();

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
