package maelstrom.funge.interpreter.operator;

import maelstrom.funge.interpreter.*;


public class NoopOperator implements Operator {

	public static final String SPACE_DESCRIPTION = "";
	public static final String NOOP_DESCRIPTION = "Does nothing for one tick";
	public static final String JUMP_DESCRIPTION = "Skips all code untill the next jump character is found";

	private String description;

	public NoopOperator(String decription) {
		this.description = decription;
	}
	/**
	 * The noop operation. This does nothing. Nothing at all. Seriously. It is an empty method
	 */
	public void perform(Funge funge) {}

	public String getDescription() {
		return description;
	}
}
