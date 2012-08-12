package org.goplayer.player;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.goplayer.go.Goban;
import org.goplayer.move.IMove;
import org.goplayer.move.PassMove;
import org.goplayer.move.StoneMove;
import org.goplayer.util.Coord;
import org.junit.Test;

public class DeterminedPlayerTest {

	@Test
	public void testSteps() {
		List<Coord> steps = new ArrayList<Coord>(Arrays.asList(new Coord(0, 0),
				new Coord(1, 3), new Coord(0, 0), null, new Coord(2, 2),
				new Coord(2, 2), new Coord(5, 3), null, new Coord(1, 4),
				new Coord(3, 2)));
		DeterminedPlayer player = new DeterminedPlayer(steps);
		Goban goban = new Goban(10);
		while (!steps.isEmpty()) {
			IMove move = player.play(goban);
			Coord expected = steps.remove(0);
			if (expected == null) {
				assertTrue(move instanceof PassMove);
			} else {
				assertTrue(move instanceof StoneMove);
				assertEquals(expected, ((StoneMove) move).getCoord());
			}
		}
		for (int i = 0; i < 10; i++) {
			assertTrue(player.play(goban) instanceof PassMove);
		}
	}

}
