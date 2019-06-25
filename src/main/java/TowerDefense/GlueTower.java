package TowerDefense;

import java.util.List;

import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.tooltip.TooltipModule;

import view.GlueTowerView;
import view.TowerView;

public class GlueTower extends Tower {
	public GlueTower(Tile tile) {
		super("GLUETOWER", tile);
		properties[TowerProperty.DAMAGE.ordinal()] = Constants.GLUETOWER_DAMAGE;
		properties[TowerProperty.RANGE.ordinal()] = Constants.GLUETOWER_RANGE;
		properties[TowerProperty.RELOAD.ordinal()] = Constants.GLUETOWER_RELOAD;
		cost = Constants.GLUETOWER_COST;
	}

	@Override
	boolean doAttack(List<Attacker> attackers) {
		Attacker target = null;
		for (Attacker a : attackers) {
			if (getOwner() == a.getOwner() || !inRange(a) || a.isSlow())
				continue;
			if (target == null || a.getPathLength() < target.getPathLength())
				target = a;
		}
		if (target == null)
			return false;

		target.slowDown((int) getProperty(TowerProperty.DAMAGE));
		getView().attack(target);
		return true;
	}

	@Override
	public TowerView createView(Group boardGroup, GraphicEntityModule graphics, TooltipModule tooltipModule) {
		return new GlueTowerView(this, boardGroup, graphics, tooltipModule);
	}
}