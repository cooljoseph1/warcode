package mapmaker;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.FileWriter;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButton;
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

		setTitle("*Untitiled* - Warcode Map Maker");
		setCurrentTool(Tile.IMPASSABLE);
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
			JMenuItem save = new JMenuItem();
			save.setText("Save");
			save.setMnemonic(KeyEvent.VK_S);
			fileMenu.add(save);
			save.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (saveLocation == null) {
						if (!chooseSaveLocation()) { // User pressed cancel, so don't save it.
							return;
						}
					}
					saveFile();
				}
			});

			JMenuItem saveAs = new JMenuItem("Save As...");
			saveAs.setMnemonic(KeyEvent.VK_A);
			saveAs.setDisplayedMnemonicIndex(5);
			fileMenu.add(saveAs);
			saveAs.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (!chooseSaveLocation()) { // User pressed cancel, so don't save it.
						return;
					}
					saveFile();
				}
			});

			JMenuItem open = new JMenuItem("Open");
			open.setMnemonic(KeyEvent.VK_O);
			fileMenu.add(open);
			open.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					openFile();
				}
			});

			menubar.add(fileMenu);
		}

		{// add tool drop down
			JMenu toolMenu = new JMenu("Tool");
			toolMenu.setMnemonic(KeyEvent.VK_T);
			ButtonGroup toolGroup = new ButtonGroup();

			passable = new JRadioButton();
			passable.setText("Passable");
			passable.setMnemonic(KeyEvent.VK_P);
			toolMenu.add(passable);
			toolGroup.add(passable);
			passable.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					display.setTool(Tile.PASSABLE);
				}
			});

			impassable = new JRadioButton();
			impassable.setText("Impassable");
			impassable.setMnemonic(KeyEvent.VK_I);
			toolMenu.add(impassable);
			toolGroup.add(impassable);
			impassable.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					display.setTool(Tile.IMPASSABLE);
				}
			});

			gold = new JRadioButton();
			gold.setText("Gold");
			gold.setMnemonic(KeyEvent.VK_G);
			toolMenu.add(gold);
			toolGroup.add(gold);
			gold.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					display.setTool(Tile.GOLD);
				}
			});

			wood = new JRadioButton();
			wood.setText("Wood");
			wood.setMnemonic(KeyEvent.VK_W);
			toolMenu.add(wood);
			toolGroup.add(wood);
			wood.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					display.setTool(Tile.WOOD);
				}
			});

			redCastle = new JRadioButton();
			redCastle.setText("Red Castle");
			redCastle.setMnemonic(KeyEvent.VK_R);
			toolMenu.add(redCastle);
			toolGroup.add(redCastle);
			redCastle.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					display.setTool(Tile.RED_CASTLE);
				}
			});

			blueCastle = new JRadioButton();
			blueCastle.setText("Blue Castle");
			blueCastle.setMnemonic(KeyEvent.VK_B);
			toolMenu.add(blueCastle);
			toolGroup.add(blueCastle);
			blueCastle.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					display.setTool(Tile.BLUE_CASTLE);
				}
			});

			menubar.add(toolMenu);
		}
		setJMenuBar(menubar);
	}

	public void setCurrentTool(Tile tool) {
		switch (tool) {
		case PASSABLE:
			passable.setSelected(true);
			break;
		case IMPASSABLE:
			impassable.setSelected(true);
			break;
		case GOLD:
			gold.setSelected(true);
			break;
		case WOOD:
			wood.setSelected(true);
			break;
		case RED_CASTLE:
			redCastle.setSelected(true);
			break;
		case BLUE_CASTLE:
			blueCastle.setSelected(true);
			break;
		default:
			break;
		}
	}

	/**
	 * 
	 * @return boolean. true means it chose successfully, false means the user
	 *         cancelled.
	 */
	private boolean chooseSaveLocation() {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Warcode 2019 Map", "wcm");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showSaveDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			fileName = chooser.getSelectedFile().getName();
			if (fileName.endsWith(".wcm")) {
				fileName = fileName.substring(0, fileName.length() - 4);
			}
			String file = chooser.getSelectedFile().getAbsolutePath();
			if (!file.endsWith(".wcm")) {
				file = file + ".wcm";
			}
			saveLocation = file;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Saves the map
	 */
	private void saveFile() {
		try {
			FileWriter writer = new FileWriter(saveLocation);
			writer.write(display.mapToString());
			writer.close();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			setStatus(Status.SAVED);
		}
	}

	/**
	 * Opens a map
	 */
	private void openFile() {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Warcode 2019 Map", "wcm");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			fileName = chooser.getSelectedFile().getName();
			if (fileName.endsWith(".wcm")) {
				fileName = fileName.substring(0, fileName.length() - 4);
			}
			String file = chooser.getSelectedFile().getAbsolutePath();
			if (!file.endsWith(".wcm")) {
				file = file + ".wcm";
			}
			saveLocation = file;
		} else {
			return;
		}

		setStatus(Status.SAVED);
		display.openMap(saveLocation);
	}

	public void setStatus(Status status) {
		this.status = status;
		if (status == Status.UNSAVED) {
			setTitle("*" + fileName + "* - Warcode Map Maker");
		} else if (status == Status.SAVED) {
			setTitle(fileName + " - Warcode Map Maker");
		}
	}
}
