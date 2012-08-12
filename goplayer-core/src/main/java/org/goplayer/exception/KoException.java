package org.goplayer.exception;

import org.goplayer.go.StoneColor;
import org.goplayer.util.Coord;

@SuppressWarnings("serial")
public class KoException extends RuntimeException {

	public KoException(StoneColor playerColor, Coord coord) {
		super(playerColor + " cannot play " + coord
				+ ": it is forbidden by the ko rule.");
	}
}
