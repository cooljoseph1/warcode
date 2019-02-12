package mapmaker;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.FileWriter;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Window extends JFrame {

	private static final long serialVersionUID = -585839154376527986L;
	String saveLocation;
	Display display;

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

		// add menubar
		JMenuBar menubar = new JMenuBar();
		JMenu menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_F);
		JMenuItem save = new JMenuItem();
		save.setText("Save");
		save.setMnemonic(KeyEvent.VK_S);
		menu.add(save);
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
		menu.add(saveAs);
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
		menu.add(open);
		open.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openFile();
			}
		});

		menubar.add(menu);

		setJMenuBar(menubar);

		// TODO: Make a name for Warcode 2019
		setTitle("Warcode 2019"); // This can be changed at a later time.
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
			String file = chooser.getSelectedFile().getAbsolutePath();
			if (!file.endsWith(".wcm")) {
				file = file + ".wcm";
			}
			saveLocation = file;
		} else {
			return;
		}

		display.openMap(saveLocation);
	}
}
