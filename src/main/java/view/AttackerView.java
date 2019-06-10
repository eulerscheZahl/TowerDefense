package view;

import TowerDefense.Attacker;
import TowerDefense.SubTile;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.entities.Sprite;
import com.codingame.gameengine.module.tooltip.TooltipModule;

import java.util.ArrayList;

public class AttackerView {
	private Attacker attacker;
	private Sprite sprite;
	private GraphicEntityModule graphics;
	private TooltipModule tooltips;

	public AttackerView(Attacker attacker, Group boardGroup, GraphicEntityModule graphics, TooltipModule tooltips) {
		this.attacker = attacker;
		this.graphics = graphics;
		this.tooltips = tooltips;
		attacker.setView(this);
		sprite = Utils.createAttackerSprite(graphics, "attacker.png", attacker.getLocation().getX(), attacker.getLocation().getY());
		tooltips.setTooltipText(sprite, attacker.getTooltipString());
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
        tooltips.setTooltipText(sprite, attacker.getTooltipString());
	}

	public void kill() {
		sprite.setAlpha(0);
	}
}
