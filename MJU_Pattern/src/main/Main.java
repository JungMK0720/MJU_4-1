package main;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Main extends JFrame {
    JButton click_button = new JButton("Click");
    JButton cancel_button = new JButton("Cancel");
    JButton draw_button = new JButton("마우스로 그리기");  // 마우스로 그리기 버튼
    
    JTextField point1_x = new JTextField(5);
    JTextField point1_y = new JTextField(5);
    JTextField point2_x = new JTextField(5);
    JTextField point2_y = new JTextField(5);
    
    JPanel panel = new JPanel();
    
    int x1, y1, x2, y2;  // 두 점의 좌표
    
    public Main() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 800);
        setVisible(true);
        
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        
        // 좌표 입력 필드를 포함한 패널
        panel.setLayout(new FlowLayout());
        panel.add(new JLabel("Point 1 X:"));
        panel.add(point1_x);
        panel.add(new JLabel("Point 1 Y:"));
        panel.add(point1_y);
        panel.add(new JLabel("Point 2 X:"));
        panel.add(point2_x);
        panel.add(new JLabel("Point 2 Y:"));
        panel.add(point2_y);
        
        // 버튼 패널
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(click_button);
        buttonPanel.add(cancel_button);
        buttonPanel.add(draw_button);  // 마우스로 그리기 버튼 추가
        
        contentPane.add(panel, BorderLayout.CENTER);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);
        
        // 클릭 버튼 이벤트 (네모 그리기)
        click_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    x1 = Integer.parseInt(point1_x.getText());
                    y1 = Integer.parseInt(point1_y.getText());
                    x2 = Integer.parseInt(point2_x.getText());
                    y2 = Integer.parseInt(point2_y.getText());
                    new DrawRectangleFrame(x1, y1, x2, y2);  // 네모 그리기 화면 새로 띄우기
                    setVisible(false);  // 기존 화면 숨기기
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid input. Please enter valid coordinates.");
                }
            }
        });
        
        // 취소 버튼 이벤트 (모든 필드 초기화)
        cancel_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                point1_x.setText("");
                point1_y.setText("");
                point2_x.setText("");
                point2_y.setText("");
            }
        });
        
        // 마우스로 그리기 버튼 클릭 이벤트
        draw_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MouseDrawingFrame();  // 마우스로 그리기 화면 새로 띄우기
                setVisible(false);  // 기존 화면 숨기기
            }
        });
    }
    
    // 네모 그리기 프레임 클래스
    class DrawRectangleFrame extends JFrame {
        JButton back_button = new JButton("Back");
        
        public DrawRectangleFrame(int x1, int y1, int x2, int y2) {
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(600, 800);
            setVisible(true);
            
            // Back 버튼 이벤트 (좌표 입력 화면으로 돌아가기)
            back_button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setVisible(false);  // 현재 네모 그리기 화면 숨기기
                    new Main();  // 좌표 입력 화면 다시 띄우기
                }
            });
            
            Container contentPane = getContentPane();
            contentPane.setLayout(new BorderLayout());
            contentPane.add(back_button, BorderLayout.NORTH);  // Back 버튼을 상단에 배치
            
            // 네모 그리기
            add(new JPanel() {
                @Override
                public void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    int width = Math.abs(x2 - x1);
                    int height = Math.abs(y2 - y1);
                    g.drawRect(Math.min(x1, x2), Math.min(y1, y2), width, height);
                }
            });
        }
    }
    
    // 마우스로 그리기 프레임 클래스
    class MouseDrawingFrame extends JFrame {
        int startX, startY, endX, endY;  // 마우스 시작과 끝 좌표
        JButton back_button = new JButton("Back");  // Back 버튼 추가
        
        public MouseDrawingFrame() {
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(600, 800);
            setVisible(true);
            
            // 마우스 이벤트 처리
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    startX = e.getX();
                    startY = e.getY();
                }
                
                @Override
                public void mouseReleased(MouseEvent e) {
                    endX = e.getX();
                    endY = e.getY();
                    repaint();  // 네모 최종 그리기
                }
            });
            
            // 드래깅 중에 실시간으로 네모를 그리기
            addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    endX = e.getX();
                    endY = e.getY();
                    repaint();  // 드래깅 중에도 네모 그리기
                }
            });
            
            // Back 버튼 이벤트 (좌표 입력 화면으로 돌아가기)
            back_button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setVisible(false);  // 현재 마우스로 그리기 화면 숨기기
                    new Main();  // 좌표 입력 화면 다시 띄우기
                }
            });
            
            Container contentPane = getContentPane();
            contentPane.setLayout(new BorderLayout());
            contentPane.add(back_button, BorderLayout.NORTH);  // Back 버튼을 상단에 배치
        }
        
        // 네모 그리기
        @Override
        public void paint(Graphics g) {
            super.paint(g);
            if (startX != 0 && startY != 0) {
                int width = Math.abs(endX - startX);
                int height = Math.abs(endY - startY);
                g.drawRect(Math.min(startX, endX), Math.min(startY, endY), width, height);
            }
        }
    }

    public static void main(String[] args) {
        new Main();
    }
}


//명지대_정민규_패턴_프로젝트

//setTitle("명지대_정민규_패턴_프로젝트");