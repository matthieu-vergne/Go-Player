package org.goplayer;

public class Goban {

	private int width = 19;
	private int height = 19;
	private PlayerColor[][] field = null;
	private PlayerColor[][] previousField;
	private boolean currentPlayerIsBlack = true;
	private boolean isPlaying = false;
	private Player whitePlayer;
	private Player blackPlayer;

	public Goban() {
		// nothing to do
	}

	public void initField() {
		field = new PlayerColor[width][height];
	}

	public boolean isInitialized() {
		return field != null;
	}

	public void placeHandicap(PlayerColor color, int x, int y) {
		if (field[x][y] != null) {
			throw new IllegalArgumentException("Another ishi is on (" + x + ","
					+ y + ").");
		} else if (isPlaying) {
			throw new IllegalStateException(
					"The game is running, you cannot place handicap during a party.");
		} else {
			field[x][y] = color;
		}
	}

	public int placeIshiFor(Player player, int x, int y) {
		if (field[x][y] != null) {
			throw new IllegalArgumentException("(" + x + "," + y
					+ ") is already taken by " + field[x][y] + ".");
		} else if (player != getCurrentPlayer()) {
			throw new IllegalArgumentException(player
					+ " is not the current player (" + getCurrentPlayer() + ")");
		} else {
			PlayerColor[][] newField = copyField();
			newField[x][y] = getColorOf(player);
			int killed = 0;// TODO count killed pieces
			// TODO eliminate killed pieces
			if (areSameFields(previousField, newField)) {
				throw new IllegalArgumentException(
						"The Ko rule forbid this movement : you go back to the previous state.");
			} else {
				previousField = field;
				field = newField;
				currentPlayerIsBlack = !currentPlayerIsBlack;
				return killed;
			}
		}
	}

	public PlayerColor getColorOf(Player player) {
		return player == blackPlayer ? PlayerColor.BLACK
				: player == whitePlayer ? PlayerColor.WHITE : null;
	}

	public PlayerColor[][] copyField() {
		PlayerColor[][] copy = new PlayerColor[width][height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				copy[x][y] = field[x][y];
			}
		}
		return copy;
	}

	private boolean areSameFields(PlayerColor[][] field1, PlayerColor[][] field2) {
		if (field1.length != field2.length) {
			return false;
		} else if (field1[0].length != field2[0].length) {
			return false;
		} else {
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					if (field1[x][y] != field2[x][y]) {
						return false;
					}
				}
			}
		}
		return true;
	}

	public PlayerColor[][] getField() {
		return field;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		if (isInitialized()) {
			throw new IllegalStateException(
					"The width cannot be changed after the goban has been initialized.");
		} else {
			this.width = width;
		}
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		if (isInitialized()) {
			throw new IllegalStateException(
					"The height cannot be changed after the goban has been initialized.");
		} else {
			this.height = height;
		}
	}

	public PlayerColor[][] getPreviousField() {
		return previousField;
	}

	public Player getCurrentPlayer() {
		return currentPlayerIsBlack ? blackPlayer : whitePlayer;
	}

	public boolean isPlaying() {
		return isPlaying;
	}

	public void startPlaying() {
		if (isPlaying) {
			throw new IllegalStateException("The game is already running.");
		} else {
			isPlaying = true;
		}
	}

	public void stopPlaying() {
		if (!isPlaying) {
			throw new IllegalStateException("The game is not running.");
		} else {
			isPlaying = false;
		}
	}

	public Player getWhitePlayer() {
		return whitePlayer;
	}

	public void setWhitePlayer(Player player) {
		if (whitePlayer != null) {
			throw new IllegalStateException(
					"The white player is already decided.");
		} else {
			this.whitePlayer = player;
		}
	}

	public Player getBlackPlayer() {
		return blackPlayer;
	}

	public void setBlackPlayer(Player player) {
		if (blackPlayer != null) {
			throw new IllegalStateException(
					"The black player is already decided.");
		} else {
			this.blackPlayer = player;
		}
	}
}
