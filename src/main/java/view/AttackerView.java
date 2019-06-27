package view;

import java.util.ArrayList;

import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.entities.Sprite;
import com.codingame.gameengine.module.tooltip.TooltipModule;

import TowerDefense.Attacker;
import TowerDefense.SubTile;

public class AttackerView {
	private static ArrayList<Sprite> spriteCache = new ArrayList<>();

	private Attacker attacker;
	private Sprite sprite;
	private GraphicEntityModule graphics;
	private TooltipModule tooltips;

	public AttackerView(Attacker attacker, Group boardGroup, GraphicEntityModule graphics, TooltipModule tooltips) {
		this.attacker = attacker;
		this.graphics = graphics;
		this.tooltips = tooltips;
		attacker.setView(this);
		for (Sprite s : spriteCache) {
			if (s.getTint() == attacker.getOwner().getColor()) {
				sprite = s;
				SubTile t = attacker.getLocation();
				sprite.setAlpha(1).setX((int) (BoardView.CELL_SIZE * t.getX())).setY((int) (BoardView.CELL_SIZE * t.getY()));
				graphics.commitEntityState(0, sprite);
				spriteCache.remove(s);
				break;
			}
		}
		if (sprite == null) {
			sprite = Utils.createAttackerSprite(graphics, "attacker.png", attacker.getLocation().getX(), attacker.getLocation().getY());
			boardGroup.add(sprite);
		}
		//tooltips.setTooltipText(sprite, getTooltipString());
		sprite.setTint(attacker.getOwner().getColor());
	}

	public void move(ArrayList<SubTile> steps) {
		if (steps.size() == 0)
			return;
		steps.add(attacker.getLocation());

		for (int i = 1; i < steps.size() - 1; i++) {
			SubTile current = steps.get(i);
			SubTile prev = steps.get(i - 1);
			SubTile next = steps.get(i + 1);
			boolean changeDir = current.getX() != prev.getX() && current.getY() != next.getY();
			changeDir |= current.getX() != next.getX() && current.getY() != prev.getY();
			if (changeDir) {
				sprite.setX((int) (BoardView.CELL_SIZE * current.getX()));
				sprite.setY((int) (BoardView.CELL_SIZE * current.getY()));
				graphics.commitEntityState((double) i / (steps.size() - 1), sprite);
			}
		}

		SubTile last = steps.get(steps.size() - 1);
		sprite.setX((int) (BoardView.CELL_SIZE * last.getX()));
		sprite.setY((int) (BoardView.CELL_SIZE * last.getY()));
		//tooltips.setTooltipText(sprite, getTooltipString());
	}

	public String getTooltipString() {
		StringBuilder sb = new StringBuilder();
		sb.append("x: ").append(attacker.getLocation().getX()).append("\ny: ").append(attacker.getLocation().getY());
		sb.append("\nid: ").append(attacker.getId());
		sb.append("\nowner: ").append(attacker.getOwner().getIndex());
		sb.append("\nhp: ").append(attacker.getHitPoints());
		sb.append("\nspeed: ").append(attacker.getSpeed());
		sb.append("\nslowdown: ").append(attacker.getSlowCountdown()).append(" rounds");
		sb.append("\nbounty: ").append(attacker.getBounty());

		return sb.toString();
	}

	public void kill() {
		sprite.setAlpha(0);
		//spriteCache.add(sprite);
	}
}
