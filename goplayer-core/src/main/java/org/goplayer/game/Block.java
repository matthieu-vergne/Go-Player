package org.goplayer.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.goplayer.go.Goban;
import org.goplayer.go.Stone;
import org.goplayer.go.StoneColor;
import org.goplayer.util.Coord;

public class Block implements Iterable<Stone> {
	private final Set<Stone> stones;
	private final Set<Coord> liberties;

	private Block(Collection<Stone> stones, Collection<Coord> liberties) {
		this.stones = Collections.unmodifiableSet(new HashSet<Stone>(stones));
		this.liberties = Collections.unmodifiableSet(new HashSet<Coord>(
				liberties));
	}

	public Collection<Stone> getStones() {
		return stones;
	}

	public Integer size() {
		return getStones().size();
	}

	public StoneColor getColor() {
		return stones.iterator().next().getColor();
	}

	public Set<Coord> getLiberties() {
		return liberties;
	}

	public boolean contains(Stone stone) {
		return stones.contains(stone);
	}

	public static Block generateFrom(Goban goban, Coord start) {
		return generateFrom(goban, start.getRow(), start.getCol());
	}

	public static Block generateFrom(Goban goban, int startRow, int startCol) {
		final Stone reference = goban.getCoordContent(startRow, startCol);
		if (reference == null) {
			throw new IllegalArgumentException("No stone is at "
					+ new Coord(startRow, startCol));
		} else {
			final List<Stone> stones = new ArrayList<Stone>();
			final List<Coord> liberties = new ArrayList<Coord>();
			final List<Coord> check = new ArrayList<Coord>();
			final Set<Coord> explored = new HashSet<Coord>();
			for (int row = 0; row < goban.getRowCount(); row++) {
				explored.add(new Coord(row, -1));
				explored.add(new Coord(row, goban.getColCount()));
			}
			for (int col = 0; col < goban.getColCount(); col++) {
				explored.add(new Coord(-1, col));
				explored.add(new Coord(goban.getRowCount(), col));
			}

			check.add(new Coord(startRow, startCol));
			StoneColor refColor = reference.getColor();
			while (!check.isEmpty()) {
				Coord coord = check.remove(0);
				int row = coord.getRow();
				int col = coord.getCol();
				Stone stone = goban.getCoordContent(row, col);
				if (stone == null) {
					liberties.add(coord);
				} else if (stone.getColor() == refColor) {
					stones.add(stone);
					explored.add(coord);
					check.add(new Coord(row + 1, col));
					check.add(new Coord(row - 1, col));
					check.add(new Coord(row, col + 1));
					check.add(new Coord(row, col - 1));
					check.removeAll(explored);
				} else {
					continue;
				}
			}
			return new Block(stones, liberties);
		}
	}

	@Override
	public Iterator<Stone> iterator() {
		return stones.iterator();
	}
}
