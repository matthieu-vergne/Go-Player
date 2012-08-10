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

	public void setCoordContent(int row, int col, Stone stone) {
		field[row][col] = stone;
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
}
