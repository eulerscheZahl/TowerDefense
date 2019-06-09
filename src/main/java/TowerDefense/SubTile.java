package TowerDefense;

public class SubTile {
	public final static int SUBTILE_SIZE = 10;
	private Tile tile;
	private int subX;
	private int subY;

	public SubTile(Tile tile, int subX, int subY) {
		this.tile = tile;
		this.subX = subX;
		this.subY = subY;
	}

	public Tile getTile() {
		return tile;
	}

	public double getX() {
		return tile.getX() + (double) subX / SUBTILE_SIZE;
	}

	public double getY() {
		return tile.getY() + (double) subY / SUBTILE_SIZE;
	}

	@Override
	public String toString() {
		return String.format("%.1f %.1f", getX(), getY());
	}
}
