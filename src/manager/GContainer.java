package manager;

import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import frames.GColorToolBar;
import frames.GFunctionToolBar;
import frames.GShapeToolBar;
import global.GConstants.EElement;
import global.GConstants.EFunctionTool;
import global.GConstants.EShapeTool;
import global.GConstants.EToolBarGroup;

public class GContainer extends JPanel {
	// serializable
    private static final long serialVersionUID = 1L;
    
    // components
    private GShapeToolBar toolbar;
    private GFunctionToolBar functionTool;
    private GColorToolBar colorTool;
    private List<JRadioButton> btnList;
    private List<JButton> fList;
    private List<JButton> cList;

    // constructor
    public GContainer() {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        createGroupPanels();
    }

    // 그룹별 패널 생성 (eShape, eFunction, eColor 등)
    private void createGroupPanels() {
        for (EToolBarGroup group : EToolBarGroup.values()) {
            JPanel groupPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            groupPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(java.awt.Color.GRAY),
                group.getLabel(),
                TitledBorder.LEFT,
                TitledBorder.TOP));
            groupPanel.setToolTipText(group.getToolTipText());
            this.add(groupPanel);
        }
    }

    // === 도형 도구(모드) 버튼 (JRadioButton) 할당 ===
    public void setToolBar(GShapeToolBar toolBar) {
        this.toolbar = toolBar;
        this.btnList = toolbar.getButtons();
        setButtons(btnList);
    }

    private void setButtons(List<JRadioButton> btnList) {
        if (btnList == null || btnList.isEmpty()) return;
        int groupCount = getComponentCount();
        for (JRadioButton btn : btnList) {
            String name = btn.getActionCommand();
            EShapeTool tool = EShapeTool.valueOf(name);
            EToolBarGroup group = tool.getPertainGroup();
            int groupIndex = group.ordinal();
            if (groupIndex < groupCount) {
                JPanel groupPanel = (JPanel) getComponent(groupIndex);
                groupPanel.add(btn);
            }
        }
        revalidate();
        repaint();
    }

    // === 기능 버튼 (즉시 실행) 할당 ===
    public void setFunctionBar(GFunctionToolBar functionTool) {
        this.functionTool = functionTool;
        this.fList = functionTool.getButtons();
        setFButtons(fList);
    }

    private void setFButtons(List<JButton> fList) {
        if (fList == null || fList.isEmpty()) return;
        int groupCount = getComponentCount();
        for (JButton btn : fList) {
            String name = btn.getActionCommand();
            EFunctionTool tool = EFunctionTool.valueOf(name);
            EToolBarGroup group = tool.getPertainGroup();
            int groupIndex = group.ordinal();
            if (groupIndex < groupCount) {
                JPanel groupPanel = (JPanel) getComponent(groupIndex);
                groupPanel.add(btn);
            }
        }
        revalidate();
        repaint();
    }

    // === 색상 팔레트/더보기 버튼 (JButton) 할당 ===
    public void setColorBar(GColorToolBar colorTool) {
        this.colorTool = colorTool;
        this.cList = colorTool.getButtons();
        setCButtons(cList);
    }

    private void setCButtons(List<JButton> cList) {
        if (cList == null || cList.isEmpty()) return;
        int groupCount = getComponentCount();
        for (JButton btn : cList) {
        	
            EToolBarGroup group = EToolBarGroup.eColor;
            int groupIndex = group.ordinal();
            if (groupIndex < groupCount) {
                JPanel groupPanel = (JPanel) getComponent(groupIndex);
                groupPanel.add(btn);
            }
        }
        revalidate();
        repaint();
    }

    // getter
    public GColorToolBar getColorToolBar() {
        return colorTool;
    }
}
