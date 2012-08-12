package org.goplayer.game;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
	public void testClone() {
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
		Block b2 = b1.clone();
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

	@SuppressWarnings("unchecked")
	@Test
	public void testBlocksCoveringCustomGoban() {
		// X----
		// XXX--
		// -X---
		// -XXX-
		// -----
		Map<Coord, Stone> group1 = new HashMap<Coord, Stone>();
		group1.put(new Coord(0, 0), new Stone(StoneColor.BLACK));
		group1.put(new Coord(1, 0), new Stone(StoneColor.BLACK));
		group1.put(new Coord(1, 1), new Stone(StoneColor.BLACK));
		group1.put(new Coord(1, 2), new Stone(StoneColor.BLACK));
		group1.put(new Coord(2, 1), new Stone(StoneColor.BLACK));
		group1.put(new Coord(3, 1), new Stone(StoneColor.BLACK));
		group1.put(new Coord(3, 2), new Stone(StoneColor.BLACK));
		group1.put(new Coord(3, 3), new Stone(StoneColor.BLACK));

		// X----
		// XXX--
		// -XOO-
		// -XXX-
		// -----
		Map<Coord, Stone> group2 = new HashMap<Coord, Stone>();
		group2.put(new Coord(2, 2), new Stone(StoneColor.WHITE));
		group2.put(new Coord(2, 3), new Stone(StoneColor.WHITE));

		// X----
		// XXX--
		// OXOO-
		// OXXX-
		// OO---
		Map<Coord, Stone> group3 = new HashMap<Coord, Stone>();
		group3.put(new Coord(2, 0), new Stone(StoneColor.WHITE));
		group3.put(new Coord(3, 0), new Stone(StoneColor.WHITE));
		group3.put(new Coord(4, 0), new Stone(StoneColor.WHITE));
		group3.put(new Coord(4, 1), new Stone(StoneColor.WHITE));

		// create goban
		Goban goban = new Goban(5);
		for (Map<Coord, Stone> group : Arrays.asList(group1, group2, group3)) {
			for (Map.Entry<Coord, Stone> entry : group.entrySet()) {
				goban.setCoordContent(entry.getKey(), entry.getValue());
			}
		}

		// get blocks
		Coord coord1 = group1.keySet().iterator().next();
		Coord coord2 = group2.keySet().iterator().next();
		Coord coord3 = group3.keySet().iterator().next();
		List<Coord> starts = new ArrayList<Coord>(Arrays.asList(coord1, coord2,
				coord3));
		Set<Block> blocks = Block.getBlocksCovering(goban, starts);

		// test enough blocks
		assertEquals(3, blocks.size());

		// test each block correspond to a start
		for (Block block : blocks) {
			int index = -1;
			for (int i = 0; i < starts.size(); i++) {
				Coord coord = starts.get(i);
				if (block.contains(goban.getCoordContent(coord))) {
					if (index >= 0) {
						fail("One block contains both " + starts.get(index)
								+ " and " + coord);
					} else {
						index = i;
					}
				} else {
					continue;
				}
			}
			starts.remove(index);
		}
		assertEquals(0, starts.size());
	}

	@Test
	public void testBlocksCoveringUnicity() {
		Goban goban = new Goban(5);
		goban.setCoordContent(0, 0, new Stone(StoneColor.BLACK));
		goban.setCoordContent(1, 0, new Stone(StoneColor.BLACK));
		goban.setCoordContent(1, 1, new Stone(StoneColor.BLACK));
		goban.setCoordContent(1, 2, new Stone(StoneColor.BLACK));
		goban.setCoordContent(2, 1, new Stone(StoneColor.BLACK));
		goban.setCoordContent(3, 1, new Stone(StoneColor.BLACK));
		goban.setCoordContent(3, 2, new Stone(StoneColor.BLACK));
		goban.setCoordContent(3, 3, new Stone(StoneColor.BLACK));

		// get expected block
		Block block1 = Block.getBlockCovering(goban, new Coord(0, 0));
		Collection<Stone> stones1 = block1.getStones();
		Collection<Coord> coords = new HashSet<Coord>();
		for (Stone stone : stones1) {
			coords.add(goban.getStoneCoord(stone));
		}

		// test unique block
		Set<Block> blocks = Block.getBlocksCovering(goban, coords);
		assertEquals(1, blocks.size());

		// test block equivalence
		Block block2 = blocks.iterator().next();
		assertEquals(block1, block2);
	}
}
