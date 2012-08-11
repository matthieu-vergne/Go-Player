package org.goplayer.util;

public class ScoreCouple {
	private final int black;
	private final int white;

	public ScoreCouple(int black, int white) {
		this.black = black;
		this.white = white;
	}

	public int getBlack() {
		return black;
	}

	public int getWhite() {
		return white;
	}

	@Override
	public String toString() {
		return "(B:" + black + ",W:" + white + ")";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (obj instanceof ScoreCouple) {
			ScoreCouple s = (ScoreCouple) obj;
			return black == s.black && white == s.white;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return black * 3779 + white * 3881;
	}
}
