package org.goplayer;

import static org.junit.Assert.*;

import org.junit.Test;

public class GobanTest {

	@Test
	public void testWidth() {
		Goban goban = new Goban();
		assertEquals(19, goban.getWidth());

		int width = 13;
		goban.setWidth(width);
		assertEquals(width, goban.getWidth());

		goban.initField();
		assertEquals(width, goban.getWidth());
	}

	@Test
	public void testHeight() {
		Goban goban = new Goban();
		assertEquals(19, goban.getWidth());

		int height = 13;
		goban.setHeight(height);
		assertEquals(height, goban.getHeight());

		goban.initField();
		assertEquals(height, goban.getHeight());
	}

	@Test
	public void testInitField() {
		Goban goban = new Goban();
		int width = 12;
		int height = 15;
		goban.setWidth(width);
		goban.setHeight(height);
		goban.initField();
		assertEquals(width, goban.getField().length);
		assertEquals(height, goban.getField()[0].length);
	}

	@Test
	public void testIsInitialized() {
		Goban goban = new Goban();
		assertFalse(goban.isInitialized());

		goban.initField();
		assertTrue(goban.isInitialized());
	}

	@Test
	public void testHandicap() {
		fail("Not implemented yet.");
	}

	@Test
	public void testPlaceIshi() {
		fail("Not implemented yet.");
	}

	@Test
	public void testPlayerColor() {
		fail("Not implemented yet.");
	}

	@Test
	public void testCurrentPlayer() {
		fail("Not implemented yet.");
	}

	@Test
	public void testWhitePlayer() {
		Goban goban = new Goban();
		assertNull(goban.getWhitePlayer());

		Player player = new Player();
		goban.setWhitePlayer(player);
		assertEquals(player, goban.getWhitePlayer());
	}

	@Test
	public void testBlackPlayer() {
		Goban goban = new Goban();
		assertNull(goban.getBlackPlayer());

		Player player = new Player();
		goban.setBlackPlayer(player);
		assertEquals(player, goban.getBlackPlayer());
	}

	@Test
	public void testPlaying() {
		fail("Not implemented yet.");
	}

	@Test
	public void testField() {
		fail("Not implemented yet.");
	}

	@Test
	public void testPreviousField() {
		fail("Not implemented yet.");
	}

}
