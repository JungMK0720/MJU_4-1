package shapes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import global.GConstants.EAnchor;

public abstract class GShape implements Serializable {
	// attribute
	private static final long serialVersionUID = 1L;

	// components
	private final static int ANCHOR_W = 10;
	private final static int ANCHOR_H = 10;

	public enum EPoints {
		e2P, eNP
	}

	// this is not expose. this is called "Encapsulation"
	private Shape shape;
	// temp protected
	protected Ellipse2D anchors[];
	private boolean bSelected;
	private EAnchor eSelectedAnchor; // anchors type (NN, NE, etc.)
	protected Color fillColor;

	private AffineTransform affineTransform;

	public AffineTransform getAffineTransform() {
		return this.affineTransform;
	}

	public GShape(Shape shape) {
		this.shape = shape;
		this.affineTransform = new AffineTransform();

		this.anchors = new Ellipse2D[EAnchor.values().length - 1]; // move is excluded
		for (int i = 0; i < this.anchors.length; i++) {
			this.anchors[i] = new Ellipse2D.Double();
		}
		this.bSelected = false;
		this.eSelectedAnchor = null;
		this.fillColor = null;
	}

	@Override
	public GShape clone() {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(this);
			oos.flush();
			ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bis);
			return (GShape) ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// getters && setters
	// Through Function is only to use Shape.
	// color Parts
	public void setFillColor(Color color) {
		this.fillColor = color;
//		filling();
	}

	public Color getFillColor() {
		return fillColor;
	}

	protected Shape getShape() {
		return this.shape;
	}

	public Shape getTransformedShape() {
		return this.affineTransform.createTransformedShape(this.shape);
	}

	public boolean isSelected() {
		return this.bSelected;
	}

	public void setSelected(boolean bSelected) {
		this.bSelected = bSelected;
	}

	public EAnchor getESelectedAnchor() {
		return this.eSelectedAnchor;
	}

	public void setESelectedAnchor(EAnchor anchor) {
		this.eSelectedAnchor = anchor;
	}

	public Rectangle getBounds() {
		// return this.shape.getBounds -> Refactor below Code

		Shape tShape = this.getTransformedShape();
		if (tShape == null) {
			return (this.shape != null) ? this.shape.getBounds() : new Rectangle(0, 0, 0, 0);
		}
		return tShape.getBounds();
	}

	// methods
	// temp protected before private
	protected void setAnchors() {
		if (this.shape == null) {
			this.eSelectedAnchor = null;
			return;
		}

		Rectangle bounds = this.shape.getBounds();

		// anchors point
		int bx = bounds.x;
		int by = bounds.y;
		int bw = bounds.width;
		int bh = bounds.height;

		int cx = 0;
		int cy = 0;

		for (int i = 0; i < this.anchors.length; i++) {
			switch (EAnchor.values()[i]) {
			case eSS:
				cx = bx + (bw / 2);
				cy = by + bh;
				break;
			case eSE:
				cx = bx + bw;
				cy = by + bh;
				break;
			case eSW:
				cx = bx;
				cy = by + bh;
				break;
			case eNN:
				cx = bx + (bw / 2);
				cy = by;
				break;
			case eNE:
				cx = bx + bw;
				cy = by;
				break;
			case eNW:
				cx = bx;
				cy = by;
				break;
			case eEE:
				cx = bx + bw;
				cy = by + (bh / 2);
				break;
			case eWW:
				cx = bx;
				cy = by + (bh / 2);
				break;
			case eRR:
				cx = bx + (bw / 2);
				cy = by - 30;
				break;
			default:
				break;
			}

			// left - up radius moved
			anchors[i].setFrame(cx - (ANCHOR_W / 2), cy - (ANCHOR_H / 2), ANCHOR_W, ANCHOR_H);
		}

	}

	public void draw(Graphics2D graphics2D) {
		// it apply for current shape at transformed shape
		Shape transformedShape = this.affineTransform.createTransformedShape(shape);

		// 2. (항상) 내부 색상 채우기
		if (fillColor != null) {
			Color prev = graphics2D.getColor();
			graphics2D.setColor(fillColor);
			graphics2D.fill(transformedShape);
			graphics2D.setColor(prev);
		}

		graphics2D.draw(transformedShape);

		// GShape 내부 draw(Graphics2D graphics2D)
		if (bSelected) {
			this.setAnchors();
			for (int i = 0; i < this.anchors.length; i++) {
				Shape transformedAnchor = this.affineTransform.createTransformedShape(anchors[i]);
				Color prev = graphics2D.getColor();
				Color bg = graphics2D.getBackground();
				graphics2D.setColor(bg);
				// 채우기: 패널 배경색
				graphics2D.fill(transformedAnchor);
				graphics2D.setColor(prev);
				graphics2D.draw(transformedAnchor);
			}
		} else {

		}

	}

	// +@ current selected vs unselected
	public boolean contains(GShape other) {
		Rectangle currentBounds = this.getBounds();
		Rectangle otherBounds = other.getBounds();
		return currentBounds.contains(otherBounds);
	}

	public boolean contains(int x, int y) {
		// 1) 선택된 상태라면 앵커(hit) 검사
		if (bSelected) {
			for (int i = 0; i < anchors.length; i++) {
				Shape anchorShape = (affineTransform != null) ? affineTransform.createTransformedShape(anchors[i])
						: anchors[i];
				if (anchorShape.contains(x, y)) {
					this.eSelectedAnchor = EAnchor.values()[i];
					return true;
				}
			}
		}

		// 2) 본체(hit) 검사
		if (shape == null) {
			// 도형 자체가 없으면 false
			return false;
		}
		Shape body = (affineTransform != null) ? affineTransform.createTransformedShape(shape) : shape;
		if (body.contains(x, y)) {
			this.eSelectedAnchor = EAnchor.eMM;
			return true;
		}

		// 3) 아무 것도 hit 하지 않음
		this.eSelectedAnchor = null;
		return false;
	}
	
	public EAnchor hitTestAnchor(int x, int y) {
	    // 0) shape 검사
		if (this.shape == null) {
	        this.eSelectedAnchor = null;
	        return null;
	    }
		// 1) 앵커 위치 계산 (always)
		setAnchors();
		for (int i = 0; i < anchors.length; i++) {
			Shape a = affineTransform.createTransformedShape(anchors[i]);
			if (a.contains(x, y)) {
				eSelectedAnchor = EAnchor.values()[i];
				return eSelectedAnchor;
			}
		}

		// 2) 본체(hit) 검사
		Shape body = affineTransform.createTransformedShape(shape);
		if (body.contains(x, y)) {
			eSelectedAnchor = EAnchor.eMM; // 이동 커서
			return eSelectedAnchor;
		}

		// 3) 아무 곳도 아니면
		eSelectedAnchor = null;
		return null;
	}
	
	// abstract Method
	public abstract void setPoint(int x, int y);

	public abstract void addPoint(int x, int y);

	public abstract void dragPoint(int x, int y);

	public abstract Rectangle getSideBounds();

}
