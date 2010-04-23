package maelstrom.funge.interpreter.operator;

import maelstrom.funge.interpreter.*;
import maelstrom.funge.interpreter.stack.*;


public abstract class PointerOperator implements Operator {

	public static class North implements Operator {

		public void perform(Funge funge) {
			funge.getPointer().setDirection(new Vector(0, -1));
		}

		public String getDescription() {
			return "Starts the IP moving North";
		}
	}

	public static class East implements Operator {

		public void perform(Funge funge) {
			funge.getPointer().setDirection(new Vector(1, 0));
		}

		public String getDescription() {
			return "Starts the IP moving East";
		}
	}

	public static class South implements Operator {

		public void perform(Funge funge) {
			funge.getPointer().setDirection(new Vector(0, 1));
		}

		public String getDescription() {
			return "Starts the IP moving South";
		}
	}

	public static class West implements Operator {

		public void perform(Funge funge) {
			funge.getPointer().setDirection(new Vector(-1, 0));
		}

		public String getDescription() {
			return "Starts the IP moving West";
		}
	}


	public static class Random implements Operator {

		public void perform(Funge funge) {

			Vector dir;

			switch ((int)(Math.random() * 4)) {
				case 0:
					dir = new Vector(0, -1);
					break;
				case 1:
					dir = new Vector(1, 0);
					break;
				case 2:
					dir = new Vector(0, 1);
					break;
				case 3:
					dir = new Vector(-1, 0);
					break;
				default:
					throw new AssertionError("Random direction was bad");
			}

			funge.getPointer().setDirection(dir);
		}

		public String getDescription() {
			return "Starts the IP moving in a random cardinal direction";
		}
	}


	public static class Clockwise implements Operator {

		public void perform(Funge funge) {

			Vector old = funge.getPointer().getDirection();

			Vector dir = new Vector(-old.getY(), old.getX());

			funge.getPointer().setDirection(dir);
		}

		public String getDescription() {
			return "Turns the movement vector 90 degrees to the left.";
		}
	}

	public static class Anticlockwise implements Operator {

		public void perform(Funge funge) {

			Vector old = funge.getPointer().getDirection();

			Vector dir = new Vector(old.getY(), -old.getX());

			funge.getPointer().setDirection(dir);
		}

		public String getDescription() {
			return "Turns the movement vector 90 degrees to the right.";
		}
	}


	public static class Absolute implements Operator {

		public void perform(Funge funge) {

			Stack stack = funge.getStack();

			int y = (int)stack.pop();
			int x = (int)stack.pop();

			Vector dir = new Vector(x, y);

			funge.getPointer().setDirection(dir);
		}

		public String getDescription() {
			return "Sets the movement vector.";
		}
	}

	public static class Reflect implements Operator {

		public void perform(Funge funge) {

			Vector old = funge.getPointer().getDirection();
			Vector dir = new Vector(-old.getX(), -old.getY());

			funge.getPointer().setDirection(dir);
		}

		public String getDescription() {
			return "Reverses the movement vector.";
		}
	}


	public static class Trampoline implements Operator {

		public void perform(Funge funge) {
			funge.getPointer().move();
		}

		public String getDescription() {
			return "Jumps over one square.";
		}
	}

	public static class Jump implements Operator {

		public void perform(Funge funge) {

			long scalar = funge.getStack().pop();
			Pointer pointer = funge.getPointer();
			Vector vector = pointer.getDirection();

			pointer.setDirection(vector.scale((int)scalar));
			pointer.move();
			pointer.setDirection(vector);
		}

		public String getDescription() {
			return "Pops a value off the stack, and moves the pointer forwards (or backwards, for negatives) that many times.";
		}
	}
}
