package maelstrom.funge.event;

import java.util.EventObject;


@SuppressWarnings("serial")
public class StackChangeEvent extends EventObject {

	public static final int PUSH  = 1;
	public static final int POP  = 2;
	public static final int CLEAR = 3;

	private int             state;
	private long            data;

	public StackChangeEvent(Object stack, int state) {
		super(stack);
		this.state = state;
		data = 0;
	}

	public StackChangeEvent(Object stack, long data, int state) {
		super(stack);
		this.state = state;
		this.data = data;
	}

	public int getState() {
		return state;
	}
	public long getData() {
		return data;
	}
}
