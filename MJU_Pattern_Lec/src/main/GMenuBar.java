package main;

import javax.swing.JMenuBar;

public class GMenuBar extends JMenuBar {
	private static final long serialVersionUID = 1L;

	// 자식은 모두 private으로 선언한다
	private GFileMenu fileMenu;

	public GMenuBar() {
		// 최상의 윈도우를 만들고 자식 안에 자식을 계속해서 붙여나가는 구조 이거가 Aggregation Hierarchier 구조를 만드는 가장 기본적인 방법이고. 내가 부모에서 어떤 자식을 연관하거나 그렇게 만들면 안된다. 자기 자식은 자기가 만들어야 한다.
		// 네이버 같은 곳에는 구조가 이렇게 되어 있지 않는다. 그렇게 작성하면 0점이다. Aggregation Hierarchy 방법으로 만드는 가장 중요한 기법이 직므 하고 있는 것
		// 자식을 하나 만들어서 붙였다.
		this.fileMenu = new GFileMenu();
		this.add(this.fileMenu);
	}

	public void initialize() {
		// TODO Auto-generated method stub
		
	}
}
