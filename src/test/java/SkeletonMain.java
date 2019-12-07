import java.io.File;

import com.codingame.gameengine.runner.MultiplayerGameRunner;

public class SkeletonMain {
	public static void main(String[] args) {
		MultiplayerGameRunner gameRunner = new MultiplayerGameRunner();

		String botPath = "/home/eulerschezahl/Documents/Programming/challenges/CodinGame/TowerDefense/bin/Debug/";

		if (new File(botPath + "basicGun.exe").isFile()) {
			gameRunner.addAgent("mono " + botPath + "fireTower.exe", "eulerscheZahl", "https://static.codingame.com/servlet/fileservlet?id=29379905825543&format=profile_avatar");
			gameRunner.addAgent("mono " + botPath + "basicGunUpgrade.exe", "CodinGame", "https://static.codingame.com/servlet/fileservlet?id=19333912201092&format=profile_avatar");
		} else {
			gameRunner.addAgent(Agent1.class, "eulerscheZahl", "https://static.codingame.com/servlet/fileservlet?id=29379905825543&format=profile_avatar");
			gameRunner.addAgent(Agent1.class, "CodinGame", "https://static.codingame.com/servlet/fileservlet?id=19333912201092&format=profile_avatar");
		}

		gameRunner.setSeed(7975987388959628300L);
		gameRunner.start();
	}
}
