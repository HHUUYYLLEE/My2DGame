package main;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
//import java.sql.Connection;
import java.util.Random;

import object.OBJ_Fireball;
import object.OBJ_Shield_Wood;
import object.OBJ_Sword_Normal;

public class Player extends Entity{
	private KeyHandler keyH;
	private int thud = 0;
	private final int screenX;
	private final int screenY;
	private int tempScreenX, tempScreenY, x, y;
	private int spriteAttackNum;
	private int healingFrame;
	private ArrayList<Entity> inventory = new ArrayList<>();
	private Random random = new Random();
	private int maxInventorySize;
	
	public Player(GamePanel gp, KeyHandler keyH) {
		super(gp);
		this.keyH = keyH;
		maxInventorySize = gp.getUi().getSlotsInAColumn() * gp.getUi().getSlotsInARow();
		screenX = gp.getScreenWidth() / 2 - gp.getTileSize() / 2;
		screenY = gp.getScreenHeight() / 2 - gp.getTileSize() / 2;
		
		setSolidArea(new Rectangle(gp.getTileSize() / 6, gp.getTileSize() / 3, gp.getTileSize() * 2 / 3, gp.getTileSize() * 2 / 3));//8, 16, 32, 32
		setSolidAreaDefaultX(getSolidArea().x);
		setSolidAreaDefaultY(getSolidArea().y);
		setDefaultValues();
		getPlayerImage();
		getPlayerAttackImage();
		setItems();
	}
	public void setDefaultValues() {
		healingFrame = 0;
		setName("Player");
		setWorldX(gp.getTileSize() * 23);
		setWorldY(gp.getTileSize() * 21);
		setSpeed(/*gp.database.getPlayerValue("Speed");*/ 4);
		setDirection("left");
		//Player's default status
		setLevel(1);
		setMaxLife(/*gp.database.getPlayerValue("HP");*/ 8);
		setLife(getMaxLife());
		setMaxMana(8);
		setMana(getMaxMana());
		setAmmo(10);
		setStrength(4); //Deals more damage
		setDexterity(1);//Takes less damage
		setExp(0);
		setNextLevelExp(5);
		setCoin(0);
		inventory.clear();
		setCurrentWeapon(new OBJ_Sword_Normal(gp));
		setCurrentShield(new OBJ_Shield_Wood(gp));
		setProjectile(new OBJ_Fireball(gp));
		setAttack(getPlayerAttack());//strength
		setDefense(getPlayerDefense());//dexterity
		
	}
	public void setItems() {
		//inventory.add(currentWeapon);
		//inventory.add(currentShield);
		inventory.add(getCurrentWeapon());
		gp.getUi().setSlotCol(0);
		gp.getUi().setSlotRow(0);
		}
	public int getPlayerAttack() {
		setAttackArea(getCurrentWeapon().getAttackArea());
		setAttack(getStrength() * getCurrentWeapon().getAttackValue());
		return getAttack();
	}
	public int getPlayerDefense() {
		setDefense(getDexterity() * getCurrentShield().getDefenseValue());
		return getDefense();
	}
	public void getPlayerImage() {
		setUp0(setup("/player/up0", gp.getTileSize(), gp.getTileSize()));
		setUp1(setup("/player/up1", gp.getTileSize(), gp.getTileSize()));
		setUp2(setup("/player/up2", gp.getTileSize(), gp.getTileSize()));
		setDown0(setup("/player/down0", gp.getTileSize(), gp.getTileSize()));
		setDown1(setup("/player/down1", gp.getTileSize(), gp.getTileSize()));
		setDown2(setup("/player/down2", gp.getTileSize(), gp.getTileSize()));
		setLeft0(setup("/player/left0", gp.getTileSize(), gp.getTileSize()));
		setLeft1(setup("/player/left1", gp.getTileSize(), gp.getTileSize()));
		setLeft2(setup("/player/left2", gp.getTileSize(), gp.getTileSize()));
		setRight0(setup("/player/right0", gp.getTileSize(), gp.getTileSize()));
		setRight1(setup("/player/right1", gp.getTileSize(), gp.getTileSize()));
		setRight2(setup("/player/right2", gp.getTileSize(), gp.getTileSize()));
	}
	public void getPlayerAttackImage() {
		setAttackUp1(setup("/player/upSlash1", gp.getTileSize(), gp.getTileSize() * 2));
		setAttackDown1(setup("/player/downSlash1", gp.getTileSize(), gp.getTileSize() * 2));
		setAttackLeft1(setup("/player/leftSlash1", gp.getTileSize() * 2, gp.getTileSize()));
		setAttackRight1(setup("/player/rightSlash1", gp.getTileSize() * 2, gp.getTileSize()));
		if(getCurrentWeapon().getName().contentEquals("Normal Sword")) {
		setAttackUp2(setup("/player/upSlash2", gp.getTileSize(), gp.getTileSize() * 2));
		setAttackDown2(setup("/player/downSlash2", gp.getTileSize(), gp.getTileSize() * 2));
		setAttackLeft2(setup("/player/leftSlash2", gp.getTileSize() * 2, gp.getTileSize()));
		setAttackRight2(setup("/player/rightSlash2", gp.getTileSize() * 2, gp.getTileSize()));
		}
		if(getCurrentWeapon().getName().contentEquals("Fire Sword")) {
		setAttackUp2(setup("/player/upSlash2Fire", gp.getTileSize(), gp.getTileSize() * 2));
		setAttackDown2(setup("/player/downSlash2Fire", gp.getTileSize(), gp.getTileSize() * 2));
		setAttackLeft2(setup("/player/leftSlash2Fire", gp.getTileSize() * 2, gp.getTileSize()));
		setAttackRight2(setup("/player/rightSlash2Fire", gp.getTileSize() * 2, gp.getTileSize()));	
		}
	}
	public void update() {
		gp.geteHandler().checkEvent();
		if(getMana() < getMaxMana()) manaGradualHealing();
		if(keyH.isAttackPressed()) {
			setAttacking(true);
			attacking();
			keyH.setAttackPressed(false);
		}
		else {
			if(keyH.isUpPressed() || keyH.isLeftPressed() || keyH.isRightPressed() || keyH.isDownPressed() || keyH.isEnterPressed()) {
			changeDirection();
			checkAllCollisions();
			
			//IF COLLISION IS FALSE, PLAYER CAN MOVE
			if(!isCollisionOn() && !keyH.isEnterPressed()) {
				moving();
			}
			else if(!keyH.isEnterPressed()) thud = 1;
			
			//Reset sprite and moving state
			if(!isMoving() && !keyH.isEnterPressed()) {
				setSpriteNum(1);
				setMoving(true);
			}
			
			keyH.setEnterPressed(false);
			updateSprite();
	}
	else notMoving();
}
		firingProjectile();
		updateInvincibility();
		if(getShotCooldown() < 80) setShotCooldown(getShotCooldown() + 1);
}
	
	private void updateInvincibility() {
		if(isInvincible()) {
			setInvincibleCounter(getInvincibleCounter() + 1);
			if(getInvincibleCounter() > gp.getFPS() + gp.getFPS() / 5) {
				setInvincible(false);
				setInvincibleCounter(0);
			}
		}
	}
	private void firingProjectile() {
		if(gp.getKeyH().isMagicKeyPressed() && !getProjectile().isAlive() && getShotCooldown() == 80 && getProjectile().haveResources(this)) {
			
			setCasting(true);
			//Set default coordinates, direction and user
			getProjectile().set(getWorldX(), getWorldY(), getDirection(), true, this);
			
			//Subtract mana
			getProjectile().subtractResources(this);
			
			//Add to list
			gp.getProjectileList().add(getProjectile());
			setShotCooldown(0);
			
			gp.playSE(17);
			}
	}
	private void notMoving() {
			setMoving(false);
			gp.changePlayThud(false);
			setSpriteNum(0);
			setSpriteCounter(4);
			setReverse(0);
	}
	private void updateSprite() {
		if(getSpriteCounter() > 8) {
			if(getSpriteNum() == 1 && getReverse() == 0) setSpriteNum(2);
			else if(getSpriteNum() == 2 && getReverse() == 0) setSpriteNum(3);
			else if(getSpriteNum() == 3 && getReverse() == 0) {
				setReverse(1);
				setSpriteNum(2);
			}
			else if(getSpriteNum() == 2 && getReverse() == 1) {
				setReverse(0);
				setSpriteNum(1);
			}
			setSpriteCounter(0);
		}
		setSpriteCounter(getSpriteCounter() + 1);
	}
	private void moving() {
		thud = 0;
		switch(getDirection()) {
		case "up": setWorldY(getWorldY() - getSpeed()); break;
		case "down": setWorldY(getWorldY() + getSpeed()); break;
		case "left": setWorldX(getWorldX() - getSpeed()); break;
		case "right": setWorldX(getWorldX() + getSpeed()); break;
		}
	}
	private void checkAllCollisions() {
		//CHECK TILE COLLISION
		setCollisionOn(false);
		gp.getcChecker().checkTile(this);
		//CHECK OBJECT COLLISION
		int objIndex = gp.getcChecker().checkObject(this, true);
		pickupObject(objIndex);
		//CHECK NPC COLLISION
		int npcIndex = gp.getcChecker().checkEntity(this, gp.getNpc());
		interactNPC(npcIndex);
		
		//CHECK MONSTER COLLISION
		int monsterIndex = gp.getcChecker().checkEntity(this, gp.getMonster());
		contactMonster(monsterIndex);
	}
	private void changeDirection() {
		if(keyH.isLeftPressed()) {
			gp.changePlayThud(true);
			setDirection("left");
		}
		else if(keyH.isRightPressed()) {
			gp.changePlayThud(true);
			setDirection("right");
		}
		else if(keyH.isUpPressed()) {
			gp.changePlayThud(true);
			setDirection("up");
		}
		else if(keyH.isDownPressed()) {
			gp.changePlayThud(true);
			setDirection("down");
		}
	}
	private void manaGradualHealing() {
		healingFrame++;
		if(healingFrame >= gp.getFPS() * 6) {
		increaseMana(1);
		healingFrame = 0;
		}
	}
	
	public void gainEXP(Entity monster) {
		setExp(getExp() + monster.getExp());
		gp.getUi().addMessage("Gained " + monster.getExp() + " Experience Points.");
		checkLevelUp();
	}
	
	public void attacking() {
		setSpriteCounter(getSpriteCounter() + 1);
		spriteAttackNum = 2;
		if(getSpriteCounter() > 3 && getSpriteCounter() <= 20) {
			
			
			// Save current player's position and solidArea
			int currentWorldX = getWorldX();
			int currentWorldY = getWorldY();
			int solidAreaWidth = getSolidArea().width;
			int solidAreaHeight = getSolidArea().height;
			
			//Adjust player's position for attackArea
			switch(getDirection()) {
			case "up": setWorldY(getWorldY() - getAttackArea().height); break;
			case "down": setWorldY(getWorldY() + getAttackArea().height); break;
			case "left": setWorldX(getWorldX() - getAttackArea().width); break;
			case "right": setWorldX(getWorldX() + getAttackArea().width); break;
			}
			
			//Adjust player's solidArea for collision with monsters
			getSolidArea().width = getAttackArea().width;
			getSolidArea().height = getAttackArea().height;
			
			//Check monster being hit
			int monsterIndex = gp.getcChecker().checkEntity(this,  gp.getMonster());
			damageMonster(monsterIndex);
			
			//Return player's position and solidArea
			setWorldX(currentWorldX);
			setWorldY(currentWorldY);
			getSolidArea().width = solidAreaWidth;
			getSolidArea().height = solidAreaHeight;

		}
		if(getSpriteCounter() > 20) {
			spriteAttackNum  = 1;
			setSpriteCounter(0);
			setAttacking(false);
		}
	}



	public void pickupObject(int i) {
		if(i != 999) {
			
			//Pickup only items
			if(gp.getObj()[i].getCategory() == type_pickUpOnly) {
				gp.getObj()[i].use(this);
				gp.getObj()[i] = null;
			}else {
			String text;
			if(inventory.size() < maxInventorySize) {
				inventory.add(gp.getObj()[i]);
				gp.playSE(1);
				text = "Got a " + gp.getObj()[i].getName() + ".";
			}else text = "Inventory is full.";
			gp.getUi().addMessage(text);
			gp.getObj()[i] = null;
			}
		}
	}
	public void interactNPC(int i) {
		if(i != 999) {
			if(gp.getKeyH().isEnterPressed() == true) {
			setSpriteNum(0);
			gp.setGameState(gp.dialogueState);
			gp.getNpc()[i].speak();
			}
		}
		
	}
	public void contactMonster(int i) {
		if(i != 999 && isInvincible() == false && gp.getMonster()[i].getLife() > 0) {
			int damage = gp.getMonster()[i].getAttack() - getDefense();
			if(damage <= 0) damage = 1;
			if(getLife() > damage) gp.playSE(8);
			else gp.playSE(10);
			setLife(getLife() - damage);
			setInvincible(true);
		}
	}
	
	public void damageMonster(int i) {
		if(i != 999 && gp.getMonster()[i].isInvincible() == false) {
			int damage = getAttack() - gp.getMonster()[i].getDefense();
			if(damage <= 0) damage = 1;
			gp.getMonster()[i].decreaseLife(damage);
			gp.getMonster()[i].setInvincible(true);
			gp.getMonster()[i].damageReaction();
			
			if(gp.getMonster()[i].getLife() <= 0) {
				{
					gp.getMonster()[i].setDead(true);
					gp.getMonster()[i].setMoving(false);
					if(isImpact()) gp.playSE(18);
					else if(getCurrentWeapon().getName().contentEquals("Fire Sword")) gp.playSE(16);
					gp.playSE(10);
				}
				gp.getMonster()[i].setDead(true);
				
			}else {
				int t = random.nextInt(2) + 1;
				if( t == 1 ) {
					if(isImpact()) gp.playSE(18);
					else if(getCurrentWeapon().getName().contentEquals("Fire Sword")) gp.playSE(16);
					gp.playSE(8);
				}
				if( t == 2 ) {
					if(isImpact()) gp.playSE(18);
					else if(getCurrentWeapon().getName().contentEquals("Fire Sword")) gp.playSE(16);
					gp.playSE(9);
				}
			}
		}else {
			if(getCurrentWeapon().getName().contentEquals("Fire Sword")) gp.playSE(17);
			if(!isImpact()) gp.playSE(7);
		}
	}
	public void checkLevelUp() {
		
		if(getExp() >= getNextLevelExp()) {
			setExp(getExp() - getNextLevelExp());
			increaseLevel(1);
			setNextLevelExp((int)Math.pow((double)getLevel(), (double)3) * 5 / 4);
			increaseLife(2);
			increaseMaxLife(2);
			increaseStrength(1);
			increaseDexterity(1);
			setAttack(getPlayerAttack());
			setDefense(getPlayerDefense());
			
			gp.playSE(12);
			gp.getUi().addMessage("Grew to level " + getLevel() + ".");
		}
	}
	
	public void selectItem() {
		int itemSlot = gp.getUi().getItemSlot();
		if(itemSlot < inventory.size()) {
			Entity selectedItem = inventory.get(itemSlot);
			
			if(selectedItem.getCategory() == type_sword) {
				setCurrentWeapon(selectedItem);
				setAttack(getPlayerAttack());
				getPlayerAttackImage();
			}
			if(selectedItem.getCategory() == type_consumable) {
				selectedItem.use(this);
				inventory.remove(itemSlot);
			}
		}
	}
	public void draw(Graphics2D g2) {
		
		BufferedImage image = null;
		tempScreenX = screenX;
		tempScreenY = screenY;
		switch(getDirection()) {
		case "up":
			if(!isAttacking()) {
			if(getSpriteNum() == 0) image = getUp0();
			if(getSpriteNum() == 1) image = getUp1();
			if(getSpriteNum() == 2) image = getUp0();
			if(getSpriteNum() == 3) image = getUp2();
		}else {
			tempScreenY = screenY - gp.getTileSize();
			if(spriteAttackNum == 1) image = getAttackUp1();
			if(spriteAttackNum == 2) image = getAttackUp2();
			setAttacking(false);
		}
			break;
		case "down":
			if(!isAttacking()) {
			if(getSpriteNum() == 0) image = getDown0();
			if(getSpriteNum() == 1) image = getDown1();
			if(getSpriteNum() == 2) image = getDown0();
			if(getSpriteNum() == 3) image = getDown2();
		}else {
			if(spriteAttackNum == 1) image = getAttackDown1();
			if(spriteAttackNum == 2) image = getAttackDown2();
			setAttacking(false);
		}
			break;
		case "left":
			if(!isAttacking()) {
			if(getSpriteNum() == 0) image = getLeft0();
			if(getSpriteNum() == 1) image = getLeft1();
			if(getSpriteNum() == 2) image = getLeft0();
			if(getSpriteNum() == 3) image = getLeft2();
		}else {
			tempScreenX = screenX - gp.getTileSize();
			if(spriteAttackNum == 1) image = getAttackLeft1();
			if(spriteAttackNum == 2) image = getAttackLeft2();
			setAttacking(false);
		}
			break;
		case "right":
			if(isAttacking() == false) {
			if(getSpriteNum() == 0) image = getRight0();
			if(getSpriteNum() == 1) image = getRight1();
			if(getSpriteNum() == 2) image = getRight0();
			if(getSpriteNum() == 3) image = getRight2();
		}else {
			if(spriteAttackNum == 1) image = getAttackRight1();
			if(spriteAttackNum == 2) image = getAttackRight2();
			setAttacking(false);
		}
			break;
		}
		
		
		if(isInvincible()) g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
		
		// Keep camera when player is at the map's edge
		
		if(screenX > getWorldX()) tempScreenX = getWorldX() - (screenX - tempScreenX);
		if(screenY > getWorldY()) tempScreenY = getWorldY() - (screenY - tempScreenY);
		
		int rightOffset = gp.getScreenWidth() - screenX;
		if(rightOffset > gp.getWorldWidth() - getWorldX()) tempScreenX = gp.getScreenWidth() - (gp.getWorldWidth() - getWorldX()) - (screenX - tempScreenX);
		
		int bottomOffset = gp.getScreenHeight() - screenY;
		if(bottomOffset > gp.getWorldHeight() - getWorldY()) tempScreenY = gp.getScreenHeight() - (gp.getWorldHeight() - getWorldY()) - (screenY - tempScreenY);
		
		g2.drawImage(image, tempScreenX, tempScreenY, null);
		
		x = screenX;
		y = screenY;
		if(screenX > getWorldX()) x = getWorldX();
		if(screenY > getWorldY()) y = getWorldY();
		rightOffset = gp.getScreenWidth() - screenX;
		if(rightOffset > gp.getWorldWidth() - getWorldX()) x = gp.getScreenWidth() - (gp.getWorldWidth() - getWorldX());
		bottomOffset = gp.getScreenHeight() - screenY;
		if(bottomOffset > gp.getWorldHeight() - getWorldY()) y = gp.getScreenHeight() - (gp.getWorldHeight() - getWorldY());
		//Reset opacity
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public int getScreenX() {
		return screenX;
	}
	public int getScreenY() {
		return screenY;
	}
	public int getThud() {
		return thud;
	}
	public void setThud(int thud) {
		this.thud = thud;
	}
	public ArrayList<Entity> getInventory() {
		return inventory;
	}
	public int getMaxInventorySize() {
		return maxInventorySize;
	}

}
