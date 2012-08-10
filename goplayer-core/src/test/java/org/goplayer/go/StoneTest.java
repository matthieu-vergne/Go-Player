package org.goplayer.go;

import static org.junit.Assert.*;

import org.junit.Test;

public class StoneTest {

	@Test
	public void testColor() {
		assertEquals(StoneColor.BLACK, new Stone(StoneColor.BLACK).getColor());
		assertEquals(StoneColor.WHITE, new Stone(StoneColor.WHITE).getColor());
	}

}
