package maelstrom.funge.interpreter;

import java.awt.*;
import java.util.*;

import maelstrom.funge.interpreter.error.*;
import maelstrom.funge.event.*;


public class Grid {

	private Dimension                     size;
	private long[][]                      grid;

	// A list of objects to notify when this grid changes.
	private ArrayList<GridChangeListener> listeners = new ArrayList<GridChangeListener>();


	/**
	 * Initialise the grid using Befunge-93 defaults
	 */
	public Grid() {
		grid = new long[25][80];
		resetGrid();
	}

	/**
	 * Initialise the grid using the supplied size
	 *
	 * @param size
	 *        The dimensions to initialise the grid to
	 */
	public Grid(Dimension size) {
		grid = new long[size.width][size.height];
		resetGrid();
	}

	public Grid(long[][] grid) {
		this.grid = grid;

		// Reset size
		size = new Dimension(grid[0].length, grid.length);
	}

	public static Grid fromString(String string) {
		String[] lines = string.split("\n");

		int height = lines.length;
		int width = 0;
		for (String s : lines) {
			if (width < s.length()) width = s.length();
		}

		return Grid.fromString(string, new Dimension(width, height));
	}

	public static Grid fromString(String string, Dimension size) {
		String[] lines = string.split("\n");

		Grid grid = new Grid(size);
		for (int y = 0; y < lines.length; y++) {
			String line = lines[y];
			for (int x = 0; x < line.length(); x++) {
				char character = line.charAt(x);
				grid.set(x, y, character);
			}
		}

		return grid;
	}

	/**
	 * Initialise the grid using the supplied sizes
	 *
	 * @param width
	 *        The width of the new grid
	 * @param height
	 *        The height of the new grid
	 */
	public Grid(int width, int height) {
		grid = new long[height][width];
		resetGrid();
	}


	/**
	 * Clear the grid, setting everything to the default character
	 */
	public void resetGrid() {

		// Clear out the grid to be blank spaces
		for (int y = 0; y < grid.length; y++) {
			for (int x = 0; x < grid[y].length; x++) {
				grid[y][x] = Funge.SPACE_CHAR;
			}
		}

		// Reset size, just to be on the safe side
		size = new Dimension(grid.length, grid[0].length);

	}

	/**
	 * Gets the operator at the supplied grid location
	 *
	 * @param x
	 *        x-coordinate of the grid to fetch
	 * @param y
	 *        y-coordinate of the grid to fetch
	 * @return The operator at (x, y)
	 */
	public long get(int x, int y) {

		// Check for valid grid locations first
		if (this.checkValidLocation(x, y)) {
	        return grid[x][y];
		} else {
	        throw new GridIndexOutOfBoundsException();
		}
	}

	/**
	 * Gets the operator at the supplied grid location
	 *
	 * @param pos
	 *        The grid location to fetch
	 * @return The operator at the supplied location
	 */
	public long get(Vector pos) {
		return get(pos.getX(), pos.getY());
	}

	/**
	 * Sets the operator in the grid at the specified coordinates
	 * @param x X coordinate of the cell to set
	 * @param y Y coordinate of the cell to set
	 * @param operator The operator to set
	 */
	public void set(int x, int y, long operator) {


		// Check for valid grid locations first
		if (this.checkValidLocation(x, y)) {

			grid[x][y] = operator;
			fireGridChangeEvent(new Vector(x, y));

		} else
	        throw new GridIndexOutOfBoundsException();
	}

	/**
	 * Sets the operator in the grid at the specified position
	 * @param pos The position of the cell to set
	 * @param operator The operator to set
	 */
	public void set(Vector pos, long operator) {
		this.set(pos.getX(), pos.getY(), operator);
	}

	public boolean checkValidLocation(int x, int y) {

		if (x >= 0 && y >= 0) {
			if (x < size.width && y < size.height)
	            return true;
		}

		return false;
	}

	public boolean checkValidLocation(Vector v) {
		return this.checkValidLocation(v.getX(), v.getY());
	}

	/**
	 * Returns the size of the grid in a Dimension
	 *
	 * @return A Dimension containing the size of the grid
	 */
	public Dimension getSize() {
		return new Dimension(size);
	}

	/**
	 * Sets the size of the grid
	 *
	 * @param size A Dimension containing the size of the grid
	 */
	public void setSize(Dimension size) {
		this.size = new Dimension(size);
	}

	/**
	 * Adds a listener for this grid.
	 *
	 * @param listener
	 *        The object to notify when this grid is modified
	 */
	public synchronized void addGridChangeListener(GridChangeListener listener) {
		listeners.add(listener);
	}

	/**
	 * Removes a listener for the grid.
	 *
	 * @param listener
	 *        The object to remove from the notify list
	 */
	public synchronized void removeGridChangeListener(GridChangeListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Notifies all listening objects that the grid has changed
	 *
	 * @param location
	 *        The location that the grid changed
	 */
	private synchronized void fireGridChangeEvent(Vector location) {
		GridChangeEvent event = new GridChangeEvent(this, location);
		Iterator<GridChangeListener> iter = listeners.iterator();

		while (iter.hasNext()) {
			iter.next().gridChanged(event);
		}
	}

	public Grid clone() {
		Grid newGrid = new Grid(grid.clone());
		newGrid.setSize(size);
		return newGrid;
	}
}
