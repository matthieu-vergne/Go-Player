package org.goplayer;

import static org.junit.Assert.*;

import org.junit.Test;

public class PlayerTest {

	@Test
	public void testGoban() {
		Player player = new Player();
		assertNull(player.getGoban());

		Goban goban1 = new Goban();
		player.setGoban(goban1);
		assertEquals(goban1, player.getGoban());

		Goban goban2 = new Goban();
		player.setGoban(goban2);
		assertEquals(goban2, player.getGoban());
	}

	@Test
	public void testColor() {
		Player player = new Player();
		assertNull(player.getColor());

		Goban goban1 = new Goban();
		goban1.setWhitePlayer(player);
		player.setGoban(goban1);
		assertEquals(PlayerColor.WHITE, player.getColor());

		Goban goban2 = new Goban();
		goban2.setBlackPlayer(player);
		player.setGoban(goban2);
		assertEquals(PlayerColor.BLACK, player.getColor());

		player.setGoban(null);
		assertNull(player.getColor());

		player.setGoban(goban1);
		assertEquals(PlayerColor.WHITE, player.getColor());

		player.setGoban(goban2);
		assertEquals(PlayerColor.BLACK, player.getColor());
	}
}
