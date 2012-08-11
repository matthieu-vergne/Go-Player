package org.goplayer.move;

import org.goplayer.util.Coord;

public class StoneMove implements IMove {
	private final Coord coord;

	public StoneMove(int row, int col) {
		this.coord = new Coord(row, col);
	}

	public int getRow() {
		return coord.getRow();
	}

	public int getCol() {
		return coord.getCol();
	}
	
	public Coord getCoord() {
		return coord;
	}

	@Override
	public String toString() {
		return coord.toString();
	}
}
