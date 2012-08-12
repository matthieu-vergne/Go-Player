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
import org.goplayer.util.StoneIterator;

public class Block implements Iterable<Stone> {
	private final Set<Stone> stones;
	private final Set<Coord> liberties;

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (obj instanceof Block) {
			Block b = (Block) obj;
			return stones.containsAll(b.stones) && b.stones.containsAll(stones)
					&& liberties.containsAll(b.liberties)
					&& b.liberties.containsAll(liberties);
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		for (Stone stone : stones) {
			hash += stone.hashCode();
		}
		for (Coord liberty : liberties) {
			hash += liberty.hashCode();
		}
		return hash;
	}

	@Override
	public Block clone() {
		return new Block(stones, liberties);
	}

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

	public static Set<Block> getBlocksCovering(Goban goban,
			Collection<Coord> starts) {
		Set<Block> blocks = new HashSet<Block>();
		for (Coord start : starts) {
			boolean alreadyCovered = false;
			for (Block block : blocks) {
				if (block.contains(goban.getCoordContent(start))) {
					alreadyCovered = true;
					break;
				} else {
					continue;
				}
			}
			if (alreadyCovered) {
				continue;
			} else {
				blocks.add(getBlockCovering(goban, start.getRow(), start.getCol()));
			}
		}
		return blocks;
	}

	public static Block getBlockCovering(Goban goban, Coord start) {
		return getBlockCovering(goban, start.getRow(), start.getCol());
	}

	public static Block getBlockCovering(Goban goban, int startRow, int startCol) {
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

	public static Set<Block> getAllBlocks(Goban goban) {
		Set<Block> blocks = getAllBlocks(goban, StoneColor.BLACK);
		blocks.addAll(getAllBlocks(goban, StoneColor.WHITE));
		return blocks;
	}

	public static Set<Block> getAllBlocks(Goban goban, StoneColor color) {
		final Set<Block> blocks = new HashSet<Block>();
		StoneIterator iterator = new StoneIterator(goban, color);
		while (iterator.hasNext()) {
			Stone stone = (Stone) iterator.next();
			if (isStoneInAllBlocks(stone, blocks)) {
				continue;
			} else {
				Block block = getBlockCovering(goban,
						goban.getStoneCoord(stone));
				blocks.add(block);
			}
		}
		return blocks;
	}

	private static boolean isStoneInAllBlocks(Stone stone, Set<Block> blocks) {
		for (Block block : blocks) {
			if (block.contains(stone)) {
				return true;
			} else {
				continue;
			}
		}
		return false;
	}

	@Override
	public Iterator<Stone> iterator() {
		return stones.iterator();
	}
}
