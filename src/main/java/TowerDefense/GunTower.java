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
		properties[TowerProperty.DAMAGE.ordinal()] = new double[] { 5, 7, 9, 11 };
		properties[TowerProperty.RANGE.ordinal()] = new double[] { 3, 4, 5, 6 };
		properties[TowerProperty.RELOAD.ordinal()] = new double[] { 5, 4, 3, 2 };
		cost = 100;
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
