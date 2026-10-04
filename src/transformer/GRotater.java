package transformer;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import global.GConstants.EAnchor;
import shapes.GShape;

public class GRotater extends GTransformer {
	private int px, py;
	private int cx, cy;
	private double startAngle;
	private EAnchor eRotateAnchor;

	public GRotater(GShape shape) {
		super(shape); // 부모의 protected shape 필드 초기화
		this.eRotateAnchor = null;
	}

	@Override
	public void start(Graphics2D graphics, int x, int y) {
		this.px = x;
		this.py = y;

		this.eRotateAnchor = this.shape.getESelectedAnchor(); // 부모의 protected shape 사용
		Rectangle bounds = this.shape.getBounds();
		this.cx = bounds.x + bounds.width / 2;
		this.cy = bounds.y + bounds.height / 2;
		this.startAngle = Math.atan2(y - cy, x - cx);
	}

	@Override
	public void drag(Graphics2D graphics, int x, int y) {
		double currentAngle = Math.atan2(y - cy, x - cx);
		double angleDelta = currentAngle - startAngle;

		this.shape.getAffineTransform().rotate(angleDelta, cx, cy);

		this.px = x;
		this.py = y;
		this.startAngle = currentAngle;
	}

	@Override
	public void finish(Graphics2D graphics, int x, int y) {

	}

	@Override
	public void addPoint(Graphics2D graphics, int x, int y) {

	}

}
