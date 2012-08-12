package org.goplayer.game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.goplayer.go.Goban;
import org.goplayer.go.Stone;
import org.goplayer.util.Coord;

@SuppressWarnings("serial")
public class GameHistory extends LinkedList<GameHistory.Entry> {

	public boolean add(Goban goban, Coord coord, Stone stone) {
		return super.add(new Entry(goban, coord, stone));
	}

	public static class Entry {
		private final Goban goban;
		private final Coord coord;
		private final Stone stone;

		/**
		 * 
		 * @param goban
		 *            the state of the goban <b>before</b> the move
		 * @param coord
		 *            the coordinate of the move
		 * @param stone
		 *            the stone placed at the given coordinate
		 */
		public Entry(Goban goban, Coord coord, Stone stone) {
			this.goban = goban;
			this.coord = coord;
			this.stone = stone;
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
	public GameHistory clone() {
		GameHistory clone = new GameHistory();
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
