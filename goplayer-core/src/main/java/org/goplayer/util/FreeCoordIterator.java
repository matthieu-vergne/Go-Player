package org.goplayer.util;

import java.util.Iterator;

import org.goplayer.go.Goban;
import org.goplayer.go.Stone;

public class FreeCoordIterator implements Iterator<Coord> {

	private final Goban goban;
	private int row = 0;
	private int col = -1;
	private int nextRow = 0;
	private int nextCol = 0;
	private boolean noNext = false;

	public FreeCoordIterator(Goban goban) {
		this.goban = goban;
		lookForNext();
	}

	private void lookForNext() {
		nextRow = row;
		nextCol = col + 1;
		for (; nextRow < goban.getRowCount(); nextRow++) {
			for (; nextCol < goban.getColCount(); nextCol++) {
				Stone stone = goban.getCoordContent(nextRow, nextCol);
				if (stone == null) {
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
	public Coord next() {
		if (noNext) {
			throw new IllegalStateException("No next element.");
		} else {
			row = nextRow;
			col = nextCol;
			lookForNext();
			return new Coord(row, col);
		}
	}

	@Override
	public void remove() {
		throw new IllegalStateException("Free places cannot be removed.");
	}

	public Goban getGoban() {
		return goban;
	}

}
