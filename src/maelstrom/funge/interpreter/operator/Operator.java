package maelstrom.funge.interpreter.operator;

import maelstrom.funge.interpreter.Funge;


public interface Operator {

	// public void Operator();
	public void perform(Funge funge);

	public String getDescription();
}
