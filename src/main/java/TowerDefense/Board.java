package TowerDefense;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;
import java.util.Random;
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
	private List<Player> players;
	private BoardView view;
	private List<List<Attacker>> futureAttackers = new ArrayList<>();
	private List<BuildAction> buildActions = new ArrayList<>();

	public Board(String s, List<Player> players, Random random) {
		this.players = players;
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

		for (int turn : new int[] { 1, 3, 5, 7, 9, 10 }) {
			List<SubTile> path = selectPath(paths);
			futureAttackers.get(turn).add(new Attacker(new ArrayList<SubTile>(path), players.get(1), players.get(0)));
			futureAttackers.get(turn).add(new Attacker(new ArrayList<SubTile>(path), players.get(0), players.get(1)));
		}
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

	public Player getPlayer(int index) {
		return players.get(index);
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
					int result = t1.getTile().getX() * height + t1.getTile().getY() - t2.getTile().getX() * height - t2.getTile().getY();
					if (t1.getOwner().getIndex() == 1)
						result *= -1;
					return result;
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
					t.getOwner().kill(a);
				}
			}
		}

		for (int i = attackers.size() - 1; i >= 0; i--) {
			Attacker a = attackers.get(i);
			if (a.hasSucceeded()) {
				attackers.remove(i);
				a.kill();
				a.getEnemy().loseLife();
			}
		}
	}

	public List<Attacker> getAttackers() {
		return attackers;
	}

	public void cacheBuild(Player player, int x, int y, String type) {
		for (BuildAction buildAction : buildActions) {
			if (buildAction.getX() == x && buildAction.getY() == y) {
				// TODO add error message for build conflict
				if (isCloserTo(player, x, y)) {
					buildAction.setPlayer(player);
					buildAction.setType(type);
				}
				return;
			}
		}

		buildActions.add(new BuildAction(player, x, y, type));
	}

	private boolean isCloserTo(Player player, int x, int y) {
		// if width is even
		if (width % 2 == 0) {
			if (player.getIndex() == 0) {
				return x < width / 2;
			} else {
				return x >= width / 2;
			}
		}
		// if width is odd
		else {
			if (player.getIndex() == 0) {
				if (x < width / 2) {
					return true;
				} else if (x == width / 2) {
					return y < height / 2;
				} else {
					return false;
				}
			} else {
				if (x > width / 2) {
					return true;
				} else if (x == width / 2) {
					return y >= height / 2;
				} else {
					return false;
				}
			}
		}
	}

	public void executeBuilds() {
		for (BuildAction buildAction : buildActions) {
			build(buildAction.getPlayer(), buildAction.getX(), buildAction.getY(), buildAction.getType());
		}
		buildActions.clear();
	}

	private void build(Player player, int x, int y, String type) {
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
		case "HEALTOWER":
			tower = new HealTower(grid[x][y]);
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

	public List<String> getPlayerInput(Player player, boolean initialInput) {
		List<String> input = new ArrayList<>();
		if (initialInput) {
			input.add(String.valueOf(player.getIndex()));
			input.add(width + " " + height);
			for (int y = 0; y < height; y++) {
				StringBuilder sb = new StringBuilder();
				for (int x = 0; x < width; x++) {
					sb.append(grid[x][y].getMapChar());
				}
				input.add(sb.toString());
			}
		}

		// player + opponent
		input.add(player.getPlayerInput());
		if (players.get(0) == player)
			input.add(players.get(1).getPlayerInput());
		else
			input.add(players.get(0).getPlayerInput());

		// towers, attackers
		input.add(String.valueOf(towers.size() + attackers.size()));
		for (Tower t : towers)
			input.add(t.getPlayerInput());
		for (Attacker a : attackers)
			input.add(a.getPlayerInput());
		return input;
	}
}
