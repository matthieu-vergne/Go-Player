package org.goplayer.util;

public class Coord {
	private final int row;
	private final int col;

	public Coord(int row, int col) {
		this.row = row;
		this.col = col;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (obj instanceof Coord) {
			Coord c = (Coord) obj;
			return row == c.row && col == c.col;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return (int) (row * 3881 + col * 3967);
	}
}
