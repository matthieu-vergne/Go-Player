package org.goplayer.game;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.goplayer.go.Goban;
import org.goplayer.go.Stone;
import org.goplayer.go.StoneColor;
import org.goplayer.util.Coord;
import org.junit.Test;

public class BlockTest {

	@Test
	public void testEquality() {
		Map<Coord, Stone> stones = new HashMap<Coord, Stone>();
		stones.put(new Coord(0, 0), new Stone(StoneColor.BLACK));
		stones.put(new Coord(1, 0), new Stone(StoneColor.BLACK));
		stones.put(new Coord(1, 1), new Stone(StoneColor.BLACK));
		stones.put(new Coord(1, 2), new Stone(StoneColor.BLACK));
		stones.put(new Coord(2, 1), new Stone(StoneColor.BLACK));
		stones.put(new Coord(3, 1), new Stone(StoneColor.BLACK));
		stones.put(new Coord(3, 2), new Stone(StoneColor.BLACK));
		stones.put(new Coord(3, 3), new Stone(StoneColor.BLACK));

		Goban goban = new Goban(5);
		for (Map.Entry<Coord, Stone> entry : stones.entrySet()) {
			goban.setCoordContent(entry.getKey(), entry.getValue());
		}
		Block b1 = Block.getBlockCovering(goban, 0, 0);
		Block b2 = Block.getBlockCovering(goban, 0, 0);
		assertFalse(b1 == b2);
		assertTrue(b1.equals(b2));
		assertTrue(b2.equals(b1));
	}

	@Test
	public void testBlockCoveringFreePlace() {
		int size = 5;
		Goban goban = new Goban(size);
		int startRow = 2;
		int startCol = 3;
		try {
			Block.getBlockCovering(goban, startRow, startCol);
			fail("No exception thrown");
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void testBlockCoveringIsolatedPlace() {
		int size = 5;
		Goban goban = new Goban(size);
		int startRow = 2;
		int startCol = 3;
		goban.setCoordContent(startRow, startCol, new Stone(StoneColor.BLACK));

		Block block = Block.getBlockCovering(goban, startRow, startCol);
		assertEquals(1, block.getStones().size());
		assertTrue(block.getStones().contains(
				goban.getCoordContent(startRow, startCol)));
		assertEquals(4, block.getLiberties().size());
		assertTrue(block.getLiberties().contains(
				new Coord(startRow + 1, startCol)));
		assertTrue(block.getLiberties().contains(
				new Coord(startRow - 1, startCol)));
		assertTrue(block.getLiberties().contains(
				new Coord(startRow, startCol + 1)));
		assertTrue(block.getLiberties().contains(
				new Coord(startRow, startCol - 1)));
		assertEquals(StoneColor.BLACK, block.getColor());
	}

	@Test
	public void testBlockCoveringFullGoban() {
		int size = 5;
		Goban goban = Goban.createFullGoban(size, size, StoneColor.BLACK);
		int startRow = 2;
		int startCol = 3;

		Block block = Block.getBlockCovering(goban, startRow, startCol);
		assertEquals(size * size, block.getStones().size());
		for (int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++) {
				assertTrue(block.getStones().contains(
						goban.getCoordContent(row, col)));
			}
		}
		assertEquals(0, block.getLiberties().size());
		assertEquals(StoneColor.BLACK, block.getColor());
	}

	@Test
	public void testBlockCoveringCustomBlock() {
		int size = 5;
		Goban goban = new Goban(size);

		// create custom white block
		// -----
		// ---0-
		// --000
		// ----0
		// ---00
		Map<Coord, Stone> target = new HashMap<Coord, Stone>();
		target.put(new Coord(1, 3), new Stone(StoneColor.WHITE));
		target.put(new Coord(2, 2), new Stone(StoneColor.WHITE));
		target.put(new Coord(2, 3), new Stone(StoneColor.WHITE));
		target.put(new Coord(2, 4), new Stone(StoneColor.WHITE));
		target.put(new Coord(3, 4), new Stone(StoneColor.WHITE));
		target.put(new Coord(4, 4), new Stone(StoneColor.WHITE));
		target.put(new Coord(4, 3), new Stone(StoneColor.WHITE));

		// fill block
		for (Map.Entry<Coord, Stone> entry : target.entrySet()) {
			int row = entry.getKey().getRow();
			int col = entry.getKey().getCol();
			Stone stone = entry.getValue();
			goban.setCoordContent(row, col, stone);
		}

		// add whites which touch only
		// 0----
		// -0-0-
		// --000
		// -0--0
		// ---00
		goban.setCoordContent(0, 0, new Stone(StoneColor.WHITE));
		goban.setCoordContent(1, 1, new Stone(StoneColor.WHITE));
		goban.setCoordContent(3, 1, new Stone(StoneColor.WHITE));

		// add some blacks
		// 0--X-
		// -0X0-
		// X-000
		// -0--0
		// -X-00
		goban.setCoordContent(0, 3, new Stone(StoneColor.BLACK));
		goban.setCoordContent(1, 2, new Stone(StoneColor.BLACK));
		goban.setCoordContent(2, 0, new Stone(StoneColor.BLACK));
		goban.setCoordContent(4, 1, new Stone(StoneColor.BLACK));

		// compute expected liberties
		Set<Coord> liberties = new HashSet<Coord>();
		liberties.add(new Coord(1, 4));
		liberties.add(new Coord(2, 1));
		liberties.add(new Coord(3, 2));
		liberties.add(new Coord(3, 3));
		liberties.add(new Coord(4, 2));

		// check equivalence whatever we take as a start
		for (Coord start : target.keySet()) {
			Block block = Block.getBlockCovering(goban, start);
			assertTrue(block.getStones().containsAll(target.values()));
			assertTrue(target.values().containsAll(block.getStones()));
			assertTrue(block.getLiberties().containsAll(liberties));
			assertTrue(liberties.containsAll(block.getLiberties()));
			assertEquals(StoneColor.WHITE, block.getColor());
		}
	}
}
