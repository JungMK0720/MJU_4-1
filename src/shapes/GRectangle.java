package shapes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

public class GRectangle extends GShape implements Serializable {
	// attribute
	private static final long serialVersionUID = 1L;
	
	// components
	private Rectangle2D rectangle;

	public GRectangle() {
		super(new Rectangle2D.Float(0, 0, 0, 0));
		this.rectangle = (Rectangle2D) this.getShape();
	}

	public void setPoint(int x, int y) {
		this.rectangle.setFrame(x, y, 0, 0);
	}

	public void dragPoint(int x, int y) {
		double ox = rectangle.getX();
		double oy = rectangle.getY();
		double w = x - ox;
		double h = y - oy;
		this.rectangle.setFrame(ox, oy, w, h);
	}

	@Override
	public void addPoint(int x, int y) {
	}

	@Override
	public Rectangle getSideBounds() {
		return null;
	}

	@Override
	public void draw(Graphics2D g2d) {
		Shape transformedShape = getTransformedShape();
		// 1. 내부 채우기
		if (getFillColor() != null) {
			Color prev = g2d.getColor();
			g2d.setColor(getFillColor());
			g2d.fill(transformedShape);
			g2d.setColor(prev);
		}
		// 2. 외곽선 그리기
		g2d.draw(transformedShape);
		// 3. 앵커 그리기 (선택 상태면)
		if (isSelected()) {
			this.setAnchors();
			for (int i = 0; i < anchors.length; i++) {
				Shape anchor = getAffineTransform().createTransformedShape(anchors[i]);
				Color prev = g2d.getColor();
				g2d.setColor(Color.WHITE);
				g2d.fill(anchor);
				g2d.setColor(Color.black);
				g2d.draw(anchor);
				g2d.setColor(prev);
			}
		}
	}

}
