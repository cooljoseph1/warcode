package mapmaker;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Window extends JFrame {

	private static final long serialVersionUID = -585839154376527986L;
	File saveLocation;

	public Window() {
		super();

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(this);
		} catch (Exception e) {
			e.printStackTrace();
		}

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
				JFileChooser chooser = new JFileChooser();
				int returnVal = chooser.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					saveLocation = file;
					FileUtils.writeStringToFile(saveLocation, getSaveMap());
					
				}

			}

		});
		JMenuItem saveAs = new JMenuItem("Save As...");
		saveAs.setMnemonic(KeyEvent.VK_A);
		saveAs.setDisplayedMnemonicIndex(5);
		menu.add(saveAs);
		JMenuItem open = new JMenuItem("Open");
		open.setMnemonic(KeyEvent.VK_O);
		menu.add(open);
		menubar.add(menu);

		setJMenuBar(menubar);

		// TODO: Make a name for Warcode 2019
		setTitle("Warcode 2019"); // This can be changed at a later time.
	}
}
