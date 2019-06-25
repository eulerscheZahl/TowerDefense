package TowerDefense;

import java.util.List;

import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.tooltip.TooltipModule;

import view.GunTowerView;
import view.TowerView;

public class GunTower extends Tower {
	public GunTower(Tile tile) {
		super("GUNTOWER", tile);
		properties[TowerProperty.DAMAGE.ordinal()] = Constants.GUNTOWER_DAMAGE;
		properties[TowerProperty.RANGE.ordinal()] = Constants.GUNTOWER_RANGE;
		properties[TowerProperty.RELOAD.ordinal()] = Constants.GUNTOWER_RELOAD;
		cost = Constants.GUNTOWER_COST;
	}

	@Override
	boolean doAttack(List<Attacker> attackers) {
		Attacker target = null;
		for (Attacker a : attackers) {
			if (getOwner() == a.getOwner() || !inRange(a))
				continue;
			if (target == null || a.getPathLength() < target.getPathLength())
				target = a;
		}
		if (target == null)
			return false;

		target.dealDamage((int) getProperty(TowerProperty.DAMAGE));
		getView().attack(target);
		return true;
	}

	@Override
	public TowerView createView(Group boardGroup, GraphicEntityModule graphics, TooltipModule tooltipModule) {
		return new GunTowerView(this, boardGroup, graphics, tooltipModule);
	}
}
