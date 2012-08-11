package org.goplayer.util;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.goplayer.go.Goban;
import org.goplayer.go.StoneColor;
import org.junit.Test;

public class FreeCoordIteratorTest {

	@Test
	public void testOnEmptyGoban() {
		int size = 5;
		Goban goban = new Goban(size);
		Set<Coord> coords = new HashSet<Coord>();
		for (int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++) {
				coords.add(new Coord(row, col));
			}
		}

		FreeCoordIterator iterator = new FreeCoordIterator(goban);
		int total = size * size;
		for (int i = 0; i < total; i++) {
			assertTrue("There is no element after the first " + i,
					iterator.hasNext());
			Coord coord = iterator.next();
			assertTrue(coord + " is not in the remaining coords",
					coords.contains(coord));
			coords.remove(coord);
		}
		assertFalse("There is an element " + (total + 1), iterator.hasNext());
	}

	@Test
	public void testOnFullGoban() {
		int size = 5;
		Goban goban = Goban.createFullGoban(size, size, StoneColor.BLACK);
		FreeCoordIterator iterator = new FreeCoordIterator(goban);
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testOnCustomGoban() {
		int size = 5;
		Goban goban = Goban.createFullGoban(size, size, StoneColor.BLACK);
		Set<Coord> coords = new HashSet<Coord>();
		coords.add(new Coord(2, 3));
		coords.add(new Coord(1, 2));
		coords.add(new Coord(1, 4));
		coords.add(new Coord(4, 0));
		coords.add(new Coord(0, 1));
		for (Coord coord : coords) {
			int row = coord.getRow();
			int col = coord.getCol();
			goban.setCoordContent(row, col, null);
		}

		FreeCoordIterator iterator = new FreeCoordIterator(goban);
		int total = coords.size();
		for (int i = 0; i < total; i++) {
			assertTrue("There is no element after the first " + i,
					iterator.hasNext());
			Coord coord = iterator.next();
			assertTrue(coord + " is not in the remaining coords",
					coords.contains(coord));
			coords.remove(coord);
		}
		assertFalse("There is an element " + (total + 1), iterator.hasNext());
	}
}
