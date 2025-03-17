package main;

import java.awt.FlowLayout;

import javax.swing.JRadioButton;
import javax.swing.JToolBar;

public class GToolBar extends JToolBar {
	private static final long serialVersionUID = 1L;

	private JRadioButton rectangleButton;
	public GToolBar() {
		// 왼쪽으로 옮겨오는 건 알아서 해오기
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		this.rectangleButton = new JRadioButton("rectangle");
		this.add(this.rectangleButton);
	}
	public void initialize() {
		// TODO Auto-generated method stub
		
	}
}
