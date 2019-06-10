package TowerDefense;

import com.codingame.game.Player;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.tooltip.TooltipModule;
import view.TowerView;

import java.text.DecimalFormat;
import java.util.List;

public abstract class Tower {
	private int id;
	private String type;
	private Tile tile;
	private int[] upgradeStates;
	protected double[][] properties;
	private int cooldown;
	protected int cost;
	private final int[] upgradeCosts = { 0, 100, 200, 500 };
	private TowerView view;
	private Player owner;
	public static final String[] TowerOrder = { "HEALTOWER", "FIRETOWER", "GUNTOWER", "GLUETOWER" };

	static int idCounter = 0;

	public Tower(String type, Tile tile) {
		this.id = idCounter++;
		this.type = type;
		this.tile = tile;
		this.upgradeStates = new int[TowerProperty.values().length];
		this.properties = new double[TowerProperty.values().length][];
	}

	public boolean canUpgrade(TowerProperty property, Player player) {
		int upgradeState = upgradeStates[property.ordinal()];
		double[] values = properties[property.ordinal()];
		int upgradeCost = upgradeCosts[upgradeState];
		return values.length > upgradeState + 1 && player.getMoney() >= upgradeCost;
	}

	public double getProperty(TowerProperty property) {
		int updateState = upgradeStates[property.ordinal()];
		double[] values = properties[property.ordinal()];
		return values[updateState];
	}

	public int getCost() {
		return cost;
	}

	public Tile getTile() {
		return tile;
	}

	public String getType() {
		return type;
	}

	public int getId() {
		return id;
	}

	public TowerView getView() {
		return view;
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public void attack(List<Attacker> attackers) {
		if (cooldown > 0) {
			cooldown--;
			return;
		}
		if (doAttack(attackers)) {
			cooldown = (int) getProperty(TowerProperty.SPEED);
		}
	}

	public boolean inRange(Attacker a) {
		double dx = a.getLocation().getX() - tile.getX();
		double dy = a.getLocation().getY() - tile.getY();
		int rangeIndex = TowerProperty.RANGE.ordinal();
		double range = properties[rangeIndex][upgradeStates[rangeIndex]];
		double dist = Math.sqrt(dx * dx + dy * dy);
		return range >= dist;
	}

	abstract boolean doAttack(List<Attacker> attackers);

	public String getPlayerInput() {
		StringBuilder sb = new StringBuilder();
		sb.append(type).append(" ");
		sb.append(id).append(" ");
		sb.append(owner.getIndex()).append(" ");
		sb.append(tile.toString()).append(" ");
		for (TowerProperty p : TowerProperty.values()) {
			sb.append(new DecimalFormat("0.#").format(getProperty(p))).append(" ");
		}
		sb.append(cooldown).append(" ");
		sb.append(-1);
		return sb.toString();
	}


    public String getTooltipString(){
        StringBuilder sb = new StringBuilder();
        sb.append("x: ").append(tile.getX()).append("\ny: ").append(tile.getY());
        sb.append("\ntype: ").append(type);
        sb.append("\nid: ").append(id);
        sb.append("\nowner: ").append(owner.getIndex());
        for (TowerProperty p : TowerProperty.values()) {
            sb.append("\n").append(p).append(": ").append(new DecimalFormat("0.#").format(getProperty(p)));
        }
        sb.append("\ncooldown: ").append(cooldown);
        return sb.toString();
    }

	public abstract TowerView createView(Group boardGroup, GraphicEntityModule graphics, TooltipModule tooltipModule);

	public void setView(TowerView view) {
		this.view = view;
	}

}
