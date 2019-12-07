import java.util.Locale;
import java.util.Scanner;

public class Agent1 {
	public static void main(String[] args) {
		Locale.setDefault(new Locale("en", "US"));
		Scanner scanner = new Scanner(System.in);
		String initialInput = "";
		int playerId = scanner.nextInt();
		initialInput += playerId + "\n";
		int width = scanner.nextInt();
		int height = scanner.nextInt();
		initialInput += width + " " + height + "\n";
		scanner.nextLine();
		for (int y = 0; y < height; y++) {
			String line = scanner.nextLine();
			initialInput += line + "\n";
		}
		for (int turn = 0;; turn += 1) {
			int myMoney = scanner.nextInt();
			int myLives = scanner.nextInt();
			System.err.println(initialInput + myMoney + " " + myLives);
			int opponentMoney = scanner.nextInt();
			int opponentLives = scanner.nextInt();
			System.err.println(opponentMoney + " " + opponentLives);
			int towerCount = scanner.nextInt();
			System.err.println(towerCount);
			for (int i = 0; i < towerCount; i++) {
				String type = scanner.next();
				int towerId = scanner.nextInt();
				int owner = scanner.nextInt();
				int x = scanner.nextInt();
				int y = scanner.nextInt();
				int damage = scanner.nextInt();
				double range = scanner.nextDouble();
				int reload = scanner.nextInt();
				int coolDown = scanner.nextInt();
				System.err.println(type + " " + towerId + " " + owner + " " + x + " " + y + " " + damage + " " + range + " " + reload + " " + coolDown);
			}
			int attackerCount = scanner.nextInt();
			System.err.println(attackerCount);
			for (int i = 0; i < attackerCount; i++) {
				int attackerId = scanner.nextInt();
				int owner = scanner.nextInt();
				double x = scanner.nextDouble();
				double y = scanner.nextDouble();
				int hitPoints = scanner.nextInt();
				int maxHitPoints = scanner.nextInt();
				double currentSpeed = scanner.nextDouble();
				double maxSpeed = scanner.nextDouble();
				int slowTime = scanner.nextInt();
				int bounty = scanner.nextInt();
				System.err.println(attackerId + " " + owner + " " + x + " " + y + " " + hitPoints + " " + maxHitPoints + " " + currentSpeed + " " + maxSpeed + " " + slowTime + " " + bounty);
			}

			System.err.println("money: " + myMoney + "   lives: " + myLives);
			if (myMoney >= 350) {
				if (playerId == 0)
					System.out.println("BUILD 2 3 GUNTOWER;BUILD 13 2 HEALTOWER;BUILD 5 3 GLUETOWER;MSG MELDING YO");
				else
					System.out.println("BUILD 12 2 GUNTOWER;BUILD 10 3 FIRETOWER;MSG MELDING YO hra hrae");
			} else if (myMoney >= 200 && playerId == 1) {
				System.out.println("UPGRADE 3 DAMAGE;MSG MELDING YO");
			} else if (playerId == 1 && turn == 3) {
				System.out.println("BUILD 2 10 GUNTOWER;MSG MELDING YO hreah rea hr");
			} else
				System.out.println("PASS;MSG MELDING YO yoloyoylyl");
		}
	}
}
