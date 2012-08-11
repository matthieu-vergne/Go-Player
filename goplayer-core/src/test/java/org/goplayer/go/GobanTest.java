package org.goplayer.go;

import static org.junit.Assert.*;

import org.goplayer.util.Coord;
import org.junit.Test;

public class GobanTest {

	@Test
	public void testInvalidGobanSize() {
		try {
			new Goban(0);
			fail("No exception thrown");
		} catch (IllegalArgumentException e) {
		}

		try {
			new Goban(0, 0);
			fail("No exception thrown");
		} catch (IllegalArgumentException e) {
		}

		try {
			new Goban(0, 5);
			fail("No exception thrown");
		} catch (IllegalArgumentException e) {
		}

		try {
			new Goban(5, 0);
			fail("No exception thrown");
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void testSquareGobanSize() {
		for (int size : new int[] { 9, 13, 19 }) {
			Goban goban = new Goban(size);
			assertEquals(size, goban.getColCount());
			assertEquals(size, goban.getRowCount());
		}
	}

	@Test
	public void testRectangleGobanSize() {
		for (int colCount : new int[] { 9, 13, 19 }) {
			for (int rowCount : new int[] { 9, 13, 19 }) {
				Goban goban = new Goban(rowCount, colCount);
				assertEquals(colCount, goban.getColCount());
				assertEquals(rowCount, goban.getRowCount());
			}
		}
	}

	@Test
	public void testContentSetting() {
		int size = 5;
		Goban goban = new Goban(size);
		for (int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++) {
				assertNull(goban.getCoordContent(row, col));
			}
		}

		{
			Stone stone = new Stone(StoneColor.BLACK);
			int row = 0;
			int col = 0;
			goban.setCoordContent(row, col, stone);
			assertEquals(stone, goban.getCoordContent(row, col));
		}

		{
			Stone stone = new Stone(StoneColor.BLACK);
			int row = 2;
			int col = 3;
			goban.setCoordContent(row, col, stone);
			assertEquals(stone, goban.getCoordContent(row, col));
		}

		{
			Stone stone = new Stone(StoneColor.BLACK);
			int row = 4;
			int col = 1;
			goban.setCoordContent(row, col, stone);
			assertEquals(stone, goban.getCoordContent(row, col));
		}
	}

	@Test
	public void testFullGobanCreation() {
		int rowCount = 3;
		int colCount = 5;
		StoneColor color = StoneColor.BLACK;
		Goban goban = Goban.createFullGoban(rowCount, colCount, color);
		assertEquals(rowCount, goban.getRowCount());
		assertEquals(colCount, goban.getColCount());
		for (int row = 0; row < rowCount; row++) {
			for (int col = 0; col < colCount; col++) {
				Stone stone = goban.getCoordContent(row, col);
				assertNotNull(stone);
				assertEquals(color, stone.getColor());
			}
		}
	}

	@Test
	public void testStoneCoord() {
		int size = 5;
		Goban goban = Goban.createFullGoban(size, size, StoneColor.BLACK);
		for (int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++) {
				assertEquals(new Coord(row, col),
						goban.getStoneCoord(goban.getCoordContent(row, col)));
			}
		}
	}

	@Test
	public void testString() {
		assertEquals("-", new Goban(1).toString());
		assertEquals("--\n--", new Goban(2).toString());
		assertEquals("XX\nXX", Goban.createFullGoban(2, 2, StoneColor.BLACK)
				.toString());
		assertEquals("OO\nOO", Goban.createFullGoban(2, 2, StoneColor.WHITE)
				.toString());

		Goban goban = new Goban(2);
		goban.setCoordContent(0, 0, new Stone(StoneColor.BLACK));
		goban.setCoordContent(0, 1, new Stone(StoneColor.WHITE));
		goban.setCoordContent(1, 0, null);
		goban.setCoordContent(1, 1, new Stone(StoneColor.WHITE));
		assertEquals("XO\n-O", goban.toString());
	}

	@Test
	public void testClone() {
		Goban goban = new Goban(5, 6);
		goban.setCoordContent(0, 1, new Stone(StoneColor.BLACK));
		goban.setCoordContent(0, 2, new Stone(StoneColor.BLACK));
		goban.setCoordContent(0, 3, new Stone(StoneColor.WHITE));
		goban.setCoordContent(0, 5, new Stone(StoneColor.WHITE));
		goban.setCoordContent(1, 0, new Stone(StoneColor.BLACK));
		goban.setCoordContent(1, 1, new Stone(StoneColor.WHITE));
		goban.setCoordContent(1, 2, new Stone(StoneColor.BLACK));
		goban.setCoordContent(1, 4, new Stone(StoneColor.WHITE));
		goban.setCoordContent(2, 0, new Stone(StoneColor.WHITE));
		goban.setCoordContent(2, 1, new Stone(StoneColor.BLACK));
		goban.setCoordContent(2, 3, new Stone(StoneColor.BLACK));
		goban.setCoordContent(2, 5, new Stone(StoneColor.BLACK));
		goban.setCoordContent(3, 0, new Stone(StoneColor.WHITE));
		goban.setCoordContent(4, 1, new Stone(StoneColor.BLACK));
		goban.setCoordContent(4, 2, new Stone(StoneColor.WHITE));
		goban.setCoordContent(4, 4, new Stone(StoneColor.WHITE));

		// check max coverage
		boolean hasBlack = false;
		boolean hasWhite = false;
		boolean hasFree = false;
		for (int row = 0; row < goban.getRowCount(); row++) {
			for (int col = 0; col < goban.getColCount(); col++) {
				Stone stone = goban.getCoordContent(row, col);
				if (stone == null) {
					hasFree = true;
				} else if (stone.getColor() == StoneColor.BLACK) {
					hasBlack = true;
				} else if (stone.getColor() == StoneColor.WHITE) {
					hasWhite = true;
				} else {
					throw new IllegalStateException(
							"This case should not happen.");
				}
			}
		}
		assertTrue(hasBlack);
		assertTrue(hasWhite);
		assertTrue(hasFree);

		// test
		Goban clone = goban.clone();
		assertFalse(goban == clone);
		assertEquals(goban.getRowCount(), clone.getRowCount());
		assertEquals(goban.getColCount(), clone.getColCount());
		for (int row = 0; row < goban.getRowCount(); row++) {
			for (int col = 0; col < goban.getColCount(); col++) {
				Stone expected = goban.getCoordContent(row, col);
				Stone actual = clone.getCoordContent(row, col);
				if (expected == null) {
					assertNull(actual);
				} else {
					assertFalse(expected == actual);
					assertNotNull(actual);
					assertEquals(expected.getColor(), actual.getColor());
				}
			}
		}
	}
}
