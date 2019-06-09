package TowerDefense;

import java.util.ArrayList;
import java.util.List;

import view.AttackerView;

public class Attacker {
	private List<SubTile> remainingPath;
	private int id;
	private int hitPoints;
	private int maxSpeed;
	private int slowCountdown;
	private int money;
	private AttackerView view;
	private static int idCounter;

	public Attacker(List<SubTile> path) {
		id = idCounter++;
		this.remainingPath = path;
		maxSpeed = 10;
		hitPoints = 10;
		money = 15;
	}

	public int getSpeed() {
		if (slowCountdown == 0)
			return maxSpeed;
		return maxSpeed * 2 / 3;
	}

	public int getMoney() {
		return money;
	}

	public int getPathLength() {
		return remainingPath.size();
	}

	public SubTile getLocation() {
		return remainingPath.get(remainingPath.size() - 1);
	}

	public void kill() {
		dealDamage(hitPoints);
	}

	public void dealDamage(int damage) {
		this.hitPoints = Math.max(0, hitPoints - damage);
		if (isDead())
			view.kill();
	}

	public void slowDown(int countdown) {
		this.slowCountdown = countdown;
	}

	public boolean isDead() {
		return hitPoints <= 0;
	}

	public boolean hasSucceeded() {
		return remainingPath.size() == 1;
	}

	public void setView(AttackerView view) {
		this.view = view;
	}

	public void move() {
		int speed = getSpeed();
		ArrayList<SubTile> steps = new ArrayList<>();
		while (steps.size() < speed && remainingPath.size() > 1) {
			steps.add(remainingPath.get(remainingPath.size() - 1));
			remainingPath.remove(remainingPath.size() - 1);
		}
		view.move(steps);
		if (slowCountdown > 0)
			slowCountdown--;
	}

	public String getPlayerInput() {
		StringBuilder sb = new StringBuilder();
		sb.append("ATTACKER ");
		sb.append(id).append(" ");
		sb.append(getLocation().toString()).append(" ");
		sb.append(hitPoints).append(" ");
		sb.append(maxSpeed).append(" ");
		sb.append(getSpeed()).append(" ");
		sb.append(slowCountdown).append(" ");
		sb.append(money);

		return sb.toString();
	}
}
