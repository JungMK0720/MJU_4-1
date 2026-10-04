package transformer;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import shapes.GGroup;
import shapes.GShape;

public class GMover extends GTransformer {

	private GShape shape;
	private AffineTransform affineTransform;
	int px, py;

	public GMover(GShape shape) {
		super(shape);
		this.shape = shape;
	}

	public void start(Graphics2D graphics, int x, int y) {
		this.px = x;
		this.py = y;
	}

	public void drag(Graphics2D graphics, int x, int y) {
		int dx = x - px;
		int dy = y - py;

		// 그룹이면 내부 도형 모두 이동
		if (shape instanceof GGroup) {
			for (GShape child : ((GGroup) shape).getChildren()) {
				child.getAffineTransform().translate(dx, dy);
			}
		} else {
			shape.getAffineTransform().translate(dx, dy);
		}

		this.px = x;
		this.py = y;
	}

	public void setMovePoint(int x, int y) {

	}

	// it will be changed with AffineTransformation
	public void movePoint(int x, int y) {
		int dx = x - px;
		int dy = y - py;

		this.affineTransform.translate(dx, dy);

		this.px = x;
		this.py = y;
	}

	public void finish(Graphics2D graphics, int x, int y) {

	}

	@Override
	public void addPoint(Graphics2D graphics, int x, int y) {

	}
}
