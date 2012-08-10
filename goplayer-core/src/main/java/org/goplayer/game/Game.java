package org.goplayer.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.goplayer.exception.UnknownPlayerException;
import org.goplayer.go.Goban;
import org.goplayer.go.Stone;
import org.goplayer.go.StoneColor;
import org.goplayer.move.AbandonMove;
import org.goplayer.move.IMove;
import org.goplayer.move.PassMove;
import org.goplayer.move.StoneMove;
import org.goplayer.player.IPlayer;

// TODO manage prisoners removing
// TODO manage ko rule
public class Game {

	private final Goban goban;
	private final Map<StoneColor, IPlayer> players = new HashMap<StoneColor, IPlayer>();
	private final Map<StoneColor, Integer> prisoners = new HashMap<StoneColor, Integer>();
	private StoneColor nextPlayerColor = StoneColor.BLACK;
	private boolean previousHasPassed = false;
	private IPlayer winner = null;

	public Game(Goban goban, IPlayer blackPlayer, IPlayer whitePlayer) {
		this.goban = goban;
		players.put(StoneColor.BLACK, blackPlayer);
		players.put(StoneColor.WHITE, whitePlayer);
		prisoners.put(StoneColor.BLACK, 0);
		prisoners.put(StoneColor.WHITE, 0);
	}

	public Goban getGoban() {
		return goban;
	}

	public IPlayer getPlayer(StoneColor color) {
		return players.get(color);
	}

	public int getPrisoners(StoneColor color) {
		return prisoners.get(color);
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
}
