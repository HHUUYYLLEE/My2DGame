package object;


import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import main.Entity;
import main.GamePanel;

public class OBJ_Coin extends Entity{
	GamePanel gp;
	public OBJ_Coin(GamePanel gp) {
		super(gp);
		this.gp = gp;
		
		setCategory(type_pickUpOnly);
		setName("Coin");
		setMoneyValue(1);
		setDown0(setup("/objects/coin1", gp.getTileSize(), gp.getTileSize()));
		setDown1(setup("/objects/coin2", gp.getTileSize(), gp.getTileSize()));
		setDown2(setup("/objects/coin3", gp.getTileSize(), gp.getTileSize()));
		setDown3(setup("/objects/coin4", gp.getTileSize(), gp.getTileSize()));
		setDown4(setup("/objects/coin5", gp.getTileSize(), gp.getTileSize()));
		setDown5(setup("/objects/coin6", gp.getTileSize(), gp.getTileSize()));
	}
	public void use(Entity entity) {
		
		gp.playSE(1);
		gp.getUi().addMessage("Coin + " + getMoneyValue());
		gp.getPlayer().increaseCoin(getMoneyValue());
	}
	public void update() {
		if(getSpriteCounter() > 8) {
			if(getSpriteNum() == 0) setSpriteNum(1);
			else if(getSpriteNum() == 1) setSpriteNum(2);
			else if(getSpriteNum() == 2) setSpriteNum(3);
			else if(getSpriteNum() == 3) setSpriteNum(4);
			else if(getSpriteNum() == 4) setSpriteNum(5);
			else setSpriteNum(0);
			setSpriteCounter(0);
		}
		setSpriteCounter(getSpriteCounter() + 1);
}
	public void draw(Graphics2D g2) {
		
		BufferedImage image = null;
		int screenX, screenY;
		screenX = getWorldX() - gp.getPlayer().getWorldX() + gp.getPlayer().getX();
		screenY = getWorldY() - gp.getPlayer().getWorldY() + gp.getPlayer().getY();
		//Improve rendering efficiency by not render out of screen stuff (or dying off screen)
		if(getWorldX() + gp.getTileSize() > gp.getPlayer().getWorldX() - gp.getPlayer().getScreenX() &&
		   getWorldX() - gp.getTileSize() < gp.getPlayer().getWorldX() + gp.getPlayer().getScreenX() &&
		   getWorldY() + gp.getTileSize() > gp.getPlayer().getWorldY() - gp.getPlayer().getScreenY() &&
		   getWorldY() - gp.getTileSize() < gp.getPlayer().getWorldY() + gp.getPlayer().getScreenY()){

			if(getSpriteNum() == 0) image = getDown0();
			if(getSpriteNum() == 1) image = getDown1();
			if(getSpriteNum() == 2) image = getDown2();
			if(getSpriteNum() == 3) image = getDown3();
			if(getSpriteNum() == 4) image = getDown4();
			if(getSpriteNum() == 5) image = getDown5();

		g2.drawImage(image, screenX, screenY, gp.getTileSize(), gp.getTileSize(), null);
		
	}
}
}
