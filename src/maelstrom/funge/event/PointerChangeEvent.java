package maelstrom.funge.event;

import java.util.EventObject;
import maelstrom.funge.interpreter.*;

@SuppressWarnings("serial")
public class PointerChangeEvent extends EventObject {

	private Vector vector;

	public PointerChangeEvent(Object pointer, Vector vector) {
		super(pointer);
		this.vector = vector;
	}

	public Vector getVector() {
		return vector.clone();
	}
}
