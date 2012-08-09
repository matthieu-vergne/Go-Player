package org.goplayer.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.goplayer.exception.UnknownPlayerException;
import org.goplayer.go.Goban;
import org.goplayer.go.Stone;
import org.goplayer.move.AbandonMove;
import org.goplayer.move.IMove;
import org.goplayer.move.PassMove;
import org.goplayer.move.StoneMove;

// TODO manage prisoners removing
// TODO manage ko rule
public class Game {

	private final Goban goban;
	private final Map<PlayerColor, IPlayer> players = new HashMap<PlayerColor, IPlayer>();
	private final Map<PlayerColor, Integer> prisoners = new HashMap<PlayerColor, Integer>();
	private PlayerColor nextPlayerColor = PlayerColor.BLACK;
	private boolean previousHasPassed = false;
	private IPlayer winner = null;

	public Game(Goban goban, IPlayer blackPlayer, IPlayer whitePlayer) {
		this.goban = goban;
		players.put(PlayerColor.BLACK, blackPlayer);
		players.put(PlayerColor.WHITE, whitePlayer);
		prisoners.put(PlayerColor.BLACK, 0);
		prisoners.put(PlayerColor.WHITE, 0);
	}

	public Goban getGoban() {
		return goban;
	}

	public IPlayer getPlayer(PlayerColor color) {
		return players.get(color);
	}

	public int getPrisoners(PlayerColor color) {
		return prisoners.get(color);
	}

	public PlayerColor getPlayerColor(final IPlayer player) {
		for (PlayerColor color : PlayerColor.values()) {
			final IPlayer p1 = players.get(color);
			if (p1 == player) {
				return color;
			} else {
				continue;
			}
		}
		throw new UnknownPlayerException(player);
	}

	public PlayerColor getNextPlayerColor() {
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
				int row = stoneMove.getRow();
				int col = stoneMove.getCol();
				if (getGoban().getCoordContent(row, col) != null) {
					throw new RuntimeException(player + " cannot play " + move
							+ ", the place is not free");
				} else {
					getGoban().setCoordContent(row, col,
							new Stone(nextPlayerColor));
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
			nextPlayerColor = nextPlayerColor == PlayerColor.BLACK ? PlayerColor.WHITE
					: PlayerColor.BLACK;
		}
	}

	private IPlayer computeWinner() {
		// TODO compute winner using user feedback
		return players.get(PlayerColor.BLACK);
	}

	public IPlayer getWinner() {
		return winner;
	}
}
