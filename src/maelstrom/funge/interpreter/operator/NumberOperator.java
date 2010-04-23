package maelstrom.funge.interpreter.operator;

import maelstrom.funge.interpreter.Funge;


public class NumberOperator implements Operator {

	private int num;

	public NumberOperator(int num) {
		this.num = num;
	}

	public void perform(Funge funge) {
		funge.getStackStack().getStack().push(num);
	}

	public String getDescription() {
		return "Pushes a " + num + " on to the stack";
	}
}
