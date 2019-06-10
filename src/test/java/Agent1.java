import java.util.Locale;
import java.util.Scanner;

public class Agent1 {
	public static void main(String[] args) {
		Locale.setDefault(new Locale("en", "US"));
		Scanner scanner = new Scanner(System.in);

		while (true) {
			int myMoney = scanner.nextInt();
			int myLives = scanner.nextInt();
			System.err.println(myMoney + " " + myLives);
			int opponentMoney = scanner.nextInt();
			int opponentLives = scanner.nextInt();
			System.err.println(opponentMoney + " " + opponentLives);
			int entityCount = scanner.nextInt();
			System.err.println(entityCount);
			for (int i = 0; i < entityCount; i++) {
				String type = scanner.next();
				int entityId = scanner.nextInt();
				double x = scanner.nextDouble();
				double y = scanner.nextDouble();
				double param0 = scanner.nextDouble();
				int param1 = scanner.nextInt();
				int param2 = scanner.nextInt();
				int param3 = scanner.nextInt();
				int param4 = scanner.nextInt();
				System.err.println(type + " " + entityId + " " + x + " " + y + " " + param0 + " " + param1 + " " + param2 + " " + param3 + " " + param4);
			}

			System.err.println("money: " + myMoney + "   lives: " + myLives);
			if (myMoney >= 100)
				System.out.println("BUILD 2 3 GUNTOWER;BUILD 12 2 FIRETOWER;BUILD 5 3 GLUETOWER");
			else
				System.out.println("PASS");
		}
	}
}
