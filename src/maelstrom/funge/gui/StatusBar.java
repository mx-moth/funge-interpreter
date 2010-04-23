/**
 *
 */
package maelstrom.funge.gui;

import java.awt.*;
import javax.swing.*;

import maelstrom.funge.event.*;
import maelstrom.funge.interpreter.*;

/**
 * @author Tim
 *
 */
@SuppressWarnings("serial")
public class StatusBar extends Container implements PointerChangeListener {

	JLabel position;
	JLabel direction;

	Pointer pointer;

	public StatusBar() {
		this.setLayout(new FlowLayout(FlowLayout.LEFT));

		position = new JLabel("(0, 0)");
		position.setPreferredSize(new Dimension(50, 16));
		position.setHorizontalAlignment(JLabel.CENTER);

		direction = new JLabel(">");
		direction.setPreferredSize(new Dimension(80, 16));
		direction.setHorizontalAlignment(JLabel.CENTER);

		JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
		separator.setPreferredSize(new Dimension(5, 14));

		this.add(position);
		this.add(separator);
		this.add(direction);

		this.validate();
	}

	public void updatePosition(Vector pos) {
		this.position.setText("(" + pos.getX() + ", " + pos.getY() + ")");
	}

	public void updateDirection(Vector dir) {
		if (dir.equals(new Vector(0, -1))) {
			this.direction.setText("^");
		} else if (dir.equals(new Vector(1, 0))) {
			this.direction.setText(">");
		} else if (dir.equals(new Vector(0, 1))) {
			this.direction.setText("v");
		} else if (dir.equals(new Vector(-1, 0))) {
			this.direction.setText("<");
		} else if (dir.equals(new Vector(0, 0))) {
			this.direction.setText("Stopped");
		} else {
			this.direction.setText("Flying: " + dir.getX() + ", " + dir.getY());
		}
	}

	@Override
	public void pointerMoved(PointerChangeEvent e) {
		if (FungeGui.isUpdating()) {
			updatePosition(e.getVector());
		}
	}

	@Override
	public void pointerTurned(PointerChangeEvent e) {
		if (FungeGui.isUpdating()) {
			updateDirection(e.getVector());
		}
	}

	public void setPointer(Pointer pointer) {
		this.pointer = pointer;
		pointer.addPointerChangeListener(this);
	}

}
