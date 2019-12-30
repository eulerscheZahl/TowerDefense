package view;

import java.util.ArrayList;
import java.util.Random;

import TowerDefense.Constants;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.entities.Sprite;
import com.codingame.gameengine.module.entities.SpriteAnimation;
import com.codingame.gameengine.module.tooltip.TooltipModule;

import TowerDefense.Attacker;
import TowerDefense.SubTile;

public class AttackerView {
	private static final int WALK_DURATION = 800;
	private static final int DEATH_DURATION = 1000;

	private static ArrayList<ArrayList<Group>> spriteCache = new ArrayList<>();

	private Attacker attacker;
	private Group group;
	private Sprite glueSprite = null;
	private SpriteAnimation attackerBody, attackerHelmet;
	private GraphicEntityModule graphics;
	private TooltipModule tooltips;
	private String[] attackerBodySprites;
	private String[] attackerHelmetSprites;
	private String[] attackerBodyDeathSprites;
	private String[] attackerHelmetDeathSprites;
	private String[] attackerBodyWinSprites;
	private String[] attackerHelmetWinSprites;

	static {
		spriteCache.add(new ArrayList<Group>());
		spriteCache.add(new ArrayList<Group>());
	}

	public AttackerView(Attacker attacker, Group boardGroup, GraphicEntityModule graphics, TooltipModule tooltips) {
		if (attackerBodySprites == null) {
			attackerBodySprites = graphics.createSpriteSheetSplitter().setSourceImage("att_body.png").setHeight(94).setWidth(100).setImageCount(10).setImagesPerRow(4).setOrigRow(0).setOrigCol(0).setName("ab").split();
			attackerHelmetSprites = graphics.createSpriteSheetSplitter().setSourceImage("att_helmet.png").setHeight(94).setWidth(100).setImageCount(10).setImagesPerRow(4).setOrigRow(0).setOrigCol(0).setName("ah").split();
			attackerBodyDeathSprites = graphics.createSpriteSheetSplitter().setSourceImage("die_body.png").setHeight(94).setWidth(100).setImageCount(10).setImagesPerRow(4).setOrigRow(0).setOrigCol(0).setName("db").split();
			attackerHelmetDeathSprites = graphics.createSpriteSheetSplitter().setSourceImage("die_helmet.png").setHeight(94).setWidth(100).setImageCount(10).setImagesPerRow(4).setOrigRow(0).setOrigCol(0).setName("dh").split();
			attackerBodyWinSprites = graphics.createSpriteSheetSplitter().setSourceImage("jump_body.png").setHeight(94).setWidth(100).setImageCount(10).setImagesPerRow(4).setOrigRow(0).setOrigCol(0).setName("jb").split();
			attackerHelmetWinSprites = graphics.createSpriteSheetSplitter().setSourceImage("jump_helmet.png").setHeight(94).setWidth(100).setImageCount(10).setImagesPerRow(4).setOrigRow(0).setOrigCol(0).setName("jh").split();
		}
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
					setImages(attackerBodySprites).
					setDuration(WALK_DURATION).setLoop(true).setPlaying(true);
			attackerHelmet = graphics.createSpriteAnimation().
					setImages(attackerHelmetSprites).
					setDuration(WALK_DURATION).setLoop(true).setPlaying(true).
					setTint(attacker.getOwner().getColor());
			group = graphics.createGroup(attackerBody, attackerHelmet)
					.setX((int) (BoardView.CELL_SIZE * attacker.getLocation().getX()))
					.setY((int) (BoardView.CELL_SIZE * attacker.getLocation().getY()));
			if (attacker.getOwner().getIndex() == 1) {
				attackerBody.setX(-BoardView.CELL_SIZE);
				attackerHelmet.setX(-BoardView.CELL_SIZE);
				group.setScaleX(-1);
			}
			boardGroup.add(group);
		}
		//tooltips.setTooltipText(sprite, getTooltipString());
	}

	private int finalY = -1;
	public void move() {
		if (attacker.isSlow()) {
			if (glueSprite == null) {
				glueSprite = graphics.createSprite().setImage("glue" + (1 + attacker.getId() % 3) + ".png").setScale(0.3).setY(50);
				if (attacker.getOwner().getIndex() == 1) {
					glueSprite.setX(-BoardView.CELL_SIZE);
				}
				group.add(glueSprite);
				graphics.commitEntityState(0, group, glueSprite);
			}
			if (glueSprite.getAlpha() == 0) {
				glueSprite.setAlpha(1);
				graphics.commitEntityState(0, glueSprite);
			}
		} else if (glueSprite != null && glueSprite.getAlpha() == 1) {
			glueSprite.setAlpha(0);
			graphics.commitEntityState(0, glueSprite);
		}


		ArrayList<SubTile> steps = attacker.getSteps();
		if (steps.size() == 0) {
			if (attacker.hasSucceeded()) {
				if (finalY == -1) {
					changeAnimation(attackerBodySprites, attackerHelmetSprites);
					attackerBody.setLoop(true).play();
					attackerHelmet.setLoop(true).play();
					finalY = 923 * attacker.getId() % (Constants.MAP_HEIGHT * BoardView.CELL_SIZE);
				}

				int missing = group.getY() - finalY;
				if (missing == 0) {
					if (attackerBody.isPlaying()) {
						attackerBody.pause();
						attackerHelmet.pause();
					}
					return;
				}
				int moveDist = attacker.getSpeed() * BoardView.CELL_SIZE / SubTile.SUBTILE_SIZE;
				if (Math.abs(missing) < moveDist) moveDist = Math.abs(missing);
				if (group.getY() < finalY) group.setY(group.getY() + moveDist);
				else group.setY(group.getY() - moveDist);
			}
			return;
		}
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
		if (attacker.hasSucceeded()) {
			changeAnimation(attackerBodyWinSprites, attackerHelmetWinSprites);
		} else {
			changeAnimation(attackerBodyDeathSprites, attackerHelmetDeathSprites);
			graphics.commitEntityState(0.9, group);
			group.setVisible(false);
			//spriteCache.get(attacker.getOwner().getIndex()).add(group);
		}
	}

	private void changeAnimation(String[] body, String[] helmet) {
		attackerBody.setImages(body);
		attackerBody.setDuration(DEATH_DURATION);
		attackerBody.reset();
		attackerHelmet.setImages(helmet);
		attackerHelmet.setDuration(DEATH_DURATION);
		attackerHelmet.reset();
		graphics.commitEntityState(0, attackerBody);
		graphics.commitEntityState(0, attackerHelmet);
	}
}
