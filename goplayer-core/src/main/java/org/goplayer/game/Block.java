package org.goplayer.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.goplayer.Coord;
import org.goplayer.go.Goban;
import org.goplayer.go.Stone;

// TODO implement contiguous block
public class Block {
	private final Set<Stone> stones;

	public Block(Collection<Stone> stones) {
		this.stones = Collections.unmodifiableSet(new HashSet<Stone>(stones));
	}

	public Block(Stone... stones) {
		this(Arrays.asList(stones));
	}

	public Collection<Stone> getStones() {
		return stones;
	}

	public static Block generateFrom(Goban goban, int startRow, int startCol) {
		final List<Stone> stones = new ArrayList<Stone>();
		final Stone reference = goban.getCoordContent(startRow, startCol);
		if (reference == null) {
			// do not fill anything
		} else {
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
			PlayerColor refColor = reference.getColor();
			while (!check.isEmpty()) {
				Coord coord = check.remove(0);
				int row = coord.getRow();
				int col = coord.getCol();
				Stone stone = goban.getCoordContent(row, col);
				if (stone != null && stone.getColor() == refColor) {
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
		}
		return new Block(stones);
	}
}
