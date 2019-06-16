import com.codingame.gameengine.runner.MultiplayerGameRunner;

public class SkeletonMain {
	public static void main(String[] args) {
		MultiplayerGameRunner gameRunner = new MultiplayerGameRunner();
		gameRunner.addAgent(Agent1.class);
		//gameRunner.addAgent(Agent1.class);

		// Another way to add a player
		gameRunner.addAgent("mono /home/eulerschezahl/Dokumente/Programmieren/challenges/CodinGame/TowerDefense/bin/Debug/TowerDefense.exe");
		gameRunner.setSeed(7129279169689094050L);
		gameRunner.start();
	}
}
