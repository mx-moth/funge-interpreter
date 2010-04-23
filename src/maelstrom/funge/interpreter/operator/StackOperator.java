package maelstrom.funge.interpreter.operator;

import maelstrom.funge.interpreter.Funge;
import maelstrom.funge.interpreter.stack.Stack;


public abstract class StackOperator {

	// Clones the first element on the stack
	public static class Clone implements Operator {

		public void perform(Funge funge) {
			Stack stack = funge.getStackStack().getStack();
			long a = stack.pop();
			stack.push(a);
			stack.push(a);
		}

		public String getDescription() {
			return "Clones the first element on the stack";
		}
	}

	// Swaps the first two values on the stack
	public static class Swap implements Operator {

		public void perform(Funge funge) {
			Stack stack = funge.getStackStack().getStack();
			long b = stack.pop();
			long a = stack.pop();

			stack.push(b);
			stack.push(a);
		}

		public String getDescription() {
			return "Swaps the first two values on the stack";
		}
	}

	// Pops and discards a value off the stack
	public static class Pop implements Operator {

		public void perform(Funge funge) {
			Stack stack = funge.getStackStack().getStack();
			stack.pop();
		}

		public String getDescription() {
			return "Pops and discards a value off the stack";
		}
	}

	// Empties the stack
	public static class Empty implements Operator {

		public void perform(Funge funge) {
			Stack stack = funge.getStackStack().getStack();
			stack.clear();
		}

		public String getDescription() {
			return "Empties the stack";
		}
	}
}
