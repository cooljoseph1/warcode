package mapmaker;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;

import javax.swing.JPanel;

public class Display extends JPanel {

	private static final long serialVersionUID = 5268301898468656990L;

	private Window window;

	private int displayWidth;
	private int displayHeight;
	private int mapWidth;
	private int mapHeight;
	private double scaleSize;
	private int top;
	private int left;

	public Display(int width, int height, int mapWidth, int mapHeight) {
		super();
		window = new Window();
		//setPreferredSize(new Dimension(width, height));
		window.setPreferredSize(new Dimension(width, height));
		window.setLayout(new BorderLayout());
		window.add(this, BorderLayout.CENTER);
		window.pack();
		
		window.setVisible(true);
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;
		
		
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
		g2d.setColor(Color.BLACK);

		for (int y = 0; y < mapHeight + 1; y++) {
			g2d.drawLine(left, top + (int) (y * scaleSize), left + (int) (mapWidth * scaleSize),
					top + (int) (y * scaleSize));
		}
		for (int x = 0; x < mapWidth + 1; x++) {
			g2d.drawLine(left + (int) (x * scaleSize), top, left + (int) (x * scaleSize),
					top + (int) (mapHeight * scaleSize));
		}
	}

	private void setDefaultScale() {
		scaleSize = Math.min(((double) displayWidth) / mapWidth, ((double) displayHeight) / mapHeight) - 1;
		top = (int) ((displayHeight - (scaleSize * mapHeight)) / 2);
		left = (int) ((displayWidth - (scaleSize * mapWidth)) / 2);
	}
}
