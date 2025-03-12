package main;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class HomeFrame {

    JButton button_1 = new JButton();
    JButton button_2 = new JButton();
    JButton button_3 = new JButton();
    JButton button_4 = new JButton();
    JButton button_5 = new JButton();
    
    JFrame home;

    public JFrame homeFrame() {
        home = new JFrame("Home Frame");  // home 객체 초기화
        home.setSize(400, 400);
        home.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        setTopComponents();  // home 객체가 초기화된 후에 호출
        setComponents();  // 다른 컴포넌트들도 설정
        
        home.setVisible(true);  // 새로운 프레임을 보이게 하기
        return home;
    }
    
//    private void initialize(new ActionListener() {
//    	@Override
//    	public void actionPerformed(ActionEvent e) {
//    		new Main().initialize();
//    	}
//    });
    
    private void setComponents() {
        button_1.setText("홈화면");
        button_1.setBounds(10, 10, 10, 50);  // 버튼 크기 설정
        home.add(button_1);  // 버튼을 JFrame에 추가
    }

    private void setTopComponents() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        // 기존 button_1을 클래스 변수로 사용
        button_1.setText("홈화면");
        button_1.setBounds(10, 10, 100, 50);  // 버튼 크기와 위치 설정
        topPanel.add(button_1);

        // "네모 그리기" 버튼 추가
        JButton drawRectangleButton = new JButton("네모 그리기");
        drawRectangleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RectangleFrame().openRectangleFrame();  // 새로운 JFrame 열기
            }
        });
        
        JButton drawRectangle = new JButton("직접 그리기");
        drawRectangle.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		new RectangleFrame().openRectangleFrame();
        	}
        });

        topPanel.add(drawRectangleButton);  // "네모 그리기" 버튼을 추가
        home.add(topPanel, BorderLayout.NORTH);  // 상단에 패널 추가
    }
}
