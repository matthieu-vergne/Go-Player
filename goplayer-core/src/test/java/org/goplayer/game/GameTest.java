package org.goplayer.game;

import static org.junit.Assert.*;

import org.goplayer.exception.UnknownPlayerException;
import org.goplayer.go.Goban;
import org.goplayer.go.StoneColor;
import org.goplayer.move.AbandonMove;
import org.goplayer.move.IMove;
import org.goplayer.move.StoneMove;
import org.goplayer.player.IPlayer;
import org.junit.Test;

public class GameTest {

	@Test
	public void testInitGobanAndPlayers() {
		Goban goban = new Goban(9);
		IPlayer blackPlayer = new TestPlayer();
		IPlayer whitePlayer = new TestPlayer();
		Game game = new Game(goban, blackPlayer, whitePlayer);
		assertEquals(goban, game.getGoban());
		assertEquals(blackPlayer, game.getPlayer(StoneColor.BLACK));
		assertEquals(whitePlayer, game.getPlayer(StoneColor.WHITE));
	}

	@Test
	public void testPlayerColor() {
		Goban goban = new Goban(9);
		IPlayer blackPlayer = new TestPlayer();
		IPlayer whitePlayer = new TestPlayer();
		Game game = new Game(goban, blackPlayer, whitePlayer);
		assertEquals(goban, game.getGoban());
		assertEquals(StoneColor.BLACK, game.getPlayerColor(blackPlayer));
		assertEquals(StoneColor.WHITE, game.getPlayerColor(whitePlayer));
		try {
			game.getPlayerColor(new TestPlayer());
			fail("No exception thrown for unknown player");
		} catch (UnknownPlayerException e) {
		}
	}

	@Test
	public void testWinnerOnFinish() {
		Goban goban = new Goban(9);
		IPlayer blackPlayer = new TestPlayer();
		IPlayer whitePlayer = new TestPlayer();
		Game game = new Game(goban, blackPlayer, whitePlayer);
		game.finish(blackPlayer);
		assertEquals(blackPlayer, game.getWinner());
		game.finish(whitePlayer);
		assertEquals(whitePlayer, game.getWinner());
		try {
			game.finish(new TestPlayer());
			fail("No exception thrown for unknown player");
		} catch (UnknownPlayerException e) {
		}
	}

	@Test
	public void testIsFinished() {
		Goban goban = new Goban(9);
		IPlayer blackPlayer = new TestPlayer();
		IPlayer whitePlayer = new TestPlayer();
		Game game = new Game(goban, blackPlayer, whitePlayer);
		assertFalse(game.isFinished());
		game.finish(blackPlayer);
		assertTrue(game.isFinished());
	}

	@Test
	public void testPlayTurns() {
		Goban goban = new Goban(9);
		final IPlayer[] lastPlayer = new IPlayer[1];
		IPlayer blackPlayer = new TestPlayer() {
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
		IPlayer whitePlayer = new TestPlayer() {
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

	static class TestPlayer implements IPlayer {
		@Override
		public IMove play(Goban goban) {
			for (int row = 0; row < goban.getRowCount(); row++) {
				for (int col = 0; col < goban.getColCount(); col++) {
					if (goban.getCoordContent(row, col) == null) {
						return new StoneMove(row, col);
					} else {
						continue;
					}
				}
			}
			return new AbandonMove();
		}
	}
}
