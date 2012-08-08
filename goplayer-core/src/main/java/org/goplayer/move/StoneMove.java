package org.goplayer.move;

public class StoneMove implements IMove {
	private final int row;
	private final int col;

	public StoneMove(int row, int col) {
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
	public String toString() {
		return "(" + row + ", " + col + ")";
	}
}
