package view;

import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Line;
import com.codingame.gameengine.module.entities.Sprite;

import TowerDefense.Attacker;
import TowerDefense.Tower;

public abstract class TowerView {
	protected Tower tower;
	protected Sprite towerSprite;
	protected Sprite attackSprite;
	protected Line attackLine;
	protected GraphicEntityModule graphics;

	public abstract void attack(Attacker a);
}
