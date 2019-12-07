package com.codingame.game;

import com.codingame.gameengine.core.AbstractMultiplayerPlayer;
import com.codingame.gameengine.module.entities.GraphicEntityModule;

import TowerDefense.Attacker;
import TowerDefense.Constants;
import TowerDefense.Tower;
import view.PlayerView;

public class Player extends AbstractMultiplayerPlayer {
	private int money = Constants.PLAYER_MONEY;
	private int lives = Constants.PLAYER_LIVES;
	private PlayerView view;
	private String message = "";

	private static int[] colors = { 0xff4040, 0x4040ff };

	@Override
	public int getExpectedOutputLines() {
		return 1;
	}

	public void initView(GraphicEntityModule graphics) {
		new PlayerView(this, graphics);
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

	public void kill() {
		lives = 0;
	}

	public int getScorePoints() {
		if (isDead() || !isActive())
			return 0;
		return 100 * lives + money;
	}

	public boolean isDead() {
		return lives <= 0;
	}

	public int getLives() {
		return lives;
	}

	public int getColor() {
		return colors[getIndex()];
	}

	public void setView(PlayerView view) {
		this.view = view;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void updateView() {
		view.updateView();
	}
}
