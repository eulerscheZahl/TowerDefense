package TowerDefense;

import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.tooltip.TooltipModule;
import view.GlueTowerView;
import view.TowerView;

import java.util.List;

public class GlueTower extends Tower {
	public GlueTower(Tile tile) {
		super("GLUETOWER", tile);
		properties[TowerProperty.DAMAGE.ordinal()] = new double[] { 3, 5, 7, 9 }; // duration of slowdown effect
		properties[TowerProperty.RANGE.ordinal()] = new double[] { 2, 3, 4, 5 };
		properties[TowerProperty.RELOAD.ordinal()] = new double[] { 6, 5, 4, 3 };
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

		target.slowDown((int) getProperty(TowerProperty.DAMAGE));
		getView().attack(target);
		return true;
	}

	@Override
	public TowerView createView(Group boardGroup, GraphicEntityModule graphics, TooltipModule tooltipModule) {
		return new GlueTowerView(this, boardGroup, graphics, tooltipModule);
	}
}