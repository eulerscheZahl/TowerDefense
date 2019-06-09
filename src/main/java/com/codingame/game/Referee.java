package com.codingame.game;

import java.util.Locale;
import java.util.Random;

import com.codingame.gameengine.core.AbstractPlayer.TimeoutException;
import com.codingame.gameengine.core.AbstractReferee;
import com.codingame.gameengine.core.SoloGameManager;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.google.inject.Inject;

import TowerDefense.Board;
import view.BoardView;

public class Referee extends AbstractReferee {
	public static final int FRAME_DURATION = 500;
	public static final int GAME_TURNS = 200;
	public static final Random random = new Random();

	@Inject
	private SoloGameManager<Player> gameManager;
	@Inject
	private GraphicEntityModule graphicEntityModule;

	private Board board;

	@Override
	public void init() {
		Locale.setDefault(new Locale("en", "US"));
		String input = gameManager.getTestCaseInput().get(0);
		gameManager.setMaxTurns(GAME_TURNS);
		board = new Board(input);
		board.setPlayer(gameManager.getPlayer());

		BoardView view = new BoardView(board, graphicEntityModule);
	}

	@Override
	public void gameTurn(int turn) {
		Player player = gameManager.getPlayer();
		for (String input : board.getPlayerInput()) {
			player.sendInputLine(input);
		}
		player.execute();

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
					board.build(x, y, type);
				}
			}
			board.moveAttackers(turn);
			board.fireTowers();
		} catch (TimeoutException e) {
			//player.deactivate(String.format("$%d timeout!", player.getIndex()));
		}
	}
}
