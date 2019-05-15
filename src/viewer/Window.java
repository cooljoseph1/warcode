package viewer;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileWriter;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import warcode.Tile;

public class Window extends JFrame {

	private static final long serialVersionUID = -585839154376527986L;
	String saveLocation;
	Display display;
	private String fileName = "Untitiled";
	private Status status = Status.UNSAVED;

	JRadioButton passable;
	JRadioButton impassable;
	JRadioButton gold;
	JRadioButton wood;
	JRadioButton redCastle;
	JRadioButton blueCastle;

	public Window(Display display) {
		super();

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(this);
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.display = display;

		ImageIcon img = new ImageIcon("Resources/WarcodeIcon.png");
		setIconImage(img.getImage());

		makeMenuBar();

		setTitle("Warcode Viewer");

		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		
	}

	/**
	 * Code put into a function to make more readable. Called on initializing.
	 */
	private void makeMenuBar() {
		JMenuBar menubar = new JMenuBar();

		{
			// make file drop down
			JMenu fileMenu = new JMenu("File");
			fileMenu.setMnemonic(KeyEvent.VK_F);
			JMenuItem open = new JMenuItem("Open");
			open.setMnemonic(KeyEvent.VK_O);
			open.setAccelerator(KeyStroke.getKeyStroke('O', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
			fileMenu.add(open);
			open.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					openFile();
				}
			});

			menubar.add(fileMenu);
		}

		setJMenuBar(menubar);
	}

	/**
	 * Opens a map
	 */
	private void openFile() {
		JFileChooser chooser = new JFileChooser("replays");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Warcode 2019 Replay", "wcr");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			fileName = chooser.getSelectedFile().getName();
			if (fileName.endsWith(".wcr")) {
				fileName = fileName.substring(0, fileName.length() - 4);
			}
			String file = chooser.getSelectedFile().getAbsolutePath();
			if (!file.endsWith(".wcr")) {
				file = file + ".wcr";
			}
			saveLocation = file;
		} else {
			return;
		}

		display.openGame(saveLocation);
	}
	
}
