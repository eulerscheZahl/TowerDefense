package com.codingame.game;

import java.util.Locale;
import java.util.Random;

import com.codingame.gameengine.core.AbstractPlayer.TimeoutException;
import com.codingame.gameengine.core.AbstractReferee;
import com.codingame.gameengine.core.MultiplayerGameManager;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.tooltip.TooltipModule;
import com.google.inject.Inject;

import TowerDefense.Board;
import TowerDefense.Constants;
import TowerDefense.InvalidActionException;
import TowerDefense.MapGenerator;
import TowerDefense.Tile;
import view.BoardView;

public class Referee extends AbstractReferee {
	public static final int FRAME_DURATION = 500;
	public static final Random random = new Random();

	@Inject
	private MultiplayerGameManager<Player> gameManager;
	@Inject
	private GraphicEntityModule graphicEntityModule;
	@Inject
	private TooltipModule tooltipModule;

	private Board board;

	@Override
	public void init() {
		Locale.setDefault(new Locale("en", "US"));
		Random random = new Random(gameManager.getSeed());
		Tile[][] grid = MapGenerator.generateMap(random);
		gameManager.setMaxTurns(Constants.TURN_COUNT);
		board = new Board(grid, gameManager.getPlayers(), random);

		BoardView view = new BoardView(board, graphicEntityModule, tooltipModule);
	}

	@Override
	public void gameTurn(int turn) {
		for (Player player : gameManager.getActivePlayers()) {
			for (String line : board.getPlayerInput(player, turn == 1))
				player.sendInputLine(line);
			player.execute();
		}

		for (Player player : gameManager.getActivePlayers()) {
			try {
				String actions = player.getOutputs().get(0);
				for (String action : actions.split(";")) {
					try {
						String[] parts = action.trim().split(" ");
						if (parts.length == 0)
							continue;
						if (parts[0].equals("PASS"))
							continue;
						if (parts[0].equals("BUILD")) {
							if (parts.length != 4)
								throw new InvalidActionException("wrong amount of arguments for BUILD", true, player);
							int x = Integer.parseInt(parts[1]);
							int y = Integer.parseInt(parts[2]);
							String type = parts[3];
							board.cacheBuild(player, x, y, type);
						}
						if (parts[0].equals("UPGRADE")) {
							if (parts.length != 3)
								throw new InvalidActionException("wrong amount of arguments for UPGRADE", true, player);
							int id = Integer.parseInt(parts[1]);
							String type = parts[2];
							board.upgrade(player, id, type); // upgrade before build => can't build and upgrade in the same turn
						}
					} catch (InvalidActionException ex) {
						if (ex.isGameBreaking()) {
							ex.getPlayer().deactivate(ex.getPlayer().getNicknameToken() + ": " + ex.getMessage());
						} else {
							gameManager.addToGameSummary(ex.getPlayer().getNicknameToken() + ": " + ex.getMessage());
						}
					}
				}
			} catch (TimeoutException e) {
				player.deactivate(String.format("$%d timeout!", player.getIndex()));
			}
		}
		while (true) {
			try {
				if (!board.executeBuilds())
					break;
			} catch (InvalidActionException ex) {
				if (ex.isGameBreaking()) {
					ex.getPlayer().deactivate(ex.getPlayer().getNicknameToken() + ": " + ex.getMessage());
				} else {
					gameManager.addToGameSummary(ex.getPlayer().getNicknameToken() + ": " + ex.getMessage());
				}
			}
		}
		board.fireTowers();
		board.moveAttackers(turn);

		for (Player player : gameManager.getActivePlayers()) {
			player.setScore(player.getScorePoints());
			if (player.isDead())
				player.deactivate(player.getNicknameToken() + ": no lives left");
		}
		if (gameManager.getActivePlayers().size() < 2)
			gameManager.endGame();
	}
}
