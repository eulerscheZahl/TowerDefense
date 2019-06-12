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

	public SubTile mirror(Tile[][] grid, int width, int height) {
		Tile t = new Tile(width - 2 - tile.getX(), height - 2 - tile.getY(), true);
		SubTile result = new SubTile(t, SUBTILE_SIZE - subX, SUBTILE_SIZE - subY);
		return result;
	}

	@Override
	public String toString() {
		return String.format("%.1f %.1f", getX(), getY());
	}
}
