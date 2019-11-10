package view;

import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.entities.Sprite;
import com.codingame.gameengine.module.entities.Text;
import com.codingame.gameengine.module.tooltip.TooltipModule;

import TowerDefense.Attacker;
import TowerDefense.Board;
import TowerDefense.Tower;

public class BoardView {
	public static final int CELL_SIZE = 100;
	private Board board;
	private GraphicEntityModule graphics;
	private Group boardGroup;
	private TooltipModule tooltips;
	private Text wave;

	public BoardView(Board board, GraphicEntityModule graphics, TooltipModule tooltips) {
		this.board = board;
		board.setView(this);
		this.graphics = graphics;
		this.tooltips = tooltips;

		wave = graphics.createText("").setAnchor(0.5).setFillColor(0xffffff).setFontSize(40).setStrokeColor(0x000000).setStrokeThickness(4.0).setX(855 / 2).setY(1080 / 2);

		boardGroup = graphics.createGroup();
		// TODO: switch gridgroup back to BufferedGroup
		boardGroup.setScale(1080.0 / (board.getHeight() * CELL_SIZE));
		boardGroup.setX(1920 - 1080);
		Group gridGroup = graphics.createGroup();
		boardGroup.add(gridGroup);
		Group innerGroup = graphics.createGroup();
		gridGroup.add(innerGroup);

		for (int x = 0; x < board.getWidth(); x++) {
			for (int y = 0; y < board.getHeight(); y++) {
				if (board.getGrid()[x][y].canBuild()) {
					Sprite plateau = Utils.createBoardSprite(graphics, "plateau.png", x, y);
					tooltips.setTooltipText(plateau, "x: " + x + "\ny: " + y);
					innerGroup.add(plateau);
				} else if (board.getGrid()[x][y].canEnter()) {
					Sprite canyon = Utils.createBoardSprite(graphics, "canyon.png", x, y).setZIndex(-1);
					tooltips.setTooltipText(canyon, "x: " + x + "\ny: " + y);
					boardGroup.add(canyon);
					if (x == 0) {
						Sprite headquarter = Utils.createBoardSprite(graphics, "headquarter.png", x, y);
						headquarter.setTint(board.getPlayer(0).getColor());
						innerGroup.add(headquarter);
					}
					if (x == board.getWidth() - 1) {
						Sprite headquarter = Utils.createBoardSprite(graphics, "headquarter.png", x, y);
						headquarter.setTint(board.getPlayer(1).getColor());
						innerGroup.add(headquarter);
					}
				}
			}
		}
	}

	public void addAttacker(Attacker attacker) {
		AttackerView view = new AttackerView(attacker, boardGroup, graphics, tooltips);
	}

	public void addTower(Tower tower) {
		TowerView view = tower.createView(boardGroup, graphics, tooltips);
	}

	public void updateView() {
		wave.setText("Wave " + board.getWaveNumber());
	}
}
