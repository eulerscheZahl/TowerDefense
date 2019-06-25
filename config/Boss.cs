using System;
using System.Linq;
using System.Collections.Generic;

class Player
{
	static void Main (string[] args)
	{
		string[] inputs;
		int playerID = int.Parse (Console.ReadLine ());
		inputs = Console.ReadLine ().Split (' ');
		int width = int.Parse (inputs [0]);
		int height = int.Parse (inputs [1]);
		Tile[,] grid = new Tile[width, height];
		for (int y = 0; y < height; y++) {
			string line = Console.ReadLine ();
			for (int x = 0; x < width; x++) {
				grid [x, y] = new Tile (x, y, line [x]);
			}
		}
		List<Tile> tileList = new List<Tile> ();
		foreach (Tile t in grid)
			tileList.Add (t);

		while (true) {
			inputs = Console.ReadLine ().Split (' ');
			int myMoney = int.Parse (inputs [0]);
			int myLives = int.Parse (inputs [1]);
			inputs = Console.ReadLine ().Split (' ');
			int opponentMoney = int.Parse (inputs [0]);
			int opponentLives = int.Parse (inputs [1]);
			int towerCount = int.Parse (Console.ReadLine ());
			for (int i = 0; i < towerCount; i++) {
				inputs = Console.ReadLine ().Split (' ');
				string type = inputs [0];
				int towerId = int.Parse (inputs [1]);
				int owner = int.Parse (inputs [2]);
				int x = int.Parse (inputs [3]);
				int y = int.Parse (inputs [4]);
				int damage = int.Parse (inputs [5]);
				double range = double.Parse (inputs [6]);
				int reload = int.Parse (inputs [7]);
				int coolDown = int.Parse (inputs [8]);
				grid [x, y].Tower = true;
			}
			int attackerCount = int.Parse (Console.ReadLine ());
			for (int i = 0; i < attackerCount; i++) {
				inputs = Console.ReadLine ().Split (' ');
				int attackerId = int.Parse (inputs [0]);
				int owner = int.Parse (inputs [1]);
				double x = double.Parse (inputs [2]);
				double y = double.Parse (inputs [3]);
				int hitPoints = int.Parse (inputs [4]);
				int maxHitPoints = int.Parse (inputs [5]);
				double currentSpeed = double.Parse (inputs [6]);
				double maxSpeed = double.Parse (inputs [7]);
				int slowTime = int.Parse (inputs [8]);
				int bounty = int.Parse (inputs [9]);
			}

			List<Tile> candidates = tileList.Where (t => !t.Tower && !t.Canyon).ToList ();
			candidates = candidates.OrderByDescending (c => c.TowerScore (grid, width, height)).ToList ();
			string output = "PASS";
			while (myMoney >= 100 && candidates.Count > 0) {
				myMoney -= 100;
				Tile take = candidates [0];
				candidates.Remove (take);
				output += ";BUILD " + take.X + " " + take.Y + " GUNTOWER";
			}
			Console.WriteLine (output);
		}
	}
}

class Tile
{
	public readonly int X, Y;
	public bool Tower;
	public bool Canyon;

	public Tile (int x, int y, char c)
	{
		this.X = x;
		this.Y = y;
		this.Canyon = c == '.';
	}

	public double TowerScore (Tile[,] grid, int width, int height)
	{
		double score = 0;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (!grid [x, y].Canyon)
					continue;
				double dist = grid [x, y].Dist (this);
				score += 1.0 / (dist * dist);
			}
		}
		return score;
	}

	public double Dist (Tile t)
	{
		int dx = X - t.X;
		int dy = Y - t.Y;
		return Math.Sqrt (dx * dx + dy * dy);
	}
}
