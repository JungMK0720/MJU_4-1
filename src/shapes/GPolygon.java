package shapes;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.io.Serializable;

public class GPolygon extends GShape implements Serializable {
	// attribute
	private static final long serialVersionUID = 1L;

	// components
	private Polygon polygon;

	public GPolygon() {
		super(new Polygon());
		this.polygon = (Polygon) this.getShape();
	}

	// below all methods will be into Transformer
	public void setPoint(int x, int y) {
		this.polygon.addPoint(x, y);
		this.polygon.addPoint(x, y);
	}

	public void dragPoint(int x, int y) {
		this.polygon.xpoints[this.polygon.npoints - 1] = x;
		this.polygon.ypoints[this.polygon.npoints - 1] = y;
	}

	@Override
	public void addPoint(int x, int y) {
		this.polygon.addPoint(x, y);
	}

	@Override
	public Rectangle getSideBounds() {
		// TODO Auto-generated method stub
		return null;
	}
}
