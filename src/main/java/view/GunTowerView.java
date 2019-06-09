package view;

import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;

import TowerDefense.Attacker;
import TowerDefense.Tower;

public class GunTowerView extends TowerView {

	public GunTowerView(Tower tower, Group boardGroup, GraphicEntityModule graphics) {
		this.tower = tower;
		this.graphics = graphics;
		tower.setView(this);
		towerSprite = Utils.createTowerSprite(graphics, "gunTower.png", tower.getTile().getX(), tower.getTile().getY());
		attackSprite = graphics.createSprite().setImage("gunTowerAttack.png").setAlpha(0);
		attackLine = graphics.createLine();
		attackLine.setX(BoardView.CELL_SIZE * tower.getTile().getX());
		attackLine.setY(BoardView.CELL_SIZE * tower.getTile().getY());
		attackLine.setLineColor(0xff0000).setAlpha(0);
		attackLine.setLineWidth(5);
	}

	public void attack(Attacker a) {
		attackSprite.setAlpha(1);
		attackSprite.setX((int) (BoardView.CELL_SIZE * a.getLocation().getX()));
		attackSprite.setY((int) (BoardView.CELL_SIZE * a.getLocation().getY()));
		attackLine.setAlpha(1);
		attackLine.setX2((int) (BoardView.CELL_SIZE * a.getLocation().getX()));
		attackLine.setY2((int) (BoardView.CELL_SIZE * a.getLocation().getY()));

		graphics.commitEntityState(0, attackSprite, attackLine);
		attackSprite.setAlpha(0);
		attackLine.setAlpha(0);
	}
}
