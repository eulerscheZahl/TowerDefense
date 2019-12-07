package view;

import TowerDefense.Constants;
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

		wave = graphics.createText("").setAnchor(0.5).setFillColor(0xffffff).setFontSize(40).setStrokeColor(0x000000).setStrokeThickness(4.0).setX(855 / 2).setY(540);

		String[] attackerBodySprites = graphics.createSpriteSheetSplitter().setSourceImage("att_body.png").setHeight(94).setWidth(100).setImageCount(10).setImagesPerRow(4).setOrigRow(0).setOrigCol(0).setName("b").split();
		String[] attackerHelmetSprites = graphics.createSpriteSheetSplitter().setSourceImage("att_helmet.png").setHeight(94).setWidth(100).setImageCount(10).setImagesPerRow(4).setOrigRow(0).setOrigCol(0).setName("h").split();
		graphics.createSpriteAnimation().setImages(attackerBodySprites).setLoop(true).setPlaying(true).setX(100).setY(440).setScale(2);
		graphics.createSpriteAnimation().setImages(attackerHelmetSprites).setLoop(true).setPlaying(true).setX(100).setY(440).setScale(2);


		boardGroup = graphics.createGroup();
		// TODO: switch gridgroup back to BufferedGroup
		boardGroup.setScale((double) graphics.getWorld().getHeight() / (board.getHeight() * CELL_SIZE));
		boardGroup.setX(graphics.getWorld().getWidth() - graphics.getWorld().getHeight() * (1 + Constants.MAP_HEIGHT) / Constants.MAP_HEIGHT);
		Group gridGroup = graphics.createGroup();
		boardGroup.add(gridGroup);
		Group innerGroup = graphics.createGroup();
		gridGroup.add(innerGroup);

		for (int y = 0; y < board.getHeight(); y++) {
			innerGroup.add(Utils.createBoardSprite(graphics, "canyon.png", -1, y).setAlpha(0.7));
			innerGroup.add(Utils.createBoardSprite(graphics, "canyon.png", board.getWidth(), y).setAlpha(0.7));
			for (int x = 0; x < board.getWidth(); x++) {
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
		String text = board.getWaveInfo();
		if (!text.equals(wave.getText()))
			wave.setText(text);
	}
}
