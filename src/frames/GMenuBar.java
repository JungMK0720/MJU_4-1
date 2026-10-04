package frames;

import java.awt.event.ActionListener;

import javax.swing.JMenuBar;

import menus.GFileMenu;

public class GMenuBar extends JMenuBar {
	private static final long serialVersionUID = 1L;

	// components
	private GFileMenu fileMenu;
	private GHelpBar helpBar;

	// association
	private GDrawingPanel drawingPanel;

	public GMenuBar() {
		this.fileMenu = new GFileMenu();
		this.add(this.fileMenu);

		this.helpBar = new GHelpBar();
		this.add(this.helpBar);
	}

	public void initialize() {
		this.fileMenu.associate(this.drawingPanel);
		this.fileMenu.initialize();
	}

	public void associate(GDrawingPanel drawingPanel) {
		this.drawingPanel = drawingPanel;

	}

	// getter
	public GFileMenu getFileMenu() {
		return this.fileMenu;
	}
	
	// enroll Action Listner(For GHelpBar)
	public void addHelpListener(ActionListener action) {
        this.helpBar.addShortcutListener(action);
    }
}
