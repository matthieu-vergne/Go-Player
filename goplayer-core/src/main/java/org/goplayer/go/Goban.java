package org.goplayer.go;

public class Goban {

	private final Stone[][] field;

	public Goban(int size) {
		this(size, size);
	}

	public Goban(int width, int height) {
		if (width > 0) {
			if (height > 0) {
				field = new Stone[height][width];
			} else {
				throw new IllegalArgumentException(
						"The height must be strictly positive: " + height);
			}
		} else {
			throw new IllegalArgumentException(
					"The width must be strictly positive: " + width);
		}
	}

	public int getWidth() {
		return field[0].length;
	}

	public int getHeight() {
		return field.length;
	}

	public Stone getCoordContent(int row, int col) {
		return field[row][col];
	}

	public void setCoordContent(int row, int col, Stone stone) {
		field[row][col] = stone;
	}

}
