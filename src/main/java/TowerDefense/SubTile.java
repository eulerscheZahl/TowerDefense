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
		Tile t = new Tile(width - 1 - tile.getX(), height - 1 - tile.getY(), true);
		//return new SubTile(new Tile(0, 0, true), SUBTILE_SIZE * (width - t.getX()) - subX, SUBTILE_SIZE * (height - t.getY()) - subY);
		return new SubTile(t, SUBTILE_SIZE - 1 - subX, SUBTILE_SIZE - 1 - subY);
	}

	@Override
	public String toString() {
		return String.format("%.1f %.1f", getX(), getY());
	}
}
