package org.goplayer.util;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.goplayer.go.Goban;
import org.goplayer.go.Stone;
import org.goplayer.go.StoneColor;
import org.junit.Test;

public class StoneIteratorTest {

	@Test
	public void testBlackIteratorOnFullBlackGoban() {
		int size = 5;
		Goban goban = Goban.createFullGoban(size, size, StoneColor.BLACK);
		Set<Coord> coords = new HashSet<Coord>();
		for (int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++) {
				coords.add(new Coord(row, col));
			}
		}

		StoneIterator iterator = new StoneIterator(goban, StoneColor.BLACK);
		int total = size * size;
		for (int i = 0; i < total; i++) {
			assertTrue("There is no element after the first " + i,
					iterator.hasNext());
			Stone stone = iterator.next();
			Coord coord = goban.getStoneCoord(stone);
			assertTrue(coords.contains(coord));
			coords.remove(coord);
		}
		assertFalse("There is an element " + (total + 1), iterator.hasNext());
	}

	@Test
	public void testWhiteIteratorOnFullBlackGoban() {
		int size = 5;
		Goban goban = Goban.createFullGoban(size, size, StoneColor.BLACK);
		StoneIterator iterator = new StoneIterator(goban, StoneColor.WHITE);
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testBlackIteratorOnCustomGoban() {
		int size = 5;
		Goban goban = new Goban(size);
		Map<Coord, Stone> stones = new HashMap<Coord, Stone>();
		stones.put(new Coord(2, 3), new Stone(StoneColor.BLACK));
		stones.put(new Coord(1, 2), new Stone(StoneColor.BLACK));
		stones.put(new Coord(1, 4), new Stone(StoneColor.BLACK));
		stones.put(new Coord(4, 0), new Stone(StoneColor.BLACK));
		stones.put(new Coord(0, 1), new Stone(StoneColor.BLACK));
		for (Map.Entry<Coord, Stone> entry : stones.entrySet()) {
			int row = entry.getKey().getRow();
			int col = entry.getKey().getCol();
			Stone stone = entry.getValue();
			goban.setCoordContent(row, col, stone);
		}

		StoneIterator iterator = new StoneIterator(goban, StoneColor.BLACK);
		int total = stones.size();
		for (int i = 0; i < total; i++) {
			assertTrue("There is no element after the first " + i,
					iterator.hasNext());
			Stone stone = iterator.next();
			Coord coord = goban.getStoneCoord(stone);
			assertTrue(stones.containsKey(coord));
			assertEquals(stone, stones.get(coord));
			stones.remove(coord);
		}
		assertFalse("There is an element " + (total + 1), iterator.hasNext());
	}
}
