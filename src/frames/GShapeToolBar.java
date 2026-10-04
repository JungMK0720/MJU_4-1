package frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.JToolBar;

import global.GConstants.EShapeTool;

public class GShapeToolBar extends JToolBar {
	private static final long serialVersionUID = 1L;

	// components
    private List<JRadioButton> buttons = new ArrayList<>();
	
	// associations
	private GDrawingPanel drawingPanel;

	// constructor
	public GShapeToolBar() {
        ButtonGroup buttonGroup = new ButtonGroup();
        for (EShapeTool eShapeType : EShapeTool.values()) {
            JRadioButton radioButton = new JRadioButton(eShapeType.getLabel());
            radioButton.setToolTipText(eShapeType.getToolTipText());
            radioButton.setActionCommand(eShapeType.name());
            radioButton.addActionListener(new ActionHandler());

            buttonGroup.add(radioButton);
            this.add(radioButton);
            buttons.add(radioButton);
        }
    }

	
	// Getter
	public List<JRadioButton> getButtons() {
        return buttons;
    }

		
	public void initialize() {
		// RadioButton 가져오는 용도 Default는 Select
		if (!buttons.isEmpty()) {
            buttons.get(0).doClick();  // 기본 선택 첫 버튼 클릭
        }
	}
	public void associate(GDrawingPanel drawingPanel) {
		this.drawingPanel = drawingPanel;
	}
	
	private class ActionHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String shapeType = e.getActionCommand();
            EShapeTool selectedTool = EShapeTool.valueOf(shapeType);
            if (drawingPanel != null && selectedTool != null) {
                drawingPanel.setEShapeTool(selectedTool);
            }
        }
    }
}
