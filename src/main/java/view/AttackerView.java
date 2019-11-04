package view;

import java.util.ArrayList;

import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.entities.SpriteAnimation;
import com.codingame.gameengine.module.tooltip.TooltipModule;

import TowerDefense.Attacker;
import TowerDefense.SubTile;

public class AttackerView {
	private static final int ANIMATION_DURATION = 800;

	private static ArrayList<ArrayList<Group>> spriteCache = new ArrayList<>();

	private Attacker attacker;
	private Group group;
	private SpriteAnimation attackerBody, attackerHelmet;
	private GraphicEntityModule graphics;
	private TooltipModule tooltips;

	static {
		spriteCache.add(new ArrayList<Group>());
		spriteCache.add(new ArrayList<Group>());
	}

	public AttackerView(Attacker attacker, Group boardGroup, GraphicEntityModule graphics, TooltipModule tooltips) {
		this.attacker = attacker;
		this.graphics = graphics;
		this.tooltips = tooltips;
		attacker.setView(this);
		for (Group g : spriteCache.get(attacker.getOwner().getIndex())) {
			group = g;
			SubTile t = attacker.getLocation();
			group.setAlpha(1).setX((int) (BoardView.CELL_SIZE * t.getX())).setY((int) (BoardView.CELL_SIZE * t.getY()));
			graphics.commitEntityState(0, group);
			spriteCache.get(attacker.getOwner().getIndex()).remove(g);
			break;
		}
		if (group == null) {
			attackerBody = graphics.createSpriteAnimation().
					setImages("att_body_01.png", "att_body_03.png", "att_body_05.png", "att_body_07.png", "att_body_09.png", "att_body_11.png", "att_body_13.png", "att_body_15.png", "att_body_17.png", "att_body_19.png").
					setDuration(ANIMATION_DURATION).setLoop(true).setPlaying(true);
			attackerHelmet = graphics.createSpriteAnimation().
					setImages("att_helmet_01.png", "att_helmet_03.png", "att_helmet_05.png", "att_helmet_07.png", "att_helmet_09.png", "att_helmet_11.png", "att_helmet_13.png", "att_helmet_15.png", "att_helmet_17.png", "att_helmet_19.png").
					setDuration(ANIMATION_DURATION).setLoop(true).setPlaying(true).
					setTint(attacker.getOwner().getColor());
			group = graphics.createGroup(attackerBody, attackerHelmet)
					.setX((int) (BoardView.CELL_SIZE * attacker.getLocation().getX()))
					.setY((int) (BoardView.CELL_SIZE * attacker.getLocation().getY()));
			if (attacker.getOwner().getIndex() == 1) {
				attackerBody.setX(-BoardView.CELL_SIZE );
				attackerHelmet.setX(-BoardView.CELL_SIZE );
				group.setScaleX(-1);
			}
			boardGroup.add(group);
		}
		//tooltips.setTooltipText(sprite, getTooltipString());
	}

	public void move(ArrayList<SubTile> steps) {
		if (steps.size() == 0)
			return;
		steps.add(attacker.getLocation());

		// Seems to be needed for the animations to play
		graphics.commitEntityState(0, attackerBody);
		graphics.commitEntityState(0, attackerHelmet);
		
		for (int i = 1; i < steps.size() - 1; i++) {
			SubTile current = steps.get(i);
			SubTile prev = steps.get(i - 1);
			SubTile next = steps.get(i + 1);
			boolean changeDir = current.getX() != prev.getX() && current.getY() != next.getY();
			changeDir |= current.getX() != next.getX() && current.getY() != prev.getY();
			if (changeDir) {
				group.setX((int) (BoardView.CELL_SIZE * current.getX()));
				group.setY((int) (BoardView.CELL_SIZE * current.getY()));
				graphics.commitEntityState((double) i / (steps.size() - 1), group);
			}
		}

		SubTile last = steps.get(steps.size() - 1);
		group.setX((int) (BoardView.CELL_SIZE * last.getX()));
		group.setY((int) (BoardView.CELL_SIZE * last.getY()));
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
		group.setAlpha(0);
		//spriteCache.get(attacker.getOwner().getIndex()).add(sprite);
	}
}
