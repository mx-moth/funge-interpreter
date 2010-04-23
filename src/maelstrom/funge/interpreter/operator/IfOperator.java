package maelstrom.funge.interpreter.operator;

import maelstrom.funge.interpreter.*;


public abstract class IfOperator implements Operator {

	public static class Horizontal implements Operator {

		/**
		 * Sets the pointer moving West if stack is non 0, East if 0
		 */
		public void perform(Funge funge) {
			Long a = funge.getStack().pop();
			Vector newDir = new Vector(a == 0 ? 1 : -1, 0);

			funge.getPointer().setDirection(newDir);
		}

		public String getDescription() {
			return "Pops a value from the stack. Sets the pointer moving West if the value is non 0, East if 0.";
		}
	}

	public static class Vertical implements Operator {

		/**
		 * Sets the pointer moving North if stack is non 0, South if 0
		 */
		public void perform(Funge funge) {
			Long a = funge.getStack().pop();
			Vector newDir = new Vector(0, a == 0 ? 1 : -1);

			funge.getPointer().setDirection(newDir);
		}

		public String getDescription() {
			return "Pops a value from the stack. Sets the pointer moving North if the value is non 0, South if 0.";
		}
	}

	public static class Branch implements Operator {

		/**
		 * Sets the pointer moving North if stack is non 0, South if 0
		 */
		public void perform(Funge funge) {
			Long b = funge.getStack().pop();
			Long a = funge.getStack().pop();

			if (a < b) {
				new PointerOperator.Anticlockwise().perform(funge);
			} else if (a > b) {
				new PointerOperator.Clockwise().perform(funge);
			}
		}

		public String getDescription() {
			return "Pops two values (b then a) from the stack. Turns clockwise if a > b, anticlockwise if a < b, and continues on if a == b.";
		}
	}
}
