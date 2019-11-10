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
		boolean attacked = false;
		for (Attacker a : attackers) {
			if (getOwner() == a.getOwner() || !inRange(a) || a.isSlow())
				continue;
			a.slowDown((int) getProperty(TowerProperty.DAMAGE));
			getView().attack(a);
			attacked = true;
		}
		return attacked;
	}

	@Override
	public TowerView createView(Group boardGroup, GraphicEntityModule graphics, TooltipModule tooltipModule) {
		return new GlueTowerView(this, boardGroup, graphics, tooltipModule);
	}
}