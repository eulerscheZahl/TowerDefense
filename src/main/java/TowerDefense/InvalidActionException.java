package TowerDefense;

import com.codingame.game.Player;

public class InvalidActionException extends Exception {
	private static final long serialVersionUID = -8394144560674068353L;
	private final String message;
	private final boolean gameBreaking;
	private Player player;

	public InvalidActionException(String message, boolean gameBreaking, Player player) {
		this.message = message;
		this.gameBreaking = gameBreaking;
		this.player = player;
	}

	public boolean isGameBreaking() {
		return gameBreaking;
	}

	public Player getPlayer() {
		return player;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
