package org.goplayer.game.strategy;

import org.goplayer.go.Goban;
import org.goplayer.move.IMove;
import org.goplayer.player.IPlayer;

public interface IStrategy {

	public IMove chooseMove(Goban goban, IPlayer player);
}
