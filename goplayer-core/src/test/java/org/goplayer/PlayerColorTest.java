package org.goplayer;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.HashSet;

import org.junit.Test;

public class PlayerColorTest {

	@Test
	public void testPossibleValues() {
		Collection<String> values = new HashSet<String>();
		for (PlayerColor color : PlayerColor.values()) {
			values.add(color.name().toLowerCase());
		}
		assertTrue(values.contains("black"));
		assertTrue(values.contains("white"));
		assertEquals(2, values.size());
	}

}
