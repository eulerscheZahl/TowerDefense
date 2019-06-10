package com.codingame.game;

import TowerDefense.Board;
import TowerDefense.MapGenerator;
import com.codingame.gameengine.core.AbstractPlayer.TimeoutException;
import com.codingame.gameengine.core.AbstractReferee;
import com.codingame.gameengine.core.MultiplayerGameManager;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.tooltip.TooltipModule;
import com.google.inject.Inject;
import view.BoardView;

import java.util.Locale;
import java.util.Random;

public class Referee extends AbstractReferee {
	public static final int FRAME_DURATION = 500;
	public static final int GAME_TURNS = 50;
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
		String input = MapGenerator.generateMap(random);
		gameManager.setMaxTurns(GAME_TURNS);
		board = new Board(input, gameManager.getPlayers(), random);

		BoardView view = new BoardView(board, graphicEntityModule, tooltipModule);
	}

	@Override
	public void gameTurn(int turn) {
		for (Player player : gameManager.getActivePlayers()) {
			for (String line : board.getPlayerInput(player, turn == 0))
				player.sendInputLine(line);
			player.execute();
		}

		for (Player player : gameManager.getActivePlayers()) {
			try {
				String actions = player.getOutputs().get(0);
				for (String action : actions.split(";")) {
					String[] parts = action.trim().split(" ");
					if (parts.length == 0)
						continue;
					if (parts[0].equals("PASS"))
						continue;
					if (parts[0].equals("BUILD")) {
						if (parts.length != 4)
							continue; // TODO
						int x = Integer.parseInt(parts[1]);
						int y = Integer.parseInt(parts[2]);
						String type = parts[3];
						board.cacheBuild(player, x, y, type);
					}
				}
			} catch (TimeoutException e) {
				//player.deactivate(String.format("$%d timeout!", player.getIndex()));
			}
		}
		board.executeBuilds();
		board.moveAttackers(turn);
		board.fireTowers();
	}
}
