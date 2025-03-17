package main;

import java.awt.FlowLayout;

import javax.swing.JFrame;

public class GMainFrame extends JFrame {
	// 메뉴바 툴바 이제 만든다.
	private static final long serialVersionUID = 1L;
	
//	private int x, y; // 실제로는 JFrame이 갖고있어서 상속받아서 쓸 것이다 따라서 제거
	
	// 부품을 만들 떄는 항상 private로 선언
	// 아래에 있는 클래스를 만들어서 instantitation해서 부품화 할 것이다.
	private GMenuBar menuBar;
	private GToolBar toolBar;
	private GDrawingPanel drawingPanel;
	
	public GMainFrame() {
		// 나의 속성 변화에 관한 이야기. Attribute에 관한 이야기
		this.setLocation(100, 200); // 내가 setLocation이라는 함수 안가지고 있음 JFrame이 가지고 있는 것. 이미 여기에 SRC Code 다 오픈되어 있음. Java 중에서 src 오픈된게 있음. sun에서 만든 java source code는 다 오픈되어 있으니까 그거 찾아오기
		this.setSize(600, 400);
		// 창닫기 누르면 프로그램 꺼지게 만듦
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		
		// Components
		// FlowLayout을 쓰겠다고 얘기하는 것.
		// 이걸 쓰면 overlapping 안시키고 순서대로 쌓는다. add하는 순서대로 순서대로 쌓는다.
		this.setLayout(new FlowLayout()); 
		
		
		// 내 자식에 관한 이야기. Components (menubar toolbar drawingpanel)
		// instantiation했다.

		// 자식으로 만들어서 단다. 이거만 set
		this.menuBar = new GMenuBar();
		this.setJMenuBar(menuBar);
		
		this.toolBar = new GToolBar();
		this.add(toolBar);
		
		this.drawingPanel = new GDrawingPanel();
		this.add(drawingPanel);
		
		
		
	}

	public void initialize() {
		this.menuBar.initialize();
		this.toolBar.initialize();
		this.drawingPanel.initialize();
	}

}
