package org.goplayer.game.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.goplayer.go.Goban;
import org.goplayer.move.IMove;
import org.goplayer.move.PassMove;
import org.goplayer.move.StoneMove;
import org.goplayer.player.IPlayer;
import org.goplayer.util.Coord;

public class RandomStrategy implements IStrategy {

	private final Random rand = new Random();

	@Override
	public IMove chooseMove(Goban goban, IPlayer player) {
		List<Coord> freePlaces = new ArrayList<Coord>();
		for (int row = 0; row < goban.getRowCount(); row++) {
			for (int col = 0; col < goban.getColCount(); col++) {
				if (goban.getCoordContent(row, col) == null) {
					freePlaces.add(new Coord(row, col));
				}
			}
		}
		if (freePlaces.isEmpty()) {
			return new PassMove();
		} else {
			Coord coord = freePlaces.get(rand.nextInt(freePlaces.size()));
			return new StoneMove(coord.getRow(), coord.getCol());
		}
	}

}
