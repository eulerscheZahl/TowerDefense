package TowerDefense;

import java.util.ArrayList;
import java.util.List;

import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.tooltip.TooltipModule;

import view.FireTowerView;
import view.TowerView;

public class FireTower extends Tower {
	private ArrayList<Attacker> alreadyAttacked = new ArrayList<>();
	private int fireEffect = 0;

	public FireTower(Tile tile) {
		super("FIRETOWER", tile);
		properties[TowerProperty.DAMAGE.ordinal()] = Constants.FIRETOWER_DAMAGE;
		properties[TowerProperty.RANGE.ordinal()] = Constants.FIRETOWER_RANGE;
		properties[TowerProperty.RELOAD.ordinal()] = Constants.FIRETOWER_RELOAD;
		cost = Constants.FIRETOWER_COST;
	}

	@Override
	public void attack(List<Attacker> attackers) {
		if (cooldown > 0) {
			cooldown--;
		}
		if (fireEffect > 0) {
			fireEffect--;
			doAttack(attackers);
		}
		if (cooldown > 0) return;
		if (doAttack(attackers)) {
			cooldown = (int) getProperty(TowerProperty.RELOAD) - 1;
			fireEffect = Constants.FIRE_EFFECT_DURATION - 1;
		}
	}

	@Override
	boolean doAttack(List<Attacker> attackers) {
		if (cooldown == 0) alreadyAttacked.clear();
		boolean firing = false;
		for (Attacker a : attackers) {
			if (getOwner() == a.getOwner() || !inRange(a) || alreadyAttacked.contains(a))
				continue;
			a.dealDamage((int) getProperty(TowerProperty.DAMAGE));
			alreadyAttacked.add(a);
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
