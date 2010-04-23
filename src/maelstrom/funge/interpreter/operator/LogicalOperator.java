package maelstrom.funge.interpreter.operator;

import maelstrom.funge.interpreter.Funge;
import maelstrom.funge.interpreter.stack.Stack;


public abstract class LogicalOperator implements Operator {

	public static class Not implements Operator {

		public void perform(Funge funge) {
			Stack stack = funge.getStackStack().getStack();

			stack.push(stack.pop() == 0 ? 1 : 0);
		}

		public String getDescription() {
			return "Pushes the logical not of the first element on the stack";
		}
	}

	public static class GreaterThan implements Operator {

		/**
		 * Pops one entry from the stack. Pushes a 1 if the entry is greater than 0, pushes a 0 otherwise
		 */
		public void perform(Funge funge) {
			Stack stack = funge.getStackStack().getStack();

			Long b = stack.pop();
			Long a = stack.pop();

			stack.push(a > b ? 1 : 0);
		}

		public String getDescription() {
			return "Pops one entry from the stack. Pushes a 1 if the entry is greater than 0, pushes a 0 otherwise";
		}
	}

}
