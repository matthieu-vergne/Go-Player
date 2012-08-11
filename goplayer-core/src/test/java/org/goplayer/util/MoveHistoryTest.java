package org.goplayer.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.goplayer.game.Game;
import org.goplayer.game.strategy.IStrategy;
import org.goplayer.go.Goban;
import org.goplayer.move.AbandonMove;
import org.goplayer.move.IMove;
import org.goplayer.move.StoneMove;
import org.goplayer.player.IPlayer;
import org.goplayer.player.StrategicalPlayer;
import org.junit.Test;

public class MoveHistoryTest {

	@Test
	public void testHistory() {
		final List<StoneMove> steps = new ArrayList<StoneMove>();
		// -----
		// -----
		// -----
		// -----
		// -----
		steps.add(new StoneMove(0, 0));
		// X----
		// -----
		// -----
		// -----
		// -----
		steps.add(new StoneMove(0, 1));
		// X0---
		// -----
		// -----
		// -----
		// -----
		steps.add(new StoneMove(1, 1));
		// X0---
		// -X---
		// -----
		// -----
		// -----
		steps.add(new StoneMove(1, 0));
		// -0---
		// 0X---
		// -----
		// -----
		// -----
		steps.add(new StoneMove(0, 2));
		// -0X--
		// 0X---
		// -----
		// -----
		// -----
		steps.add(new StoneMove(1, 2));
		// -0X--
		// 0XO--
		// -----
		// -----
		// -----
		steps.add(new StoneMove(0, 0));
		// X-X--
		// 0XO--
		// -----
		// -----
		// -----
		steps.add(new StoneMove(2, 1));
		// X-X--
		// 0XO--
		// -O---
		// -----
		// -----
		steps.add(new StoneMove(0, 1));
		// XXX--
		// 0XO--
		// -O---
		// -----
		// -----
		steps.add(new StoneMove(0, 3));
		// ---O-
		// 0-O--
		// -O---
		// -----
		// -----

		int size = 5;
		Goban goban = new Goban(size);
		IStrategy strategy = new IStrategy() {
			private int step = -1;

			@Override
			public IMove chooseMove(Goban goban, IPlayer player) {
				step++;
				if (step < steps.size()) {
					return steps.get(step);
				} else {
					return new AbandonMove();
				}
			}
		};
		IPlayer blackPlayer = new StrategicalPlayer(strategy);
		IPlayer whitePlayer = new StrategicalPlayer(strategy);
		Game game = new Game(goban, blackPlayer, whitePlayer);
		while (!game.isFinished()) {
			game.play();
		}

		MoveHistory history = game.getHistory();
		assertEquals(steps.size(), history.size());
		for (int i = 0; i < steps.size(); i++) {
			Coord expected = steps.get(0).getCoord();
			Coord actual = history.get(0).getCoord();
			assertEquals(expected, actual);
		}
	}

}
