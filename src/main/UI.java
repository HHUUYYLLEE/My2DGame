package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import entity.Entity;
import object.OBJ_Heart;
import object.OBJ_ManaCrystal;

public class UI {
	GamePanel gp;
	Graphics2D g2;
	Font maruMonica, purisaBold, arial_12B, arial_38BI;
	BufferedImage heart_full, heart_half, heart_blank, manaCrystal_full, manaCrystal_blank;
	public boolean messageOn = false;
	//public String message = "";
	//int messageCounter = 0;
	ArrayList<String> message = new ArrayList<>();
	ArrayList<Integer> messageCounter = new ArrayList<>();
	public boolean gameFinished = false;
	public String currentDialogue = "";
	public int commandNum = 0;
	public int titleScreenState = 0;
	double playTime;
	
	//Inventory variables
	public int slotCol = 0;
	public int slotRow = 0;
	public int slotsInARow = 5;
	public int slotsInAColumn = 6;
	
	public UI(GamePanel gp) {
		this.gp = gp;
		
		try {
			InputStream is = getClass().getResourceAsStream("/font/x12y16pxMaruMonica.ttf");
			maruMonica = Font.createFont(Font.TRUETYPE_FONT, is);
			is = getClass().getResourceAsStream("/font/Purisa Bold.ttf");
			purisaBold = Font.createFont(Font.TRUETYPE_FONT, is);
			arial_12B = new Font("Arial", Font.BOLD, 12);
			arial_38BI = new Font("Arial", Font.BOLD | Font.ITALIC, 38);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//CREATE HUD
		Entity heart = new OBJ_Heart(gp);
		heart_full = heart.image;
		heart_half = heart.image1;
		heart_blank = heart.image2;
		Entity crystal = new OBJ_ManaCrystal(gp);
		manaCrystal_full = crystal.image;
		manaCrystal_blank = crystal.image2;
	}
	public void addMessage(String text) {
		
		///message = text;
		//messageOn = true;
		message.add(text);
		messageCounter.add(0);
	}
	public void draw(Graphics2D g2) {
		
		this.g2 = g2;
		
		g2.setFont(maruMonica);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setColor(Color.white);
		
		//TITLE STATE
		if(gp.gameState == gp.titleState) drawTitleScreen();
		
		//PLAY STATE
		if(gp.gameState == gp.playState) {
			drawPlayerLifeandMana();
			drawEXPBar();
			drawMessage();
		}
		
		//PAUSE STATE
		if(gp.gameState == gp.pauseState) {
			drawPlayerLifeandMana();
			drawEXPBar();
			drawPauseScreen();
		}
		
		//DIALOGUE STATE
		if(gp.gameState == gp.dialogueState) {
			g2.setFont(arial_12B);
			drawDialogueScreen();
			g2.setFont(maruMonica);
		}
		
		//CHARACTER STATE
		if(gp.gameState == gp.characterState) {
			drawCharacterScreen();
			drawInventory();
		}
		
		
	}
	public void drawPlayerLifeandMana() {
		int x = gp.tileSize / 2;
		int y = gp.tileSize / 2;
		int i = 0;
		
		//Draw all blank hearts
		while((i < gp.player.maxLife / 2 && gp.player.maxLife % 2 == 0) || (i <= gp.player.maxLife / 2 && gp.player.maxLife % 2 == 1)) {
			g2.drawImage(heart_blank, x, y, null);
			i++;
			x += gp.tileSize;
		}
		//Reset i and reduce x
		i = 0;
		x = gp.tileSize / 2;
		
		//Draw current life
		while(i < gp.player.life) {
			g2.drawImage(heart_half, x, y, null);
			i++;
			if(i < gp.player.life) g2.drawImage(heart_full, x, y, null);
			i++;
			x += gp.tileSize;
		}
		
		//Draw max mana
		x = gp.tileSize / 2 - 5;
		y = gp.tileSize + gp.tileSize / 2;
		i = 0;
		while(i < gp.player.maxMana) {
			g2.drawImage(manaCrystal_blank, x, y, null);
			i++;
			x += 35;
		}
		
		//Draw current mana
		x = gp.tileSize / 2 - 5;
		y = gp.tileSize + gp.tileSize / 2;
		i = 0;
		while(i < gp.player.mana) {
			g2.drawImage(manaCrystal_full, x, y, null);
			i++;
			x += 35;
		}
	}
	public void drawEXPBar() {
		int x = gp.tileSize / 2;
		int y = gp.tileSize * 3;
		double oneScale = (double)gp.tileSize * 6 / gp.player.nextLevelExp;
		double expBarValue = oneScale * gp.player.exp;
		if(expBarValue > oneScale * gp.player.nextLevelExp) expBarValue = oneScale * gp.player.nextLevelExp;
		
		g2.setColor(new Color(35, 35, 35));
		g2.fillRect(x , y - 16, gp.tileSize * 6, 30);
	
		g2.setColor(new Color(108, 127, 249));
		g2.fillRect(x + 5, y - 11, (int)expBarValue - 10, 20);
		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20F));
		g2.setColor(Color.white);
		g2.drawString("Level: " + gp.player.level, x + 10, y + 5);
		g2.drawString("EXP: " + gp.player.exp + "/" + gp.player.nextLevelExp, x + gp.tileSize * 4, y + 5);
	}
	public void drawMessage() {
		int messageX = gp.tileSize / 2;
		int messageY = gp.tileSize * 4;
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 38F));
		for(int i = 0; i < message.size(); ++i) {
			
			if(message.get(i) != null) {
				
				g2.setColor(Color.black);
				g2.drawString(message.get(i), messageX + 2, messageY + 2);
				g2.setColor(Color.white);
				g2.drawString(message.get(i), messageX, messageY);
				
				//messageCounter++, increase messageCounter
				int counter = messageCounter.get(i) + 1;
				messageCounter.set(i, counter);
				messageY += 50;
				
				if(messageCounter.get(i) > gp.FPS * 3) {
					//disappear after
					message.remove(i);
					messageCounter.remove(i);
				}
			}
		}
	}
	public void drawTitleScreen() {
		if(titleScreenState == 0) {
			BufferedImage titleChar = null;
			try {
				titleChar = ImageIO.read(getClass().getResourceAsStream("/player/Skeleton.png"));
			}catch(IOException e) {
				e.printStackTrace();
			}
		g2.setColor(new Color(0, 0, 0));
		g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
		
		//TITLE NAME
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 96F));
		String text = "Adventure";
		int x = getXforCenteredText(text);
		int y = gp.tileSize * 3;
		//SHADOW FOR TITLE NAME
		g2.setColor(Color.gray);
		g2.drawString(text,x + 8, y + 8);
		//MAIN COLOR
		g2.setColor(Color.white);
		g2.drawString(text, x, y);
		
		//CHAR IMAGE
		
		x = gp.tileSize;
		y += gp.tileSize * 2;
		g2.drawImage(titleChar, x, y, gp.tileSize * 6, gp.tileSize * 6, null);
		
		//MENU
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48F));
		
		text = "New game";
		x = getXforCenteredText(text);
		y += gp.tileSize * 2.5;
		g2.drawString(text, x, y);
		
		if(commandNum == 0) {
			g2.setColor(Color.cyan);
			g2.drawString(text, x, y);
			g2.setColor(Color.white);
		}
		
		text = "Load game";
		y += gp.tileSize;
		g2.drawString(text, x, y);
		
		if(commandNum == 1) {
			g2.setColor(Color.cyan);
			g2.drawString(text, x, y);
			g2.setColor(Color.white);
		}
		
		text = "Controls";
		y += gp.tileSize;
		g2.drawString(text, x, y);
		
		if(commandNum == 2) {
			g2.setColor(Color.cyan);
			g2.drawString(text, x, y);
			g2.setColor(Color.white);
		}
		
		text = "Quit";
		y += gp.tileSize;
		g2.drawString(text, x, y);
		
		if(commandNum == 3) {
			g2.setColor(Color.cyan);
			g2.drawString(text, x, y);
			g2.setColor(Color.white);
		}
	}else if(titleScreenState == 2) {
		BufferedImage keyboard = null;
		try {
			keyboard = ImageIO.read(getClass().getResourceAsStream("/misc/keyboard.png"));
		}catch(IOException e) {
			e.printStackTrace();
		}
		g2.drawImage(keyboard, 20, 160, keyboard.getWidth() / 10 * 3, keyboard.getHeight() / 10 * 3, null);
	}
}
	public void drawPauseScreen() {
		
		String text = "Game paused.";
		g2.setFont(g2.getFont().deriveFont(42F));
		int x = getXforCenteredText(text);
		int y = gp.screenHeight / 2;
		g2.drawString(text, x, y);
		
	}
	public void drawDialogueScreen() {
		//WINDOW
		int x = gp.tileSize * 2;
		int y = gp.tileSize / 2;
		int width = gp.screenWidth - (gp.tileSize * 4);
		int height = gp.tileSize * 4;
		drawSubWindow(x, y, width, height);
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 32F));
		x += gp.tileSize;
		y += gp.tileSize;
		
		for(String line : currentDialogue.split("\n")) {
		g2.drawString(line, x, y);
		y += 40;
		}
		
	}
	public void drawCharacterScreen() {
		
		// Create a frame
		final int frameX = gp.tileSize * 2;
		final int frameY = gp.tileSize;
		final int frameWidth = gp.tileSize * 5;
		final int frameHeight = gp.tileSize * 11;
		drawSubWindow(frameX, frameY, frameWidth, frameHeight);
		
		//Text
		g2.setColor(Color.white);
		g2.setFont(g2.getFont().deriveFont(32F));
		
		int textX = frameX + 20;
		int textY = frameY + gp.tileSize;
		final int lineHeight = 35;
		
		//Name
		g2.drawString("Level", textX, textY);
		textY += lineHeight;
		g2.drawString("Life", textX, textY);
		textY += lineHeight;
		g2.drawString("Mana", textX, textY);
		textY += lineHeight;
		g2.drawString("Strength", textX, textY);
		textY += lineHeight;
		g2.drawString("Dexterity", textX, textY);
		textY += lineHeight;
		g2.drawString("Attack", textX, textY);
		textY += lineHeight;
		g2.drawString("Defense", textX, textY);
		textY += lineHeight;
		g2.drawString("Exp", textX, textY);
		textY += lineHeight;
		g2.drawString("Next Level", textX, textY);
		textY += lineHeight;
		g2.drawString("Coin", textX, textY);
		textY += lineHeight + 10;
		g2.drawString("Weapon", textX, textY);
		textY += lineHeight + 10;
		g2.drawString("Shield", textX, textY);
		textY += lineHeight;
		
		//Stat values
		int tailX = (frameX + frameWidth) - 30;
		
		textY = frameY + gp.tileSize;
		String value;
		
		value = String.valueOf(gp.player.level);
		textX = getXforRightAllignedText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.player.life + "/" + gp.player.maxLife);
		textX = getXforRightAllignedText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.player.mana + "/" + gp.player.maxMana);
		textX = getXforRightAllignedText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;

		value = String.valueOf(gp.player.strength);
		textX = getXforRightAllignedText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.player.dexterity);
		textX = getXforRightAllignedText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.player.attack);
		textX = getXforRightAllignedText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.player.defense);
		textX = getXforRightAllignedText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.player.exp);
		textX = getXforRightAllignedText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.player.nextLevelExp);
		textX = getXforRightAllignedText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.player.coin);
		textX = getXforRightAllignedText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;
		
		g2.drawImage(gp.player.currentWeapon.down1, tailX - gp.tileSize, textY - 24, null);
	}
	public void drawInventory() {
		
		//Frame window
		int slotSize = gp.tileSize + 10;
		int frameX = gp.tileSize * 9;
		int frameY = gp.tileSize;
		int frameWidth = slotSize * (slotsInARow + 1);
		int frameHeight = slotSize * (slotsInAColumn + 1);
		drawSubWindow(frameX, frameY, frameWidth, frameHeight);
		//Slot
		final int slotXStart = frameX + 20;
		final int slotYStart = frameY + 20;
		int slotX = slotXStart;
		int slotY = slotYStart;
		
		//Draw items
		if(gp.player.inventory.size() > 0) {
		
		for(int i = 0; i < gp.player.inventory.size(); ++i) {
			
			//Equip cursor
			if(gp.player.inventory.get(i) == gp.player.currentWeapon) {
				g2.setColor(new Color(240, 190, 90));
				g2.fillRoundRect(slotX, slotY, gp.tileSize, gp.tileSize, 10, 10);
			}
			
			g2.drawImage(gp.player.inventory.get(i).down1, slotX, slotY, null);
			slotX += slotSize;
			if((i+1) % 5 == 0 && i < gp.player.maxInventorySize) {
				slotX = slotXStart;
				slotY += slotSize;
			}
		}
		
		//Cursor-pointer
		int cursorX = slotXStart + (slotSize * slotCol);
		int cursorY = slotYStart + (slotSize * slotRow);
		int cursorWidth = gp.tileSize;
		int cursorHeight = gp.tileSize;
		
		//Draw cursor
		g2.setColor(Color.yellow);
		g2.setStroke(new BasicStroke(3));
		g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);
		
		//Description
		int dFrameX = frameX;
		int dFrameY = frameY + frameHeight;
		int dFrameWidth = frameWidth;
		int dFrameHeight = gp.tileSize * 4;
		drawSubWindow(dFrameX, dFrameY, dFrameWidth, dFrameHeight);
		
		//Description text
		int textX = dFrameX + 20;
		int textY = dFrameY + gp.tileSize;
		g2.setColor(Color.cyan);
		g2.setFont(g2.getFont().deriveFont(Font.BOLD|Font.ITALIC, 38F));
		
		//Get item slot
		int itemSlot = getItemSlot();
		
		//Item name
		g2.drawString(gp.player.inventory.get(itemSlot).name, textX, textY);
		textY += 38;
		
		//Item description
		g2.setColor(Color.white);
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 28F));
		for(String line: gp.player.inventory.get(itemSlot).description.split("\n")) {
		g2.drawString(line, textX, textY);
		textY += 32;
		}
	}
	}
	public int getItemSlot() {
		return slotCol + slotRow * 5;
	}
	public void drawSubWindow(int x, int y, int width, int height) {
		//For dialogue and character stat screen
		Color c = new Color(0, 0, 0, 210);
		g2.setColor(c);
		g2.fillRoundRect(x, y, width, height, 35, 35);
		
		c = new Color(255, 255, 255);
		g2.setColor(c);
		g2.setStroke(new BasicStroke(5));
		g2.drawRoundRect(x+5, y+5, width-10, height-10, 35, 35);
	}
	
	public int getXforCenteredText(String text) {
		int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		int x = gp.screenWidth / 2 - length / 2;
		return x;
	}

	public int getXforRightAllignedText(String text, int tailX) {
		int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		int x = tailX - length;
		return x;
	}
}
