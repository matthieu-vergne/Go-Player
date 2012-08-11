package org.goplayer.util;

import java.util.Iterator;

import org.goplayer.go.Goban;
import org.goplayer.go.Stone;
import org.goplayer.go.StoneColor;

public class StoneIterator implements Iterator<Stone> {

	private final Goban goban;
	private final StoneColor color;
	private int row = 0;
	private int col = -1;
	private int nextRow = 0;
	private int nextCol = 0;
	private boolean noNext = false;

	public StoneIterator(Goban goban) {
		this(goban, null);
	}

	public StoneIterator(Goban goban, StoneColor color) {
		this.goban = goban;
		this.color = color;
		lookForNext();
	}

	private void lookForNext() {
		nextRow = row;
		nextCol = col + 1;
		for (; nextRow < goban.getRowCount(); nextRow++) {
			for (; nextCol < goban.getColCount(); nextCol++) {
				Stone stone = goban.getCoordContent(nextRow, nextCol);
				if (stone != null
						&& (color == null || stone.getColor() == color)) {
					return;
				} else {
					continue;
				}
			}
			nextCol = 0;
		}
		noNext = true;
	}

	@Override
	public boolean hasNext() {
		return !noNext;
	}

	@Override
	public Stone next() {
		if (noNext) {
			throw new IllegalStateException("No next element.");
		} else {
			row = nextRow;
			col = nextCol;
			lookForNext();
			return goban.getCoordContent(row, col);
		}
	}

	@Override
	public void remove() {
		goban.setCoordContent(row, col, null);
	}

	public Goban getGoban() {
		return goban;
	}

	public StoneColor getColor() {
		return color;
	}

}
