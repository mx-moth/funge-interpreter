package maelstrom.funge.interpreter.operator;

import maelstrom.funge.interpreter.*;
import maelstrom.funge.interpreter.stack.Stack;


public abstract class GridOperator implements Operator {

	public static class Get implements Operator {

		/**
		 * Gets a value from the grid, pushes it to the stack
		 */
		public void perform(Funge funge) {

			Stack stack = funge.getStack();
			long y = stack.pop();
			long x = stack.pop();

			long gridVal = funge.getGrid().get((int) x, (int) y);

			stack.push(gridVal);
		}

		public String getDescription() {
			return "Pops b, then a from the stack. Pushes the value of grid square (a, b) to the stack";
		}
	}

	public static class Put implements Operator {

		/**
		 * Sets a value in the grid.
		 */
		public void perform(Funge funge) {

			Stack stack = funge.getStack();
			long y = stack.pop();
			long x = stack.pop();
			long c = stack.pop();

			funge.getGrid().set((int) x, (int) y, c);
		}

		public String getDescription() {
			return "Pops c, b, then a from the stack. Sets grid square (a, b) to c";
		}
	}


	public static class GetNext implements Operator {

		/**
		 * Gets the value of the next grid square, pushes it to the stack
		 */
		public void perform(Funge funge) {

			Pointer pointer = funge.getPointer();
			pointer.move();

			long a = funge.getGrid().get(pointer.getPosition());

			funge.getStack().push(a);
		}

		public String getDescription() {
			return "Gets the value of the next grid square, pushes it to the stack";
		}
	}

	public static class PutNext implements Operator {

		/**
		 * Sets the value of the next grid square
		 */
		public void perform(Funge funge) {

			Pointer pointer = funge.getPointer();
			pointer.move();

			long a = funge.getStack().pop();

			funge.getGrid().set(pointer.getPosition(), a);
		}

		public String getDescription() {
			return "Pops a from the stack, then sets the value of the next grid square to a";
		}
	}
}
