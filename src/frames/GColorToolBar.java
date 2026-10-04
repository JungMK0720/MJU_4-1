package frames;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JPanel;

import global.GConstants.EElement;

public class GColorToolBar extends JPanel {
	private static final long serialVersionUID = 1L;

	// Color List
	private List<JButton> buttons = new ArrayList<>();

	// Parent
	private GDrawingPanel drawingPanel;

	public GColorToolBar() {
		setLayout(new FlowLayout(FlowLayout.LEFT, 2, 2));
		setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

		// Color Button List. From GConstants parsed xml.
		for (EElement.ColorSwatch swatch : EElement.colorSwatches) {
			JButton btn = new JButton();
			btn.setPreferredSize(new Dimension(18, 18));
			btn.setBackground(swatch.color);
			btn.setToolTipText(swatch.tooltip);
			btn.setFocusPainted(false);
			btn.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
			// actionListener
			btn.addActionListener(e -> fireColorSelected(swatch.color));
			add(btn);
			buttons.add(btn);
		}

		// 2. ...other Color Btn
		JButton moreBtn = new JButton(EElement.colorBtn.getLabel());
		moreBtn.setToolTipText(EElement.colorBtn.getToolTipText());
		moreBtn.setPreferredSize(new Dimension(40, 18));
		moreBtn.addActionListener(e -> {
			Color color = JColorChooser.showDialog(this, EElement.colorDialog.getLabel(), Color.BLACK);
			if (color != null)
				fireColorSelected(color);
		});
		add(moreBtn);
		buttons.add(moreBtn);
	}

	// getter
	public List<JButton> getButtons() {
		return buttons;
	}

	// association
	public void associate(GDrawingPanel drawingPanel) {
		this.drawingPanel = drawingPanel;
	}

	// DI -> GDrawingPanel
	private void fireColorSelected(Color color) {
		if (drawingPanel != null) {
			drawingPanel.setSelectedShapeFillColor(color);
		}
	}

	public void initialize() {
	}

}
