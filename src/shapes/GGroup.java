package shapes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import global.GConstants.EAnchor;

public class GGroup extends GShape implements Serializable {
	// attribute
	private static final long serialVersionUID = 1L;

	// components
	private List<GShape> children = new ArrayList<>();

	public GGroup() {
		super(null);
	}

	public void add(GShape shape) {
		children.add(shape);
	}

	public void remove(GShape shape) {
		children.remove(shape);
	}

	public List<GShape> getChildren() {
		return children;
	}

	// getter&& setter
	// getter
	@Override
	public Shape getShape() {
		Rectangle b = getBounds();
		return new Rectangle(b.x, b.y, b.width, b.height);
	}

	@Override
	public Rectangle getBounds() {
		if (children.isEmpty())
			return new Rectangle();
		Rectangle r = children.get(0).getBounds();
		for (int i = 1; i < children.size(); i++) {
			r = r.union(children.get(i).getBounds());
		}
		return r;
	}

	@Override
	public Rectangle getSideBounds() {
		if (children.isEmpty())
			return new Rectangle();
		Rectangle r = children.get(0).getSideBounds();
		for (int i = 1; i < children.size(); i++) {
			r = r.union(children.get(i).getSideBounds());
		}
		return r;
	}

	@Override
	public boolean contains(int x, int y) {
		// 1) 자식 도형 중 하나라도 contains(x,y) 이면 그룹 전체 선택
		for (GShape child : children) {
			if (child.contains(x, y)) {
				this.setESelectedAnchor(EAnchor.eMM);
				return true;
			}
		}

		// 2) 자식은 아니지만 그룹 바운딩 박스 내이면 선택
		Rectangle bounds = getBounds();
		if (bounds.contains(x, y)) {
			this.setESelectedAnchor(EAnchor.eMM);
			return true;
		}

		// 3) 그 외에는 unselected
		this.setESelectedAnchor(null);
		return false;
	}

	@Override
	public GShape clone() {
		GGroup copy = new GGroup();
		children.forEach(c -> copy.add(c.clone()));
		copy.setSelected(isSelected());
		return copy;
	}

	// setter
	@Override
	public void setPoint(int x, int y) {
		children.forEach(c -> c.setPoint(x, y));
	}

	@Override
	public void addPoint(int x, int y) {
		children.forEach(c -> c.addPoint(x, y));
	}

	@Override
	public void dragPoint(int x, int y) {
		children.forEach(c -> c.dragPoint(x, y));
	}

	// method
	@Override
	public void draw(Graphics2D g2D) {
		children.forEach(c -> c.draw(g2D));
		if (isSelected()) {
			Rectangle bounds = getBounds();
			Stroke old = g2D.getStroke();
			g2D.setColor(Color.BLUE);
			g2D.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.0f,
					new float[] { 6.0f }, 0f));
			g2D.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
			g2D.setStroke(old);
			int[][] pts = {

			};
			for (int[] p : pts) {
				Ellipse2D anchor = new Ellipse2D.Double(p[0] - 5, p[1] - 5, 10, 10);
				g2D.setColor(Color.WHITE);
				g2D.fill(anchor);
				g2D.setColor(Color.BLUE);
				g2D.draw(anchor);
			}
		}
	}
}
