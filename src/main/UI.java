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

import object.OBJ_Heart;
import object.OBJ_ManaCrystal;

public class UI {
	private GamePanel gp;
	private Graphics2D g2;
	private Font maruMonica, purisaBold, arial_12B, arial_38BI;
	private BufferedImage heart_full, heart_half, heart_blank, manaCrystal_full, manaCrystal_blank;
	public boolean messageOn = false;
	//public String message = "";
	//int messageCounter = 0;
	ArrayList<String> message = new ArrayList<>();
	ArrayList<Integer> messageCounter = new ArrayList<>();
	private String currentDialogue = "";
	private int commandNum = 0;
	private int titleScreenState = 0;
	private int maxOptions;
	private int subOptionState;
	//Inventory variables
	private int slotCol = 0;
	private int slotRow = 0;
	private int slotsInARow = 5;
	private int slotsInAColumn = 6;
	
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
		heart_full = heart.getImage();
		heart_half = heart.getImage1();
		heart_blank = heart.getImage2();
		Entity crystal = new OBJ_ManaCrystal(gp);
		manaCrystal_full = crystal.getImage();
		manaCrystal_blank = crystal.getImage2();
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
		if(gp.getGameState() == gp.titleState) drawTitleScreen();
		
		//PLAY STATE
		if(gp.getGameState() == gp.playState) {
			drawPlayerLifeandMana();
			drawEXPBar();
			drawMessage();
		}
		
		//PAUSE STATE
		if(gp.getGameState() == gp.pauseState) drawPauseScreen();
		
		//DIALOGUE STATE
		if(gp.getGameState() == gp.dialogueState) {
			g2.setFont(arial_12B);
			drawDialogueScreen();
			g2.setFont(maruMonica);
		}
		
		//CHARACTER STATE
		if(gp.getGameState() == gp.characterState) {
			drawCharacterScreen();
			drawInventory();
		}
		
		
	}
	public void drawPlayerLifeandMana() {
		int x = gp.getTileSize() / 2;
		int y = gp.getTileSize() / 2;
		int i = 0;
		
		//Draw all blank hearts
		while((i < gp.getPlayer().getMaxLife() / 2 && gp.getPlayer().getMaxLife() % 2 == 0) || (i <= gp.getPlayer().getMaxLife() / 2 && gp.getPlayer().getMaxLife() % 2 == 1)) {
			g2.drawImage(heart_blank, x, y, null);
			i++;
			x += gp.getTileSize();
		}
		//Reset i and reduce x
		i = 0;
		x = gp.getTileSize() / 2;
		
		//Draw current life
		while(i < gp.getPlayer().getLife()) {
			g2.drawImage(heart_half, x, y, null);
			i++;
			if(i < gp.getPlayer().getLife()) g2.drawImage(heart_full, x, y, null);
			i++;
			x += gp.getTileSize();
		}
		
		//Draw max mana
		x = gp.getTileSize() / 2 - 5;
		y = gp.getTileSize() + gp.getTileSize() / 2;
		i = 0;
		while(i < gp.getPlayer().getMaxMana()) {
			g2.drawImage(manaCrystal_blank, x, y, null);
			i++;
			x += 35;
		}
		
		//Draw current mana
		x = gp.getTileSize() / 2 - 5;
		y = gp.getTileSize() + gp.getTileSize() / 2;
		i = 0;
		while(i < gp.getPlayer().getMana()) {
			g2.drawImage(manaCrystal_full, x, y, null);
			i++;
			x += 35;
		}
	}
	public void drawEXPBar() {
		int x = gp.getTileSize() / 2;
		int y = gp.getTileSize() * 3;
		double oneScale = (double)gp.getTileSize() * 6 / gp.getPlayer().getNextLevelExp();
		double expBarValue = oneScale * gp.getPlayer().getExp();
		if(expBarValue > oneScale * gp.getPlayer().getNextLevelExp()) expBarValue = oneScale * gp.getPlayer().getNextLevelExp();
		
		g2.setColor(new Color(35, 35, 35));
		g2.fillRect(x , y - 16, gp.getTileSize() * 6, 30);
	
		g2.setColor(new Color(108, 127, 249));
		g2.fillRect(x + 5, y - 11, (int)expBarValue - 10, 20);
		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20F));
		g2.setColor(Color.white);
		g2.drawString("Level: " + gp.getPlayer().getLevel(), x + 10, y + 5);
		g2.drawString("EXP: " + gp.getPlayer().getExp() + "/" + gp.getPlayer().getNextLevelExp(), x + gp.getTileSize() * 4, y + 5);
	}
	public void drawMessage() {
		int messageX = gp.getTileSize() / 2;
		int messageY = gp.getTileSize() * 4;
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
				
				if(messageCounter.get(i) > gp.getFPS() * 3) {
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
		g2.fillRect(0, 0, gp.getScreenWidth(), gp.getScreenHeight());
		
		//TITLE NAME
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 96F));
		String text = "Adventure";
		int x = getXforCenteredText(text);
		int y = gp.getTileSize() * 3;
		//SHADOW FOR TITLE NAME
		g2.setColor(Color.gray);
		g2.drawString(text,x + 8, y + 8);
		//MAIN COLOR
		g2.setColor(Color.white);
		g2.drawString(text, x, y);
		
		//CHAR IMAGE
		
		x = gp.getTileSize();
		y += gp.getTileSize() * 2;
		g2.drawImage(titleChar, x, y, gp.getTileSize() * 6, gp.getTileSize() * 6, null);
		
		//MENU
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48F));
		
		text = "New game";
		x = getXforCenteredText(text);
		y += gp.getTileSize() * 2.5;
		g2.drawString(text, x, y);
		
		if(commandNum == 0) {
			g2.setColor(Color.cyan);
			g2.drawString(text, x, y);
			g2.setColor(Color.white);
		}
		
		text = "Load game";
		y += gp.getTileSize();
		g2.drawString(text, x, y);
		
		if(commandNum == 1) {
			g2.setColor(Color.cyan);
			g2.drawString(text, x, y);
			g2.setColor(Color.white);
		}
		
		text = "Controls";
		y += gp.getTileSize();
		g2.drawString(text, x, y);
		
		if(commandNum == 2) {
			g2.setColor(Color.cyan);
			g2.drawString(text, x, y);
			g2.setColor(Color.white);
		}
		
		text = "Quit";
		y += gp.getTileSize();
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
		
		g2.setColor(Color.white);
		g2.setFont(g2.getFont().deriveFont(32F));
		
		int frameX = gp.getTileSize() * 6;
		int frameY = gp.getTileSize();
		int frameWidth = gp.getTileSize() * 10;
		int frameHeight = gp.getTileSize() * 10;
		drawSubWindow(frameX, frameY, frameWidth, frameHeight);
		switch(subOptionState) {
		case 0: options_top(frameX, frameY);break;
		}
		
	}
	public void options_top(int frameX, int frameY) {
		int textX, textY;
		
		//title
		String text = "Options";
		textX = getXforCenteredText(text);
		textY = frameY + gp.getTileSize();
		g2.drawString(text, textX, textY);
		
		textX = frameX + gp.getTileSize();
		
		//music
		textY += gp.getTileSize() * 2;
		g2.drawString("Music", textX, textY);
		if(commandNum == 0) {
			g2.setColor(Color.cyan);
			g2.drawString("Music", textX, textY);
			g2.setColor(Color.white);
		}
		//sound effect
		textY += gp.getTileSize();
		g2.drawString("Sound effects", textX, textY);
		if(commandNum == 1) {
			g2.setColor(Color.cyan);
			g2.drawString("Sound effects", textX, textY);
			g2.setColor(Color.white);
		}
		//quit to menu
		textY += gp.getTileSize();
		g2.drawString("Quit to main menu", textX, textY);
		if(commandNum == 2) {
			g2.setColor(Color.cyan);
			g2.drawString("Quit to main menu", textX, textY);
			g2.setColor(Color.white);
		}
		//go back
		textY += gp.getTileSize() * 2;
		g2.drawString("Continue", textX, textY);
		if(commandNum == 3) {
			g2.setColor(Color.cyan);
			g2.drawString("Continue", textX, textY);
			g2.setColor(Color.white);
		}
		
		
		//Right side
		g2.setStroke(new BasicStroke(3));
		textX = gp.getTileSize() * 5 + frameX;
		textY = frameY + gp.getTileSize() * 2 + 24;
		
		//Music
		g2.drawRect(textX, textY, 180, 24);
		int volumeWidth = 36 * gp.getMusic().getVolumeScale();
		g2.fillRect(textX, textY, volumeWidth, 24);
		
		//Sound effects
		textY += gp.getTileSize();
		g2.drawRect(textX, textY, 180, 24);
		volumeWidth = 36 * gp.getSe().getVolumeScale();
		g2.fillRect(textX, textY, volumeWidth, 24);
	}
	public void drawDialogueScreen() {
		//WINDOW
		int x = gp.getTileSize() * 2;
		int y = gp.getTileSize() / 2;
		int width = gp.getScreenWidth() - (gp.getTileSize() * 4);
		int height = gp.getTileSize() * 4;
		drawSubWindow(x, y, width, height);
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 32F));
		x += gp.getTileSize();
		y += gp.getTileSize();
		
		for(String line : currentDialogue.split("\n")) {
		g2.drawString(line, x, y);
		y += 40;
		}
		
	}
	public void drawCharacterScreen() {
		
		// Create a frame
		final int frameX = gp.getTileSize() * 2;
		final int frameY = gp.getTileSize();
		final int frameWidth = gp.getTileSize() * 5;
		final int frameHeight = gp.getTileSize() * 11;
		drawSubWindow(frameX, frameY, frameWidth, frameHeight);
		
		//Text
		g2.setColor(Color.white);
		g2.setFont(g2.getFont().deriveFont(32F));
		
		int textX = frameX + 20;
		int textY = frameY + gp.getTileSize();
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
		
		textY = frameY + gp.getTileSize();
		String value;
		
		value = String.valueOf(gp.getPlayer().getLevel());
		textX = getXforRightAllignedText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.getPlayer().getLife() + "/" + gp.getPlayer().getMaxLife());
		textX = getXforRightAllignedText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.getPlayer().getMana() + "/" + gp.getPlayer().getMaxMana());
		textX = getXforRightAllignedText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;

		value = String.valueOf(gp.getPlayer().getStrength());
		textX = getXforRightAllignedText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.getPlayer().getDexterity());
		textX = getXforRightAllignedText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.getPlayer().getPlayerAttack());
		textX = getXforRightAllignedText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.getPlayer().getPlayerDefense());
		textX = getXforRightAllignedText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.getPlayer().getExp());
		textX = getXforRightAllignedText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.getPlayer().getNextLevelExp());
		textX = getXforRightAllignedText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.getPlayer().getCoin());
		textX = getXforRightAllignedText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;
		
		g2.drawImage(gp.getPlayer().getCurrentWeapon().getDown1(), tailX - gp.getTileSize(), textY - 24, null);
	}
	public void drawInventory() {
		
		//Frame window
		int slotSize = gp.getTileSize() + 10;
		int frameX = gp.getTileSize() * 9;
		int frameY = gp.getTileSize();
		int frameWidth = slotSize * (slotsInARow + 1);
		int frameHeight = slotSize * (slotsInAColumn + 1);
		drawSubWindow(frameX, frameY, frameWidth, frameHeight);
		//Slot
		final int slotXStart = frameX + 20;
		final int slotYStart = frameY + 20;
		int slotX = slotXStart;
		int slotY = slotYStart;
		
		//Draw items
		if(gp.getPlayer().getInventory().size() > 0) {
		
		for(int i = 0; i < gp.getPlayer().getInventory().size(); ++i) {
			
			//Equip cursor
			if(gp.getPlayer().getInventory().get(i) == gp.getPlayer().getCurrentWeapon()) {
				g2.setColor(new Color(240, 190, 90));
				g2.fillRoundRect(slotX, slotY, gp.getTileSize(), gp.getTileSize(), 10, 10);
			}
			
			g2.drawImage(gp.getPlayer().getInventory().get(i).getDown1(), slotX, slotY, null);
			slotX += slotSize;
			if((i+1) % 5 == 0 && i < gp.getPlayer().getMaxInventorySize()) {
				slotX = slotXStart;
				slotY += slotSize;
			}
		}
		
		//Cursor-pointer
		int cursorX = slotXStart + (slotSize * slotCol);
		int cursorY = slotYStart + (slotSize * slotRow);
		int cursorWidth = gp.getTileSize();
		int cursorHeight = gp.getTileSize();
		
		//Draw cursor
		g2.setColor(Color.yellow);
		g2.setStroke(new BasicStroke(3));
		g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);
		
		//Description
		int dFrameX = frameX;
		int dFrameY = frameY + frameHeight;
		int dFrameWidth = frameWidth;
		int dFrameHeight = gp.getTileSize() * 4;
		drawSubWindow(dFrameX, dFrameY, dFrameWidth, dFrameHeight);
		
		//Description text
		int textX = dFrameX + 20;
		int textY = dFrameY + gp.getTileSize();
		g2.setColor(Color.cyan);
		g2.setFont(g2.getFont().deriveFont(Font.BOLD|Font.ITALIC, 38F));
		
		//Get item slot
		int itemSlot = getItemSlot();
		
		//Item name
		g2.drawString(gp.getPlayer().getInventory().get(itemSlot).getName(), textX, textY);
		textY += 38;
		
		//Item description
		g2.setColor(Color.white);
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 28F));
		for(String line: gp.getPlayer().getInventory().get(itemSlot).getDescription().split("\n")) {
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
		int x = gp.getScreenWidth() / 2 - length / 2;
		return x;
	}

	public int getXforRightAllignedText(String text, int tailX) {
		int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		int x = tailX - length;
		return x;
	}
	public String getCurrentDialogue() {
		return currentDialogue;
	}
	public void setCurrentDialogue(String currentDialogue) {
		this.currentDialogue = currentDialogue;
	}
	public int getTitleScreenState() {
		return titleScreenState;
	}
	public void setTitleScreenState(int titleScreenState) {
		this.titleScreenState = titleScreenState;
	}
	public int getCommandNum() {
		return commandNum;
	}
	public void setCommandNum(int commandNum) {
		this.commandNum = commandNum;
	}
	public int getSlotCol() {
		return slotCol;
	}
	public void setSlotCol(int slotCol) {
		this.slotCol = slotCol;
	}
	public int getSlotRow() {
		return slotRow;
	}
	public void setSlotRow(int slotRow) {
		this.slotRow = slotRow;
	}
	public int getSlotsInARow() {
		return slotsInARow;
	}
	public int getSlotsInAColumn() {
		return slotsInAColumn;
	}
	public int getMaxOptions() {
		return maxOptions;
	}
	public void setMaxOptions(int maxOptions) {
		this.maxOptions = maxOptions;
	}
	public int getSubOptionState() {
		return subOptionState;
	}
	public void setSubOptionState(int subOptionState) {
		this.subOptionState = subOptionState;
	}

}
