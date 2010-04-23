package maelstrom.funge.interpreter.stack;


class StackNode {

	private long      data;
	private StackNode previousNode;

	public StackNode(long data, StackNode previousNode) {
		this.previousNode = previousNode;
		this.data = data;
	}

	public long getData() {
		return data;
	}

	public void setData(long data) {
		this.data = data;
	}

	public StackNode getPreviousNode() {
		return previousNode;
	}

	public void setPreviousNode(StackNode previousNode) {
		this.previousNode = previousNode;
	}

}
