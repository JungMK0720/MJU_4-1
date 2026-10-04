package frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import manager.GPanelManager;

public class GBelowPanel extends JPanel {
	// Serializable
	private static final long serialVersionUID = 1L;
	
	// components
	private JTabbedPane tabbedPane;
    @SuppressWarnings("unused")
    private final int TAB_HEIGHT = 30;
    private final String BLANK_TAB_TITLE = "";
    
    // parent
    private GPanelManager panelManager;

    public GBelowPanel(GPanelManager panelManager) {
        this.panelManager = panelManager;
        this.setLayout(new BorderLayout());

        tabbedPane = new JTabbedPane() {
			private static final long serialVersionUID = 1L;

			@Override
            public Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                return new Dimension(d.width, TAB_HEIGHT);
            }

            @Override
            public void doLayout() {
                for (int i = 0; i < getTabCount(); i++) {
                    Component c = getComponentAt(i);
                    if (c != null) {
                        c.setBounds(0, 0, 0, 0);
                    }
                }
                super.doLayout();
            }
        };

        this.add(tabbedPane, BorderLayout.CENTER);

        addNewTab("탭");
        addBlankTab();

        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int idx = tabbedPane.getSelectedIndex();
                if (idx < 0) return;

                String title = tabbedPane.getTitleAt(idx);

                if (BLANK_TAB_TITLE.equals(title)) {
                    int newTabIndex = idx;
                    String newTitle = "탭 " + newTabIndex;
                    tabbedPane.setTitleAt(newTabIndex, newTitle);
                    panelManager.addPanel(new GDrawingPanel());
                    addBlankTab();
                    tabbedPane.setSelectedIndex(newTabIndex);
                } else {
                    panelManager.selectPanel(idx);
                }
            }
        });
    }

    private void addNewTab(String title) {
        tabbedPane.addTab(title, new JPanel());
    }

    private void addBlankTab() {
        tabbedPane.addTab(BLANK_TAB_TITLE, new JPanel() {
			private static final long serialVersionUID = 1L;

		{
            setBackground(new Color(200, 220, 255));
        }});
    }

    public void initialize() {
        this.setVisible(true);
    }
}
