package org.goplayer.go;

import static org.junit.Assert.*;

import java.util.Random;

import org.goplayer.game.PlayerColor;
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
		Random r = new Random();
		for (int size : new int[] { 9, 13, 19, r.nextInt(30), r.nextInt(30),
				r.nextInt(30) }) {
			Goban goban = new Goban(size);
			assertEquals(size, goban.getWidth());
			assertEquals(size, goban.getHeight());
		}
	}

	@Test
	public void testRectangleGobanSize() {
		for (int width : new int[] { 9, 13, 19 }) {
			for (int height : new int[] { 9, 13, 19 }) {
				Goban goban = new Goban(width, height);
				assertEquals(width, goban.getWidth());
				assertEquals(height, goban.getHeight());
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
			Stone stone = new Stone(PlayerColor.BLACK);
			int row = 0;
			int col = 0;
			goban.setCoordContent(row, col, stone);
			assertEquals(stone, goban.getCoordContent(row, col));
		}

		{
			Stone stone = new Stone(PlayerColor.BLACK);
			int row = 2;
			int col = 3;
			goban.setCoordContent(row, col, stone);
			assertEquals(stone, goban.getCoordContent(row, col));
		}

		{
			Stone stone = new Stone(PlayerColor.BLACK);
			int row = 4;
			int col = 1;
			goban.setCoordContent(row, col, stone);
			assertEquals(stone, goban.getCoordContent(row, col));
		}
	}

}
