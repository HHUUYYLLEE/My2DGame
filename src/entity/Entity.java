package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class Entity {
	GamePanel gp;
	
	//Location on map
	public int worldX, worldY;
	
	//Movement speed
	public int speed;
	
	//Handling sprites
	public BufferedImage up0, up1, up2, up3, down0, down1, down2, down3, left0, left1, left2, left3, right0, right1, right2, right3, dead0;
	public BufferedImage attackUp1, attackUp2, attackDown1, attackDown2, attackLeft1, attackLeft2, attackRight1, attackRight2;
	public BufferedImage cast1, cast2, cast3, cast4, cast5, impact1, impact2, impact3;
	public BufferedImage up4, up5, up6, up7, down4, down5, down6, down7, left4, left5, left6, left7, right4, right5, right6, right7;
	public String direction = "down";
	public int spriteCounter = 0;
	public int spriteNum = 1;
	public int spriteCastNum = 0;
	public int spriteImpactNum = 0;
	public int reverse = 0;
	public boolean moving = false;
	
	//Collisions
	public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
	public Rectangle attackArea = new Rectangle(0, 0, 0, 0);
	public int solidAreaDefaultX, solidAreaDefaultY;
	public boolean collisionOn = false;
	
	//Attack
	public boolean attacking = false;
	
	//NPCs and Enemies' movements
	public int actionLockCounter = 0;
	
	//Invincibility check
	public boolean invincible = false;
	public int invincibleCounter = 0;
	
	//Dialogue
	String dialogues[] = new String[20];
	public int dialogueIndex = 0;
	
	public BufferedImage image, image1, image2;
	
	//Entity state
	public boolean collision = false;
	public boolean alive = true;
	public boolean dead = false;
	public int dyingCounter = 0;
	public int shotCooldown = 0;
	public boolean casting = false;
	public boolean impact = false;
	
	//Character Status
	public String name;
	public int maxLife;
	public int life;
	public int level;
	public int strength;
	public int dexterity;
	public int attack;
	public int defense;
	public int exp;
	public int nextLevelExp;
	public int coin;
	public int mana;
	public int maxMana;
	public int ammo;
	public Entity currentWeapon;
	public Entity currentShield;
	public Projectile projectile;
	
	
	//ITEM ATTRIBUTES
	public int moneyValue;
	public int attackValue;
	public int defenseValue;
	public String description = "";
	public int useCost;
	
	//Category
	public int category = 999;
	public final int type_player = 0;
	public final int type_npc = 1;
	public final int type_monster = 2;
	public final int type_sword = 3;
	public final int type_magic = 4;
	public final int type_consumable = 5;
	public final int type_pickUpOnly= 6;
	public Entity(GamePanel gp) {
		this.gp = gp;
	}
	public void setAction() {
		actionLockCounter++;
		if(actionLockCounter == 120) {
		Random random = new Random();
		int i = random.nextInt(100) + 1; //random number 1-100 for npc to move
		if(i <= 20) {
			moving = true;
			direction = "up";
		}
		else if(i <= 40) {
			moving = true;
			direction = "down";
		}
		else if(i <= 60) {
			moving = true;
			direction = "left";
		}
		else if(i <= 80) {
			moving = true;
			direction = "right";
		}else moving = false;
		actionLockCounter = 0;
		}
	}
	public void damageReaction() {
		
		actionLockCounter = 0;
		switch(gp.player.direction) {
		case "up":
			direction = "down";
			break;
		case "down":
			direction = "up";
			break;
		case "left":
			direction = "right";
			break;
		case "right":
			direction = "left";
			break;
		}
		moving = true;
		
	}
	public void speak() {
		if(dialogues[dialogueIndex] == null) {
			dialogueIndex = 0;
		}
		gp.ui.currentDialogue = dialogues[dialogueIndex];
		dialogueIndex++;
		
		switch(gp.player.direction) {
		case "up": direction = "down"; break;
		case "down": direction = "up"; break;
		case "left": direction = "right"; break;
		case "right": direction = "left"; break;
		}
	}
	public void update() {
		setAction();
		collisionOn = false;
		gp.cChecker.checkTile(this);
		gp.cChecker.checkObject(this, false);
		gp.cChecker.checkEntity(this, gp.npc);
		gp.cChecker.checkEntity(this, gp.monster);
		boolean contactPlayer = gp.cChecker.checkPlayer(this);
		
		if(this.category == type_monster && contactPlayer == true && gp.player.invincible == false) {
				damagePlayer(attack);
		}
		
		//IF COLLISION IS FALSE, NPCS AND MONSTERS CAN MOVE
		if(collisionOn == false && moving == true) {
			switch(direction) {
			case "up": worldY -= speed; break;
			case "down": worldY += speed; break;
			case "left": worldX -= speed; break;
			case "right": worldX += speed; break;
			}
		}else moving = false;
	
		if(spriteCounter > 8 && moving == true) {
			if(spriteNum == 0 || (spriteNum == 1 && reverse == 0)) spriteNum = 2;
			else if(spriteNum == 2 && reverse == 0) spriteNum = 3;
			else if(spriteNum == 3 && reverse == 0) {
				reverse = 1;
				spriteNum = 2;
			}
			else if(spriteNum == 2 && reverse == 1) {
				reverse = 0;
				spriteNum = 1;
			}
			spriteCounter = 0;
		} else if(moving == false) spriteNum = 0;
		
		spriteCounter++;
		
		if(invincible == true) {
			invincibleCounter++;
			if(invincibleCounter > gp.FPS + gp.FPS / 5) {
				invincible = false;
				invincibleCounter = 0;
			}
		}
		if(shotCooldown < 80) shotCooldown++;
		
	}
	public void damagePlayer(int attack) {
		int damage = attack - gp.player.defense;
		if(damage <= 0) damage = 1;
		if(gp.player.life > damage) gp.playSE(8);
		else gp.playSE(10);
		gp.player.life -= damage;
		gp.player.invincible = true;
	}
	public void draw(Graphics2D g2) {
		
		BufferedImage image = null;
		int screenX, screenY;
		screenX = worldX - gp.player.worldX + gp.player.x;
		screenY = worldY - gp.player.worldY + gp.player.y;
		//Improve rendering efficiency by not render out of screen stuff (or dying off screen)
		if((worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
		   worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
		   worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
		   worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) || dyingCounter > 0){

				switch(direction) {
			
			case "up":
				if(spriteNum == 0) image = up0;
				if(spriteNum == 1) image = up1;
				if(spriteNum == 2) image = up2;
				if(spriteNum == 3) image = up3;
				break;
			case "down":
				if(spriteNum == 0) image = down0;
				if(spriteNum == 1) image = down1;
				if(spriteNum == 2) image = down2;
				if(spriteNum == 3) image = down3;
				break;
			case "left":
				if(spriteNum == 0) image = left0;
				if(spriteNum == 1) image = left1;
				if(spriteNum == 2) image = left2;
				if(spriteNum == 3) image = left3;
				break;
			case "right":
				if(spriteNum == 0) image = right0;
				if(spriteNum == 1) image = right1;
				if(spriteNum == 2) image = right2;
				if(spriteNum == 3) image = right3;
				break;
			}

			//Monsters' HP
			if(category == type_monster) {
				//Monsters' health bars
				double oneScale = (double)gp.tileSize / maxLife;
				double hpBarValue = oneScale * life;
				
			g2.setColor(new Color(35, 35, 35));
			g2.fillRect(screenX - 1 , screenY - 16, gp.tileSize, 12);
			
			g2.setColor(new Color(255, 0, 30));
			g2.fillRect(screenX, screenY - 15, (int)hpBarValue, 10);
			}
			
			
			if(invincible == true) g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
			if(dead == true) dyingAnimation(g2);
			
		g2.drawImage(image, screenX, screenY, null);
		
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
	}
}
	public void dyingAnimation(Graphics2D g2) {
		if(dyingCounter <= 66) changeAlpha(g2 , 0.9f);
		if(dyingCounter > 66 &&  dyingCounter <= 72) changeAlpha(g2 , 0.8f);
		if(dyingCounter > 72 && dyingCounter <= 78) changeAlpha(g2 , 0.7f);
		if(dyingCounter > 78 && dyingCounter <= 84) changeAlpha(g2 , 0.6f);
		if(dyingCounter > 84 && dyingCounter <= 90) changeAlpha(g2 , 0.5f);
		if(dyingCounter > 90 && dyingCounter <= 96) changeAlpha(g2 , 0.4f);
		if(dyingCounter > 96 && dyingCounter <= 102) changeAlpha(g2 , 0.3f);
		if(dyingCounter > 102 && dyingCounter <= 108) changeAlpha(g2 , 0.2f);
		if(dyingCounter > 108 && dyingCounter <= 114) changeAlpha(g2 , 0.1f);
		if(dyingCounter > 114 && dyingCounter <= 120) changeAlpha(g2 , 0f);
		if(dyingCounter > 120) {
			changeAlpha(g2 , 0f);
			alive = false;
		}
	}
	public void changeAlpha(Graphics2D g2, float alphaValue) {
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
	}
	public BufferedImage setup(String imagePath, int width, int height) {
		UtilityTool uTool = new UtilityTool();
		BufferedImage image = null;
		try {
			image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
			image = uTool.scaleImage(image, width, height);
		}catch(IOException e) {
			e.printStackTrace();
		}
		return image;
	}
	public void use(Entity entity) {
		//Only to override
	}
	
}
