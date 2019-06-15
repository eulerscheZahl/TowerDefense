package TowerDefense;

import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.tooltip.TooltipModule;
import view.HealTowerView;
import view.TowerView;

import java.util.List;

public class HealTower extends Tower {
	public HealTower(Tile tile) {
		super("HEALTOWER", tile);
		properties[TowerProperty.DAMAGE.ordinal()] = new double[] { 3, 5, 7, 9 };
		properties[TowerProperty.RANGE.ordinal()] = new double[] { 3, 4, 5, 6 };
		properties[TowerProperty.RELOAD.ordinal()] = new double[] { 6, 5, 4, 3 };
		cost = 100;
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
