// RectangleDrawer.java
package draw;

import javax.swing.*;
import java.awt.*;

public class RectangleDrawer extends JPanel {
    private int x, y, width, height;

    // 생성자에서 네모의 좌표와 크기를 받아서 초기화
    public RectangleDrawer(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    // paintComponent 메서드를 오버라이드하여 네모 그리기
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);  // 네모 색상 설정
        g.fillRect(x, y, width, height);  // 네모 그리기
    }
}
