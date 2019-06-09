package TowerDefense;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.codingame.game.Player;
import com.codingame.game.Referee;

import view.BoardView;

public class Board {
	private Tile[][] grid;
	private List<Attacker> attackers = new ArrayList<>();
	private List<Tower> towers = new ArrayList<>();
	private int width;
	private int height;
	private Player player;
	private BoardView view;
	private List<List<Attacker>> futureAttackers = new ArrayList<>();

	public Board(String s) {
		for (int i = 0; i <= Referee.GAME_TURNS; i++)
			futureAttackers.add(new ArrayList<>());

		String[] parts = s.split(" ");
		width = parts[0].length();
		height = parts.length;
		grid = new Tile[width][height];

		for (int y = 0; y < parts.length; y++) {
			for (int x = 0; x < parts[0].length(); x++) {
				grid[x][y] = new Tile(x, y, parts[y].charAt(x));
			}
		}

		for (int y = 0; y < parts.length; y++) {
			for (int x = 0; x < parts[0].length(); x++) {
				grid[x][y].initNeighbors(grid);
			}
		}

		List<Tile> targets = new ArrayList<>();
		for (int y = 0; y < height; y++) {
			Tile tile = grid[width - 1][y];
			if (tile.canEnter())
				targets.add(tile);
		}

		List<List<SubTile>> paths = new ArrayList<>();
		for (Tile target : targets) {
			findPaths(grid, width, height, target, paths);
		}

		futureAttackers.get(1).add(new Attacker(selectPath(paths)));
		futureAttackers.get(3).add(new Attacker(selectPath(paths)));
		futureAttackers.get(5).add(new Attacker(selectPath(paths)));
		futureAttackers.get(7).add(new Attacker(selectPath(paths)));
		futureAttackers.get(9).add(new Attacker(selectPath(paths)));
		futureAttackers.get(10).add(new Attacker(selectPath(paths)));
	}

	private List<SubTile> selectPath(List<List<SubTile>> paths) {
		int index = Referee.random.nextInt(paths.size());
		return new ArrayList<>(paths.get(index));
	}

	private void findPaths(Tile[][] grid, int width, int height, Tile target, List<List<SubTile>> paths) {
		int[][] dist = new int[width][height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				dist[x][y] = -1;
			}
		}

		dist[target.getX()][target.getY()] = 0;
		Queue<Tile> bfs = new ConcurrentLinkedQueue<>();
		bfs.add(target);

		while (bfs.size() > 0) {
			Tile sub = bfs.poll();
			for (Tile neighbor : sub.getNeighbors()) {
				if (neighbor == null || !neighbor.canEnter())
					continue;
				if (dist[neighbor.getX()][neighbor.getY()] >= 0)
					continue;
				dist[neighbor.getX()][neighbor.getY()] = dist[sub.getX()][sub.getY()] + 1;
				bfs.add(neighbor);
			}
		}

		buildPaths(grid, width, height, target, paths, dist, new ArrayList<Tile>());
	}

	private void buildPaths(Tile[][] grid, int width, int height, Tile currentTile, List<List<SubTile>> paths, int[][] dist, List<Tile> currentPath) {
		currentPath.add(currentTile);
		if (currentTile.getX() == 0) {
			ArrayList<Tile> path = new ArrayList<>(currentPath);
			path.add(0, new Tile(path.get(0).getX() + 1, path.get(0).getY(), '.'));
			path.add(new Tile(currentTile.getX() - 1, currentTile.getY(), '.'));
			ArrayList<SubTile> result = new ArrayList<>();
			for (int i = 1; i < path.size(); i++) {
				Tile t1 = path.get(i - 1);
				Tile t2 = path.get(i);
				for (SubTile sub : t1.connectTo(t2))
					result.add(sub);
			}
			paths.add(result);

			currentPath.remove(currentPath.size() - 1);
			return;
		}

		for (Tile neighbor : currentTile.getNeighbors()) {
			if (neighbor != null && dist[neighbor.getX()][neighbor.getY()] == dist[currentTile.getX()][currentTile.getY()] + 1)
				buildPaths(grid, width, height, neighbor, paths, dist, currentPath);
		}

		currentPath.remove(currentPath.size() - 1);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Tile[][] getGrid() {
		return grid;
	}

	public void setView(BoardView view) {
		this.view = view;
	}

	public void moveAttackers(int turn) {
		for (Attacker a : attackers)
			a.move();

		for (Attacker a : futureAttackers.get(turn)) {
			attackers.add(a);
			view.addAttacker(a);
		}
	}

	public void fireTowers() {
		Collections.sort(towers, new Comparator<Tower>() {
			@Override
			public int compare(Tower t1, Tower t2) {
				int t1Index = 0;
				while (!Tower.TowerOrder[t1Index].equals(t1.getType()))
					t1Index++;
				int t2Index = 0;
				while (!Tower.TowerOrder[t2Index].equals(t2.getType()))
					t2Index++;
				if (t1Index == t2Index) {
					return t1.getTile().getX() * height + t1.getTile().getY() - t2.getTile().getX() * height - t2.getTile().getY();
				}
				return t1Index - t2Index;
			}
		});

		for (Tower t : towers) {
			t.attack(attackers);
			for (int i = attackers.size() - 1; i >= 0; i--) {
				Attacker a = attackers.get(i);
				if (a.isDead()) {
					attackers.remove(i);
					player.kill(a);
				}
			}
		}

		for (int i = attackers.size() - 1; i >= 0; i--) {
			Attacker a = attackers.get(i);
			if (a.hasSucceeded()) {
				attackers.remove(i);
				a.kill();
				player.loseLife();
			}
		}
	}

	public List<Attacker> getAttackers() {
		return attackers;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void build(int x, int y, String type) {
		Tower tower = null;
		switch (type) {
		case "GUNTOWER":
			tower = new GunTower(grid[x][y]);
			break;
		case "FIRETOWER":
			tower = new FireTower(grid[x][y]);
			break;
		case "GLUETOWER":
			tower = new GlueTower(grid[x][y]);
			break;
		default:
			// TODO error for invalid type
			break;
		}
		if (!tower.getTile().canBuild()) {
			// TODO: error message
			return;
		}
		for (Tower t : towers) {
			if (t.getTile() == tower.getTile()) {
				// TODO: error message
				return;
			}
		}
		if (player.buy(tower)) {
			towers.add(tower);
			view.addTower(tower);
		}
	}

	public List<String> getPlayerInput() {
		List<String> input = new ArrayList<>();
		input.add(player.getPlayerInput());
		input.add(String.valueOf(towers.size() + attackers.size()));
		for (Tower t : towers)
			input.add(t.getPlayerInput());
		for (Attacker a : attackers)
			input.add(a.getPlayerInput());
		return input;
	}
}
