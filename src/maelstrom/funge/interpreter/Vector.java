package maelstrom.funge.interpreter;


public class Vector {

	private int x;
	private int y;

	public Vector() {
		x = 0;
		y = 0;
	}

	public Vector(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Vector add(Vector that) {
		return new Vector(x + that.x, y + that.y);
	}

	public Vector subtract(Vector that) {
		return new Vector(x - that.x, y - that.y);
	}

	public Vector scale(int scale) {
		return new Vector(x * scale, y * scale);
	}

	@Override
	public String toString() {
		return this.getClass().getCanonicalName() + "[x=" + x + ",y=" + y + "]";
	}

	@Override
	public Vector clone() {
		return new Vector(x, y);
	}


	public boolean equals(Object obj) {
		if (obj instanceof Vector) {
			Vector vector = (Vector)obj;
			if (vector.x == this.x && vector.y == this.y) {
				return true;
			}
		}
		return false;
	}
}
