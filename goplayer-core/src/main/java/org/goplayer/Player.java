package org.goplayer;

//TODO implement a way to stop playing by agreeing
//TODO implement a way to stop playing by abandon
public class Player {

	private Goban goban;

	public Player() {
		// nothing to do
	}

	public PlayerColor getColor() {
		return goban == null ? null : goban.getColorOf(this);
	}

	public Goban getGoban() {
		return goban;
	}

	public void setGoban(Goban goban) {
		this.goban = goban;
	}

	public void play() {
		// TODO implement the round
	}

}
