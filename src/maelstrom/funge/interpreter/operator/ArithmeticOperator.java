package maelstrom.funge.interpreter.operator;

import maelstrom.funge.interpreter.Funge;
import maelstrom.funge.interpreter.stack.Stack;


public abstract class ArithmeticOperator {

	// Adds the two top most numbers on the stack
	public static class Add implements Operator {

		public void perform(Funge funge) {
			Stack stack = funge.getStackStack().getStack();
			stack.push((stack.pop() + stack.pop()));
		}

		public String getDescription() {
			return "Pops b, a off the stack. Pushes a + b";
		}
	}

	// Subtracts the two top most numbers on the stack
	public static class Subtract implements Operator {

		public void perform(Funge funge) {
			Stack stack = funge.getStackStack().getStack();
			stack.push(0 - (stack.pop() - stack.pop()));
		}

		public String getDescription() {
			return "Pops b, a off the stack. Pushes a - b";
		}
	}

	// Adds the two top most numbers on the stack
	public static class Multiply implements Operator {

		public void perform(Funge funge) {
			Stack stack = funge.getStackStack().getStack();
			stack.push((stack.pop() * stack.pop()));
		}

		public String getDescription() {
			return "Pops b, a off the stack. Pushes a * b";
		}
	}

	// Divides the two top most numbers on the stack
	public static class Divide implements Operator {

		public void perform(Funge funge) {
			Stack stack = funge.getStackStack().getStack();

			long b = stack.pop();
			long a = stack.pop();

			if (b == 0) {
				stack.push(0);
			} else {
				stack.push(a / b);
			}
		}

		public String getDescription() {
			return "Pops b, a off the stack. Pushes a / b";
		}
	}

	// Divides the two top most numbers on the stack
	public static class Modulus implements Operator {

		public void perform(Funge funge) {
			Stack stack = funge.getStackStack().getStack();

			long b = stack.pop();
			long a = stack.pop();

			if (b == 0) {
				stack.push(0);
			} else {
				stack.push(a % b);
			}
		}

		public String getDescription() {
			return "Pops b, a off the stack. Pushes a % b";
		}
	}
}
