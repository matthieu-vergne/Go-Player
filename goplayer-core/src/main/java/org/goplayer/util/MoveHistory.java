package org.goplayer.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.goplayer.go.Goban;
import org.goplayer.go.Stone;

@SuppressWarnings("serial")
public class MoveHistory extends LinkedList<MoveHistory.Entry> {

	public boolean add(Coord coord, Stone stone, Goban goban) {
		return super.add(new Entry(coord, stone, goban));
	}

	public static class Entry {
		private final Coord coord;
		private final Stone stone;
		private final Goban goban;

		/**
		 * 
		 * @param coord
		 *            the coordinate of the move
		 * @param stone
		 *            the stone placed at the given coordinate
		 * @param goban
		 *            the state of the goban <b>before</b> the move
		 */
		public Entry(Coord coord, Stone stone, Goban goban) {
			this.coord = coord;
			this.stone = stone;
			this.goban = goban;
		}

		/**
		 * 
		 * @return the coordinate of the move
		 */
		public Coord getCoord() {
			return coord;
		}

		/**
		 * 
		 * @return the stone placed at the given coordinate
		 */
		public Stone getStone() {
			return stone;
		}

		/**
		 * 
		 * @return the state of the goban <b>before</b> the move
		 */
		public Goban getGoban() {
			return goban;
		}

		@Override
		public String toString() {
			return "{" + stone.getColor() + "=" + coord + "}";
		}
	}

	@Override
	public MoveHistory clone() {
		MoveHistory clone = new MoveHistory();
		clone.addAll(this);
		return clone;
	}

	public List<Integer> getEquivalentGobansIndexes(Goban goban) {
		return getEquivalentGobansIndexes(goban, Integer.MAX_VALUE);
	}

	public List<Integer> getEquivalentGobansIndexes(Goban goban, int limit) {
		List<Integer> indexes = new ArrayList<Integer>();
		Iterator<Entry> iterator = iterator();
		while (iterator.hasNext() && indexes.size() < limit) {
			Entry entry = iterator.next();
			if (entry.getGoban().isEquivalentTo(goban)) {
				indexes.add(indexOf(entry));
			} else {
				continue;
			}
		}
		return indexes;
	}
}
