package view;

import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.entities.Line;
import com.codingame.gameengine.module.entities.Sprite;

import TowerDefense.Attacker;
import TowerDefense.Tower;
import com.codingame.gameengine.module.tooltip.TooltipModule;

public abstract class TowerView {
	protected Tower tower;
	protected Sprite towerSprite;
	protected Sprite attackSprite;
	protected Line attackLine;
	protected GraphicEntityModule graphics;
	protected TooltipModule tooltipModule;

	public TowerView(Tower tower, Group boardGroup, GraphicEntityModule graphics, TooltipModule tooltips) {
		this.tower = tower;
		this.graphics = graphics;
		tower.setView(this);
		this.tooltipModule = tooltips;
	}

	public abstract void attack(Attacker a);
}
