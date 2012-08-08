package org.goplayer.go;

import static org.junit.Assert.*;

import org.goplayer.game.PlayerColor;
import org.junit.Test;

public class StoneTest {

	@Test
	public void testColor() {
		assertEquals(PlayerColor.BLACK, new Stone(PlayerColor.BLACK).getColor());
		assertEquals(PlayerColor.WHITE, new Stone(PlayerColor.WHITE).getColor());
	}

}
