package org.goplayer.go;

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
						"The number of rows must be strictly positive: " + rowCount);
			}
		} else {
			throw new IllegalArgumentException(
					"The number of columns must be strictly positive: " + colCount);
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

	public void setCoordContent(int row, int col, Stone stone) {
		field[row][col] = stone;
	}

}
