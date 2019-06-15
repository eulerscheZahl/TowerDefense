package com.codingame.game;

import com.codingame.gameengine.core.AbstractMultiplayerPlayer;

import TowerDefense.Attacker;
import TowerDefense.Tower;

public class Player extends AbstractMultiplayerPlayer {
	private int money = 750;
	private int lives = 10;

	private static int[] colors = { 0xff8080, 0x8080ff };

	@Override
	public int getExpectedOutputLines() {
		return 1;
	}

	public int getMoney() {
		return money;
	}

	public void kill(Attacker a) {
		this.money += a.getBounty();
	}

	public boolean buy(Tower tower) {
		if (money < tower.getCost())
			return false;
		money -= tower.getCost();
		tower.setOwner(this);
		return true;
	}

	public void spendMoney(int money) {
		this.money -= money;
	}

	public String getPlayerInput() {
		return money + " " + lives;
	}

	public void loseLife() {
		lives--;
	}

	public boolean isDead() {
		return lives <= 0;
	}

	public int getColor() {
		return colors[getIndex()];
	}
}
