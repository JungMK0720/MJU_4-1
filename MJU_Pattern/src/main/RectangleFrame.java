package main;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import draw.RectangleDrawer;

public class RectangleFrame {
    private JFrame frame;
    private JTextField xField;
    private JTextField yField;

    public void openRectangleFrame() {
        frame = new JFrame("네모 그리기");
        frame.setSize(400, 400);
        frame.setLayout(new FlowLayout());  // 레이아웃을 FlowLayout으로 설정

        // 레이블 및 텍스트 필드 생성
        JLabel xLabel = new JLabel("X 좌표:");
        frame.add(xLabel);
        
        xField = new JTextField(10); // 10칸 크기
        frame.add(xField);
        
        JLabel yLabel = new JLabel("Y 좌표:");
        frame.add(yLabel);
        
        yField = new JTextField(10); // 10칸 크기
        frame.add(yField);

        // "그리기" 버튼 설정
        JButton drawButton = new JButton("그리기");
        drawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // 입력값을 정수로 변환
                    int x = Integer.parseInt(xField.getText());
                    int y = Integer.parseInt(yField.getText());

                    // 입력된 값이 콘솔에 출력되어야 합니다
                    System.out.println("입력된 값 - X: " + x + ", Y: " + y);

                    // RectangleDrawer로 네모 그리기 (너비, 높이는 고정)
                    drawRectangle(x, y);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "유효한 좌표를 입력하세요.", "오류", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        frame.add(drawButton);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void drawRectangle(int x, int y) {
        // 고정된 너비와 높이 설정 (예: 100x100)
        int width = 100;
        int height = 100;

        // RectangleDrawer를 생성하여 네모를 그림
        RectangleDrawer drawer = new RectangleDrawer(x, y, width, height);
        frame.setContentPane(drawer);  // 기존 컨텐츠를 네모가 그려지는 JPanel로 교체
        frame.revalidate();  // 컴포넌트 재배치
        frame.repaint();     // 화면을 다시 그려줌
    }
}
