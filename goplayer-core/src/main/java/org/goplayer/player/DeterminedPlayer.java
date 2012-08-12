package org.goplayer.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.goplayer.go.Goban;
import org.goplayer.move.IMove;
import org.goplayer.move.PassMove;
import org.goplayer.move.StoneMove;
import org.goplayer.util.Coord;

public class DeterminedPlayer implements IPlayer {

	private final List<Coord> remainingSteps;

	public DeterminedPlayer(Collection<Coord> steps) {
		this.remainingSteps = new ArrayList<Coord>(steps);
	}

	public DeterminedPlayer(Coord... steps) {
		this(Arrays.asList(steps));
	}

	@Override
	public IMove play(Goban goban) {
		if (remainingSteps.isEmpty()) {
			return new PassMove();
		} else {
			Coord coord = remainingSteps.remove(0);
			return coord == null ? new PassMove() : new StoneMove(coord);
		}
	}

	public List<Coord> getRemainingSteps() {
		return remainingSteps;
	}

	@Override
	public String toString() {
		return getClass() + remainingSteps.toString();
	}
}
