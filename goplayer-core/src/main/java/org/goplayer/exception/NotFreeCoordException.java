package org.goplayer.exception;

import org.goplayer.go.StoneColor;
import org.goplayer.util.Coord;

@SuppressWarnings("serial")
public class NotFreeCoordException extends RuntimeException {

	public NotFreeCoordException(StoneColor playerColor, Coord coord) {
		super(playerColor + " cannot play " + coord + ": it is not free.");
	}
}
