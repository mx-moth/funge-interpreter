package maelstrom.funge.interpreter.stack;

import java.util.ArrayList;
import java.util.Iterator;

import maelstrom.funge.event.*;


/**
 *
 * @author Maelstrom
 *
 * A stack.
 *
 */
public class Stack {

	public StackNode top;

	private ArrayList<StackChangeListener> listeners       = new ArrayList<StackChangeListener>();

	/**
	 * Pops an element off the stack;
	 *
	 * @return The element on the top of the stack
	 */
	public long pop() {

		StackNode node = top;
		long data;
		if (node != null) {
			top = node.getPreviousNode();
			data = node.getData();
		} else {
			data = 0;
		}

		fireStackChangeEvent(data, StackChangeEvent.POP);
		return data;
	}

	/**
	 * Pushes a new element on to the stack
	 *
	 * @param data
	 *        The element to push
	 */
	public void push(long data) {

		StackNode node = new StackNode(data, top);
		top = node;

		fireStackChangeEvent(data, StackChangeEvent.PUSH);
	}

	/**
	 * Checks to see if the stack is empty, returns true if it is
	 *
	 * @return Returns true if the stack is empty, otherwise returns false
	 */
	public boolean isEmpty() {
		return top == null;
	}

	public void clear() {
		top = null;
		fireStackChangeEvent(0, StackChangeEvent.CLEAR);
	}

	/**
	 * Returns the complete stack as an array, top element last; Only useful for debugging and what
	 * not
	 *
	 * @return An array of the contents of the stack
	 */
	public long[] getCompleteStack() {

		int length;
		StackNode node;

		node = top;
		length = 0;
		while (node != null) {
			length++;
			node = node.getPreviousNode();
		}

		long[] stack = new long[length];

		node = top;
		while (node != null) {
			length--;
			stack[length] = node.getData();
			node = node.getPreviousNode();
		}

		return stack;

	}

	// Returns part of the stack as an array, top element last;
	// Only useful for debugging and what not
	public long[] getStackChunk(int length) {

		StackNode node = top;
		int count = length;
		long[] stack = new long[length];

		while (node != null && count < length) {
			count--;
			stack[count] = node.getData();
			node = node.getPreviousNode();
		}
		for (; count >= 0; count--) {
			stack[count] = 0;
		}

		return stack;

	}


	/**
	 * Adds a listener for this grid.
	 *
	 * @param listener
	 *        The object to notify when this grid is modified
	 */
	public synchronized void addStackChangeListener(StackChangeListener listener) {
		listeners.add(listener);
	}

	/**
	 * Removes a listener for the grid.
	 *
	 * @param listener
	 *        The object to remove from the notify list
	 */
	public synchronized void removeStackChangeListener(StackChangeListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Notifies all listening objects that the pointer has moved
	 */
	private synchronized void fireStackChangeEvent(long data, int changeType) {
		StackChangeEvent event = new StackChangeEvent(this, data, changeType);
		Iterator<StackChangeListener> iter = listeners.iterator();

		while (iter.hasNext()) {
			iter.next().stackChanged(event);
		}
	}

}
