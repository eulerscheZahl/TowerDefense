package TowerDefense;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public class MapGenerator {
	private static Random random;

	public static Tile[][] generateMap(Random random) {
		MapGenerator.random = random;
		BoardDraft draft = BoardDraft.GeneratePath(Constants.MAP_WIDTH, Constants.MAP_HEIGHT, 2);
		return draft.grid;
	}

	static class BoardDraft {
		private int width;
		private int height;
		private Tile[][] grid;

		public static BoardDraft generatePath(int width, int height) {
			BoardDraft board = new BoardDraft(width, height);
			while (!board.isSymmetric() || board.getPathLength() < Constants.MIN_PATH_LENGTH)
				board = new BoardDraft(width, height);
			return board;
		}

		private int getPathLength() {
			ArrayList<Tile> start = new ArrayList<Tile>();
			ArrayList<Tile> exit = new ArrayList<Tile>();
			for (int y = 0; y < height; y++) {
				if (grid[0][y].isCanyon())
					start.add(grid[0][y]);
				if (grid[width - 1][y].isCanyon())
					exit.add(grid[width - 1][y]);
			}
			int[][] dist = bfs(start);
			int result = Integer.MAX_VALUE;
			for (Tile t : exit) {
				result = Math.min(result, dist[t.getX()][t.getY()]);
			}
			return result;
		}

		public static BoardDraft GeneratePath(int width, int height, int paths) {
			BoardDraft board = tryGeneratePath(width, height, paths);
			while (!board.isSymmetric())
				board = tryGeneratePath(width, height, paths);
			return board;
		}

		private static BoardDraft tryGeneratePath(int width, int height, int paths) {
			ArrayList<BoardDraft> boards = new ArrayList<>();
			for (int i = 0; i < paths; i++)
				boards.add(generatePath(width, height));
			BoardDraft board = new BoardDraft(width, height);
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					final int x_ = x, y_ = y;
					board.grid[x][y].setCanyon(boards.stream().anyMatch(b -> b.grid[x_][y_].isCanyon()));
				}
			}

			ArrayList<Tile> start = new ArrayList<Tile>();
			ArrayList<Tile> exit = new ArrayList<Tile>();
			for (int y = 0; y < height; y++) {
				if (board.grid[0][y].isCanyon())
					start.add(board.grid[0][y]);
				if (board.grid[width - 1][y].isCanyon())
					exit.add(board.grid[width - 1][y]);
			}
			board.buildPath(width, height, start, exit, false);
			return board;
		}

		public BoardDraft(int width, int height) {
			this.width = width;
			this.height = height;
			grid = new Tile[width][height];
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					grid[x][y] = new Tile(x, y, x % 2 == 1 && y % 2 == 1);
				}
			}
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					grid[x][y].initNeighbors(grid);
				}
			}

			mazeGen(grid[1][1], new boolean[width][height]);
			ArrayList<Tile> exitCandidates = new ArrayList<Tile>();
			for (int y = 1; y < height; y += 2) {
				exitCandidates.add(grid[0][y]);
			}
			Tile exit = exitCandidates.get(random.nextInt(exitCandidates.size()));
			exit.setCanyon(true);
			Tile center = grid[width / 2][height / 2];
			center.setCanyon(true);

			ArrayList<Tile> exitList = new ArrayList<Tile>();
			exitList.add(exit);
			ArrayList<Tile> centerList = new ArrayList<Tile>();
			centerList.add(center);
			ArrayList<Tile> otherExitList = new ArrayList<Tile>();
			otherExitList.add(grid[width - 1][height - 1 - exit.getY()]);
			buildPath(width, height, exitList, centerList, true);
			buildPath(width, height, exitList, otherExitList, false);
		}

		private void buildPath(int width, int height, ArrayList<Tile> start, ArrayList<Tile> target, boolean mirror) {
			int[][] dist = bfs(start);
			ArrayList<Tile> path = new ArrayList<Tile>(target);
			ArrayList<Tile> front = new ArrayList<Tile>(target);
			while (true) {
				ArrayList<Tile> newFront = new ArrayList<Tile>();
				for (Tile t : front.stream().flatMap(f -> Arrays.stream(f.getNeighbors())).distinct().collect(Collectors.toList())) {
					if (t != null && dist[t.getX()][t.getY()] + 1 == dist[front.get(0).getX()][front.get(0).getY()]) {
						newFront.add(t);
					}
				}
				front = newFront;
				path.addAll(front);
				if (front.size() == 0 || dist[front.get(0).getX()][front.get(0).getY()] <= 0)
					break;
			}
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					grid[x][y].setCanyon(false);
				}
			}
			for (Tile t : path) {
				t.setCanyon(true);
				if (mirror)
					grid[width - 1 - t.getX()][height - 1 - t.getY()].setCanyon(true);
			}
		}

		private int[][] bfs(ArrayList<Tile> start) {
			int[][] result = new int[width][height];
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					result[x][y] = -1;
				}
			}
			ConcurrentLinkedQueue<Tile> queue = new ConcurrentLinkedQueue<Tile>();
			for (Tile t : start) {
				queue.add(t);
				result[t.getX()][t.getY()] = 0;
			}
			while (queue.size() > 0) {
				Tile t = queue.poll();
				for (Tile n : t.getNeighbors()) {
					if (n == null || !n.isCanyon() || result[n.getX()][n.getY()] != -1)
						continue;
					result[n.getX()][n.getY()] = result[t.getX()][t.getY()] + 1;
					queue.add(n);
				}
			}
			return result;
		}

		private final int[] dx = { 0, 1, 0, -1 };
		private final int[] dy = { 1, 0, -1, 0 };

		private void mazeGen(Tile current, boolean[][] visited) {
			visited[current.getX()][current.getY()] = true;
			ArrayList<Tile> candidates = new ArrayList<Tile>();
			for (int dir = 0; dir < 4; dir++) {
				int x = current.getX() + 2 * dx[dir];
				int y = current.getY() + 2 * dy[dir];
				if (x >= 0 && x < width && y >= 0 && y < height)
					candidates.add(grid[x][y]);
			}

			while (candidates.size() > 0) {
				int index = random.nextInt(candidates.size());
				Tile next = candidates.get(index);
				candidates.remove(index);
				if (!visited[next.getX()][next.getY()]) {
					Tile middle = grid[(current.getX() + next.getX()) / 2][(current.getY() + next.getY()) / 2];
					middle.setCanyon(true);
					mazeGen(next, visited);
				}
			}
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					sb.append(grid[x][y].getMapChar());
				}
				sb.append("\n");
			}
			return sb.toString();
		}

		public boolean isSymmetric() {
			if (width % 2 == 1 && height % 2 == 1 && !grid[width / 2][height / 2].isCanyon())
				return false;
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					if (grid[x][y].isCanyon() != grid[width - 1 - x][height - 1 - y].isCanyon())
						return false;
				}
			}
			return true;
		}
	}
}
