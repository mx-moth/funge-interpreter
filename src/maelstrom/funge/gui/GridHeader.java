/**
 *
 */
package maelstrom.funge.gui;

import java.awt.Component;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import javax.swing.JViewport;

import maelstrom.funge.interpreter.Grid;


/**
 * @author Tim
 *
 */
@SuppressWarnings("serial")
public class GridHeader extends Component {

	public static final int VERTICAL = 0;
	public static final int HORIZONTAL = 1;

	private GridEditor editor;
	private boolean vertical;

	public GridHeader(GridEditor editor, int direction) {
		this.editor = editor;
		vertical = direction == VERTICAL;
	}

	public JViewport createViewportForHeader() {
		JViewport viewport = new JViewport();
		viewport.add(this);
		return viewport;
	}

	public Dimension getPreferredSize() {
		Grid grid = editor.getGrid();
		Dimension size = grid.getSize();
		Dimension cell = editor.getCell();
		if (vertical) {
			return new Dimension(cell.width, (size.height + 1) * cell.height);
		} else {
			return new Dimension((size.width + 1) * cell.width + 1, cell.height);
		}
	}

	public void paint(Graphics g) {
		Rectangle bounds = g.getClipBounds();
		Grid grid = editor.getGrid();
		Dimension size = grid.getSize();
		Dimension cell = editor.getCell();

		int count = 0;
		g.setColor(Color.GRAY);
		if (vertical) {
			int barEndY = Math.min(bounds.y + bounds.height, (size.height) * cell.height);
			int startY = bounds.y / cell.height;
			int endY = Math.min((bounds.y + bounds.height) / cell.height + 1, size.height);
			g.fillRect(0, bounds.y, cell.width, barEndY - bounds.y);

			g.setColor(Color.white);
			for (int y = startY; y < endY; y++) {
				g.drawString("" + y, 1, (y + 1) * cell.height - 5);
			}
		} else {
			int barEndX = Math.min(bounds.x + bounds.width, (size.width) * cell.width);
			int startX = bounds.x / cell.width;
			int endX = Math.min((bounds.x + bounds.width) / cell.width + 1, size.width);
			g.fillRect(bounds.x, 0, barEndX - bounds.x, cell.height);

			Graphics2D g2 = (Graphics2D) g;
			AffineTransform orig = g2.getTransform();
			g2.setColor(Color.white);
			g2.rotate(-Math.PI / 2);
			for (int x = startX; x < endX; x++) {
				g2.drawString("" + x, 2 - cell.height, (x + 1) * cell.width - 5);
			}
			g2.setTransform(orig);
		}
	}
}
