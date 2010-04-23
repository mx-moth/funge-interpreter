package maelstrom.funge.event;

import java.util.EventObject;


@SuppressWarnings("serial")
public class RunStateChangeEvent extends EventObject {

	public static final int START  = 1;
	public static final int PAUSE  = 2;
	public static final int RESUME = 3;
	public static final int STEP = 4;
	public static final int STOP = 5;
	public static final int QUIT = 6;

	private int             runState;
	private long             returnVal;

	public RunStateChangeEvent(Object funge, int runState, long returnVal) {
		super(funge);
		this.runState = runState;
	}

	public int getRunState() {
		return runState;
	}

	/**
	 * Returns the return value when the program exits. Only applicable for runState = QUIT
	 * @return
	 *         The return value
	 */
	public long getReturnValue() {
		return returnVal;
	}
}
