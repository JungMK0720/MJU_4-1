package frames;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import global.GConstants;
import global.GConstants.EHelpEntry;
import manager.GContainer;
import manager.GPanelManager;

public class GMainFrame extends JFrame {
	// attributes
	private static final long serialVersionUID = 1L;
	// components
	private GMenuBar menuBar;
	private GShapeToolBar toolBar;
	private GDrawingPanel drawingPanel;
	private GBelowPanel belowPanel;
	private GPanelManager panelManager;

	private GContainer container;
	private GFunctionToolBar functionTool;
	private GColorToolBar colorTool;

	// associations
	// ...

	public GMainFrame() {
		this.setLocation(GConstants.EMainFrame.eX.getValue(), GConstants.EMainFrame.eY.getValue());
		this.setSize(GConstants.EMainFrame.eW.getValue(), GConstants.EMainFrame.eH.getValue());
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		// Components
		this.setLayout(new BorderLayout());

		this.menuBar = new GMenuBar();
		this.setJMenuBar(menuBar);
		this.enroll();

		this.toolBar = new GShapeToolBar();
		this.functionTool = new GFunctionToolBar();
		this.colorTool = new GColorToolBar();
		this.container = new GContainer();

		this.container.setToolBar(toolBar);
		this.container.setColorBar(colorTool);
		this.container.setFunctionBar(functionTool);

		this.panelManager = new GPanelManager(this);

		this.add(container, BorderLayout.NORTH);

		this.drawingPanel = new GDrawingPanel();
		this.add(drawingPanel, BorderLayout.CENTER);

		panelManager.setMenuBar(menuBar);
		panelManager.setToolBar(toolBar);
		panelManager.setColorBar(colorTool);
		panelManager.setFunctionBar(functionTool);

		panelManager.addPanel(drawingPanel);

		belowPanel = new GBelowPanel(panelManager);
		panelManager.setBelowPanel(belowPanel);
		this.add(belowPanel, BorderLayout.SOUTH);

	}

	public void setCenterPanel(GDrawingPanel panel) {
		if (this.drawingPanel != null) {
			this.remove(this.drawingPanel);
		}
		this.drawingPanel = panel;
		this.add(panel, BorderLayout.CENTER);
		this.revalidate();
		this.repaint();
	}

	public void initialize() {
		// associate
		this.toolBar.associate(this.drawingPanel);
		this.colorTool.associate(this.drawingPanel);
		this.functionTool.associate(this.drawingPanel);

		this.menuBar.associate(this.drawingPanel);

		// associated attributes
		this.setVisible(true);

		this.menuBar.initialize();
		this.toolBar.initialize();
		this.colorTool.initialize();
		this.functionTool.initialize();

		this.drawingPanel.initialize();

		this.belowPanel.initialize();

	}

	// help Menu
	private void enroll() {
		this.menuBar.addHelpListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showHelpDialog();
			}
		});
	}

	private void showHelpDialog() {
		StringBuilder sb = new StringBuilder();
		sb.append("단축키 안내:\n\n");
		/*
		 * @Information
		 * %s 는 “문자열(String) 삽입 자리 표시자”입니다. %-8s 는 “왼쪽 정렬(left-justify)된 너비 8칸의 문자열”을 뜻함.
		 * 숫자(8)만큼 최소 너비를 확보하고, 그보다 문자열이 짧으면 오른쪽에 공백을 채웁니다. 음수 부호(-)가 붙으면 왼쪽 정렬, 없으면 오른쪽 정렬.
		 */
		for (EHelpEntry item : EHelpEntry.values()) {
			sb.append(String.format("  %-8s : %s\n", item.getShortcut(), item.getDescription()));
		}
		JOptionPane.showMessageDialog(this, sb.toString(), "단축키 안내", JOptionPane.INFORMATION_MESSAGE);
	}

}
