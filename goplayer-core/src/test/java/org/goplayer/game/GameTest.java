package org.goplayer.game;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.goplayer.exception.UnknownPlayerException;
import org.goplayer.game.strategy.FirstFreeStrategy;
import org.goplayer.game.strategy.IStrategy;
import org.goplayer.go.Goban;
import org.goplayer.go.StoneColor;
import org.goplayer.move.AbandonMove;
import org.goplayer.move.IMove;
import org.goplayer.move.StoneMove;
import org.goplayer.player.DeterminedPlayer;
import org.goplayer.player.IPlayer;
import org.goplayer.player.StrategicalPlayer;
import org.goplayer.util.ScoreCouple;
import org.junit.Test;

public class GameTest {

	@Test
	public void testInitGobanAndPlayers() {
		Goban goban = new Goban(9);
		IPlayer blackPlayer = new DeterminedPlayer();
		IPlayer whitePlayer = new DeterminedPlayer();
		Game game = new Game(goban, blackPlayer, whitePlayer);
		assertEquals(goban, game.getGoban());
		assertEquals(blackPlayer, game.getPlayer(StoneColor.BLACK));
		assertEquals(whitePlayer, game.getPlayer(StoneColor.WHITE));
	}

	@Test
	public void testPlayerColor() {
		Goban goban = new Goban(9);
		IPlayer blackPlayer = new DeterminedPlayer();
		IPlayer whitePlayer = new DeterminedPlayer();
		Game game = new Game(goban, blackPlayer, whitePlayer);
		assertEquals(goban, game.getGoban());
		assertEquals(StoneColor.BLACK, game.getPlayerColor(blackPlayer));
		assertEquals(StoneColor.WHITE, game.getPlayerColor(whitePlayer));
		try {
			game.getPlayerColor(new DeterminedPlayer());
			fail("No exception thrown for unknown player");
		} catch (UnknownPlayerException e) {
		}
	}

	@Test
	public void testWinnerOnFinish() {
		Goban goban = new Goban(9);
		IPlayer blackPlayer = new DeterminedPlayer();
		IPlayer whitePlayer = new DeterminedPlayer();
		Game game = new Game(goban, blackPlayer, whitePlayer);
		game.finish(blackPlayer);
		assertEquals(blackPlayer, game.getWinner());
		game.finish(whitePlayer);
		assertEquals(whitePlayer, game.getWinner());
		try {
			game.finish(new DeterminedPlayer());
			fail("No exception thrown for unknown player");
		} catch (UnknownPlayerException e) {
		}
	}

	@Test
	public void testIsFinished() {
		Goban goban = new Goban(9);
		IPlayer blackPlayer = new DeterminedPlayer();
		IPlayer whitePlayer = new DeterminedPlayer();
		Game game = new Game(goban, blackPlayer, whitePlayer);
		assertFalse(game.isFinished());
		game.finish(blackPlayer);
		assertTrue(game.isFinished());
	}

	@Test
	public void testPlayTurns() {
		Goban goban = new Goban(9);
		final IPlayer[] lastPlayer = new IPlayer[1];
		IPlayer blackPlayer = new StrategicalPlayer(new FirstFreeStrategy()) {
			@Override
			public IMove play(Goban goban) {
				if (lastPlayer[0] == this) {
					fail("Black player play 2 times");
				} else {
					lastPlayer[0] = this;
				}
				return super.play(goban);
			}
		};
		IPlayer whitePlayer = new StrategicalPlayer(new FirstFreeStrategy()) {
			@Override
			public IMove play(Goban goban) {
				if (lastPlayer[0] == this) {
					fail("White player play 2 times");
				} else {
					lastPlayer[0] = this;
				}
				return super.play(goban);
			}
		};
		Game game = new Game(goban, blackPlayer, whitePlayer);
		for (int i = 0; i < 50; i++) {
			game.play();
		}
	}

	@Test
	public void testCapture() {
		final List<StoneMove> steps = new ArrayList<StoneMove>();
		final List<ScoreCouple> lostStones = new ArrayList<ScoreCouple>();
		// -----
		// -----
		// -----
		// -----
		// -----
		lostStones.add(new ScoreCouple(0, 0));
		steps.add(new StoneMove(0, 0));
		// X----
		// -----
		// -----
		// -----
		// -----
		lostStones.add(new ScoreCouple(0, 0));
		steps.add(new StoneMove(0, 1));
		// X0---
		// -----
		// -----
		// -----
		// -----
		lostStones.add(new ScoreCouple(0, 0));
		steps.add(new StoneMove(1, 1));
		// X0---
		// -X---
		// -----
		// -----
		// -----
		lostStones.add(new ScoreCouple(0, 0));
		steps.add(new StoneMove(1, 0));
		// -0---
		// 0X---
		// -----
		// -----
		// -----
		lostStones.add(new ScoreCouple(1, 0));
		steps.add(new StoneMove(0, 2));
		// -0X--
		// 0X---
		// -----
		// -----
		// -----
		lostStones.add(new ScoreCouple(1, 0));
		steps.add(new StoneMove(1, 2));
		// -0X--
		// 0XO--
		// -----
		// -----
		// -----
		lostStones.add(new ScoreCouple(1, 0));
		steps.add(new StoneMove(0, 0));
		// X-X--
		// 0XO--
		// -----
		// -----
		// -----
		lostStones.add(new ScoreCouple(1, 1));
		steps.add(new StoneMove(2, 1));
		// X-X--
		// 0XO--
		// -O---
		// -----
		// -----
		lostStones.add(new ScoreCouple(1, 1));
		steps.add(new StoneMove(0, 1));
		// XXX--
		// 0XO--
		// -O---
		// -----
		// -----
		lostStones.add(new ScoreCouple(1, 1));
		steps.add(new StoneMove(0, 3));
		// ---O-
		// 0-O--
		// -O---
		// -----
		// -----
		lostStones.add(new ScoreCouple(5, 1));

		int size = 5;
		Goban goban = new Goban(size);
		IStrategy strategy = new IStrategy() {
			private int stepCoutner = -1;

			@Override
			public IMove chooseMove(Goban goban, IPlayer player) {
				stepCoutner++;
				if (!steps.isEmpty()) {
					Game game = Game.getRunningGameOn(goban);
					ScoreCouple actual = new ScoreCouple(
							game.getLostStonesCount(StoneColor.BLACK),
							game.getLostStonesCount(StoneColor.WHITE));
					ScoreCouple expected = lostStones.remove(0);
					assertEquals("Step " + stepCoutner + ": " + actual
							+ " instead of " + expected, expected, actual);
					return steps.remove(0);
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
		assertEquals(1, lostStones.size());
		ScoreCouple scores = lostStones.remove(0);
		assertEquals(scores.getBlack(),
				game.getLostStonesCount(StoneColor.BLACK));
		assertEquals(scores.getWhite(),
				game.getLostStonesCount(StoneColor.WHITE));
	}

}
