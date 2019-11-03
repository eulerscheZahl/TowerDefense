package view;

import java.text.DecimalFormat;

import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.entities.Line;
import com.codingame.gameengine.module.entities.Sprite;
import com.codingame.gameengine.module.tooltip.TooltipModule;

import TowerDefense.Attacker;
import TowerDefense.Tower;
import TowerDefense.TowerProperty;

public abstract class TowerView {
	protected Tower tower;
	protected Sprite towerSprite;
	protected Sprite attackSprite;
	protected Line attackLine;
	protected GraphicEntityModule graphics;
	protected TooltipModule tooltipModule;
	protected Group boardGroup;

	public TowerView(Tower tower, Group boardGroup, GraphicEntityModule graphics, TooltipModule tooltips) {
		this.tower = tower;
		this.graphics = graphics;
		tower.setView(this);
		this.tooltipModule = tooltips;
		this.boardGroup = boardGroup;
	}

	protected void commitSprites() {
		boardGroup.add(towerSprite, attackSprite, attackLine);
		graphics.commitEntityState(0, boardGroup);
	}

	public void updateTooltip() {
		StringBuilder sb = new StringBuilder();
		sb.append("x: ").append(tower.getTile().getX()).append("\ny: ").append(tower.getTile().getY());
		sb.append("\ntype: ").append(tower.getType());
		sb.append("\nid: ").append(tower.getId());
		sb.append("\nowner: ").append(tower.getOwner().getIndex());
		for (TowerProperty p : TowerProperty.values()) {
			sb.append("\n").append(p).append(": ").append(new DecimalFormat("0.#").format(tower.getProperty(p)));
		}
		sb.append("\ncooldown: ").append(tower.getCooldown());
		tooltipModule.setTooltipText(towerSprite, sb.toString());
	}

	public abstract void attack(Attacker a);
}
