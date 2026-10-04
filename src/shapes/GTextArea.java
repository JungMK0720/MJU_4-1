package shapes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import global.GConstants.EAnchor;

public class GTextArea extends GShape implements Serializable {
	// attribute
	private static final long serialVersionUID = 1L;

	// components
	private String text = "";
	private Rectangle2D rectangle;

	// constructor
	public GTextArea() {
		super(new Rectangle2D.Float(0, 0, 0, 0));
		this.rectangle = (Rectangle2D) this.getShape();
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	@Override
	public boolean contains(int x, int y) {
		Shape transformed = getTransformedShape(); // 변환된 도형
		if (transformed.contains(x, y)) {
			this.setESelectedAnchor(EAnchor.eMM); // 중앙 anchor로 세팅 (여러 개면 분기)
			return true;
		}
		this.setESelectedAnchor(null);
		return false;
	}

	@Override
	public Rectangle getBounds() {
		return rectangle.getBounds();
	}

	@Override
	public void setPoint(int x, int y) {
		this.rectangle.setFrame(x, y, 0, 0);
	}

	@Override
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
	public void draw(Graphics2D graphics) {
		Shape transformedShape = getTransformedShape();

//        // 1. 배경 없음 (투명)
//        // 2. 외곽선
		graphics.setColor(Color.BLACK);
		graphics.draw(transformedShape);

		// 3. 텍스트 표시 (가운데 맞추기)
		if (text != null && !text.isEmpty()) {
			Rectangle2D bounds = transformedShape.getBounds2D();
			graphics.setColor(Color.BLACK);
			graphics.drawString(text, (int) bounds.getX() + 5, (int) (bounds.getY() + bounds.getHeight() / 2 + 5));
		}
	}
}
