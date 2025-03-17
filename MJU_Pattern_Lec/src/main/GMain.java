package main;

public class GMain {

	public static void main(String[] args) {
		// 일단은 이렇게 두 줄로 끝낸다.
		// Main과 GMainFrame을 항상 분리해놓는다. 배포, 여러 이슈로 분리가 필요
		GMainFrame mainFrmae = new GMainFrame();
		mainFrmae.setVisible(true);
		
		mainFrmae.initialize();

	}

}
