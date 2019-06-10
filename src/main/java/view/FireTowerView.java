package view;

import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;

import TowerDefense.Attacker;
import TowerDefense.Tower;
import com.codingame.gameengine.module.tooltip.TooltipModule;

public class FireTowerView extends TowerView {

	public FireTowerView(Tower tower, Group boardGroup, GraphicEntityModule graphics, TooltipModule tooltips) {
		super(tower, boardGroup, graphics, tooltips);
		towerSprite = Utils.createTowerSprite(graphics, "fireTower.png", tower.getTile().getX(), tower.getTile().getY());
		attackSprite = graphics.createSprite().setImage("fireTower.png").setAlpha(0).setScale(3);
		attackSprite.setX(BoardView.CELL_SIZE * tower.getTile().getX() - BoardView.CELL_SIZE);
		attackSprite.setY(BoardView.CELL_SIZE * tower.getTile().getY() - BoardView.CELL_SIZE);
        tooltips.setTooltipText(towerSprite, tower.getTooltipString());
	}

	public void attack(Attacker a) {
		attackSprite.setAlpha(1);
		graphics.commitEntityState(0, attackSprite);
		attackSprite.setAlpha(0);
		attackLine.setAlpha(0);
	}
}
