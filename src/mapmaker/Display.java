package mapmaker;

import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.ImageObserver;

public class Display {
	private static final int MAX_WIDTH = 100;
	private static final int MAX_HEIGHT = 100;
	private int mapWidth = 50;
	private int mapHeight = 50;
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	private static void createAndShowGUI() {
		System.out.println("Created GUI on EDT? " + SwingUtilities.isEventDispatchThread());
		JFrame f = new JFrame("Warcode Map Creater");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container container = new Container();
		TileContainer tileContainer = new TileContainer();
		Tile[][] tileArray  = new Tile[MAX_HEIGHT][MAX_WIDTH];
		tileContainer.setLayout(new GridLayout(MAX_HEIGHT, MAX_WIDTH));
		for(int i = 0; i<MAX_HEIGHT; i++) {
			for(int j = 0; j<MAX_WIDTH; j++) {
				tileArray[i][j] = new Tile();
				tileContainer.add(tileArray[i][j]);
			}
		}
		container.add(tileContainer);
		f.add(container);
		f.pack();
		f.setVisible(true);
	}
}

class Container extends JPanel {
	
	public Dimension getPreferredSize() {
		return new Dimension(1000,1000);
	}
	public void paintComponent(Graphics g) {
	}
}

class TileContainer extends JPanel {
	
	@Override
	public Dimension getPreferredSize() {
		java.awt.Container parent = this.getParent();
		int size = Math.min(parent.getWidth(), parent.getHeight());
		return new Dimension(size,size);
	}
	public void paintComponent(Graphics g) {
	}
}

class Tile extends JPanel{
	private int type = 0;
	private String[] tileTypes = {"Empty", "Tree", "Goldmine", "Impassable", "RedCastle", "BlueCastle"};
	public Dimension getPreferredSize() {
		return new Dimension(20, 20);
	}
	public void paintComponent(Graphics g) {
		int width = Math.min(this.getWidth(), this.getHeight());
		if(tileTypes[type] == "Empty") {
			g.setColor(new Color(255,255,255));
			g.fillRect(0, 0, width, width);
		}
		else if(tileTypes[type] == "Tree") {
			g.setColor(new Color(0,255,0));
			g.fillRect(0, 0, width, width);
		}
		else if(tileTypes[type] == "Goldmine") {
			g.setColor(new Color(255,255,0));
			g.fillRect(0, 0, width, width);
		}
		else if(tileTypes[type] == "Impassable") {
			g.setColor(new Color(0,0,0));
			g.fillRect(0, 0, width, width);
		}
		else if(tileTypes[type] == "RedCastle") {
			g.setColor(new Color(255,0,0));
			// actually load up image somewhere.
			Image img = null;
			g.drawImage(img, 0, 0, new Color(255,0,0), null);
		}
		else if(tileTypes[type] == "BlueCastle") {
			g.setColor(new Color(255,0,0));
			Image img = null;
			g.drawImage(img, 0, 0, new Color(255,0,0), null);
		}
		g.setColor(new Color(0,0,0));
		g.drawRect(0, 0, width, width);
	}
}

class MyPanel extends JPanel {
	private int squareX = 50;
	private int squareY = 50;
	private int squareW = 20;
	private int squareH = 20;
	public MyPanel() {
		setBorder(BorderFactory.createLineBorder(Color.black));
		
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				moveSquare(e.getX(), e.getY());
			}
		});
		
		addMouseMotionListener(new MouseAdapter() {
			public void mouseDragged(MouseEvent e) {
				moveSquare(e.getX(), e.getY());
			}
		});
	}
	
	private void moveSquare(int x, int y) {
		int OFFSET = 1;
		if((squareX!=x) || (squareY!=y)) {
			repaint(squareX, squareY, squareW+OFFSET, squareH+OFFSET);
			squareX = x;
			squareY = y;
			repaint(squareX, squareY, squareW+OFFSET, squareH+OFFSET);
		}
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(250, 200);
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.RED);
		g.fillRect(squareX, squareY, squareW, squareH);
		g.setColor(Color.BLACK);
		g.drawRect(squareX, squareY, squareW, squareH);
	}
}
