package org.goplayer.game;

import org.goplayer.game.strategy.IStrategy;
import org.goplayer.go.Goban;
import org.goplayer.move.IMove;

public class StrategicalPlayer implements IPlayer {
	private IStrategy strategy = null;
	
	public StrategicalPlayer(IStrategy initialStrategy) {
		setStrategy(initialStrategy);
	}

	@Override
	public IMove play(Goban goban) {
		return getStrategy().chooseMove(goban, this);
	}


	public IStrategy getStrategy() {
		return strategy;
	}


	public void setStrategy(IStrategy strategy) {
		this.strategy = strategy;
	}

}
