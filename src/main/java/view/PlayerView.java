package view;

import com.codingame.game.Player;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.entities.Sprite;
import com.codingame.gameengine.module.entities.Text;

public class PlayerView {
	private Player player;
	private GraphicEntityModule graphics;
	private Group group;
	private Sprite background;
	private Text gold;
	private Text lives;
	private Text pseudo;
	private Text message;
	private Sprite avatar;

	private int currentGold;
	private int currentLives;
	private String currentMessage;

	public PlayerView(Player player, GraphicEntityModule graphics) {
		this.player = player;
		player.setView(this);
		this.graphics = graphics;
		createPlayerView();
	}

	public void createPlayerView() {
		int baseY = player.getIndex() == 0 ? 12 : 744;

		avatar = graphics.createSprite().setAnchor(0).setBaseHeight(200).setBaseWidth(200).setImage(player.getAvatarToken()).setX(50).setY(baseY + 350 / 2 - 200 / 2).setZIndex(20);

		//background = entityModule.createSprite().setAnchor(0).setImage("HUD_" + color + ".png").setX(238 - 50 - PLAYER_AVATAR_RADIUS / 2).setY(baseY);

		int textGap = 85;
		pseudo = graphics.createText(player.getNicknameToken()).setAnchor(0.5).setFillColor(0xffffff).setFontSize(60).setStrokeColor(0x000000).setStrokeThickness(4.0).setX(460).setY(baseY + textGap).setFillColor(player.getColor()).setZIndex(-1);

		gold = graphics.createText("").setAnchor(0.5).setFillColor(0xffffff).setFontSize(40).setStrokeColor(0x000000).setStrokeThickness(4.0).setX(855 / 2).setY(baseY + textGap + 70);

		lives = graphics.createText("").setAnchor(0.5).setFillColor(0xffffff).setFontSize(40).setStrokeColor(0x000000).setStrokeThickness(4.0).setX(855 / 2).setY(baseY + textGap + 70 + 50);

		message = graphics.createText("").setAnchor(0.5).setFillColor(0xffffff).setFontSize(35).setStrokeColor(0x000000).setStrokeThickness(4.0).setX(855 / 2).setY(baseY + textGap + 70 + 50 + 50).setZIndex(-1);

		group = graphics.createGroup().setScale(1).setX(0).setY(0);
		group.add(avatar, pseudo, gold, lives, message);
	}

	public void updateView() {
		if (player.getMoney() != this.currentGold) {
			this.currentGold = player.getMoney();
			this.gold.setText("Gold: " + currentGold);
		}
		if (player.getLives() != this.currentLives) {
			this.currentLives = player.getLives();
			this.lives.setText("Lives: " + currentLives);
		}
		if (!player.getMessage().equals(currentMessage)) {
			this.currentMessage = player.getMessage();
			this.message.setText(currentMessage);
		}
	}
}
