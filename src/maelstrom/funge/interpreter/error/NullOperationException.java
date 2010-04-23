package maelstrom.funge.interpreter.error;


@SuppressWarnings("serial")
public class NullOperationException extends RuntimeException {

	long code;

	public NullOperationException(long code) {
		this.code = code;
	}

	@Override
    public String getMessage() {
		return "Code #" + code + ", char '" + (char) code + "' is not a valid operation";
	}

}
