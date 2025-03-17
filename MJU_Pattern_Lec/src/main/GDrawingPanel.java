package main;

import java.awt.Graphics;

import javax.swing.JPanel;

public class GDrawingPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	public GDrawingPanel() {
		
	}
	
	public void draw() {
		// new해도 상관없는데 초기화 돼버린다.
		// 색깔이 유지가 되려면 OS가 준 그래픽스 그대로 가져온다.
		Graphics graphics = this.getGraphics();
		graphics.drawRect(10, 10, 50, 50);
	}

	// 그림 그려진게 해오는 거 과제
	public void initialize() {
		// TODO Auto-generated method stub
		this.draw();
	}
}
