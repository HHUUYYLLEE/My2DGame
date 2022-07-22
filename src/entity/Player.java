package entity;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
//import java.sql.Connection;
import java.util.Random;

//import main.Connect;
import main.GamePanel;
import main.KeyHandler;
import object.OBJ_Fireball;
import object.OBJ_Shield_Wood;
import object.OBJ_Sword_Normal;

public class Player extends Entity{
	KeyHandler keyH;
	public int thud=0;
	public final int screenX;
	public final int screenY;
	public int tempScreenX, tempScreenY, x, y;
	int spriteAttackNum;
	int healingFrame;
	public ArrayList<Entity> inventory = new ArrayList<>();
	public final int maxInventorySize = gp.ui.slotsInAColumn * gp.ui.slotsInARow;
	Random random = new Random();
	
	
	public Player(GamePanel gp, KeyHandler keyH) {
		super(gp);
		this.keyH = keyH;
		screenX = gp.screenWidth / 2 - gp.tileSize / 2;
		screenY = gp.screenHeight / 2 - gp.tileSize / 2;
		
		solidArea = new Rectangle(gp.tileSize / 6, gp.tileSize / 3, gp.tileSize * 2 / 3, gp.tileSize * 2 / 3);//8, 16, 32, 32
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		
		
		setDefaultValues();
		getPlayerImage();
		getPlayerAttackImage();
		setItems();
	}
	public void setDefaultValues() {
		healingFrame = 0;
		name = "Player";
		worldX = gp.tileSize * 23;
		worldY = gp.tileSize * 21;
		speed = /*gp.database.getPlayerValue("Speed");*/ 4;
		direction = "left";
		//Player's default status
		level = 1;
		maxLife = /*gp.database.getPlayerValue("HP");*/ 8;
		life = maxLife;
		maxMana = 8;
		mana = maxMana;
		ammo = 10;
		strength = 4; //Deals more damage
		dexterity = 1;//Takes less damage
		exp = 0;
		nextLevelExp = 5;
		coin = 0;
		inventory.clear();
		currentWeapon = new OBJ_Sword_Normal(gp);
		currentShield = new OBJ_Shield_Wood(gp);
		projectile = new OBJ_Fireball(gp);
		attack = getAttack();//strength
		defense = getDefense();//dexterity
		
	}
	public void setItems() {
		//inventory.add(currentWeapon);
		//inventory.add(currentShield);
		inventory.add(currentWeapon);
		gp.ui.slotCol = 0;
		gp.ui.slotRow = 0;
		}
	public int getAttack() {
		attackArea = currentWeapon.attackArea;
		return attack = strength * currentWeapon.attackValue;
	}
	public int getDefense() {
		return defense = dexterity * currentShield.defenseValue;
	}
	public void getPlayerImage() {
		up0 = setup("/player/up0", gp.tileSize, gp.tileSize);
		up1 = setup("/player/up1", gp.tileSize, gp.tileSize);
		up2 = setup("/player/up2", gp.tileSize, gp.tileSize);
		down0 = setup("/player/down0", gp.tileSize, gp.tileSize);
		down1 = setup("/player/down1", gp.tileSize, gp.tileSize);
		down2 = setup("/player/down2", gp.tileSize, gp.tileSize);
		left0 = setup("/player/left0", gp.tileSize, gp.tileSize);
		left1 = setup("/player/left1", gp.tileSize, gp.tileSize);
		left2 = setup("/player/left2", gp.tileSize, gp.tileSize);
		right0 = setup("/player/right0", gp.tileSize, gp.tileSize);
		right1 = setup("/player/right1", gp.tileSize, gp.tileSize);
		right2 = setup("/player/right2", gp.tileSize, gp.tileSize);
	}
	public void getPlayerAttackImage() {
		attackUp1 = setup("/player/upSlash1", gp.tileSize, gp.tileSize * 2);
		attackDown1 = setup("/player/downSlash1", gp.tileSize, gp.tileSize * 2);
		attackLeft1 = setup("/player/leftSlash1", gp.tileSize * 2, gp.tileSize);
		attackRight1 = setup("/player/rightSlash1", gp.tileSize * 2, gp.tileSize);
		if(currentWeapon.name.contentEquals("Normal Sword")) {
		attackUp2 = setup("/player/upSlash2", gp.tileSize, gp.tileSize * 2);
		attackDown2 = setup("/player/downSlash2", gp.tileSize, gp.tileSize * 2);
		attackLeft2 = setup("/player/leftSlash2", gp.tileSize * 2, gp.tileSize);
		attackRight2 = setup("/player/rightSlash2", gp.tileSize * 2, gp.tileSize);
		}
		if(currentWeapon.name.contentEquals("Fire Sword")) {
		attackUp2 = setup("/player/upSlash2Fire", gp.tileSize, gp.tileSize * 2);
		attackDown2 = setup("/player/downSlash2Fire", gp.tileSize, gp.tileSize * 2);
		attackLeft2 = setup("/player/leftSlash2Fire", gp.tileSize * 2, gp.tileSize);
		attackRight2 = setup("/player/rightSlash2Fire", gp.tileSize * 2, gp.tileSize);	
		}
	}
	public void update() {
		gp.eHandler.checkEvent();
		if(mana < maxMana) {
			healingFrame++;
			if(healingFrame >= 180) {
			mana++;
			healingFrame = 0;
			}
		}
		if(keyH.attackPressed == true) {
			attacking = true;
			attacking();
			keyH.attackPressed = false;
		}
		else {
			if(keyH.upPressed == true || keyH.leftPressed == true || keyH.rightPressed == true || keyH.downPressed == true || keyH.enterPressed == true) {

			if(keyH.leftPressed == true) {
				gp.playThud = true;
				direction = "left";
			}
			else if(keyH.rightPressed == true) {
				gp.playThud = true;
				direction = "right";
			}
			else if(keyH.upPressed == true) {
				gp.playThud = true;
			direction = "up";
			}
			else if(keyH.downPressed == true) {
				gp.playThud = true;
			direction = "down";
			}
			
			//CHECK TILE COLLISION
			collisionOn = false;
			gp.cChecker.checkTile(this);
			//CHECK OBJECT COLLISION
			int objIndex = gp.cChecker.checkObject(this, true);
			pickupObject(objIndex);
			//CHECK NPC COLLISION
			int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
			interactNPC(npcIndex);
			
			//CHECK MONSTER COLLISION
			int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
			contactMonster(monsterIndex);
			
			//IF COLLISION IS FALSE, PLAYER CAN MOVE
			if(collisionOn == false && keyH.enterPressed == false) {
				thud = 0;
				switch(direction) {
				case "up": worldY -= speed; break;
				case "down": worldY += speed; break;
				case "left": worldX -= speed; break;
				case "right": worldX += speed; break;
				}
			}else if(keyH.enterPressed == false) thud = 1;
			if(moving == false && keyH.enterPressed == false) {
				spriteNum = 1;
				moving = true;
			}
			keyH.enterPressed = false;
		if(spriteCounter > 8) {
			if(spriteNum == 1 && reverse == 0) spriteNum = 2;
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
		}
		spriteCounter++;
	}else {
		moving = false;
		gp.playThud = false;
		spriteNum = 0;
		spriteCounter = 4;
		reverse = 0;
	}
	}
		if(gp.keyH.magicKeyPressed == true && projectile.alive == false && shotCooldown == 80 && projectile.haveResources(this) == true) {
			
			casting = true;
			//Set default coordinates, direction and user
			projectile.set(worldX, worldY, direction, true, this);
			
			//Subtract mana
			projectile.subtractResources(this);
			
			//Add to list
			gp.projectileList.add(projectile);
			shotCooldown = 0;
			
			gp.playSE(17);
			}
		if(invincible == true) {
			invincibleCounter++;
			if(invincibleCounter > gp.FPS + gp.FPS / 5) {
				invincible = false;
				invincibleCounter = 0;
			}
		}
		if(shotCooldown < 80) shotCooldown++;
		
		
}
	public void attacking() {
		spriteCounter++;
		spriteAttackNum = 2;
		if(spriteCounter > 3 && spriteCounter <= 20) {
			
			
			// Save current player's position and solidArea
			int currentWorldX = worldX;
			int currentWorldY = worldY;
			int solidAreaWidth = solidArea.width;
			int solidAreaHeight = solidArea.height;
			
			//Adjust player's position for attackArea
			switch(direction) {
			case "up": worldY -= attackArea.height; break;
			case "down": worldY += attackArea.height; break;
			case "left": worldX -= attackArea.width; break;
			case "right": worldX += attackArea.width; break;
			}
			
			//Adjust player's solidArea for collision with monsters
			solidArea.width = attackArea.width;
			solidArea.height = attackArea.height;
			
			//Check monster being hit
			int monsterIndex = gp.cChecker.checkEntity(this,  gp.monster);
			damageMonster(monsterIndex);
			
			//Return player's position and solidArea
			worldX = currentWorldX;
			worldY = currentWorldY;
			solidArea.width = solidAreaWidth;
			solidArea.height = solidAreaHeight;

		}
		if(spriteCounter > 20) {
			spriteAttackNum  = 1;
			spriteCounter = 0;
			attacking = false;
		}
	}



	public void pickupObject(int i) {
		if(i != 999) {
			
			//Pickup only items
			if(gp.obj[i].category == type_pickUpOnly) {
				gp.obj[i].use(this);
				gp.obj[i] = null;
			}else {
			String text;
			if(inventory.size() < maxInventorySize) {
				inventory.add(gp.obj[i]);
				gp.playSE(1);
				text = "Got a " + gp.obj[i].name + ".";
			}else text = "Inventory is full.";
			gp.ui.addMessage(text);
			gp.obj[i] = null;
			}
		}
	}
	public void interactNPC(int i) {
		if(i != 999) {
			if(gp.keyH.enterPressed == true) {
			spriteNum = 0;
			gp.gameState = gp.dialogueState;
			gp.npc[i].speak();
			}
		}
		
	}
	public void contactMonster(int i) {
		if(i != 999 && invincible == false && gp.monster[i].life > 0) {
			int damage = gp.monster[i].attack - defense;
			if(damage <= 0) damage = 1;
			if(life > damage) gp.playSE(8);
			else gp.playSE(10);
			life -= damage;
			invincible = true;
		}
	}
	
	public void damageMonster(int i) {
		if(i != 999 && gp.monster[i].invincible == false) {
			int damage = attack - gp.monster[i].defense;
			if(damage <= 0) damage = 1;
			gp.monster[i].life -= damage;
			gp.monster[i].invincible = true;
			gp.monster[i].damageReaction();
			
			if(gp.monster[i].life <= 0) {
				{
					gp.monster[i].dead = true;
					gp.monster[i].moving = false;
					if(impact == true) gp.playSE(18);
					else if(currentWeapon.name.contentEquals("Fire Sword")) gp.playSE(16);
					gp.playSE(10);
				}
				gp.monster[i].dead = true;
				
			}else {
				int t = random.nextInt(2) + 1;
				if( t == 1 ) {
					if(impact == true) gp.playSE(18);
					else if(currentWeapon.name.contentEquals("Fire Sword")) gp.playSE(16);
					gp.playSE(8);
				}
				if( t == 2 ) {
					if(impact == true) gp.playSE(18);
					else if(currentWeapon.name.contentEquals("Fire Sword")) gp.playSE(16);
					gp.playSE(9);
				}
			}
		}else {
			if(currentWeapon.name.contentEquals("Fire Sword")) gp.playSE(17);
			if(impact == false) gp.playSE(7);
		}
	}
	public void checkLevelUp() {
		
		if(exp >= nextLevelExp) {
			exp -= nextLevelExp;
			level++;
			nextLevelExp = (int)Math.pow((double)level, (double)3) * 5 / 4;
			maxLife += 2;
			strength++;
			dexterity++;
			attack = getAttack();
			defense = getDefense();
			
			gp.playSE(12);
			gp.ui.addMessage("Grew to level " + level + ".");
		}
	}
	public void selectItem() {
		int itemSlot = gp.ui.getItemSlot();
		if(itemSlot < inventory.size()) {
			Entity selectedItem = inventory.get(itemSlot);
			
			if(selectedItem.category == type_sword) {
				currentWeapon = selectedItem;
				attack = getAttack();
				getPlayerAttackImage();
			}
			if(selectedItem.category == type_consumable) {
				selectedItem.use(this);
				inventory.remove(itemSlot);
			}
		}
	}
	public void draw(Graphics2D g2) {
		
		BufferedImage image = null;
		tempScreenX = screenX;
		tempScreenY = screenY;
		switch(direction) {
		case "up":
			if(attacking == false) {
			if(spriteNum == 0) image = up0;
			if(spriteNum == 1) image = up1;
			if(spriteNum == 2) image = up0;
			if(spriteNum == 3) image = up2;
		}else {
			tempScreenY = screenY - gp.tileSize;
			if(spriteAttackNum == 1) image = attackUp1;
			if(spriteAttackNum == 2) image = attackUp2;
			attacking = false;
		}
			break;
		case "down":
			if(attacking == false) {
			if(spriteNum == 0) image = down0;
			if(spriteNum == 1) image = down1;
			if(spriteNum == 2) image = down0;
			if(spriteNum == 3) image = down2;
		}else {
			if(spriteAttackNum == 1) image = attackDown1;
			if(spriteAttackNum == 2) image = attackDown2;
			attacking = false;
		}
			break;
		case "left":
			if(attacking == false) {
			if(spriteNum == 0) image = left0;
			if(spriteNum == 1) image = left1;
			if(spriteNum == 2) image = left0;
			if(spriteNum == 3) image = left2;
		}else {
			tempScreenX = screenX - gp.tileSize;
			if(spriteAttackNum == 1) image = attackLeft1;
			if(spriteAttackNum == 2) image = attackLeft2;
			attacking = false;
		}
			break;
		case "right":
			if(attacking == false) {
			if(spriteNum == 0) image = right0;
			if(spriteNum == 1) image = right1;
			if(spriteNum == 2) image = right0;
			if(spriteNum == 3) image = right2;
		}else {
			if(spriteAttackNum == 1) image = attackRight1;
			if(spriteAttackNum == 2) image = attackRight2;
			attacking = false;
		}
			break;
		}
		
		
		if(invincible == true) g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
		
		// Keep camera when player is at the map's edge
		
		if(screenX > worldX) tempScreenX = worldX - (screenX - tempScreenX);
		if(screenY > worldY) tempScreenY = worldY - (screenY - tempScreenY);
		
		int rightOffset = gp.screenWidth - screenX;
		if(rightOffset > gp.worldWidth - worldX) tempScreenX = gp.screenWidth - (gp.worldWidth - worldX) - (screenX - tempScreenX);
		
		int bottomOffset = gp.screenHeight - screenY;
		if(bottomOffset > gp.worldHeight - worldY) tempScreenY = gp.screenHeight - (gp.worldHeight - worldY) - (screenY - tempScreenY);
		
		g2.drawImage(image, tempScreenX, tempScreenY, null);
		
		x = screenX;
		y = screenY;
		if(screenX > worldX) x = worldX;
		if(screenY > worldY) y = worldY;
		rightOffset = gp.screenWidth - screenX;
		if(rightOffset > gp.worldWidth - worldX) x = gp.screenWidth - (gp.worldWidth - worldX);
		bottomOffset = gp.screenHeight - screenY;
		if(bottomOffset > gp.worldHeight - worldY) y = gp.screenHeight - (gp.worldHeight - worldY);
		//Reset opacity
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
	}
}
