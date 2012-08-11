package org.goplayer.util;

import java.util.LinkedList;

import org.goplayer.go.Stone;

@SuppressWarnings("serial")
public class MoveHistory extends LinkedList<MoveHistory.Entry> {
	
	public boolean add(Coord coord, Stone stone) {
		return super.add(new Entry(coord, stone));
	}

	public static class Entry {
		private final Coord coord;
		private final Stone stone;
		
		public Entry(Coord coord, Stone stone) {
			this.coord = coord;
			this.stone = stone;
		}

		public Coord getCoord() {
			return coord;
		}

		public Stone getStone() {
			return stone;
		}
	}
	
	@Override
	public MoveHistory clone() {
		MoveHistory clone = new MoveHistory();
		clone.addAll(this);
		return clone;
	}
}
