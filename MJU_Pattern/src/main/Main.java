package main;

import javax.swing.*;
import java.awt.event.*;

public class Main {
    private JButton start = new JButton();
    private JFrame window;

    // ActionListener 내에서 Main 클래스 인스턴스를 참조하기 위한 방법
    private void run() {
        start.setText("시작");
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (start.getText().equals("시작")) {
                    window.setVisible(false);  // 'window'를 숨기기
                    new HomeFrame().homeFrame();  // 새로운 HomeFrame을 생성하여 표시
                } else {
                    System.out.println("알맞은 동작을 해주십시오");
                }
            }
        });
    }

    private void initialize() {
        window = new JFrame("");
        window.setSize(500, 600);
        start.setText("시작");
        window.add(start);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.initialize();
        main.run();
    }
}

//명지대_정민규_패턴_프로젝트
