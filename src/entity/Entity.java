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
	private int worldX, worldY;
	
	//Movement speed
	private int speed;
	
	//Handling sprites
	private BufferedImage up0, up1, up2, up3, down0, down1, down2, down3, left0, left1, left2, left3, right0, right1, right2, right3, dead0;
	private BufferedImage attackUp1, attackUp2;

	private BufferedImage attackDown1;

	private BufferedImage attackDown2;

	private BufferedImage attackLeft1;

	private BufferedImage attackLeft2;

	private BufferedImage attackRight1;

	private BufferedImage attackRight2;
	private BufferedImage cast1, cast2, cast3, cast4, cast5, impact1, impact2, impact3;
	private BufferedImage up4, up5, up6, up7, down4, down5, down6, down7, left4, left5, left6, left7, right4, right5, right6, right7;
	private String direction = "down";
	private int spriteCounter = 0;
	private int spriteNum = 1;
	private int spriteCastNum = 0;
	private int spriteImpactNum = 0;
	private int reverse = 0;
	private boolean moving = false;
	
	//Collisions
	private Rectangle solidArea = new Rectangle(0, 0, 48, 48);
	private Rectangle attackArea = new Rectangle(0, 0, 0, 0);
	private int solidAreaDefaultX, solidAreaDefaultY;
	private boolean collisionOn = false;
	
	//Attack
	private boolean attacking = false;
	
	//NPCs and Enemies' movements
	private int actionLockCounter = 0;
	
	//Invincibility check
	private boolean invincible = false;
	private int invincibleCounter = 0;
	
	//Dialogue
	private String dialogues[] = new String[20];
	private int dialogueIndex = 0;
	
	private BufferedImage image, image1, image2;
	
	//Entity state
	private boolean collision = false;
	private boolean alive = true;
	private boolean dead = false;
	private int dyingCounter = 0;
	private int shotCooldown = 0;
	private boolean casting = false;
	private boolean impact = false;
	
	//Character Status
	private String name;
	private int maxLife;
	private int life;
	private int level;
	private int strength;
	private int dexterity;
	private int attack;
	private int defense;
	private int exp;
	private int nextLevelExp;
	private int coin;
	private int mana;
	private int maxMana;
	private int ammo;
	private Entity currentWeapon;
	private Entity currentShield;
	private Projectile projectile;
	
	
	//ITEM ATTRIBUTES
	private int moneyValue;
	private int attackValue;
	private int defenseValue;
	private String description = "";
	private int useCost;
	
	//Category
	private int category = 999;
	public final int type_player = 0;
	public final int type_npc = 1;
	public final int type_monster = 2;
	public final int type_sword = 3;
	public final int type_magic = 4;
	public final int type_consumable = 5;
	public final int type_pickUpOnly = 6;
	
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
		switch(gp.getPlayer().getDirection()) {
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
	public void checkDrop() {
		
	}
	public void dropItem(Entity droppedItem) {
		for(int i = 0; i < gp.getObj().length; ++i) {
			if(gp.getObj()[i] == null) {
				gp.getObj()[i] = droppedItem;
				gp.getObj()[i].setWorldX(getWorldX());
				gp.getObj()[i].setWorldY(getWorldY());
				break;
			}
		}
	}
	public void speak() {
		if(dialogues[dialogueIndex] == null) {
			dialogueIndex = 0;
		}
		gp.getUi().setCurrentDialogue(dialogues[dialogueIndex]);
		dialogueIndex++;
		
		switch(gp.getPlayer().getDirection()) {
		case "up": direction = "down"; break;
		case "down": direction = "up"; break;
		case "left": direction = "right"; break;
		case "right": direction = "left"; break;
		}
	}
	public void update() {
		setAction();
		checkAllCollisions();
		
		//IF COLLISION IS FALSE, NPCS AND MONSTERS CAN MOVE
		if(!collisionOn && moving) move();
		else moving = false;
		
		updateSprite();
		
		updateInvincibility();
		if(shotCooldown < 80) shotCooldown++;
	}
	
	private void updateInvincibility() {
		if(invincible) {
			invincibleCounter++;
			if(invincibleCounter > gp.getFPS() + gp.getFPS() / 5) {
				invincible = false;
				invincibleCounter = 0;
			}
		}
	}
	private void updateSprite() {
		if(spriteCounter > 8 && moving) {
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
		} else if(!moving) spriteNum = 0;
		spriteCounter++;
	}
	private void move() {
		switch(direction) {
		case "up": worldY -= speed; break;
		case "down": worldY += speed; break;
		case "left": worldX -= speed; break;
		case "right": worldX += speed; break;
		}
	}
	private void checkAllCollisions() {
		collisionOn = false;
		gp.getcChecker().checkTile(this);
		gp.getcChecker().checkObject(this, false);
		gp.getcChecker().checkEntity(this, gp.getNpc());
		gp.getcChecker().checkEntity(this, gp.getMonster());
		boolean contactPlayer = gp.getcChecker().checkPlayer(this);
		
		if(this.category == type_monster && contactPlayer && !gp.getPlayer().isInvincible()) damagePlayer(attack);
	}
	public void damagePlayer(int attack) {
		int damage = attack - gp.getPlayer().getDefense();
		if(damage <= 0) damage = 1;
		if(gp.getPlayer().getLife() > damage) gp.playSE(8);
		else gp.playSE(10);
		gp.getPlayer().decreaseLife(damage);
		gp.getPlayer().setInvincible(true);
	}
	public void draw(Graphics2D g2) {
		
		BufferedImage image = null;
		int screenX, screenY;
		screenX = worldX - gp.getPlayer().getWorldX() + gp.getPlayer().getX();
		screenY = worldY - gp.getPlayer().getWorldY() + gp.getPlayer().getY();
		//Improve rendering efficiency by not render out of screen stuff (or dying off screen)
		if((worldX + gp.getTileSize() > gp.getPlayer().getWorldX() - gp.getPlayer().getScreenX() &&
		   worldX - gp.getTileSize() < gp.getPlayer().getWorldX() + gp.getPlayer().getScreenX() &&
		   worldY + gp.getTileSize() > gp.getPlayer().getWorldY() - gp.getPlayer().getScreenY() &&
		   worldY - gp.getTileSize() < gp.getPlayer().getWorldY() + gp.getPlayer().getScreenY()) || dyingCounter > 0){

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
				double oneScale = (double)gp.getTileSize() / maxLife;
				double hpBarValue = oneScale * life;
				
			g2.setColor(new Color(35, 35, 35));
			g2.fillRect(screenX - 1 , screenY - 16, gp.getTileSize(), 12);
			
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
	public int getWorldX() {
		return worldX;
	}
	public void setWorldX(int worldX) {
		this.worldX = worldX;
	}
	public int getWorldY() {
		return worldY;
	}
	public void setWorldY(int worldY) {
		this.worldY = worldY;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public String getDirection() {
		return direction;
	}
	public BufferedImage getUp0() {
		return up0;
	}
	public void setUp0(BufferedImage up0) {
		this.up0 = up0;
	}
	public BufferedImage getUp1() {
		return up1;
	}
	public void setUp1(BufferedImage up1) {
		this.up1 = up1;
	}
	public BufferedImage getUp2() {
		return up2;
	}
	public void setUp2(BufferedImage up2) {
		this.up2 = up2;
	}
	public BufferedImage getUp3() {
		return up3;
	}
	public void setUp3(BufferedImage up3) {
		this.up3 = up3;
	}
	public BufferedImage getDown0() {
		return down0;
	}
	public void setDown0(BufferedImage down0) {
		this.down0 = down0;
	}
	public BufferedImage getDown1() {
		return down1;
	}
	public void setDown1(BufferedImage down1) {
		this.down1 = down1;
	}
	public BufferedImage getDown2() {
		return down2;
	}
	public void setDown2(BufferedImage down2) {
		this.down2 = down2;
	}
	public BufferedImage getDown3() {
		return down3;
	}
	public void setDown3(BufferedImage down3) {
		this.down3 = down3;
	}
	public BufferedImage getLeft0() {
		return left0;
	}
	public void setLeft0(BufferedImage left0) {
		this.left0 = left0;
	}
	public BufferedImage getLeft1() {
		return left1;
	}
	public void setLeft1(BufferedImage left1) {
		this.left1 = left1;
	}
	public BufferedImage getLeft2() {
		return left2;
	}
	public void setLeft2(BufferedImage left2) {
		this.left2 = left2;
	}
	public BufferedImage getLeft3() {
		return left3;
	}
	public void setLeft3(BufferedImage left3) {
		this.left3 = left3;
	}
	public BufferedImage getRight0() {
		return right0;
	}
	public void setRight0(BufferedImage right0) {
		this.right0 = right0;
	}
	public BufferedImage getRight1() {
		return right1;
	}
	public void setRight1(BufferedImage right1) {
		this.right1 = right1;
	}
	public BufferedImage getRight2() {
		return right2;
	}
	public void setRight2(BufferedImage right2) {
		this.right2 = right2;
	}
	public BufferedImage getRight3() {
		return right3;
	}
	public void setRight3(BufferedImage right3) {
		this.right3 = right3;
	}
	public BufferedImage getDead0() {
		return dead0;
	}
	public void setDead0(BufferedImage dead0) {
		this.dead0 = dead0;
	}
	public BufferedImage getAttackUp1() {
		return attackUp1;
	}
	public void setAttackUp1(BufferedImage attackUp1) {
		this.attackUp1 = attackUp1;
	}
	public BufferedImage getAttackUp2() {
		return attackUp2;
	}
	public void setAttackUp2(BufferedImage attackUp2) {
		this.attackUp2 = attackUp2;
	}
	public BufferedImage getAttackDown1() {
		return attackDown1;
	}
	public void setAttackDown1(BufferedImage attackDown1) {
		this.attackDown1 = attackDown1;
	}
	public BufferedImage getAttackDown2() {
		return attackDown2;
	}
	public void setAttackDown2(BufferedImage attackDown2) {
		this.attackDown2 = attackDown2;
	}
	public BufferedImage getAttackLeft1() {
		return attackLeft1;
	}
	public void setAttackLeft1(BufferedImage attackLeft1) {
		this.attackLeft1 = attackLeft1;
	}
	public BufferedImage getAttackLeft2() {
		return attackLeft2;
	}
	public void setAttackLeft2(BufferedImage attackLeft2) {
		this.attackLeft2 = attackLeft2;
	}
	public BufferedImage getAttackRight1() {
		return attackRight1;
	}
	public void setAttackRight1(BufferedImage attackRight1) {
		this.attackRight1 = attackRight1;
	}
	public BufferedImage getAttackRight2() {
		return attackRight2;
	}
	public void setAttackRight2(BufferedImage attackRight2) {
		this.attackRight2 = attackRight2;
	}
	public BufferedImage getCast1() {
		return cast1;
	}
	public void setCast1(BufferedImage cast1) {
		this.cast1 = cast1;
	}
	public BufferedImage getCast2() {
		return cast2;
	}
	public void setCast2(BufferedImage cast2) {
		this.cast2 = cast2;
	}
	public BufferedImage getCast3() {
		return cast3;
	}
	public void setCast3(BufferedImage cast3) {
		this.cast3 = cast3;
	}
	public BufferedImage getCast4() {
		return cast4;
	}
	public void setCast4(BufferedImage cast4) {
		this.cast4 = cast4;
	}
	public BufferedImage getCast5() {
		return cast5;
	}
	public void setCast5(BufferedImage cast5) {
		this.cast5 = cast5;
	}
	public BufferedImage getImpact1() {
		return impact1;
	}
	public void setImpact1(BufferedImage impact1) {
		this.impact1 = impact1;
	}
	public BufferedImage getImpact2() {
		return impact2;
	}
	public void setImpact2(BufferedImage impact2) {
		this.impact2 = impact2;
	}
	public BufferedImage getImpact3() {
		return impact3;
	}
	public void setImpact3(BufferedImage impact3) {
		this.impact3 = impact3;
	}
	public BufferedImage getUp4() {
		return up4;
	}
	public void setUp4(BufferedImage up4) {
		this.up4 = up4;
	}
	public BufferedImage getUp5() {
		return up5;
	}
	public void setUp5(BufferedImage up5) {
		this.up5 = up5;
	}
	public BufferedImage getUp6() {
		return up6;
	}
	public void setUp6(BufferedImage up6) {
		this.up6 = up6;
	}
	public BufferedImage getUp7() {
		return up7;
	}
	public void setUp7(BufferedImage up7) {
		this.up7 = up7;
	}
	public BufferedImage getDown4() {
		return down4;
	}
	public void setDown4(BufferedImage down4) {
		this.down4 = down4;
	}
	public BufferedImage getDown5() {
		return down5;
	}
	public void setDown5(BufferedImage down5) {
		this.down5 = down5;
	}
	public BufferedImage getDown6() {
		return down6;
	}
	public void setDown6(BufferedImage down6) {
		this.down6 = down6;
	}
	public BufferedImage getDown7() {
		return down7;
	}
	public void setDown7(BufferedImage down7) {
		this.down7 = down7;
	}
	public BufferedImage getLeft4() {
		return left4;
	}
	public void setLeft4(BufferedImage left4) {
		this.left4 = left4;
	}
	public BufferedImage getLeft5() {
		return left5;
	}
	public void setLeft5(BufferedImage left5) {
		this.left5 = left5;
	}
	public BufferedImage getLeft6() {
		return left6;
	}
	public void setLeft6(BufferedImage left6) {
		this.left6 = left6;
	}
	public BufferedImage getLeft7() {
		return left7;
	}
	public void setLeft7(BufferedImage left7) {
		this.left7 = left7;
	}
	public BufferedImage getRight4() {
		return right4;
	}
	public void setRight4(BufferedImage right4) {
		this.right4 = right4;
	}
	public BufferedImage getRight5() {
		return right5;
	}
	public void setRight5(BufferedImage right5) {
		this.right5 = right5;
	}
	public BufferedImage getRight6() {
		return right6;
	}
	public void setRight6(BufferedImage right6) {
		this.right6 = right6;
	}
	public BufferedImage getRight7() {
		return right7;
	}
	public void setRight7(BufferedImage right7) {
		this.right7 = right7;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public int getSpriteCounter() {
		return spriteCounter;
	}
	public void setSpriteCounter(int spriteCounter) {
		this.spriteCounter = spriteCounter;
	}
	public int getSpriteNum() {
		return spriteNum;
	}
	public void setSpriteNum(int spriteNum) {
		this.spriteNum = spriteNum;
	}
	public int getSpriteCastNum() {
		return spriteCastNum;
	}
	public void setSpriteCastNum(int spriteCastNum) {
		this.spriteCastNum = spriteCastNum;
	}
	public int getSpriteImpactNum() {
		return spriteImpactNum;
	}
	public void setSpriteImpactNum(int spriteImpactNum) {
		this.spriteImpactNum = spriteImpactNum;
	}
	public int getReverse() {
		return reverse;
	}
	public void setReverse(int reverse) {
		this.reverse = reverse;
	}
	public boolean isMoving() {
		return moving;
	}
	public void setMoving(boolean moving) {
		this.moving = moving;
	}
	public Rectangle getSolidArea() {
		return solidArea;
	}
	public void setSolidArea(Rectangle solidArea) {
		this.solidArea = solidArea;
	}
	public Rectangle getAttackArea() {
		return attackArea;
	}
	public void setAttackArea(Rectangle attackArea) {
		this.attackArea = attackArea;
	}
	public int getSolidAreaDefaultX() {
		return solidAreaDefaultX;
	}
	public void setSolidAreaDefaultX(int solidAreaDefaultX) {
		this.solidAreaDefaultX = solidAreaDefaultX;
	}
	public int getSolidAreaDefaultY() {
		return solidAreaDefaultY;
	}
	public void setSolidAreaDefaultY(int solidAreaDefaultY) {
		this.solidAreaDefaultY = solidAreaDefaultY;
	}
	public boolean isCollisionOn() {
		return collisionOn;
	}
	public void setCollisionOn(boolean collisionOn) {
		this.collisionOn = collisionOn;
	}
	public boolean isAttacking() {
		return attacking;
	}
	public void setAttacking(boolean attacking) {
		this.attacking = attacking;
	}
	public int getActionLockCounter() {
		return actionLockCounter;
	}
	public void setActionLockCounter(int actionLockCounter) {
		this.actionLockCounter = actionLockCounter;
	}
	public boolean isInvincible() {
		return invincible;
	}
	public void setInvincible(boolean invincible) {
		this.invincible = invincible;
	}
	public int getInvincibleCounter() {
		return invincibleCounter;
	}
	public void setInvincibleCounter(int invincibleCounter) {
		this.invincibleCounter = invincibleCounter;
	}
	public String[] getDialogues() {
		return dialogues;
	}
	public void setDialogues(String[] dialogues) {
		this.dialogues = dialogues;
	}
	public int getDialogueIndex() {
		return dialogueIndex;
	}
	public void setDialogueIndex(int dialogueIndex) {
		this.dialogueIndex = dialogueIndex;
	}
	public BufferedImage getImage() {
		return image;
	}
	public void setImage(BufferedImage image) {
		this.image = image;
	}
	public BufferedImage getImage1() {
		return image1;
	}
	public void setImage1(BufferedImage image1) {
		this.image1 = image1;
	}
	public BufferedImage getImage2() {
		return image2;
	}
	public void setImage2(BufferedImage image2) {
		this.image2 = image2;
	}
	public boolean isCollision() {
		return collision;
	}
	public void setCollision(boolean collision) {
		this.collision = collision;
	}
	public boolean isAlive() {
		return alive;
	}
	public void setAlive(boolean alive) {
		this.alive = alive;
	}
	public boolean isDead() {
		return dead;
	}
	public void setDead(boolean dead) {
		this.dead = dead;
	}
	public int getDyingCounter() {
		return dyingCounter;
	}
	public void setDyingCounter(int dyingCounter) {
		this.dyingCounter = dyingCounter;
	}
	public void increaseDyingCounter(int counter) {
		this.dyingCounter += counter;
	}
	public int getShotCooldown() {
		return shotCooldown;
	}
	public void setShotCooldown(int shotCooldown) {
		this.shotCooldown = shotCooldown;
	}
	public boolean isCasting() {
		return casting;
	}
	public void setCasting(boolean casting) {
		this.casting = casting;
	}
	public boolean isImpact() {
		return impact;
	}
	public void setImpact(boolean impact) {
		this.impact = impact;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getMaxLife() {
		return maxLife;
	}
	public void setMaxLife(int maxLife) {
		this.maxLife = maxLife;
	}
	public void increaseMaxLife(int count) {
		this.maxLife += count;
	}
	public int getLife() {
		return life;
	}
	public void increaseLife(int life) {
		this.life +=life;
	}
	public void decreaseLife(int life) {
		this.life -=life;
	}
	public void setLife(int life) {
		this.life = life;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public void increaseLevel(int count) {
		this.level += count;
	}
	public int getStrength() {
		return strength;
	}
	public void setStrength(int strength) {
		this.strength = strength;
	}
	public void increaseStrength(int count) {
		this.strength += count;
	}
	public int getDexterity() {
		return dexterity;
	}
	public void setDexterity(int dexterity) {
		this.dexterity = dexterity;
	}
	public void increaseDexterity(int count) {
		this.dexterity += count;
	}
	public int getAttack() {
		return attack;
	}
	public void setAttack(int attack) {
		this.attack = attack;
	}
	public int getDefense() {
		return defense;
	}
	public void setDefense(int defense) {
		this.defense = defense;
	}
	public void increaseDefense(int count) {
		this.strength += count;
	}
	public int getExp() {
		return exp;
	}
	public void setExp(int exp) {
		this.exp = exp;
	}
	public void increaseExp(int count) {
		this.strength += count;
	}
	public int getNextLevelExp() {
		return nextLevelExp;
	}
	public void setNextLevelExp(int nextLevelExp) {
		this.nextLevelExp = nextLevelExp;
	}
	public int getCoin() {
		return coin;
	}
	public void setCoin(int coin) {
		this.coin = coin;
	}
	public void increaseCoin(int counter) {
		this.coin += counter;
	}
	public void decreaseCoin(int counter) {
		this.coin -= counter;
	}
	public int getMana() {
		return mana;
	}
	public void setMana(int mana) {
		this.mana = mana;
	}
	public void increaseMana(int counter) {
		this.mana += counter;
	}
	public void decreaseMana(int counter) {
		this.mana -= counter;
	}
	public int getMaxMana() {
		return maxMana;
	}
	public void setMaxMana(int maxMana) {
		this.maxMana = maxMana;
	}
	public int getAmmo() {
		return ammo;
	}
	public void setAmmo(int ammo) {
		this.ammo = ammo;
	}
	public Entity getCurrentWeapon() {
		return currentWeapon;
	}
	public void setCurrentWeapon(Entity currentWeapon) {
		this.currentWeapon = currentWeapon;
	}
	public Entity getCurrentShield() {
		return currentShield;
	}
	public void setCurrentShield(Entity currentShield) {
		this.currentShield = currentShield;
	}
	public Projectile getProjectile() {
		return projectile;
	}
	public void setProjectile(Projectile projectile) {
		this.projectile = projectile;
	}
	public int getMoneyValue() {
		return moneyValue;
	}
	public void setMoneyValue(int moneyValue) {
		this.moneyValue = moneyValue;
	}
	public int getAttackValue() {
		return attackValue;
	}
	public void setAttackValue(int attackValue) {
		this.attackValue = attackValue;
	}
	public int getDefenseValue() {
		return defenseValue;
	}
	public void setDefenseValue(int defenseValue) {
		this.defenseValue = defenseValue;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getUseCost() {
		return useCost;
	}
	public void setUseCost(int useCost) {
		this.useCost = useCost;
	}
	public int getCategory() {
		return category;
	}
	public void setCategory(int category) {
		this.category = category;
	}
	public void decreaseAmmo(int count) {
		this.ammo -= count;
	}
	
}
