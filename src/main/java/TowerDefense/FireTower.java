package TowerDefense;

import java.util.List;

import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.tooltip.TooltipModule;

import view.FireTowerView;
import view.TowerView;

public class FireTower extends Tower {
	public FireTower(Tile tile) {
		super("FIRETOWER", tile);
		properties[TowerProperty.DAMAGE.ordinal()] = Constants.FIRETOWER_DAMAGE;
		properties[TowerProperty.RANGE.ordinal()] = Constants.FIRETOWER_RANGE;
		properties[TowerProperty.RELOAD.ordinal()] = Constants.FIRETOWER_RELOAD;
		cost = Constants.FIRETOWER_COST;
	}

	@Override
	boolean doAttack(List<Attacker> attackers) {
		boolean firing = false;
		for (Attacker a : attackers) {
			if (getOwner() == a.getOwner() || !inRange(a))
				continue;
			a.dealDamage((int) getProperty(TowerProperty.DAMAGE));
			firing = true;
		}
		if (firing) getView().attack(null);
		return firing;
	}

	@Override
	public TowerView createView(Group boardGroup, GraphicEntityModule graphics, TooltipModule tooltipModule) {
		return new FireTowerView(this, boardGroup, graphics, tooltipModule);
	}
}
