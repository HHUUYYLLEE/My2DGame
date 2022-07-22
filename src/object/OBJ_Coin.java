package object;


import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import entity.Entity;
import main.GamePanel;

public class OBJ_Coin extends Entity{
	GamePanel gp;
	public OBJ_Coin(GamePanel gp) {
		super(gp);
		this.gp = gp;
		
		category = type_pickUpOnly;
		name = "Coin";
		moneyValue = 1;
		down0 = setup("/objects/coin1", gp.tileSize, gp.tileSize);
		down1 = setup("/objects/coin2", gp.tileSize, gp.tileSize);
		down2 = setup("/objects/coin3", gp.tileSize, gp.tileSize);
		down3 = setup("/objects/coin4", gp.tileSize, gp.tileSize);
		down4 = setup("/objects/coin5", gp.tileSize, gp.tileSize);
		down5 = setup("/objects/coin6", gp.tileSize, gp.tileSize);
	}
	public void use(Entity entity) {
		
		gp.playSE(1);
		gp.ui.addMessage("Coin + " + moneyValue);
		gp.player.coin += moneyValue;
	}
	public void update() {
		if(spriteCounter > 8) {
			if(spriteNum == 0) spriteNum = 1;
			else if(spriteNum == 1) spriteNum = 2;
			else if(spriteNum == 2) spriteNum = 3;
			else if(spriteNum == 3) spriteNum = 4;
			else if(spriteNum == 4) spriteNum = 5;
			else spriteNum = 0;
			spriteCounter = 0;
		}
		spriteCounter++;
}
	public void draw(Graphics2D g2) {
		
		BufferedImage image = null;
		int screenX, screenY;
		screenX = worldX - gp.player.worldX + gp.player.x;
		screenY = worldY - gp.player.worldY + gp.player.y;
		//Improve rendering efficiency by not render out of screen stuff (or dying off screen)
		if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
		   worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
		   worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
		   worldY - gp.tileSize < gp.player.worldY + gp.player.screenY){

			if(spriteNum == 0) image = down0;
			if(spriteNum == 1) image = down1;
			if(spriteNum == 2) image = down2;
			if(spriteNum == 3) image = down3;
			if(spriteNum == 4) image = down4;
			if(spriteNum == 5) image = down5;

		g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
		
	}
}
}
