package object;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import entity.Entity;
import entity.Projectile;
import main.GamePanel;

public class OBJ_Fireball extends Projectile{
	GamePanel gp;

	public OBJ_Fireball(GamePanel gp) {
		super(gp);
		this.gp = gp;
		
		name = "Fireball";
		speed = 7;
		maxLife = 80;
		life = maxLife;
		attack = 2;
		useCost = 1;
		alive = false;
		getImage();
	}
	public void getImage() {
		cast1 = setup("/projectile/firecast1", gp.tileSize, gp.tileSize);
		cast2 = setup("/projectile/firecast2", gp.tileSize, gp.tileSize);
		cast3 = setup("/projectile/firecast3", gp.tileSize, gp.tileSize);
		cast4 = setup("/projectile/firecast4", gp.tileSize, gp.tileSize);
		cast5 = setup("/projectile/firecast5", gp.tileSize, gp.tileSize);
		impact1 = setup("/projectile/fireimpact1", gp.tileSize, gp.tileSize);
		impact2 = setup("/projectile/fireimpact2", gp.tileSize, gp.tileSize);
		impact3 = setup("/projectile/fireimpact3", gp.tileSize, gp.tileSize);
		up0 = setup("/projectile/firetravel1up", gp.tileSize, gp.tileSize);
		up1 = setup("/projectile/firetravel2up", gp.tileSize, gp.tileSize);
		up2 = setup("/projectile/firetravel3up", gp.tileSize, gp.tileSize);
		up3 = setup("/projectile/firetravel4up", gp.tileSize, gp.tileSize);
		up4 = setup("/projectile/firetravel5up", gp.tileSize, gp.tileSize);
		up5 = setup("/projectile/firetravel6up", gp.tileSize, gp.tileSize);
		up6 = setup("/projectile/firetravel7up", gp.tileSize, gp.tileSize);
		up7 = setup("/projectile/firetravel8up", gp.tileSize, gp.tileSize);
		down0 = setup("/projectile/firetravel1down", gp.tileSize, gp.tileSize);
		down1 = setup("/projectile/firetravel2down", gp.tileSize, gp.tileSize);
		down2 = setup("/projectile/firetravel3down", gp.tileSize, gp.tileSize);
		down3 = setup("/projectile/firetravel4down", gp.tileSize, gp.tileSize);
		down4 = setup("/projectile/firetravel5down", gp.tileSize, gp.tileSize);
		down5 = setup("/projectile/firetravel6down", gp.tileSize, gp.tileSize);
		down6 = setup("/projectile/firetravel7down", gp.tileSize, gp.tileSize);
		down7 = setup("/projectile/firetravel8down", gp.tileSize, gp.tileSize);
		left0 = setup("/projectile/firetravel1left", gp.tileSize, gp.tileSize);
		left1 = setup("/projectile/firetravel2left", gp.tileSize, gp.tileSize);
		left2 = setup("/projectile/firetravel3left", gp.tileSize, gp.tileSize);
		left3 = setup("/projectile/firetravel4left", gp.tileSize, gp.tileSize);
		left4 = setup("/projectile/firetravel5left", gp.tileSize, gp.tileSize);
		left5 = setup("/projectile/firetravel6left", gp.tileSize, gp.tileSize);
		left6 = setup("/projectile/firetravel7left", gp.tileSize, gp.tileSize);
		left7 = setup("/projectile/firetravel8left", gp.tileSize, gp.tileSize);
		right0 = setup("/projectile/firetravel1right", gp.tileSize, gp.tileSize);
		right1 = setup("/projectile/firetravel2right", gp.tileSize, gp.tileSize);
		right2 = setup("/projectile/firetravel3right", gp.tileSize, gp.tileSize);
		right3 = setup("/projectile/firetravel4right", gp.tileSize, gp.tileSize);
		right4 = setup("/projectile/firetravel5right", gp.tileSize, gp.tileSize);
		right5 = setup("/projectile/firetravel6right", gp.tileSize, gp.tileSize);
		right6 = setup("/projectile/firetravel7right", gp.tileSize, gp.tileSize);
		right7 = setup("/projectile/firetravel8right", gp.tileSize, gp.tileSize);
	}
	public boolean haveResources(Entity user) {
		return user.mana >= useCost;
	}
	public void subtractResources(Entity user) {
		user.mana -= useCost;
	}
	public void update() {
		if(user.casting == true) {
			spriteCounter++;
			if((spriteCounter + 1) % 4 == 0) {
				if(spriteCastNum == 0) spriteCastNum = 1;
				else if(spriteCastNum == 1) spriteCastNum = 2;
				else if(spriteCastNum == 2) spriteCastNum = 3;
				else if(spriteCastNum == 3) spriteCastNum = 4;
				else spriteCastNum = 0;
			}
			if(spriteCounter == 20) {
				user.casting = false;
				spriteCounter = 0;
			}
		}else if(user.impact == false){
		spriteCastNum = 0;
		switch(direction) {
		case "up": worldY -= speed; break;
		case "down": worldY += speed; break;
		case "left": worldX -= speed; break;
		case "right": worldX += speed; break;
		}
		spriteCounter++;
		if(spriteCounter > 7) {
			if(spriteNum == 0) spriteNum = 1;
			else if(spriteNum == 1) spriteNum = 2;
			else if(spriteNum == 2) spriteNum = 3;
			else if(spriteNum == 3) spriteNum = 4;
			else if(spriteNum == 4) spriteNum = 5;
			else if(spriteNum == 5) spriteNum = 6;
			else if(spriteNum == 6) spriteNum = 7;
			else if(spriteNum == 7) spriteNum = 0;
			spriteCounter = 0;
		}
		if(user == gp.player) {
			int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
			if(monsterIndex != 999) {
				user.impact = true;
				gp.player.damageMonster(monsterIndex);
				user.casting = false;
				spriteCounter = 0;
			}
		}else {
			boolean contactPlayer = gp.cChecker.checkPlayer(this);
			if(gp.player.invincible == false && contactPlayer == true) {
				damagePlayer(attack);
				alive = false;
			}
		}
		life--;
		if(life <= 0) {
			user.casting = false;
			alive = false;
		}
	}else {
		spriteCounter++;
		if(spriteCounter % 6 == 0) {
		if(spriteImpactNum == 0) spriteImpactNum = 1;
		else if(spriteImpactNum == 1) spriteImpactNum = 2;
		else spriteImpactNum = 0;
	}if(spriteCounter == 18) {
		user.impact = false;
		spriteCounter = 0;
		alive = false;
		casting = false;
	}
	}

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
		if(gp.player.casting == true) {
			if(spriteCastNum == 0) image = cast1;
			if(spriteCastNum == 1) image = cast2;
			if(spriteCastNum == 2) image = cast3;
			if(spriteCastNum == 3) image = cast4;
			if(spriteCastNum == 4) image = cast5;
		}else if(gp.player.impact == false) {
			
			switch(direction) {
			
			case "up":
				if(spriteNum == 0) image = up0;
				if(spriteNum == 1) image = up1;
				if(spriteNum == 2) image = up2;
				if(spriteNum == 3) image = up3;
				if(spriteNum == 4) image = up4;
				if(spriteNum == 5) image = up5;
				if(spriteNum == 6) image = up6;
				if(spriteNum == 7) image = up7;
				break;
			case "down":
				if(spriteNum == 0) image = down0;
				if(spriteNum == 1) image = down1;
				if(spriteNum == 2) image = down2;
				if(spriteNum == 3) image = down3;
				if(spriteNum == 4) image = down4;
				if(spriteNum == 5) image = down5;
				if(spriteNum == 6) image = down6;
				if(spriteNum == 7) image = down7;
				break;
			case "left":
				if(spriteNum == 0) image = left0;
				if(spriteNum == 1) image = left1;
				if(spriteNum == 2) image = left2;
				if(spriteNum == 3) image = left3;
				if(spriteNum == 4) image = left4;
				if(spriteNum == 5) image = left5;
				if(spriteNum == 6) image = left6;
				if(spriteNum == 7) image = left7;
				break;
			case "right":
				if(spriteNum == 0) image = right0;
				if(spriteNum == 1) image = right1;
				if(spriteNum == 2) image = right2;
				if(spriteNum == 3) image = right3;
				if(spriteNum == 4) image = right4;
				if(spriteNum == 5) image = right5;
				if(spriteNum == 6) image = right6;
				if(spriteNum == 7) image = right7;
				break;
			}
	}else {
		if(spriteImpactNum == 0) image = impact1;
		else if(spriteImpactNum == 1) image = impact2;
		else image = impact3;
		}
	
		g2.drawImage(image, screenX, screenY, null);
		}
	}
}
