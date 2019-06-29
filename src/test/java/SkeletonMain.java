import java.io.File;

import com.codingame.gameengine.runner.MultiplayerGameRunner;

public class SkeletonMain {
	public static void main(String[] args) {
		MultiplayerGameRunner gameRunner = new MultiplayerGameRunner();

		String botPath = "/home/eulerschezahl/Dokumente/Programmieren/challenges/CodinGame/TowerDefense/bin/Debug/";

		if (new File(botPath + "TowerDefense.exe").isFile()) {
			gameRunner.addAgent("mono " + botPath + "basicGun.exe");
			gameRunner.addAgent("mono " + botPath + "basicGunUpgrade.exe");
		} else {
			gameRunner.addAgent(Agent1.class);
			gameRunner.addAgent(Agent1.class);
		}

		gameRunner.setSeed(7129279169689094050L);
		gameRunner.start();
	}
}
