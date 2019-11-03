package view;

import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.tooltip.TooltipModule;

import TowerDefense.Attacker;
import TowerDefense.Tower;

public class GunTowerView extends TowerView {

	public GunTowerView(Tower tower, Group boardGroup, GraphicEntityModule graphics, TooltipModule tooltips) {
		super(tower, boardGroup, graphics, tooltips, "gunTower");
		attackSprite = graphics.createSprite().setImage("gunTowerAttack.png").setAlpha(0);
		attackLine = graphics.createLine();
		attackLine.setX((int) (BoardView.CELL_SIZE * (tower.getTile().getX() + 0.5)));
		attackLine.setY((int) (BoardView.CELL_SIZE * (tower.getTile().getY() + 0.5)));
		attackLine.setLineColor(0xff0000).setAlpha(0);
		attackLine.setLineWidth(5);
		commitSprites();
		updateTooltip();
	}

	@Override
	public void attack(Attacker a) {
		attackSprite.setAlpha(1);
		attackSprite.setX((int) (BoardView.CELL_SIZE * a.getLocation().getX()));
		attackSprite.setY((int) (BoardView.CELL_SIZE * a.getLocation().getY()));
		attackLine.setAlpha(1);
		attackLine.setX2((int) (BoardView.CELL_SIZE * (a.getLocation().getX() + 0.5)));
		attackLine.setY2((int) (BoardView.CELL_SIZE * (a.getLocation().getY() + 0.5)));

		graphics.commitEntityState(0, attackSprite, attackLine);
		attackSprite.setAlpha(0);
		attackLine.setAlpha(0);
	}
}
