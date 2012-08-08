package org.goplayer.game;

import org.goplayer.go.Goban;
import org.goplayer.move.IMove;

public interface IPlayer {
	
	public IMove play(Goban goban);
}
