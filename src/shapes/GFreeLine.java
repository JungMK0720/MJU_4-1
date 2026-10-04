package shapes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.io.Serializable;

import global.GConstants.EAnchor;

public class GFreeLine extends GShape implements Serializable {
	// attribute
	private static final long serialVersionUID = 1L;
	
	// components
	private GeneralPath path;

	public GFreeLine() {
		super(new GeneralPath());
		this.path = (GeneralPath) this.getShape();
	}

	@Override
	public void setPoint(int x, int y) {
		path.reset();
		path.moveTo(x, y);
	}

	@Override
	public void dragPoint(int x, int y) {
		path.lineTo(x, y);
	}

	@Override
	public void addPoint(int x, int y) {
	}

	@Override
	public void draw(Graphics2D g2d) {
		Shape transformed = getTransformedShape();
		g2d.draw(transformed);

		if (isSelected()) {
			this.setAnchors();
			for (int i = 0; i < anchors.length; i++) {
				Shape anchor = getAffineTransform().createTransformedShape(anchors[i]);
				Color prev = g2d.getColor();
				g2d.setColor(Color.WHITE);
				g2d.fill(anchor);
				g2d.setColor(Color.BLUE);
				g2d.draw(anchor);
				g2d.setColor(prev);
			}
		}
	}

	@Override
	public boolean contains(int x, int y) {
		Shape transformed = getTransformedShape();
		if (transformed.contains(x, y)) {
			this.setESelectedAnchor(EAnchor.eMM);
			return true;
		}
		this.setESelectedAnchor(null);
		return false;
	}

	@Override
	public Rectangle getBounds() {
		return getTransformedShape().getBounds();
	}

	@Override
	public Rectangle getSideBounds() {
		return null;
	}
}
