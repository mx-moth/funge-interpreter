package maelstrom.funge.interpreter.operator;

import maelstrom.funge.interpreter.*;


public class StringToggleOperator implements Operator {

	/**
	 * Toggles string mode on and off
	 */
	public void perform(Funge funge) {
		funge.getPointer().toggleString();
	}

	public String getDescription() {
		return "Toggles string mode";
	}
}
