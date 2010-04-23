package maelstrom.funge.interpreter;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Iterator;

import maelstrom.funge.event.*;


public class Pointer {

	private Vector                         position;
	private Vector                         direction;
	private boolean                        string;
	private Dimension                      grid;

	// A list of objects to notify when this grid changes.
	private ArrayList<PointerChangeListener> listeners = new ArrayList<PointerChangeListener>();


	public Pointer(Grid grid) {
		initialise(grid.getSize());
	}

	public Pointer(Dimension size) {
		initialise(size);
	}

	private void initialise(Dimension size) {
		grid = size;
		position = new Vector(0, 0);
		direction = new Vector(1, 0);
		string = false;
	}

	public void move() {
		position = position.add(direction);

		// TODO implement correct wrapping
		if (position.getX() < 0) {
			position.setX(grid.width - 1);
		}
		if (position.getY() < 0) {
			position.setY(grid.height - 1);
		}
		if (position.getX() >= grid.width) {
			position.setX(0);
		}
		if (position.getY() >= grid.height) {
			position.setY(0);
		}

		firePointerMoveEvent();
	}

	public void moveBack() {
		position = position.subtract(direction);

		// TODO implement correct wrapping
		if (position.getX() < 0) {
			position.setX(grid.width - 1);
		}
		if (position.getY() < 0) {
			position.setY(grid.height - 1);
		}
		if (position.getX() >= grid.width) {
			position.setX(0);
		}
		if (position.getY() >= grid.height) {
			position.setY(0);
		}

		firePointerMoveEvent();
	}


	public Vector getPosition() {
		return position;
	}

	public void setPosition(Vector position) {

		int x = position.getX();
		int y = position.getY();

		if (x < 0) {
			x = 0;
		} else if (x >= grid.width) {
			x = grid.width - 1;
		}
		if (y < 0) {
			y = 0;
		} else if (y >= grid.height) {
			y = grid.height - 1;
		}

		this.position = new Vector(x, y);

		firePointerMoveEvent();
	}

	public Vector getDirection() {
		return direction;
	}

	public void setDirection(Vector direction) {
		this.direction = direction;
		firePointerTurnEvent();
	}

	public boolean isString() {
		return string;
	}

	public void setString(boolean string) {
		this.string = string;
	}

	public boolean toggleString() {
		string = !string;
		return string;
	}


	/**
	 * Adds a listener for this grid.
	 *
	 * @param listener
	 *        The object to notify when this grid is modified
	 */
	public synchronized void addPointerChangeListener(PointerChangeListener listener) {
		listeners.add(listener);
	}

	/**
	 * Removes a listener for the grid.
	 *
	 * @param listener
	 *        The object to remove from the notify list
	 */
	public synchronized void removePointerChangeListener(PointerChangeListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Notifies all listening objects that the pointer has moved
	 */
	private synchronized void firePointerMoveEvent() {
		PointerChangeEvent event = new PointerChangeEvent(this, position);
		Iterator<PointerChangeListener> iter = listeners.iterator();

		while (iter.hasNext()) {
			iter.next().pointerMoved(event);
		}
	}

	/**
	 * Notifies all listening objects that the pointer has changed direction
	 */
	private synchronized void firePointerTurnEvent() {
		PointerChangeEvent event = new PointerChangeEvent(this, direction);
		Iterator<PointerChangeListener> iter = listeners.iterator();

		while (iter.hasNext()) {
			iter.next().pointerTurned(event);
		}
	}

}