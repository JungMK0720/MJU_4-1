package frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import global.GConstants.EFunctionTool;

public class GFunctionToolBar extends JPanel {
	// serializable
	private static final long serialVersionUID = 1L;
	
	// component
	private List<JButton> buttons = new ArrayList<>();
    
	// parent
	private GDrawingPanel drawingPanel;

	// constructor
    public GFunctionToolBar() {
        for (EFunctionTool eFunctionTool : EFunctionTool.values()) {
            System.out.println(eFunctionTool.name() + " : " + eFunctionTool.getLabel());

            JButton button = new JButton(eFunctionTool.getLabel());
            button.setToolTipText(eFunctionTool.getToolTipText());
            button.setActionCommand(eFunctionTool.name());
            button.addActionListener(new ActionHandler());
            this.add(button);
            buttons.add(button);
        }
    }

    // getter
    public List<JButton> getButtons() {
        return buttons;
    }
    
    // dead??
//	public GDrawingPanel getDrawingPanel() {
//		return this.drawingPanel;
//	}

    // associate
    public void associate(GDrawingPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
    }

    // actionHandler
    private class ActionHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String cmd = e.getActionCommand();
            EFunctionTool selectedTool = EFunctionTool.valueOf(cmd);
            if (drawingPanel != null && selectedTool != null) {
                switch (selectedTool) {
                    case eGroup:
                        drawingPanel.groupSelectedShapes();
                        break;
                    case eUnGroup:
                    	drawingPanel.ungroupSelectedShapes();
                    	break;
                    case eCrop:
                        drawingPanel.cropSelectedArea();
                        break;
                    case eUndo:
                        drawingPanel.undo();
                        break;
                    case eRedo:
                        drawingPanel.redo();
                        break;
                    case eDelete:
                        drawingPanel.removeSelectedShapes();
                        break;
                    case eCopy:
                        drawingPanel.copySelectedShapes();
                        break;
                    case ePaste:
                        drawingPanel.pasteShapes();
                        break;
                    case eToFront:
                        drawingPanel.bringToFront();
                        break;
                    case eToBack:
                        drawingPanel.sendToBack();
                        break;
                    default:
                        break;
                }
            }
        }
    }

	public void initialize() {
		// TODO Auto-generated method stub
		
	}
}
