package org.goplayer.game.strategy;

import org.goplayer.go.Goban;
import org.goplayer.move.IMove;
import org.goplayer.move.PassMove;
import org.goplayer.move.StoneMove;
import org.goplayer.player.IPlayer;

public class FirstFreeStrategy implements IStrategy {

	@Override
	public IMove chooseMove(Goban goban, IPlayer player) {
		for (int row = 0; row < goban.getRowCount(); row++) {
			for (int col = 0; col < goban.getColCount(); col++) {
				if (goban.getCoordContent(row, col) == null) {
					return new StoneMove(row, col);
				}
			}
		}
		return new PassMove();
	}

}
