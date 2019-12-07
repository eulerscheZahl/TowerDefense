package TowerDefense;

import java.util.ArrayList;
import java.util.Arrays;
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
	private List<Attacker> veterans = new ArrayList<>();
	private List<Tower> towers = new ArrayList<>();
	private int width;
	private int height;
	private List<Player> players;
	private BoardView view;
	private int waveIndex = 0;
	private int earliestWaveStart = 1;
	private List<List<Attacker>> futureAttackers = new ArrayList<>();
	private List<BuildAction> buildActions = new ArrayList<>();
	private List<List<SubTile>> paths = new ArrayList<>();
	private int waveNumber = 0;

	public Board(Tile[][] tiles, List<Player> players, Random random) {
		this.players = players;
		for (int i = 0; i <= Constants.TURN_COUNT + Constants.WAVE_TIME; i++)
			futureAttackers.add(new ArrayList<>());

		grid = tiles;
		width = tiles.length;
		height = tiles[0].length;

		List<Tile> targets = new ArrayList<>();
		for (int y = 0; y < height; y++) {
			Tile tile = grid[width - 1][y];
			if (tile.canEnter())
				targets.add(tile);
		}

		for (Tile target : targets) {
			findPaths(grid, width, height, target, paths);
		}
	}

	private List<SubTile> mirrorPath(List<SubTile> path) {
		List<SubTile> result = new ArrayList<SubTile>();
		for (SubTile s : path) {
			result.add(s.mirror(grid, width, height));
		}
		return result;
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
			path.add(0, new Tile(path.get(0).getX() + 1, path.get(0).getY(), true));
			path.add(new Tile(currentTile.getX() - 1, currentTile.getY(), true));
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

	public List<List<SubTile>> getPaths() {
		return paths;
	}

	public Player getPlayer(int index) {
		return players.get(index);
	}

	public void setView(BoardView view) {
		this.view = view;
	}

	public void moveAttackers(int turn) {
		for (Attacker a : veterans)
			a.move();
		for (Attacker a : attackers)
			a.move();
	}

	private boolean canCreateAttackers(int turn) {
		if (turn == Constants.WAVE_START[waveIndex])
			return true;
		if (turn < earliestWaveStart)
			return false;
		for (Player p : players) {
			boolean playerReady = true;
			for (Attacker a : attackers) {
				if (a.getOwner() == p && a.getPathLength() * Constants.WAVE_COMPLETION > paths.get(0).size())
					playerReady = false;
			}
			if (playerReady)
				return true;
		}
		return false;
	}

	private void createAttackers(int turn) {
		earliestWaveStart = turn + Constants.WAVE_TIME + 1;
		for (int unit = 0; unit < Constants.WAVE_COUNT[waveIndex]; unit++) {
			int time = turn + Referee.random.nextInt(Constants.WAVE_TIME);

			List<SubTile> path = selectPath(paths);
			for (int remove = Referee.random.nextInt(10); remove > 0; remove--)
				path.remove(path.size() - 1);
			List<SubTile> mirror = mirrorPath(path);
			int hp = Constants.WAVE_HP[waveIndex];
			int speed = Constants.WAVE_SPEED[waveIndex];
			int bounty = Constants.WAVE_BOUNTY[waveIndex];
			futureAttackers.get(time).add(new Attacker(mirror, hp, speed, bounty, players.get(1), players.get(0)));
			futureAttackers.get(time).add(new Attacker(path, hp, speed, bounty, players.get(0), players.get(1)));
		}
		waveNumber++;

		if (waveIndex + 1 < Constants.WAVE_START.length)
			waveIndex++;
		else { // make up new waves on the spot, if none defined
			Constants.WAVE_COUNT[waveIndex]++;
			Constants.WAVE_HP[waveIndex]++;
			Constants.WAVE_START[waveIndex] += 10;
		}
	}

	public void spawnAttackers(int turn) {
		if (canCreateAttackers(turn))
			createAttackers(turn);
		for (Attacker a : futureAttackers.get(turn)) {
			attackers.add(a);
			view.addAttacker(a);
		}
	}

	public void fireTowers() {
		Collections.sort(towers, new Comparator<Tower>() {
			@Override
			public int compare(Tower t1, Tower t2) {
				int t1Index = Arrays.asList(Tower.TowerOrder).indexOf(t1.getType());
				int t2Index = Arrays.asList(Tower.TowerOrder).indexOf(t2.getType());
				if (t1Index == t2Index) {
					if (t1.getOwner() != t2.getOwner()) // order doesn't matter here, but search will crash without definite ordering
						return t1.getOwner().getIndex() - t2.getOwner().getIndex();
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
				veterans.add(a);
				a.kill();
				a.getEnemy().loseLife();
			}
		}
	}

	public List<Attacker> getAttackers() {
		return attackers;
	}

	public void cacheBuild(Player player, int x, int y, String type) throws InvalidActionException {
		for (BuildAction buildAction : buildActions) {
			if (buildAction.getX() == x && buildAction.getY() == y) {
				if (isCloserTo(player, x, y)) {
					buildAction.setPlayer(player);
					buildAction.setType(type);
				}

				throw new InvalidActionException("Tile (" + x + "/" + y + ") already occupied", false, players.get(1 - buildAction.getPlayer().getIndex()));
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

	public boolean executeBuilds() throws InvalidActionException {
		if (buildActions.size() == 0)
			return false;
		BuildAction buildAction = buildActions.get(0);
		buildActions.remove(buildAction);
		build(buildAction.getPlayer(), buildAction.getX(), buildAction.getY(), buildAction.getType());
		return true;
	}

	private void build(Player player, int x, int y, String type) throws InvalidActionException {
		Tower tower = null;
		switch (type.toUpperCase()) {
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
			throw new InvalidActionException("tower type " + type + " unknown", true, player);
		}
		if (!tower.getTile().canBuild()) {
			tower.undoBuild();
			throw new InvalidActionException("Tile (" + x + "/" + y + ") is a canyon", false, player);
		}
		for (Tower t : towers) {
			if (t.getTile() == tower.getTile()) {
				tower.undoBuild();
				throw new InvalidActionException("Tile (" + x + "/" + y + ") is occupied by another tower already", false, player);
			}
		}
		if (player.buy(tower)) {
			towers.add(tower);
			view.addTower(tower);
		} else {
			tower.undoBuild();
			throw new InvalidActionException("not enough money to build a " + type, false, player);
		}
	}

	public void upgrade(Player player, int id, String type) throws InvalidActionException {
		type = type.toUpperCase();
		Tower tower = towers.stream().filter((t) -> t.getId() == id).findFirst().orElse(null);
		if (tower == null)
			throw new InvalidActionException("Tower " + id + " not found", true, player);
		if (tower.getOwner() != player)
			throw new InvalidActionException("Tower " + id + " belongs to the opponent", false, player);
		TowerProperty toUpgrade = TowerProperty.DAMAGE;
		if (type.equals("RANGE"))
			toUpgrade = TowerProperty.RANGE;
		else if (type.equals("RELOAD"))
			toUpgrade = TowerProperty.RELOAD;
		else if (!type.equals("DAMAGE"))
			throw new InvalidActionException("Upgrade attribute " + type + " unknown", true, player);
		if (!tower.canUpgrade(toUpgrade))
			throw new InvalidActionException("can't upgrade " + type + " of tower " + id, false, player);
		tower.upgrade(toUpgrade);
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
		input.add(String.valueOf(towers.size()));
		Comparator<Tower> compareById = (Tower t1, Tower t2) -> t1.getId() - t2.getId();
		Collections.sort(towers, compareById);
		for (Tower t : towers)
			input.add(t.getPlayerInput());

		input.add(String.valueOf(attackers.size()));
		for (Attacker a : attackers)
			input.add(a.getPlayerInput());
		return input;
	}

	public String getWaveInfo() {
		int index = Math.min(waveNumber, Constants.WAVE_COUNT.length) - 1;
		return "Wave " + waveNumber
				+ "\ncount: " + Constants.WAVE_COUNT[index]
				+ "\nhealth: " + Constants.WAVE_HP[index]
				+ "\nspeed: " + ((double)Constants.WAVE_SPEED[index] / SubTile.SUBTILE_SIZE)
				+ "\nbounty: " + Constants.WAVE_BOUNTY[index];
	}

	public void updateView() {
		view.updateView();
		for (Player player : players) {
			player.updateView();
		}
	}
}
