package maelstrom.funge.interpreter.operator;

import java.io.IOException;
import maelstrom.funge.interpreter.Funge;
import maelstrom.funge.interpreter.stack.Stack;


public abstract class IOOperator {

	// Clones the first element on the stack
	public static class OutputChar implements Operator {

		public void perform(Funge funge) {
			Stack stack = funge.getStackStack().getStack();
			System.out.print((char)stack.pop());
		}

		public String getDescription() {
			return "Prints the ASCII value of the first element on the stack";
		}
	}

	// Swaps the first two values on the stack
	public static class OutputNum implements Operator {

		public void perform(Funge funge) {
			Stack stack = funge.getStackStack().getStack();
			System.out.print(stack.pop());
		}

		public String getDescription() {
			return "Prints the numeric value of the first element on the stack";
		}
	}

	// Pops and discards a value off the stack
	public static class InputChar implements Operator {

		public void perform(Funge funge) {


			try {
				Stack stack = funge.getStackStack().getStack();
	            stack.push(System.in.read());
            } catch (IOException err) {
            	//TODO: Handle this exception as per spec
	            err.printStackTrace();
            }

		}

		public String getDescription() {
			return "Gets a character from the user";
		}
	}

	// Empties the stack
	public static class InputNum implements Operator {

		public void perform(Funge funge) {
			String text = "";

			try {
				int in = System.in.read();

				while (!Character.isDigit((char)in)) {
					in = System.in.read();
				}

				while (Character.isDigit((char)in)) {
					text = text + (char)in;
					in = System.in.read();
				}

				Stack stack = funge.getStackStack().getStack();
				stack.push(Long.parseLong(text));
			} catch (IOException err) {
				//TODO: Handle this exception as per spec
				err.printStackTrace();
			}
		}

		public String getDescription() {
			return "Gets a number from the user";
		}
	}
}
