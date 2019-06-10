import java.util.Locale;
import java.util.Scanner;

public class Agent1 {
	public static void main(String[] args) {
		Locale.setDefault(new Locale("en", "US"));
		Scanner scanner = new Scanner(System.in);
		String initialInput = "";
		int myId = scanner.nextInt();
		initialInput += myId + "\n";
		int width = scanner.nextInt();
		int height = scanner.nextInt();
		initialInput += width + " " + height + "\n";
		scanner.nextLine();
		for (int y = 0; y < height; y++) {
			String line = scanner.nextLine();
			initialInput += line + "\n";
		}
		while (true) {
			int myMoney = scanner.nextInt();
			int myLives = scanner.nextInt();
			System.err.println(initialInput + myMoney + " " + myLives);
			int opponentMoney = scanner.nextInt();
			int opponentLives = scanner.nextInt();
			System.err.println(opponentMoney + " " + opponentLives);
			int entityCount = scanner.nextInt();
			System.err.println(entityCount);
			for (int i = 0; i < entityCount; i++) {
				String type = scanner.next();
				int entityId = scanner.nextInt();
				int owner = scanner.nextInt();
				double x = scanner.nextDouble();
				double y = scanner.nextDouble();
				double param0 = scanner.nextDouble();
				int param1 = scanner.nextInt();
				int param2 = scanner.nextInt();
				int param3 = scanner.nextInt();
				int param4 = scanner.nextInt();
				System.err.println(type + " " + entityId + " " + owner + " " + x + " " + y + " " + param0 + " " + param1 + " " + param2 + " " + param3 + " " + param4);
			}

			System.err.println("money: " + myMoney + "   lives: " + myLives);
			if (myMoney >= 100) {
				if (myId == 0)
					System.out.println("BUILD 2 3 GUNTOWER;BUILD 13 2 HEALTOWER;BUILD 5 3 GLUETOWER");
				else
					System.out.println("BUILD 12 2 GUNTOWER;BUILD 11 4 GUNTOWER;BUILD 12 4 GUNTOWER");
			} else
				System.out.println("PASS");
		}
	}
}
