package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class KeyHandler implements KeyListener{
	GamePanel gp;
	public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed, attackPressed, magicKeyPressed;
	int previousValue;
	
	public KeyHandler(GamePanel gp) {
		this.gp = gp;
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		//Title state inputs
		if(gp.gameState == gp.titleState) titleState(code);
		//Play state inputs
		else if(gp.gameState == gp.playState) playState(code);
		//Pause state inputs
		else if(gp.gameState == gp.pauseState) pauseState(code);
		//Dialogue state inputs
		else if(gp.gameState == gp.dialogueState) dialogueState(code);
		//Character state inputs
		else if(gp.gameState == gp.characterState) characterState(code);
	}
	public void titleState(int code) {
		if(gp.ui.titleScreenState == 0) {
			if(code == KeyEvent.VK_UP) {
				gp.ui.commandNum--;
				if(gp.ui.commandNum < 0) gp.ui.commandNum = 3;
			}
			if(code == KeyEvent.VK_DOWN) {
				gp.ui.commandNum++;
				if(gp.ui.commandNum > 3) gp.ui.commandNum = 0;
			}
			if(code == KeyEvent.VK_ENTER) {
				if(gp.ui.commandNum == 0) {
					gp.playMusic(0);
					gp.gameState = gp.playState;
				}
				if(gp.ui.commandNum == 1) {
					
				}
				if(gp.ui.commandNum == 2) gp.ui.titleScreenState = 2;
				if(gp.ui.commandNum == 3) System.exit(0);
			}
		}else if(gp.ui.titleScreenState == 2 && code == KeyEvent.VK_ENTER) gp.ui.titleScreenState = 0;
	}
	public void playState(int code) {
		gp.playThud = true;
		if(code == KeyEvent.VK_W) upPressed = true;
		if(code == KeyEvent.VK_S) downPressed = true;
		if(code == KeyEvent.VK_A) leftPressed = true;
		if(code == KeyEvent.VK_D) rightPressed = true;
		if(code == KeyEvent.VK_P) gp.gameState = gp.pauseState;
		if(code == KeyEvent.VK_ENTER) enterPressed = true;
		if(code == KeyEvent.VK_J) attackPressed = true;
		if(code == KeyEvent.VK_K) gp.gameState = gp.characterState;
		if(code == KeyEvent.VK_F) magicKeyPressed = true;
		
		//Debug when making new maps
		//if(code == KeyEvent.VK_R) gp.tileM.loadMap("/maps/worldV2.txt");
	}
	public void pauseState(int code) {
		gp.playThud = false;
		if(code == KeyEvent.VK_P) {
			gp.gameState = gp.playState;
			gp.playThud = true;
		
		}
	}
	public void dialogueState(int code) {
		gp.playThud = false;
		if(code == KeyEvent.VK_ENTER) {
			gp.gameState = gp.playState;
			gp.playThud = true;
		}
	}
	public void characterState(int code) {
		gp.playThud = false;
		if(code == KeyEvent.VK_K) {
			gp.gameState = gp.playState;
			gp.playThud = true;
		}
		if(gp.player.inventory.size() > 0) {
		if(code == KeyEvent.VK_W) {
			previousValue = gp.ui.slotRow;
			gp.ui.slotRow--;
			if(gp.ui.slotRow < 0) gp.ui.slotRow = gp.ui.slotsInAColumn - 1;
			while(gp.ui.slotRow * gp.ui.slotsInARow + gp.ui.slotCol + 1 > gp.player.inventory.size()) gp.ui.slotRow--;
			if(gp.ui.slotRow == previousValue) gp.playSE(14);
			else gp.playSE(13);
		}
		if(code == KeyEvent.VK_S) {
			previousValue = gp.ui.slotRow;
			gp.ui.slotRow++;
			if(gp.ui.slotRow > gp.ui.slotsInAColumn - 1 || gp.ui.slotRow * gp.ui.slotsInARow + gp.ui.slotCol + 1 > gp.player.inventory.size()) gp.ui.slotRow = 0;
			if(gp.ui.slotRow == previousValue) gp.playSE(14);
				else gp.playSE(13);
		}
		if(code == KeyEvent.VK_A) {
			previousValue = gp.ui.slotCol;
			gp.ui.slotCol--;
			if(gp.ui.slotCol < 0) gp.ui.slotCol = gp.ui.slotsInARow - 1; 
			while(gp.ui.slotRow * gp.ui.slotsInARow + gp.ui.slotCol + 1 > gp.player.inventory.size()) gp.ui.slotCol--;
			if(gp.ui.slotCol == previousValue) gp.playSE(14);
			else gp.playSE(13);
		}
		if(code == KeyEvent.VK_D) {
			previousValue = gp.ui.slotCol;
			gp.ui.slotCol++;
			if(gp.ui.slotCol > gp.ui.slotsInARow - 1 || gp.ui.slotRow * gp.ui.slotsInARow + gp.ui.slotCol + 1 > gp.player.inventory.size()) gp.ui.slotCol = 0;
			if(gp.ui.slotCol == previousValue) gp.playSE(14);
			else gp.playSE(13);
		}
		} else if(code == KeyEvent.VK_W || code == KeyEvent.VK_S || code == KeyEvent.VK_A || code == KeyEvent.VK_D) gp.playSE(14);
		if(code == KeyEvent.VK_ENTER) gp.player.selectItem();
	
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		if(code == KeyEvent.VK_W) upPressed = false;
		if(code == KeyEvent.VK_S) downPressed = false;
		if(code == KeyEvent.VK_A) leftPressed = false;
		if(code == KeyEvent.VK_D) rightPressed = false;
		if(code == KeyEvent.VK_J) attackPressed = false;
		if(code == KeyEvent.VK_F) magicKeyPressed = false;
	}

}
