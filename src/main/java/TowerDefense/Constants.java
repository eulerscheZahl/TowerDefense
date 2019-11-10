package TowerDefense;

public class Constants {
	public static final int MAP_WIDTH = 17; // keep the dimensions odd for best map generation results
	public static final int MAP_HEIGHT = 17;
	public static final double ENFORCE_BRANCHING_PROBABILITY = 0.6;
	public static final int TURN_COUNT = 200;
	public static final int MIN_PATH_LENGTH = 30;

	public static final int PLAYER_MONEY = 350;
	public static final int PLAYER_LIVES = 1000;

	public static final int GUNTOWER_COST = 100;
	public static final double[] GUNTOWER_DAMAGE = { 5, 8, 15, 30 };
	public static final double[] GUNTOWER_RANGE = { 3, 4, 5, 6 };
	public static final double[] GUNTOWER_RELOAD = { 5, 4, 3, 2 };
	public static final int FIRETOWER_COST = 100;
	public static final double[] FIRETOWER_DAMAGE = { 2, 3, 5, 7 };
	public static final double[] FIRETOWER_RANGE = { 1.5, 2, 2.3, 2.5 };
	public static final double[] FIRETOWER_RELOAD = { 8, 7, 6, 5 };
	public static final int FIRE_EFFECT_DURATION = 2;
	public static final int GLUETOWER_COST = 70;
	public static final double[] GLUETOWER_DAMAGE = { 8, 15, 25, 40 }; // duration of slowdown effect
	public static final double[] GLUETOWER_RANGE = { 3, 4, 5, 6 };
	public static final double[] GLUETOWER_RELOAD = { 4, 3, 2, 1 };
	public static final int GLUE_SLOWDOWN = 4;
	public static final int HEALTOWER_COST = 100;
	public static final double[] HEALTOWER_DAMAGE = { 5, 8, 15, 30 };
	public static final double[] HEALTOWER_RANGE = { 3, 4, 5, 6 };
	public static final double[] HEALTOWER_RELOAD = { 5, 4, 3, 2 };
	public static final int[] TOWER_UPGRADE_COSTS = { 50, 100, 150, 0 };

	public static final int WAVE_TIME = 3;
	public static final double WAVE_COMPLETION = 1.5;
	public static final int[] WAVE_START = { 1, 16, 31, 46, 61, 71, 81, 91, 101 };
	public static final int[] WAVE_COUNT = { 4, 4, 5, 6, 6, 7, 7, 8, 8 };
	public static final int[] WAVE_SPEED = { 10, 12, 10, 15, 10, 10, 12, 13, 14 };
	public static final int[] WAVE_HP = { 5, 8, 12, 8, 12, 15, 15, 15, 15 };
	public static final int[] WAVE_BOUNTY = { 25, 30, 30, 20, 22, 25, 25, 30, 30 };
	
	public static final int NUM_UPGRADE_SPRITES = 3;
}
