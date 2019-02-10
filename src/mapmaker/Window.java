package mapmaker;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Window extends JFrame {

	private static final long serialVersionUID = -585839154376527986L;
	public Window() {
		super();
		ImageIcon img = new ImageIcon("Resources/WarcodeIcon.png");
		setIconImage(img.getImage());
		
		//TODO:  Make a name for Warcode 2019
		setTitle("Warcode:  Clash of Nobility"); //This can be changed at a later time.
	}
}
