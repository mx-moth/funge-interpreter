/**
 *
 */
package maelstrom.funge.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import maelstrom.funge.interpreter.Grid;


/**
 * @author Tim
 *
 */
public class GridHeader extends Component {

	public static final int VERTICAL = 0;
	public static final int HORIZONTAL = 1;

	private GridEditor editor;
	private boolean vertical;

	public GridHeader(GridEditor editor, int direction) {
		this.editor = editor;
		vertical = direction == VERTICAL;
	}

	public Dimension getPreferredSize() {
		Grid grid = editor.getGrid();
		Dimension size = grid.getSize();
		Dimension cell = editor.getCell();
		return new Dimension(size.width * cell.width, size.height * cell.height);
	}

	public void paint(Graphics g) {
		Grid grid = editor.getGrid();
		Dimension size = grid.getSize();
		Dimension cell = editor.getCell();

		int count = 0;
		if (vertical) {
			count = size.height;
		} else {
			count = size.width;
		}
		for (int i = 0; i < count; i++) {

		}
	}
}
