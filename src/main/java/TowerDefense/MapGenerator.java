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
		BoardDraft draft = BoardDraft.GeneratePath(19, 11, 2);
		return draft.Grid;
	}

	static class BoardDraft {
		public int Width;
		public int Height;

		public Tile[][] Grid;

		public static BoardDraft GeneratePath(int width, int height) {
			BoardDraft board = new BoardDraft(width, height);
			while (!board.IsSymmetric())
				board = new BoardDraft(width, height);
			return board;
		}

		public static BoardDraft GeneratePath(int width, int height, int paths) {
			BoardDraft board = TryGeneratePath(width, height, paths);
			while (!board.IsSymmetric())
				board = TryGeneratePath(width, height, paths);
			return board;
		}

		private static BoardDraft TryGeneratePath(int width, int height, int paths) {
			ArrayList<BoardDraft> boards = new ArrayList<>();
			for (int i = 0; i < paths; i++)
				boards.add(GeneratePath(width, height));
			BoardDraft board = new BoardDraft(width, height);
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					final int x_ = x, y_ = y;
					board.Grid[x][y].setCanyon(boards.stream().anyMatch(b -> b.Grid[x_][y_].isCanyon()));
				}
			}

			ArrayList<Tile> start = new ArrayList<Tile>();
			ArrayList<Tile> exit = new ArrayList<Tile>();
			for (int y = 0; y < height; y++) {
				if (board.Grid[0][y].isCanyon())
					start.add(board.Grid[0][y]);
				if (board.Grid[width - 1][y].isCanyon())
					exit.add(board.Grid[width - 1][y]);
			}
			board.BuildPath(width, height, start, exit, false);
			return board;
		}

		public BoardDraft(int width, int height) {
			this.Width = width;
			this.Height = height;
			Grid = new Tile[width][height];
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					Grid[x][y] = new Tile(x, y, x % 2 == 1 && y % 2 == 1);
				}
			}
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					Grid[x][y].initNeighbors(Grid);
				}
			}

			MazeGen(Grid[1][1], new boolean[width][height]);
			ArrayList<Tile> exitCandidates = new ArrayList<Tile>();
			for (int y = 1; y < height; y += 2) {
				exitCandidates.add(Grid[0][y]);
			}
			Tile exit = exitCandidates.get(random.nextInt(exitCandidates.size()));
			exit.setCanyon(true);
			Tile center = Grid[width / 2][height / 2];
			center.setCanyon(true);

			ArrayList<Tile> exitList = new ArrayList<Tile>();
			exitList.add(exit);
			ArrayList<Tile> centerList = new ArrayList<Tile>();
			centerList.add(center);
			ArrayList<Tile> otherExitList = new ArrayList<Tile>();
			otherExitList.add(Grid[width - 1][height - 1 - exit.getY()]);
			BuildPath(width, height, exitList, centerList, true);
			BuildPath(width, height, exitList, otherExitList, false);
		}

		private void BuildPath(int width, int height, ArrayList<Tile> start, ArrayList<Tile> target, boolean mirror) {
			int[][] dist = BFS(start);
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
				if (dist[front.get(0).getX()][front.get(0).getY()] <= 0)
					break;
			}
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					Grid[x][y].setCanyon(false);
				}
			}
			for (Tile t : path) {
				t.setCanyon(true);
				if (mirror)
					Grid[width - 1 - t.getX()][height - 1 - t.getY()].setCanyon(true);
			}
		}

		private int[][] BFS(ArrayList<Tile> start) {
			int[][] result = new int[Width][Height];
			for (int x = 0; x < Width; x++) {
				for (int y = 0; y < Height; y++) {
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

		private void MazeGen(Tile current, boolean[][] visited) {
			visited[current.getX()][current.getY()] = true;
			ArrayList<Tile> candidates = new ArrayList<Tile>();
			for (int dir = 0; dir < 4; dir++) {
				int x = current.getX() + 2 * dx[dir];
				int y = current.getY() + 2 * dy[dir];
				if (x >= 0 && x < Width && y >= 0 && y < Height)
					candidates.add(Grid[x][y]);
			}

			while (candidates.size() > 0) {
				int index = random.nextInt(candidates.size());
				Tile next = candidates.get(index);
				candidates.remove(index);
				if (!visited[next.getX()][next.getY()]) {
					Tile middle = Grid[(current.getX() + next.getX()) / 2][(current.getY() + next.getY()) / 2];
					middle.setCanyon(true);
					MazeGen(next, visited);
				}
			}
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			for (int y = 0; y < Height; y++) {
				for (int x = 0; x < Width; x++) {
					sb.append(Grid[x][y]);
				}
				sb.append("\n");
			}
			return sb.toString();
		}

		public boolean IsSymmetric() {
			if (Width % 2 == 1 && Height % 2 == 1 && !Grid[Width / 2][Height / 2].isCanyon())
				return false;
			for (int x = 0; x < Width; x++) {
				for (int y = 0; y < Height; y++) {
					if (Grid[x][y].isCanyon() != Grid[Width - 1 - x][Height - 1 - y].isCanyon())
						return false;
				}
			}
			return true;
		}
	}
}
