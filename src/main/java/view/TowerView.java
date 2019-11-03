package view;

import java.text.DecimalFormat;

import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.entities.Line;
import com.codingame.gameengine.module.entities.Sprite;
import com.codingame.gameengine.module.tooltip.TooltipModule;

import TowerDefense.Attacker;
import TowerDefense.Constants;
import TowerDefense.Tower;
import TowerDefense.TowerProperty;

public abstract class TowerView {
	protected Tower tower;
	protected Sprite[] towerSprite = new Sprite[Constants.NUM_UPGRADE_SPRITES];
	protected Sprite attackSprite;
	protected Line attackLine;
	protected GraphicEntityModule graphics;
	protected TooltipModule tooltipModule;
	protected Group boardGroup;

	int upgradeLevel = 0;
	
	public TowerView(Tower tower, Group boardGroup, GraphicEntityModule graphics, TooltipModule tooltips, String sprite) {
		this.tower = tower;
		this.graphics = graphics;
		tower.setView(this);
		this.tooltipModule = tooltips;
		this.boardGroup = boardGroup;
		for (int level = 0; level < Constants.NUM_UPGRADE_SPRITES; level++) {
			towerSprite[level] = Utils.createTowerSprite(graphics, sprite + level + ".png", tower.getTile().getX(), tower.getTile().getY());
			towerSprite[level].setTint(tower.getOwner().getColor());
			if (level == 0) {
				tooltips.setTooltipText(towerSprite[level], getTooltipString());
			} else {
				towerSprite[level].setVisible(false);
			}
		}
		boardGroup.add(towerSprite);
	}

	public String getTooltipString() {
		StringBuilder sb = new StringBuilder();
		sb.append("x: ").append(tower.getTile().getX()).append("\ny: ").append(tower.getTile().getY());
		sb.append("\ntype: ").append(tower.getType());
		sb.append("\nid: ").append(tower.getId());
		sb.append("\nowner: ").append(tower.getOwner().getIndex());
		for (TowerProperty p : TowerProperty.values()) {
			sb.append("\n").append(p).append(": ").append(new DecimalFormat("0.#").format(tower.getProperty(p)));
		}
		sb.append("\ncooldown: ").append(tower.getCooldown());
		return sb.toString();
	}

	public abstract void attack(Attacker a);
	
	public void upgrade() {
		if (upgradeLevel + 1 == Constants.NUM_UPGRADE_SPRITES)
			return;
		this.towerSprite[upgradeLevel].setVisible(false);
		upgradeLevel++;
		this.towerSprite[upgradeLevel].setVisible(true);
	}
}
