import java.io.File;

import com.codingame.gameengine.runner.MultiplayerGameRunner;

public class SkeletonMain {
	public static void main(String[] args) {
		MultiplayerGameRunner gameRunner = new MultiplayerGameRunner();

		String botPath = "/home/eulerschezahl/Documents/Programming/challenges/CodinGame/TowerDefense/bin/Debug/";

		if (new File(botPath + "basicGun.exe").isFile()) {
			gameRunner.addAgent("mono " + botPath + "basicGun.exe");
			gameRunner.addAgent("mono " + botPath + "basicGunUpgrade.exe");
		} else {
			gameRunner.addAgent(Agent1.class);
			gameRunner.addAgent(Agent1.class);
		}

		gameRunner.setSeed(7975987388959628300L);
		gameRunner.start();
	}
}
