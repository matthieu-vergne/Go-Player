package org.goplayer.exception;

import org.goplayer.game.IPlayer;

@SuppressWarnings("serial")
public class UnknownPlayerException extends RuntimeException {

	public UnknownPlayerException(IPlayer player) {
		super(player + " is not a known player");
	}
}
