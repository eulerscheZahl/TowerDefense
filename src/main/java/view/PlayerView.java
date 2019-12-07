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
		Sprite frame = graphics.createSprite().setZIndex(-10000).setImage("Player_panel.png").setTint(0xebebeb).setX(0).setY(0).setBaseWidth(713).setBaseHeight(367);
		avatar = graphics.createSprite().setAnchor(0.5).setBaseHeight(245).setBaseWidth(245).setImage(player.getAvatarToken()).setX(132).setY(239).setZIndex(20);

		//background = entityModule.createSprite().setAnchor(0).setImage("HUD_" + color + ".png").setX(238 - 50 - PLAYER_AVATAR_RADIUS / 2).setY(baseY);

		pseudo = graphics.createText(player.getNicknameToken()).setAnchor(0.5).setFontSize(60).setStrokeColor(4).setStrokeColor(0x000000).setStrokeThickness(1.0).setY(57).setX(361).setFillColor(player.getColor()).setZIndex(-1);
		int textPos = 490;
		int textGap = 87;
		gold = graphics.createText("").setAnchor(0.5).setFillColor(0x000000).setFontSize(50).setStrokeColor(0x000000).setStrokeThickness(1.0).setX(textPos).setY(154);

		lives = graphics.createText("").setAnchor(0.5).setFillColor(0x000000).setFontSize(50).setStrokeColor(0x000000).setStrokeThickness(1.0).setX(textPos).setY(gold.getY()+textGap);

		message = graphics.createText("").setAnchorY(0.5).setAnchorX(0).setFillColor(0x000000).setFontSize(35).setStrokeColor(0x000000).setStrokeThickness(0.0).setX(textPos-120).setY(lives.getY()+textGap).setZIndex(-1);

		group = graphics.createGroup().setScale(1).setX(0).setY(player.getIndex() == 0 ? 0 : (1080-375));
		group.add( avatar, pseudo, gold, lives, message, frame);
	}

	public void updateView() {
		if (player.getMoney() != this.currentGold) {
			this.currentGold = player.getMoney();
			this.gold.setText(currentGold+"");
		}
		if (player.getLives() != this.currentLives) {
			this.currentLives = player.getLives();
			this.lives.setText(currentLives+"");
		}
		if (!player.getMessage().equals(currentMessage)) {
			this.currentMessage = player.getMessage();
			this.message.setText((currentMessage+"                ").substring(0, 15));
		}
	}
}
