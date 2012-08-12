package org.goplayer.exception;

import org.goplayer.go.StoneColor;
import org.goplayer.util.Coord;

@SuppressWarnings("serial")
public class SuicideException extends RuntimeException {

	public SuicideException(StoneColor playerColor, Coord coord) {
		super(playerColor + " cannot play " + coord + ": it is a suicide.");
	}
}
