package TowerDefense;

import java.util.ArrayList;

public class Tile {
	private int x;
	private int y;
	private boolean canyon;
	private Tile[] neighbors = new Tile[4];

	public Tile(int x, int y, boolean canyon) {
		this.x = x;
		this.y = y;
		this.canyon = canyon;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Tile[] getNeighbors() {
		return neighbors;
	}

	public boolean canEnter() {
		return canyon;
	}

	public boolean canBuild() {
		return !canyon;
	}

	public char getMapChar() {
		if (canyon)
			return '.';
		return '#';
	}

	public boolean isCanyon() {
		return canyon;
	}

	public void setCanyon(boolean canyon) {
		this.canyon = canyon;
	}

	@Override
	public String toString() {
		return x + " " + y;
	}

	private static int[] dx = { 0, 1, 0, -1 };
	private static int[] dy = { 1, 0, -1, 0 };

	public void initNeighbors(Tile[][] grid) {
		for (int dir = 0; dir < 4; dir++) {
			int x_ = x + dx[dir];
			int y_ = y + dy[dir];
			if (x_ >= 0 && x_ < grid.length && y_ >= 0 && y_ < grid[0].length) {
				neighbors[dir] = grid[x_][y_];
			}
		}
	}

	public ArrayList<SubTile> connectTo(Tile to) {
		ArrayList<SubTile> result = new ArrayList<>();
		if (this.x < to.x) {
			for (int i = 0; i < SubTile.SUBTILE_SIZE; i++)
				result.add(new SubTile(this, i, 0));
		} else if (this.y < to.y) {
			for (int i = 0; i < SubTile.SUBTILE_SIZE; i++)
				result.add(new SubTile(this, 0, i));
		} else if (this.x > to.x) {
			for (int i = SubTile.SUBTILE_SIZE; i > 0; i--)
				result.add(new SubTile(to, i, 0));
		} else if (this.y > to.y) {
			for (int i = SubTile.SUBTILE_SIZE; i > 0; i--)
				result.add(new SubTile(to, 0, i));
		}
		return result;
	}
}
