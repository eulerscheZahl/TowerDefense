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
		properties[TowerProperty.DAMAGE.ordinal()] = new double[] { 2, 3, 5, 7 };
		properties[TowerProperty.RANGE.ordinal()] = new double[] { 1.5, 2, 2.5, 3 };
		properties[TowerProperty.RELOAD.ordinal()] = new double[] { 8, 7, 6, 5 };
		cost = 100;
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
		return firing;
	}

	@Override
	public TowerView createView(Group boardGroup, GraphicEntityModule graphics, TooltipModule tooltipModule) {
		return new FireTowerView(this, boardGroup, graphics, tooltipModule);
	}
}
