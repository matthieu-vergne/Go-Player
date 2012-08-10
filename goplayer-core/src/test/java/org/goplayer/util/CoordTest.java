package org.goplayer.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class CoordTest {

	@Test
	public void testRow() {
		assertEquals(3, new Coord(3, 0).getRow());
		assertEquals(-2, new Coord(-2, 5).getRow());
		assertEquals(300, new Coord(300, 20).getRow());
	}

	@Test
	public void testCol() {
		assertEquals(3, new Coord(0, 3).getCol());
		assertEquals(-2, new Coord(5, -2).getCol());
		assertEquals(300, new Coord(20, 300).getCol());
	}

	@Test
	public void testString() {
		assertEquals("(3, 0)", new Coord(3, 0).toString());
		assertEquals("(-2, 5)", new Coord(-2, 5).toString());
		assertEquals("(300, 20)", new Coord(300, 20).toString());
	}

	@Test
	public void testEquality() {
		Coord c1 = new Coord(3, 0);
		Coord c2 = new Coord(3, 0);
		Coord c3 = new Coord(3, 0);
		Coord c4 = new Coord(3, 5);
		assertTrue(c1.equals(c2));
		assertTrue(c1.equals(c3));
		assertFalse(c1.equals(c4));

		assertTrue(c1.equals(c1));// reflexive
		assertTrue(c1.equals(c2) == c2.equals(c1));// symmetric
		assertTrue(c1.equals(c4) == c4.equals(c1));// symmetric
		assertTrue(c2.equals(c3) == c1.equals(c3));// transitive
		assertTrue(c2.equals(c4) == c1.equals(c4));// transitive
	}
}
