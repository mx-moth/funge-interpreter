package maelstrom.funge.gui;

import java.awt.*;
import java.awt.event.*;
import java.util.Stack;

import javax.swing.JViewport;
import javax.swing.JScrollPane;

import maelstrom.funge.interpreter.*;
import maelstrom.funge.event.*;


@SuppressWarnings("serial")
public class GridEditor extends Container implements KeyListener, MouseListener, MouseMotionListener, GridChangeListener, PointerChangeListener {

	private static final int     AUTOSCROLL_EXTRA = 5;
	private static final int     CTRL_JUMP_SIZE = 10;

	private Dimension     size;
	private Dimension     cell;

	private Grid          grid;

	private Pointer       pointer;
	private Vector        selectionEnd;

	private Stack<Vector> changes          = new Stack<Vector>();
	private Rectangle     oldBounds;

	private Image         backBuffer;

	public GridEditor(Dimension size) {
		this(new Grid(size));
	}

	public GridEditor(Grid grid) {
		this.size = grid.getSize();
		this.cell = new Dimension(16, 18);
		this.pointer = new Pointer(this.size);

		this.grid = grid;

		this.addKeyListener(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.grid.addGridChangeListener(this);
		this.pointer.addPointerChangeListener(this);

		this.setBackground(Color.WHITE);
		this.requestFocus();
	}

	public JScrollPane createScrollPaneForEditor() {
		JScrollPane gridScroll = new JScrollPane(this);
		gridScroll.setRowHeader(new GridHeader(this, GridHeader.VERTICAL).createViewportForHeader());
		gridScroll.setColumnHeader(new GridHeader(this, GridHeader.HORIZONTAL).createViewportForHeader());
		return gridScroll;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		Rectangle bounds = g.getClipBounds();

		boolean equal = bounds.equals(oldBounds);
		oldBounds = bounds;

		// Draw the whole thing if it needs it
		if (!equal) {
			backBuffer = createImage(bounds.width, bounds.height);
			Graphics gBack = backBuffer.getGraphics();
			gBack.setColor(this.getBackground());
			gBack.fillRect(0, 0, bounds.width, bounds.height);
			gBack.translate(-bounds.x, -bounds.y);

			// Calculate the bounds of the grid we are looking at
			int startX = bounds.x / cell.width;
			int startY = bounds.y / cell.height;
			int endX = Math.min((bounds.x + bounds.width) / cell.width, size.width);
			int endY = Math.min((bounds.y + bounds.height) / cell.height, size.height);

			// Draw all the squares
			gBack.setFont(new Font("SansSerif", 0, 14));

			for (int x = startX; x < endX; x++) {
				for (int y = startY; y < endY; y++) {
					drawSquare(gBack, this.getBackground(), x, y);
				}
			}

			int barEndX = Math.min(bounds.x + bounds.width, (size.width) * cell.width);
			int barEndY = Math.min(bounds.y + bounds.height, (size.height) * cell.height);

			// Draw the grid lines
			for (int x = startX; x < endX + 1; x++) {
				gBack.drawLine((x) * cell.width, bounds.y, (x) * cell.width, barEndY);
			}
			for (int y = startY; y < endY + 1; y++) {
				gBack.drawLine(bounds.x, (y) * cell.height, barEndX, (y) * cell.height);
			}
		}

		// Get and set up the graphics object for the image
		Graphics gBack = backBuffer.getGraphics();
		gBack.setClip(0, 0, bounds.width - cell.width, bounds.height - cell.height);
		gBack.translate(-bounds.x, -bounds.y);
		gBack.setFont(new Font("SansSerif", 0, 14));

		// Paint the new changes made
		while (!changes.isEmpty()) {
			Vector pos = changes.pop();
			drawSquare(gBack, this.getBackground(), pos.getX(), pos.getY());
		}

		// Draw the selection box
		if (selectionEnd != null && isEnabled()) {

			Vector selectionStart = pointer.getPosition();

			int startX = Math.min(selectionStart.getX(), selectionEnd.getX());
			int startY = Math.min(selectionStart.getY(), selectionEnd.getY());
			int endX = Math.max(selectionStart.getX(), selectionEnd.getX()) + 1;
			int endY = Math.max(selectionStart.getY(), selectionEnd.getY()) + 1;

			for (int x = startX; x < endX; x++) {
				for (int y = startY; y < endY; y++) {
					drawSquare(gBack, Color.GREEN, x, y);

					// Add the selected square to the redraw list.
					// The next time we redraw, chances are this square will not be selected,
					// so it will need redrawing
					changes.push(new Vector(x, y));
				}
			}
		}

		// Draw the selected square
		Vector pos = pointer.getPosition();
		drawSquare(gBack, Color.YELLOW, pos.getX(), pos.getY());

		// Add the selected square to the redraw list.
		// The next time we redraw, chances are this square will not be selected,
		// so it will need redrawing
		changes.push(pos);

		// Paint the current image to the real graphics object
		g.drawImage(backBuffer, bounds.x, bounds.y, this);
	}

	/**
	 * Draws a single grid square, plus contents, with the supplied background color.
	 * @param g
	 *        The graphics object to draw the square upon.
	 * @param back
	 *        The background colour to use.
	 * @param x
	 *        The x coordinate of the grid square.
	 * @param y
	 *        The y coordinate of the grid square.
	 */
	public void drawSquare(Graphics g, Color back, int x, int y) {
		g.setColor(back);
		g.fillRect(x * cell.width + 1, y * cell.height + 1, cell.width - 1, cell.height - 1);

		g.setColor(Color.BLACK);
		String text = "" + (char) (grid.get(x, y));
		int textOffset = (cell.width - g.getFontMetrics().stringWidth(text)) >> 1;
		g.drawString(text, (x) * cell.width + textOffset, (y + 1) * cell.height - 3);
	}

	public void repaintAll() {
		backBuffer = null;
		oldBounds = null;
		repaint();
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension((size.width + 1) * cell.width, (size.height + 1) * cell.height);
	}
	@Override
	public Dimension getMinimumSize() {
		return new Dimension((size.width) * cell.width, (size.height) * cell.height);
	}


	public Grid getGrid() {
		return this.grid;
	}

	public void setGrid(Grid grid) {
		this.grid = grid;
	}


	public Pointer getPointer() {
		return this.pointer;
	}

	public void setPointer(Pointer selection) {
		this.pointer = selection;
	}

	/**
	 * Gets a copy of the cell size dimension
	 * Note: To modify the cell size, use setCell()
	 * @return The size of the cells
	 */
    public Dimension getCell() {
    	return (Dimension)this.cell.clone();
    }

    public void setCell(Dimension cell) {
    	this.cell = cell;
    	this.oldBounds = null;
    	this.repaint();
    }

	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		selectionEnd = null;
	}

	@Override
	public void keyPressed(KeyEvent e) {

		if (isEnabled()) {
			boolean moved = false;
			int keycode = e.getKeyCode();

			// Move if an arrow key has been pressed
			switch (keycode) {
				case KeyEvent.VK_UP:
					pointer.setDirection(new Vector(0, -1));
					moved = true;
					e.consume();
					break;
				case KeyEvent.VK_RIGHT:
					pointer.setDirection(new Vector(1, 0));
					moved = true;
					e.consume();
					break;
				case KeyEvent.VK_DOWN:
					pointer.setDirection(new Vector(0, 1));
					moved = true;
					e.consume();
					break;
				case KeyEvent.VK_LEFT:
					pointer.setDirection(new Vector(-1, 0));
					moved = true;
					e.consume();
					break;
			}

			// If we are supposed to move...
			if (moved) {
				// If ctrl is held down, skip a few blocks
				if ((e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) != 0) {
					for (int i = 0; i < GridEditor.CTRL_JUMP_SIZE; i++) {
						pointer.move();
					}
				} else if ((e.getModifiersEx() & KeyEvent.ALT_DOWN_MASK) != 0) {
					// alt+dir just changes the pointer direction, so do nothing.
				} else {
					pointer.move();
				}

				// If shift is not held down, update the selection pointer to the current position
				if ((e.getModifiersEx() & KeyEvent.SHIFT_DOWN_MASK) == 0) {
					selectionEnd = pointer.getPosition();
				}
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

		if (this.isEnabled()) {
			Vector pos;
			char keyChar = e.getKeyChar();
			boolean move = false;

			// Anything below 31 is a control character, and we do not need them at all.
			if (keyChar > 31 || keyChar == KeyEvent.VK_BACK_SPACE) {
				switch (keyChar) {
					case KeyEvent.VK_BACK_SPACE:
						pointer.moveBack();
						selectionEnd = pointer.getPosition();

						pos = pointer.getPosition();
						grid.set(pos, Funge.SPACE_CHAR);
						break;

					case KeyEvent.VK_DELETE:
						pos = pointer.getPosition();
						grid.set(pos, Funge.SPACE_CHAR);
						move = true;
						break;

					case KeyEvent.VK_ESCAPE:
					case KeyEvent.VK_UNDEFINED:
						break;


					default:
						pos = pointer.getPosition();
						grid.set(pos, e.getKeyChar());

						switch (keyChar) {
							case '^':
								pointer.setDirection(new Vector(0, -1));
								break;
							case '>':
								pointer.setDirection(new Vector(1, 0));
								break;
							case 'v':
								pointer.setDirection(new Vector(0, 1));
								break;
							case '<':
								pointer.setDirection(new Vector(-1, 0));
								break;
						}
						move = true;
				}
			}

			if (move) {
				pointer.move();
				selectionEnd = pointer.getPosition();
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (this.isEnabled()) {
			this.requestFocus();

			int x = e.getX() / cell.width;
			int y = e.getY() / cell.height;

			// We cant just assign them both to the same vector, as
			// pointer.setPosition() performs some bounding checks
			pointer.setPosition(new Vector(x, y));
			selectionEnd = pointer.getPosition();
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (isEnabled()) {
			int x = e.getX() / cell.width;
			int y = e.getY() / cell.height;

			pointer.setPosition(new Vector(x, y));
		}
	}

	@Override
	public void gridChanged(GridChangeEvent e) {
		if (FungeGui.isUpdating()) {
			Vector pos = e.getVector();
			changes.push(pos);
			repaint();
		}
	}

	@Override
	public void pointerMoved(PointerChangeEvent e) {
		if (FungeGui.isUpdating()) {
			scrollVectorToScreen(e.getVector());
			repaint();
		}
	}

	public void scrollVectorToScreen(Vector pos) {
		// Automatically scroll the pointer into view, if we are in a container that supports this
		if (getParent() instanceof JViewport) {
			JViewport scroll = (JViewport) (getParent());

			Point view = scroll.getViewPosition();

			int x = (pos.getX() - AUTOSCROLL_EXTRA) * cell.width - view.x;
			int y = (pos.getY() - AUTOSCROLL_EXTRA) * cell.height - view.y;
			int width = cell.width * (AUTOSCROLL_EXTRA) * 2;
			int height = cell.height * (AUTOSCROLL_EXTRA) * 2;

			Rectangle rect = new Rectangle(x, y, width, height);
			scroll.scrollRectToVisible(rect);
		}
	}


	/*
	 * Unused implements methods
	 */

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void pointerTurned(PointerChangeEvent e) {}

	@Override
	public void mouseMoved(MouseEvent e) {}


}
