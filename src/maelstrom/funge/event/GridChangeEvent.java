package maelstrom.funge.event;

import java.util.EventObject;
import maelstrom.funge.interpreter.*;


@SuppressWarnings("serial")
public class GridChangeEvent extends EventObject {

	private Vector location;

	public GridChangeEvent(Object grid, Vector location) {
		super(grid);
		this.location = location.clone();
	}

	public GridChangeEvent(Object arg0, int x, int y) {
		super(arg0);
		location = new Vector(x, y);
	}

	public Vector getVector() {
		return location.clone();
	}

	public int getX() {
		return location.getX();
	}

	public int getY() {
		return location.getY();
	}
}
