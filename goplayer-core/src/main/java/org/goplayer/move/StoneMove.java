package org.goplayer.move;

import org.goplayer.util.Coord;

public class StoneMove implements IMove {
	private final Coord coord;

	public StoneMove(int row, int col) {
		this(new Coord(row, col));
	}

	public StoneMove(Coord coord) {
		this.coord = coord;
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
