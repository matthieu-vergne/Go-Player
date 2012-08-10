package org.goplayer.go;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.HashSet;

import org.goplayer.go.StoneColor;
import org.junit.Test;

public class StoneColorTest {

	@Test
	public void testPossibleValues() {
		Collection<String> values = new HashSet<String>();
		for (StoneColor color : StoneColor.values()) {
			values.add(color.name().toLowerCase());
		}
		assertTrue(values.contains("black"));
		assertTrue(values.contains("white"));
		assertEquals(2, values.size());
	}

}
