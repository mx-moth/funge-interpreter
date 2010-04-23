package maelstrom.funge.interpreter.operator;

import maelstrom.funge.interpreter.*;

public abstract class StopOperator implements Operator {
	public static class Stop implements Operator {

		/**
		 * Stops the program normally
		 */
		public void perform(Funge funge) {
			funge.getPointer().setDirection(new Vector(0, 0));
			funge.stop();
		}

		public String getDescription() {
			return "Stops the program normally";
		}
	}

	public static class Quit implements Operator {

		/**
		 * Stops the program normally
		 */
		public void perform(Funge funge) {
			funge.getPointer().setDirection(new Vector(0, 0));
			funge.quit(funge.getStack().pop());
		}

		public String getDescription() {
			return "Quits the program abnormally, returning the first value on the stack as the quit as the return code";
		}
	}
}
