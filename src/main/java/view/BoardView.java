package view;

import com.codingame.gameengine.module.entities.BufferedGroup;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.entities.Sprite;

import TowerDefense.Attacker;
import TowerDefense.Board;
import TowerDefense.Tower;

public class BoardView {
	public static final int CELL_SIZE = 100;
	private Board board;
	private GraphicEntityModule graphics;
	private Group boardGroup;

	public BoardView(Board board, GraphicEntityModule graphics) {
		this.board = board;
		board.setView(this);
		this.graphics = graphics;

		boardGroup = graphics.createGroup();
		//boardGroup.setScale(1080.0 / (board.getHeight() * CELL_SIZE));
		BufferedGroup gridGroup = graphics.createBufferedGroup();
		boardGroup.add(gridGroup);
		Group innerGroup = graphics.createGroup();
		gridGroup.add(innerGroup);

		for (int x = 0; x < board.getWidth(); x++) {
			for (int y = 0; y < board.getHeight(); y++) {
				if (board.getGrid()[x][y].canBuild()) {
					Sprite plateau = Utils.createBoardSprite(graphics, "plateau.png", x, y);
					innerGroup.add(plateau);
				} else if (board.getGrid()[x][y].canEnter()) {
					Sprite canyon = Utils.createBoardSprite(graphics, "canyon.png", x, y);
					innerGroup.add(canyon);
				}
			}
		}
	}

	public void addAttacker(Attacker attacker) {
		AttackerView view = new AttackerView(attacker, boardGroup, graphics);
	}

	public void addTower(Tower tower) {
		TowerView view = tower.createView(boardGroup, graphics);
	}
}
