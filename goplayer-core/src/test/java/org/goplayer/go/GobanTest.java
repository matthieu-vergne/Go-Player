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
}
