package org.goplayer.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.goplayer.exception.UnknownPlayerException;
import org.goplayer.go.Goban;
import org.goplayer.go.Stone;
import org.goplayer.go.StoneColor;
import org.goplayer.move.AbandonMove;
import org.goplayer.move.IMove;
import org.goplayer.move.PassMove;
import org.goplayer.move.StoneMove;
import org.goplayer.player.IPlayer;
import org.goplayer.util.Coord;
import org.goplayer.util.MoveHistory;

// TODO manage prisoners removing
// TODO manage ko rule
public class Game {

	private final Goban goban;
	private final Map<StoneColor, IPlayer> players = new HashMap<StoneColor, IPlayer>();
	private final Map<StoneColor, Integer> lostStones = new HashMap<StoneColor, Integer>();
	private StoneColor nextPlayerColor = StoneColor.BLACK;
	private boolean previousHasPassed = false;
	private IPlayer winner = null;
	private final MoveHistory history = new MoveHistory();

	public Game(Goban goban, IPlayer blackPlayer, IPlayer whitePlayer) {
		if (getRunningGameOn(goban) != null) {
			throw new IllegalArgumentException(
					"The given goban is already in a running game.");
		} else {
			this.goban = goban;
			players.put(StoneColor.BLACK, blackPlayer);
			players.put(StoneColor.WHITE, whitePlayer);
			lostStones.put(StoneColor.BLACK, 0);
			lostStones.put(StoneColor.WHITE, 0);
			games.add(this);
		}
	}

	public Goban getGoban() {
		return goban;
	}

	public IPlayer getPlayer(StoneColor color) {
		return players.get(color);
	}

	public int getLostStonesCount(StoneColor color) {
		return lostStones.get(color);
	}

	public StoneColor getPlayerColor(final IPlayer player) {
		for (StoneColor color : StoneColor.values()) {
			final IPlayer p1 = players.get(color);
			if (p1 == player) {
				return color;
			} else {
				continue;
			}
		}
		throw new UnknownPlayerException(player);
	}

	public StoneColor getNextPlayerColor() {
		return nextPlayerColor;
	}

	public IPlayer getNextPlayer() {
		return players.get(getNextPlayerColor());
	}

	public void finish(IPlayer winner) {
		if (players.containsValue(winner)) {
			this.winner = winner;
		} else {
			throw new UnknownPlayerException(winner);
		}
	}

	public boolean isFinished() {
		return winner != null;
	}

	public void play() {
		if (isFinished()) {
			throw new RuntimeException("The game is finished, nobody can play");
		} else {
			IPlayer player = getNextPlayer();
			IMove move = player.play(getGoban());
			if (move instanceof StoneMove) {
				StoneMove stoneMove = (StoneMove) move;
				Coord coord = stoneMove.getCoord();
				if (getGoban().getCoordContent(coord) != null) {
					throw new RuntimeException(player + " cannot play " + move
							+ ", the place is not free");
				} else {
					Stone stone = new Stone(nextPlayerColor);
					getGoban().setCoordContent(coord, stone);
					history.add(coord, stone);
				}
				previousHasPassed = false;
			} else if (move instanceof PassMove) {
				if (previousHasPassed) {
					finish(computeWinner());
				} else {
					previousHasPassed = true;
				}
			} else if (move instanceof AbandonMove) {
				List<IPlayer> remaining = new ArrayList<IPlayer>(
						players.values());
				remaining.remove(player);
				finish(remaining.get(0));
			} else {
				throw new RuntimeException("Not managed case: "
						+ move.getClass());
			}
			nextPlayerColor = nextPlayerColor == StoneColor.BLACK ? StoneColor.WHITE
					: StoneColor.BLACK;
		}
	}

	private IPlayer computeWinner() {
		// TODO compute winner using user feedback
		return players.get(StoneColor.BLACK);
	}

	public IPlayer getWinner() {
		return winner;
	}

	public MoveHistory getHistory() {
		return history.clone();
	}

	private static final Set<Game> games = new HashSet<Game>();

	public static Set<Game> getAllGames() {
		return new HashSet<Game>(games);
	}

	public static Set<Game> getAllRunningGames() {
		HashSet<Game> games = new HashSet<Game>();
		for (Game game : games) {
			if (!game.isFinished()) {
				games.add(game);
			} else {
				continue;
			}
		}
		return games;
	}

	public static Set<Game> getAllFinishedGames() {
		HashSet<Game> games = new HashSet<Game>();
		for (Game game : games) {
			if (game.isFinished()) {
				games.add(game);
			} else {
				continue;
			}
		}
		return games;
	}

	public static Set<Game> getAllGamesOn(Goban goban) {
		HashSet<Game> games = new HashSet<Game>();
		for (Game game : games) {
			if (game.getGoban() == goban) {
				games.add(game);
			} else {
				continue;
			}
		}
		return games;
	}

	public static Game getRunningGameOn(Goban goban) {
		for (Game game : games) {
			if (game.getGoban() == goban && !game.isFinished()) {
				return game;
			} else {
				continue;
			}
		}
		return null;
	}
}
