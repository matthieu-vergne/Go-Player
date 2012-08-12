package org.goplayer.go;

import org.goplayer.util.Coord;

public class Goban {

	private final Stone[][] field;

	public Goban(int size) {
		this(size, size);
	}

	public Goban(int rowCount, int colCount) {
		if (colCount > 0) {
			if (rowCount > 0) {
				field = new Stone[rowCount][colCount];
			} else {
				throw new IllegalArgumentException(
						"The number of rows must be strictly positive: "
								+ rowCount);
			}
		} else {
			throw new IllegalArgumentException(
					"The number of columns must be strictly positive: "
							+ colCount);
		}
	}

	public int getColCount() {
		return field[0].length;
	}

	public int getRowCount() {
		return field.length;
	}

	public Stone getCoordContent(int row, int col) {
		return field[row][col];
	}

	public Stone getCoordContent(Coord coord) {
		return getCoordContent(coord.getRow(), coord.getCol());
	}

	public void setCoordContent(int row, int col, Stone stone) {
		field[row][col] = stone;
	}

	public void setCoordContent(Coord coord, Stone stone) {
		setCoordContent(coord.getRow(), coord.getCol(), stone);
	}

	public Coord getStoneCoord(Stone stone) {
		for (int row = 0; row < field.length; row++) {
			for (int col = 0; col < field[0].length; col++) {
				if (stone == field[row][col]) {
					return new Coord(row, col);
				} else {
					continue;
				}
			}
		}
		throw new IllegalArgumentException(
				"The given stone is not on this goban.");
	}

	public static Goban createFullGoban(int rowCount, int colCount,
			StoneColor color) {
		Goban goban = new Goban(rowCount, colCount);
		for (int row = 0; row < rowCount; row++) {
			for (int col = 0; col < colCount; col++) {
				goban.setCoordContent(row, col, new Stone(color));
			}
		}
		return goban;
	}

	@Override
	public String toString() {
		String string = "";
		for (int row = 0; row < field.length; row++) {
			for (int col = 0; col < field[0].length; col++) {
				Stone stone = field[row][col];
				if (stone == null) {
					string += "-";
				} else if (stone.getColor() == StoneColor.BLACK) {
					string += "X";
				} else if (stone.getColor() == StoneColor.WHITE) {
					string += "O";
				} else {
					throw new RuntimeException("This case should never happen.");
				}
			}
			string += "\n";
		}
		return string.trim();
	}

	@Override
	public Goban clone() {
		Goban clone = new Goban(getRowCount(), getColCount());
		for (int row = 0; row < getRowCount(); row++) {
			for (int col = 0; col < getColCount(); col++) {
				Stone stone = getCoordContent(row, col);
				if (stone != null) {
					clone.setCoordContent(row, col, new Stone(stone.getColor()));
				} else {
					continue;
				}
			}
		}
		return clone;
	}

	public boolean isOut(Coord coord) {
		return coord.getRow() >= getRowCount() || coord.getRow() < 0
				|| coord.getCol() >= getColCount() || coord.getCol() < 0;
	}

	public boolean isEquivalentTo(Goban goban) {
		if (getRowCount() != goban.getRowCount()) {
			return false;
		} else if (getColCount() != goban.getColCount()) {
			return false;
		} else {
			for (int row = 0; row < getRowCount(); row++) {
				for (int col = 0; col < getColCount(); col++) {
					Stone s1 = getCoordContent(row, col);
					Stone s2 = goban.getCoordContent(row, col);
					if (s1 == null) {
						if (s2 == null) {
							continue;
						} else {
							// empty but filled
							return false;
						}
					} else {
						if (s2 == null) {
							// filled but empty
							return false;
						} else {
							if (s1.getColor() == s2.getColor()) {
								continue;
							} else {
								// different stone colors
								return false;
							}
						}
					}
				}
			}
		}
		return true;
	}
}
