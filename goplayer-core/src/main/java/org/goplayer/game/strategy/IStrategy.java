package org.goplayer.game.strategy;

import org.goplayer.game.IPlayer;
import org.goplayer.go.Goban;
import org.goplayer.move.IMove;

public interface IStrategy {

	public IMove chooseMove(Goban goban, IPlayer player);
}
