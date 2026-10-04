package frames;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import global.GConstants.EHelpMenuItem;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class GHelpBar extends JMenu {
	// serializable
    private static final long serialVersionUID = 1L;
    
    // components
    private Map<EHelpMenuItem, JMenuItem> menuItems = new HashMap<>();

    // constructor
    public GHelpBar() {
        super("Help");
        for (EHelpMenuItem item : EHelpMenuItem.values()) {
            JMenuItem mi = new JMenuItem(item.getLabel());
            mi.setActionCommand(item.name());
            menuItems.put(item, mi);
            this.add(mi);
        }
    }

    // actionListner
    public void addShortcutListener(ActionListener listener) {
        JMenuItem mi = menuItems.get(EHelpMenuItem.eShortcut);
        if (mi != null) {
            mi.addActionListener(listener);
        }
    }
}
