package global;

import javax.swing.JOptionPane;

import frames.GMainFrame;

public class GMain {

	public static void main(String[] args) {
		// language List
		String[] options = { "한국어", "English", "日本語" };
		String selected = (String) JOptionPane.showInputDialog(null, "언어를 선택하세요", "Language",
				JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		// default == Korea
		if (selected == null)
			selected = options[0];
		// xml configuration
		String xmlPath;
		switch (selected) {
		case "English":
			xmlPath = "src/rsc/enConfig.xml";
			break;
		case "日本語":
			xmlPath = "src/rsc/jpConfig.xml";
			break;
		case "한국어":
		default:
			xmlPath = "src/rsc/koConfig.xml";
		}
		GConstants.readFromFile(xmlPath);

		// create aggregation hierarchy
		GMainFrame mainFrame = new GMainFrame();
		// tree traverse (DFS)
		mainFrame.initialize();
	}
}
