package TowerDefense;

import java.util.List;

import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.tooltip.TooltipModule;

import view.HealTowerView;
import view.TowerView;

public class HealTower extends Tower {
	public HealTower(Tile tile) {
		super("HEALTOWER", tile);
		properties[TowerProperty.DAMAGE.ordinal()] = Constants.HEALTOWER_DAMAGE;
		properties[TowerProperty.RANGE.ordinal()] = Constants.HEALTOWER_RANGE;
		properties[TowerProperty.RELOAD.ordinal()] = Constants.HEALTOWER_RELOAD;
		cost = Constants.HEALTOWER_COST;
	}

	@Override
	boolean doAttack(List<Attacker> attackers) {
		Attacker target = null;
		for (Attacker a : attackers) {
			if (getOwner() != a.getOwner() || !inRange(a) || !a.canHeal())
				continue;
			if (target == null || a.getPathLength() < target.getPathLength())
				target = a;
		}
		if (target == null)
			return false;

		target.heal((int) getProperty(TowerProperty.DAMAGE));
		getView().attack(target);
		return true;
	}

	@Override
	public TowerView createView(Group boardGroup, GraphicEntityModule graphics, TooltipModule tooltipModule) {
		return new HealTowerView(this, boardGroup, graphics, tooltipModule);
	}
}
