package frames;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Vector;
import java.util.stream.Collectors;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import global.GConstants.EAnchor;
import global.GConstants.EShapeTool;
import shapes.GGroup;
import shapes.GShape;
import shapes.GShape.EPoints;
import shapes.GTextArea;
import transformer.GDrawer;
import transformer.GMover;
import transformer.GResizer;
import transformer.GRotater;
import transformer.GTransformer;

public class GDrawingPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	public enum EDrawingState {
		eIdle, e2P, eNP
	}

	// components
	private Vector<GShape> shapes;
	private Vector<GShape> selectedShapes; // for groups

	// redo / undo
	private Stack<List<GShape>> undoStack;
	private Stack<List<GShape>> redoStack;
	// copy / paste
	private List<GShape> copyBuffer;

	// working objects
	private GTransformer transformer;
	private GShape currentShape;
	private GShape selectedShape;

	private EShapeTool eShapeTool;
	private EDrawingState eDrawingState;
	private boolean bUpdated;

	// constructors
	public GDrawingPanel() {
		MouseEventHandler mouseHandler = new MouseEventHandler();
		this.addMouseListener(mouseHandler);
		this.addMouseMotionListener(mouseHandler);

		constMembers();
		setupKeyBindings();
	}

	private void constMembers() {
		this.shapes = new Vector<GShape>();
		this.selectedShapes = new Vector<GShape>();
		this.undoStack = new Stack<>();
		this.redoStack = new Stack<>();
		this.copyBuffer = new ArrayList<>();

		this.currentShape = null;
		this.selectedShape = null;
		this.eShapeTool = null;
		this.eDrawingState = EDrawingState.eIdle;
		this.bUpdated = false;
	}

	private void setupKeyBindings() {
		InputMap im = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap am = this.getActionMap();

		setCommandKey(im, am);
	}

	public void initialize() {
		this.shapes.clear();
		this.repaint();
	}

	// getters and setters
	public List<GShape> getSelectedShapes() {
		return shapes.stream().filter(GShape::isSelected).collect(Collectors.toList());
	}

	/** 모든 도형의 선택 상태를 해제 */
	public void clearSelection() {
		for (GShape s : shapes) {
			s.setSelected(false);
			s.setESelectedAnchor(null);
		}
		this.selectedShape = null;
	}

	// getter
	public Vector<GShape> getShapes() {
		return this.shapes;
	}

	// setter
	public void setEShapeTool(EShapeTool eShapeType) {
		this.eShapeTool = eShapeType;
	}

	@SuppressWarnings("unchecked")
	public void setShapes(Object shapes) {
		this.shapes = (Vector<GShape>) shapes; // Type Casting
		this.repaint();
	}

	public boolean isUpdated() {
		return this.bUpdated;
	}

	public void setBUpdated(boolean bUpdated) {
		this.bUpdated = bUpdated;
	}

	// Fill
	public void setSelectedShapeFillColor(Color color) {
		if (selectedShape != null) {
			selectedShape.setFillColor(color);
			saveStateForUndo();
			this.repaint();
		}
	}

	// Select
	private void selectShape(GShape shape) {
		for (GShape otherShape : this.shapes) {
			otherShape.setSelected(false);
		}
		this.currentShape.setSelected(true);
	}

	public void deselectAllShapes() {
		selectedShapes.clear();
	}

	public void removeSelectedShapes() {
		// Pick Really isSelected being true.
		List<GShape> toRemove = new ArrayList<>();
		for (GShape s : shapes) {
			if (s.isSelected()) {
				toRemove.add(s);
			}
		}
		System.out.println("삭제 대상: " + toRemove);
		if (toRemove.isEmpty())
			return;

		shapes.removeAll(toRemove);
		
		// clear current State
		clearSelection();
		selectedShapes.clear();

		// Undo/redo Repository's added
		saveStateForUndo();

		repaint();
	}

	// methods

	// Group
	public void groupSelectedShapes() {
		List<GShape> sel = getSelectedShapes();
		if (sel.size() < 2)
			return;

		GGroup group = new GGroup();
		for (GShape s : sel) {
			group.add(s);
			shapes.remove(s);
		}
		shapes.add(group);

		clearSelection();
		group.setSelected(true);

		this.setBUpdated(true);

		saveStateForUndo();
		repaint();
	}

	// unGroup
	public void ungroupSelectedShapes() {
		List<GShape> sel = getSelectedShapes();

		// Just Pick Group!
		sel.stream().filter(GGroup.class::isInstance).map(GGroup.class::cast).forEach(g -> {
			shapes.remove(g);
			shapes.addAll(g.getChildren());
		});

		clearSelection();

		// updated
		this.setBUpdated(true);
		saveStateForUndo();
		repaint();
	}

	// copy
	public void copySelectedShapes() {
		copyBuffer.clear();
		for (GShape shape : selectedShapes) {
			// Deep Copy
			copyBuffer.add(shape.clone()); 
		}
	}

	// paste
	public void pasteShapes() {
		for (GShape shape : copyBuffer) {
			GShape pastedShape = shape.clone();
			this.shapes.add(pastedShape);
			pastedShape.setSelected(false);
		}
		saveStateForUndo();
		repaint();
	}

	// re-do / undo
	// Re-do
	// Save State

	// Redo 동작
	public void redo() {
		System.out.println("redo is called");
		if (!redoStack.isEmpty()) {
			System.out.println("Redo Stak isn't empty");
			for (List<GShape> shape : redoStack) {
				System.out.println(shape);
			}

			undoStack.push(deepCopy(this.shapes)); // 현재 상태 Undo에 저장
			List<GShape> next = redoStack.pop();
			this.shapes.clear();
			this.shapes.addAll(next);
			this.repaint();
		}
	}

	// Undo
	// Save State
	private void saveStateForUndo() {
		// 도형 리스트의 깊은 복사
		List<GShape> copy = new ArrayList<>();
		for (GShape shape : this.shapes) {
			copy.add(shape.clone());
		}
		undoStack.push(copy);
		redoStack.clear();
	}

	// Undo 동작
	public void undo() {
		System.out.println("undo is called");
		if (!undoStack.isEmpty()) {
			System.out.println("uedo Stak isn't empty");

			redoStack.push(deepCopy(this.shapes)); // 현재 상태 Redo에 저장
			List<GShape> prev = undoStack.pop();
			this.shapes.clear();
			this.shapes.addAll(prev);
			this.repaint();
		}
	}

	// deepCopy 내부 함수
	private List<GShape> deepCopy(List<GShape> list) {
		List<GShape> copy = new ArrayList<>();
		for (GShape shape : list) {
			copy.add(shape.clone());
		}
		return copy;
	}

	// Z - Order
	// Move Front
	public void bringToFront() {
		List<GShape> sel = getSelectedShapes();
		if (sel.isEmpty())
			return;
		// shapes 리스트에서 삭제 후 맨 끝에 추가
		for (GShape s : sel) {
			shapes.remove(s);
			shapes.add(s);
		}
		saveStateForUndo();
		repaint();
	}

	// Move -Back
	public void sendToBack() {
		List<GShape> sel = getSelectedShapes();
		if (sel.isEmpty())
			return;
		// shapes 리스트에서 삭제 후 맨 앞에 추가 (역순으로)
		for (int i = sel.size() - 1; i >= 0; i--) {
			GShape s = sel.get(i);
			shapes.remove(s);
			shapes.add(0, s);
		}
		saveStateForUndo();
		repaint();
	}

	// Paint
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		for (GShape shape : shapes) {
			shape.draw((Graphics2D) graphics);
		}
	}

	// Check
	private GShape onShape(int x, int y) {
		for (GShape shape : shapes) {
			if (shape.contains(x, y)) {
				return shape;
			}
		}
		return null;
	}

	// Transform
	private void startTransform(int x, int y) {
		// set shape
		this.currentShape = eShapeTool.newShape();
		this.shapes.add(this.currentShape);

		if (this.eShapeTool == EShapeTool.eSelect) {
			// this checks below shapes
			this.selectedShape = onShape(x, y);
			if (this.selectedShape == null) {
				this.transformer = new GDrawer(this.currentShape);
			} else if (this.selectedShape.getESelectedAnchor() == EAnchor.eMM) {
				this.transformer = new GMover(this.selectedShape);
			} else if (this.selectedShape.getESelectedAnchor() == EAnchor.eRR) {
				this.transformer = new GRotater(this.selectedShape);
			} else {
				this.transformer = new GResizer(this.selectedShape);
			}
		} else {
			this.transformer = new GDrawer(this.currentShape);
		}
		this.transformer.start((Graphics2D) getGraphics(), x, y);
	}

	private void keepTransform(int x, int y) {
		this.transformer.drag((Graphics2D) getGraphics(), x, y);
		this.repaint();
	}

	private void finishTransform(int x, int y) {
		this.transformer.finish((Graphics2D) getGraphics(), x, y);

		// current tool selection is currentShape
		if (this.eShapeTool == EShapeTool.eSelect) {
			this.shapes.remove(this.shapes.size() - 1);
			this.selectShape(this.currentShape);
			for (GShape shape : this.shapes) {
				if (this.currentShape.contains(shape)) {
					// for GGroup
					selectedShapes.add(shape);
					shape.setSelected(true);

				} else {
					shape.setSelected(false);
				}
			}
		} else if (this.eShapeTool == EShapeTool.eTextArea) {
			String text = JOptionPane.showInputDialog(this, "텍스트를 입력하세요:");
			if (text != null) {
				((GTextArea) this.currentShape).setText(text);
			}
			saveStateForUndo();
		} else {
			for (GShape shape : shapes) {
				shape.setSelected(false);
			}
			this.currentShape.setSelected(true); // 방금 그린 도형만 선택
			saveStateForUndo();
		}
		this.bUpdated = true;
		this.repaint();
	}

	// For Polygon
	private void addPoint(int x, int y) {
		this.transformer.addPoint((Graphics2D) getGraphics(), x, y);
	}

	private void changeCursor(int x, int y) {
	    if (this.eShapeTool != EShapeTool.eSelect) {
	        return;
	    }

	    // 1) 클릭해서 선택된 도형(selectedShape)은 그대로 두고, 마우스 위치 위의 도형만 따로 찾는다.
	    GShape hovered = onShape(x, y);
	    if (hovered != null) {
	        // 2) hover용 히트테스트로 앵커·이동 커서 결정
	        EAnchor hit = hovered.hitTestAnchor(x, y);
	        if (hit != null) {
	            setCursor(hit.getCursor());
	        } else {
	            setCursor(Cursor.getDefaultCursor());
	        }
	    } else {
	        // 3) 아무 hover 대상 없으면 기본 커서
	        setCursor(Cursor.getDefaultCursor());
	    }
	}
	
	private class MouseEventHandler implements MouseListener, MouseMotionListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 1) {
				this.mouse1Clicked(e);
			} else if (e.getClickCount() == 2) {
				this.mouse2Clicked(e);
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			if (eDrawingState == EDrawingState.e2P) {
				keepTransform(e.getX(), e.getY());
			} else if (eDrawingState == EDrawingState.eNP) {
				keepTransform(e.getX(), e.getY());
			} // Idle Statements. but explicitly codes is essential
			else if (eDrawingState == EDrawingState.eIdle) {
				changeCursor(e.getX(), e.getY());
			}
		}

		private void mouse2Clicked(MouseEvent e) {
			if (eDrawingState == EDrawingState.e2P) {
				finishTransform(e.getX(), e.getY());
				eDrawingState = EDrawingState.eIdle;
			} else {
				finishTransform(e.getX(), e.getY());
				eDrawingState = EDrawingState.eIdle;
			}
		}

		private void mouse1Clicked(MouseEvent e) {
			if (eDrawingState == EDrawingState.eIdle) {
				if (eShapeTool.getEPoints() == EPoints.e2P) {
					startTransform(e.getX(), e.getY());
					eDrawingState = EDrawingState.e2P;
				} else if (eShapeTool.getEPoints() == EPoints.eNP) {
					startTransform(e.getX(), e.getY());
					eDrawingState = EDrawingState.eNP;
				}
			} else if (eDrawingState == EDrawingState.e2P) {
				finishTransform(e.getX(), e.getY());
				eDrawingState = EDrawingState.eIdle;
			} else if (eDrawingState == EDrawingState.eNP) {
				addPoint(e.getX(), e.getY());
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {

		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

	}

	// Key Setter
	private void setCommandKey(InputMap im, ActionMap am) {
		// TODO Auto-generated method stub
		// Undo (Ctrl+Z)
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK), "undo");
		am.put("undo", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				undo();
			}
		});

		// Redo (Ctrl+Y)
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK), "redo");
		am.put("redo", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				redo();
			}
		});

		// Copy (Ctrl+C)
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK), "copy");
		am.put("copy", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				copySelectedShapes();
			}
		});

		// Paste (Ctrl+V)
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK), "paste");
		am.put("paste", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				pasteShapes();
			}
		});

		// Delete (Delete)
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "delete");
		am.put("delete", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				removeSelectedShapes();
			}
		});

		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK), "toFront");
		am.put("toFront", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				bringToFront();
			}
		});

		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_DOWN_MASK), "toBack");
		am.put("toBack", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				sendToBack();
			}
		});

	}

	public void cropSelectedArea() {
		// TODO Auto-generated method stub

	}

}
