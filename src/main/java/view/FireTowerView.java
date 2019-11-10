package view;

import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.tooltip.TooltipModule;

import TowerDefense.Attacker;
import TowerDefense.Tower;
import TowerDefense.TowerProperty;

public class FireTowerView extends TowerView {

	public FireTowerView(Tower tower, Group boardGroup, GraphicEntityModule graphics, TooltipModule tooltips) {
		super(tower, boardGroup, graphics, tooltips, "fireTower");
		towerFixedSprite = Utils.createTowerSprite(graphics, "fireTowerFixed" + upgradeLevel + ".png", tower.getTile().getX(), tower.getTile().getY());
		attackSprite = graphics.createSprite().setImage("fireTowerAttack.png").setAlpha(0).setScale(tower.getProperty(TowerProperty.RANGE));
		attackSprite.setAnchor(0.5).setZIndex(-1);
		attackSprite.setX(BoardView.CELL_SIZE * tower.getTile().getX() + BoardView.CELL_SIZE / 2);
		attackSprite.setY(BoardView.CELL_SIZE * tower.getTile().getY() + BoardView.CELL_SIZE / 2);
		commitSprites();
		updateTooltip();
	}

	@Override
	public void attack(Attacker a) {
		attackSprite.setAlpha(1);
		graphics.commitEntityState(0, attackSprite);
		attackSprite.setAlpha(0);
	}
	
	@Override
	public void upgrade() {
		super.upgrade();
		attackSprite.setScale(tower.getProperty(TowerProperty.RANGE));
	}
}
