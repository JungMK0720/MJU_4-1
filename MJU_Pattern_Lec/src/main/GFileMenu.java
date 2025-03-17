package main;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class GFileMenu extends JMenu {
	private static final long serialVersionUID = 1L;
	// 얘는 특화시킬 필요가 없다. 구조적으로 변할 게 없기 때문에 그냥 가져다가 쓰면 되는 것이다.
	// 얘는 속성값, 글씨만 변하게 된다 이런 경우는 특화시키지 말라는 것. 그냥 쓰면 된다.
	JMenuItem newItem = new JMenuItem("new");

	// 값만 들어가기 때문에. 구조적으로 뭔가 추가될 ㅓㄳ이 아니기 때문에 자식을 추가할 필요가 없음
	public GFileMenu() {
		// File이랑 글씨가 Menubar에 나온다 (Super에 의해서) 글씨 쓰는 능력은 JMenu가 가지고 있고 우리는 거기에다가 특화해서 추상화하거나 변경하는 것이다. 속성을 변화시키거나 뭔가를 변경하느 ㄴ것이다
		// JMenu는 Menu로서의 모든 기능을 가지고 있지만 내가 원하는 자식, 내가 원하는 속성의 값은 가지고 있지 않다.
		super("File");
		
		this.add(this.newItem);
	}
}
