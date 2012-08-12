package org.goplayer.game;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.goplayer.exception.KoException;
import org.goplayer.exception.SuicideException;
import org.goplayer.exception.UnknownPlayerException;
import org.goplayer.game.strategy.FirstFreeStrategy;
import org.goplayer.game.strategy.IStrategy;
import org.goplayer.go.Goban;
import org.goplayer.go.Stone;
import org.goplayer.go.StoneColor;
import org.goplayer.move.AbandonMove;
import org.goplayer.move.IMove;
import org.goplayer.move.PassMove;
import org.goplayer.move.StoneMove;
import org.goplayer.player.DeterminedPlayer;
import org.goplayer.player.IPlayer;
import org.goplayer.player.StrategicalPlayer;
import org.goplayer.util.Coord;
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
		Game game = Game.createFreeGame(goban, blackPlayer, whitePlayer);
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

	@Test
	public void testSuicideCheckOnIsolatedSuicide() {
		Goban goban = new Goban(5);
		IPlayer blackPlayer = new DeterminedPlayer();
		IPlayer whitePlayer = new DeterminedPlayer();
		Game game = new Game(goban, blackPlayer, whitePlayer);

		// -----
		// -----
		// -----
		// -----
		// -----
		assertFalse(game.isForbiddenSuicide(StoneColor.WHITE, new Coord(2, 2)));

		goban.setCoordContent(1, 2, new Stone(StoneColor.BLACK));
		// -----
		// --X--
		// -----
		// -----
		// -----
		assertFalse(game.isForbiddenSuicide(StoneColor.WHITE, new Coord(2, 2)));

		goban.setCoordContent(2, 1, new Stone(StoneColor.BLACK));
		// -----
		// --X--
		// -X---
		// -----
		// -----
		assertFalse(game.isForbiddenSuicide(StoneColor.WHITE, new Coord(2, 2)));

		goban.setCoordContent(2, 3, new Stone(StoneColor.BLACK));
		// -----
		// --X--
		// -X-X-
		// -----
		// -----
		assertFalse(game.isForbiddenSuicide(StoneColor.WHITE, new Coord(2, 2)));

		goban.setCoordContent(3, 2, new Stone(StoneColor.BLACK));
		// -----
		// --X--
		// -X-X-
		// --X--
		// -----
		assertTrue(game.isForbiddenSuicide(StoneColor.WHITE, new Coord(2, 2)));
	}

	@Test
	public void testSuicideCheckOnBlockSuicide() {
		Goban goban = new Goban(5);
		IPlayer blackPlayer = new DeterminedPlayer();
		IPlayer whitePlayer = new DeterminedPlayer();
		Game game = new Game(goban, blackPlayer, whitePlayer);

		// -----
		// -----
		// -----
		// -----
		// -----
		assertFalse(game.isForbiddenSuicide(StoneColor.WHITE, new Coord(3, 2)));

		goban.setCoordContent(1, 2, new Stone(StoneColor.BLACK));
		// -----
		// --X--
		// -----
		// -----
		// -----
		assertFalse(game.isForbiddenSuicide(StoneColor.WHITE, new Coord(3, 2)));

		goban.setCoordContent(2, 1, new Stone(StoneColor.BLACK));
		// -----
		// --X--
		// -X---
		// -----
		// -----
		assertFalse(game.isForbiddenSuicide(StoneColor.WHITE, new Coord(3, 2)));

		goban.setCoordContent(2, 3, new Stone(StoneColor.BLACK));
		// -----
		// --X--
		// -X-X-
		// -----
		// -----
		assertFalse(game.isForbiddenSuicide(StoneColor.WHITE, new Coord(3, 2)));

		goban.setCoordContent(3, 0, new Stone(StoneColor.BLACK));
		// -----
		// --X--
		// -X-X-
		// X----
		// -----
		assertFalse(game.isForbiddenSuicide(StoneColor.WHITE, new Coord(3, 2)));

		goban.setCoordContent(3, 4, new Stone(StoneColor.BLACK));
		// -----
		// --X--
		// -X-X-
		// X---X
		// -----
		assertFalse(game.isForbiddenSuicide(StoneColor.WHITE, new Coord(3, 2)));

		goban.setCoordContent(4, 1, new Stone(StoneColor.BLACK));
		// -----
		// --X--
		// -X-X-
		// X---X
		// -X---
		assertFalse(game.isForbiddenSuicide(StoneColor.WHITE, new Coord(3, 2)));

		goban.setCoordContent(4, 2, new Stone(StoneColor.BLACK));
		// -----
		// --X--
		// -X-X-
		// X---X
		// -XX--
		assertFalse(game.isForbiddenSuicide(StoneColor.WHITE, new Coord(3, 2)));

		goban.setCoordContent(4, 3, new Stone(StoneColor.BLACK));
		// -----
		// --X--
		// -X-X-
		// X---X
		// -XXX-
		assertFalse(game.isForbiddenSuicide(StoneColor.WHITE, new Coord(3, 2)));

		goban.setCoordContent(2, 2, new Stone(StoneColor.WHITE));
		// -----
		// --X--
		// -XOX-
		// X---X
		// -XXX-
		assertFalse(game.isForbiddenSuicide(StoneColor.WHITE, new Coord(3, 2)));

		goban.setCoordContent(3, 1, new Stone(StoneColor.WHITE));
		// -----
		// --X--
		// -XOX-
		// XO--X
		// -XXX-
		assertFalse(game.isForbiddenSuicide(StoneColor.WHITE, new Coord(3, 2)));

		goban.setCoordContent(3, 3, new Stone(StoneColor.WHITE));
		// -----
		// --X--
		// -XOX-
		// XO-OX
		// -XXX-
		assertTrue(game.isForbiddenSuicide(StoneColor.WHITE, new Coord(3, 2)));

		goban.setCoordContent(3, 4, null);
		// -----
		// --X--
		// -XOX-
		// XO-O-
		// -XXX-
		assertFalse(game.isForbiddenSuicide(StoneColor.WHITE, new Coord(3, 2)));
	}

	@Test
	public void testSuicideRuleOnIsolatedSuicide() {
		// -----
		// --X--
		// -X-X-
		// --X--
		// -----
		Goban goban = new Goban(5);
		goban.setCoordContent(1, 2, new Stone(StoneColor.BLACK));
		goban.setCoordContent(2, 1, new Stone(StoneColor.BLACK));
		goban.setCoordContent(2, 3, new Stone(StoneColor.BLACK));
		goban.setCoordContent(3, 2, new Stone(StoneColor.BLACK));

		{
			IPlayer blackPlayer = new DeterminedPlayer();
			IPlayer whitePlayer = new DeterminedPlayer(new Coord(2, 2));
			Game game = Game.createFreeGame(goban.clone(), blackPlayer, whitePlayer);
			game.setSuicideAllowed(true);
			game.setNextPlayerColor(StoneColor.WHITE);
			game.play();
		}

		{
			IPlayer blackPlayer = new DeterminedPlayer();
			IPlayer whitePlayer = new DeterminedPlayer(new Coord(2, 2));
			Game game = Game.createFreeGame(goban.clone(), blackPlayer, whitePlayer);
			game.setSuicideAllowed(false);
			game.setNextPlayerColor(StoneColor.WHITE);
			try {
				game.play();
				fail("No exception thrown.");
			} catch (SuicideException e) {
			}
		}
	}

	@Test
	public void testSuicideRuleOnBlockSuicide() {
		// -----
		// --X--
		// -XOX-
		// XO-OX
		// -XXX-
		Goban goban = new Goban(5);
		goban.setCoordContent(1, 2, new Stone(StoneColor.BLACK));
		goban.setCoordContent(2, 1, new Stone(StoneColor.BLACK));
		goban.setCoordContent(2, 3, new Stone(StoneColor.BLACK));
		goban.setCoordContent(3, 0, new Stone(StoneColor.BLACK));
		goban.setCoordContent(3, 4, new Stone(StoneColor.BLACK));
		goban.setCoordContent(4, 1, new Stone(StoneColor.BLACK));
		goban.setCoordContent(4, 2, new Stone(StoneColor.BLACK));
		goban.setCoordContent(4, 3, new Stone(StoneColor.BLACK));
		goban.setCoordContent(2, 2, new Stone(StoneColor.WHITE));
		goban.setCoordContent(3, 1, new Stone(StoneColor.WHITE));
		goban.setCoordContent(3, 3, new Stone(StoneColor.WHITE));

		{
			IPlayer blackPlayer = new DeterminedPlayer();
			IPlayer whitePlayer = new DeterminedPlayer(new Coord(3, 2));
			Game game = Game.createFreeGame(goban.clone(), blackPlayer, whitePlayer);
			game.setSuicideAllowed(true);
			game.setNextPlayerColor(StoneColor.WHITE);
			game.play();
		}

		{
			IPlayer blackPlayer = new DeterminedPlayer();
			IPlayer whitePlayer = new DeterminedPlayer(new Coord(3, 2));
			Game game = Game.createFreeGame(goban.clone(), blackPlayer, whitePlayer);
			game.setSuicideAllowed(false);
			game.setNextPlayerColor(StoneColor.WHITE);
			try {
				game.play();
				fail("No exception thrown.");
			} catch (SuicideException e) {
			}
		}
	}

	@Test
	public void testKoCheck() {
		Goban goban = new Goban(5);
		IStrategy strategy = new IStrategy() {

			private int step = -1;

			@Override
			public IMove chooseMove(Goban goban, IPlayer player) {
				Game game = Game.getRunningGameOn(goban);
				step++;
				if (step == 0) {
					// -----
					// -----
					// -----
					// -----
					// -----
					assertFalse(game.isForbiddenKo(StoneColor.BLACK, new Coord(1, 1)));
					return new StoneMove(1, 1);
				} else if (step == 1) {
					// -----
					// -X---
					// -----
					// -----
					// -----
					assertFalse(game.isForbiddenKo(StoneColor.WHITE, new Coord(1, 2)));
					return new StoneMove(1, 2);
				} else if (step == 2) {
					// -----
					// -XO--
					// -----
					// -----
					// -----
					assertFalse(game.isForbiddenKo(StoneColor.BLACK, new Coord(2, 0)));
					return new StoneMove(2, 0);
				} else if (step == 3) {
					// -----
					// -XO--
					// X----
					// -----
					// -----
					assertFalse(game.isForbiddenKo(StoneColor.WHITE, new Coord(2, 1)));
					return new StoneMove(2, 1);
				} else if (step == 4) {
					// -----
					// -XO--
					// XO---
					// -----
					// -----
					assertFalse(game.isForbiddenKo(StoneColor.BLACK, new Coord(3, 1)));
					return new StoneMove(3, 1);
				} else if (step == 5) {
					// -----
					// -XO--
					// XO---
					// -X---
					// -----
					assertFalse(game.isForbiddenKo(StoneColor.WHITE, new Coord(3, 2)));
					return new StoneMove(3, 2);
				} else if (step == 6) {
					// -----
					// -XO--
					// XO---
					// -XO--
					// -----
					assertFalse(game.isForbiddenKo(StoneColor.BLACK, new Coord(2, 2)));
					return new StoneMove(2, 2);
				} else if (step == 7) {
					// -----
					// -XO--
					// X-X--
					// -XO--
					// -----
					assertFalse(game.isForbiddenKo(StoneColor.WHITE, new Coord(2, 3)));
					return new StoneMove(2, 3);
				} else if (step == 8) {
					// -----
					// -XO--
					// X-XO-
					// -XO--
					// -----
					assertFalse(game.isForbiddenKo(StoneColor.BLACK, new Coord(1, 0)));
					return new StoneMove(1, 0);
				} else if (step == 9) {
					// -----
					// XXO--
					// X-XO-
					// -XO--
					// -----
					assertFalse(game.isForbiddenKo(StoneColor.WHITE, new Coord(2, 1)));
					return new StoneMove(2, 1);
				} else if (step == 10) {
					// -----
					// XXO--
					// XO-O-
					// -XO--
					// -----
					assertTrue(game.isForbiddenKo(StoneColor.BLACK, new Coord(2, 2)));
					assertFalse(game.isForbiddenKo(StoneColor.BLACK, new Coord(3, 0)));
					return new StoneMove(3, 0);
				} else if (step == 11) {
					// -----
					// XXO--
					// XO-O-
					// XXO--
					// -----
					assertFalse(game.isForbiddenKo(StoneColor.WHITE, new Coord(1, 3)));
					return new StoneMove(1, 3);
				} else if (step == 12) {
					// -----
					// XXO0-
					// XO-O-
					// XXO--
					// -----
					assertFalse(game.isForbiddenKo(StoneColor.BLACK, new Coord(2, 2)));
					return new StoneMove(2, 2);
				} else if (step == 13) {
					// -----
					// XXO0-
					// X-XO-
					// XXO--
					// -----
					assertTrue(game.isForbiddenKo(StoneColor.WHITE, new Coord(2, 1)));
					assertFalse(game.isForbiddenKo(StoneColor.WHITE, new Coord(3, 3)));
					return new StoneMove(3, 3);
				} else if (step == 14) {
					// -----
					// XXO0-
					// X-XO-
					// XXOO-
					// -----
					assertFalse(game.isForbiddenKo(StoneColor.BLACK, new Coord(2, 1)));
					return new StoneMove(2, 1);
				} else {
					// -----
					// XXO0-
					// XXXO-
					// XXOO-
					// -----
					return new PassMove();
				}
			}
		};
		IPlayer blackPlayer = new StrategicalPlayer(strategy);
		IPlayer whitePlayer = new StrategicalPlayer(strategy);
		Game game = new Game(goban, blackPlayer, whitePlayer);
		while (!game.isFinished()) {
			game.play();
		}
		// check tests have been done
		assertEquals(15, game.getHistory().size());
	}

	@Test
	public void testKoRule() {
		// -----
		// -XO--
		// X-XO-
		// -XO--
		// -----
		Goban goban = new Goban(5);
		goban.setCoordContent(1, 1, new Stone(StoneColor.BLACK));
		goban.setCoordContent(2, 0, new Stone(StoneColor.BLACK));
		goban.setCoordContent(2, 2, new Stone(StoneColor.BLACK));
		goban.setCoordContent(3, 1, new Stone(StoneColor.BLACK));
		goban.setCoordContent(1, 2, new Stone(StoneColor.WHITE));
		goban.setCoordContent(2, 3, new Stone(StoneColor.WHITE));
		goban.setCoordContent(3, 2, new Stone(StoneColor.WHITE));

		{
			IPlayer blackPlayer = new DeterminedPlayer(new Coord(2, 2));
			IPlayer whitePlayer = new DeterminedPlayer(new Coord(2, 1));
			Game game = Game.createFreeGame(goban.clone(), blackPlayer, whitePlayer);
			game.setKoAllowed(true);
			game.setNextPlayerColor(StoneColor.WHITE);
			game.play();
			game.play();
		}

		{
			IPlayer blackPlayer = new DeterminedPlayer(new Coord(2, 2));
			IPlayer whitePlayer = new DeterminedPlayer(new Coord(2, 1));
			Game game = Game.createFreeGame(goban.clone(), blackPlayer, whitePlayer);
			game.setKoAllowed(false);
			game.setNextPlayerColor(StoneColor.WHITE);
			game.play();
			try {
				game.play();
				fail("No exception thrown.");
			} catch (KoException e) {
			}
		}
	}

	@Test
	public void testFreeGameGeneration() {
		Goban goban = new Goban(5);
		IPlayer blackPlayer = new DeterminedPlayer();
		IPlayer whitePlayer = new DeterminedPlayer();
		Game game = Game.createFreeGame(goban, blackPlayer, whitePlayer);
		assertEquals(goban, game.getGoban());
		assertEquals(blackPlayer, game.getPlayer(StoneColor.BLACK));
		assertEquals(whitePlayer, game.getPlayer(StoneColor.WHITE));
		assertTrue(game.isKoAllowed());
		assertTrue(game.isSuicideAllowed());
	}
}
