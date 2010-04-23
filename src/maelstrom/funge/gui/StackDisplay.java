/**
 *
 */
package maelstrom.funge.gui;

import java.awt.*;
import java.util.Vector;

import javax.swing.*;

import maelstrom.funge.event.*;


/**
 * @author Tim
 *
 */
@SuppressWarnings("serial")
public class StackDisplay extends Container implements StackChangeListener {

	private Vector<JLabel> stack;

	public StackDisplay() {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		stack = new Vector<JLabel>();
	}

	@Override
	public void stackChanged(StackChangeEvent e) {

		JLabel label;

		switch (e.getState()) {
			case StackChangeEvent.PUSH:
				long data = e.getData();
				String text = (stack.size() + 1) + ": " + data;

				if (data > 32 && data < 128) {
					text = text + " - '" + (char) data + "'";
				}

				label = new JLabel(text);
				stack.add(0, label);
				this.add(label, 0);
				break;

			case StackChangeEvent.POP:
				if (stack.size() > 0) {
					this.remove(stack.remove(0));
				}
				break;

			case StackChangeEvent.CLEAR:
				clear();
				break;
		}
	}

	public void clear() {
		stack = new Vector<JLabel>();
		removeAll();
	}

	public Dimension getPreferredSize() {
		return new Dimension(50, 1);
	}
}
