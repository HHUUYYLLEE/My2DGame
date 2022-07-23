package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class KeyHandler implements KeyListener{
	private GamePanel gp;
	private boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed, attackPressed, magicKeyPressed;
	private int previousValue;
	
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
		if(gp.getGameState() == gp.titleState) titleState(code);
		//Play state inputs
		else if(gp.getGameState() == gp.playState) playState(code);
		//Pause state inputs
		else if(gp.getGameState() == gp.pauseState) pauseState(code);
		//Dialogue state inputs
		else if(gp.getGameState() == gp.dialogueState) dialogueState(code);
		//Character state inputs
		else if(gp.getGameState() == gp.characterState) characterState(code);
	}
	public void titleState(int code) {
		gp.getUi().setMaxOptions(4);
		if(gp.getUi().getTitleScreenState() == 0) {
			if(code == KeyEvent.VK_UP) {
				gp.getUi().setCommandNum(gp.getUi().getCommandNum() - 1);
				if(gp.getUi().getCommandNum() < 0) gp.getUi().setCommandNum(gp.getUi().getMaxOptions() - 1);
			}
			if(code == KeyEvent.VK_DOWN) {
				gp.getUi().setCommandNum(gp.getUi().getCommandNum() + 1);
				if(gp.getUi().getCommandNum() > gp.getUi().getMaxOptions() - 1) gp.getUi().setCommandNum(0);
			}
			if(code == KeyEvent.VK_ENTER) {
				if(gp.getUi().getCommandNum() == 0) {
					gp.playMusic(0);
					gp.setGameState(gp.playState);
				}
				if(gp.getUi().getCommandNum() == 1) {
					
				}
				if(gp.getUi().getCommandNum() == 2) gp.getUi().setTitleScreenState(2);
				if(gp.getUi().getCommandNum() == gp.getUi().getMaxOptions() - 1) System.exit(0);
			}
		}else if(gp.getUi().getTitleScreenState() == 2 && code == KeyEvent.VK_ENTER) gp.getUi().setTitleScreenState(0);
	}
	public void playState(int code) {
		gp.changePlayThud(true);
		if(code == KeyEvent.VK_W) upPressed = true;
		if(code == KeyEvent.VK_S) downPressed = true;
		if(code == KeyEvent.VK_A) leftPressed = true;
		if(code == KeyEvent.VK_D) rightPressed = true;
		if(code == KeyEvent.VK_P) gp.setGameState(gp.pauseState);
		if(code == KeyEvent.VK_ENTER) enterPressed = true;
		if(code == KeyEvent.VK_J) attackPressed = true;
		if(code == KeyEvent.VK_K) gp.setGameState(gp.characterState);
		if(code == KeyEvent.VK_F) magicKeyPressed = true;
		
		//Debug when making new maps
		//if(code == KeyEvent.VK_R) gp.tileM.loadMap("/maps/worldV2.txt");
	}
	public void pauseState(int code) {
		
		gp.changePlayThud(false);
		if(code == KeyEvent.VK_P) {
			gp.setGameState(gp.playState);
			gp.changePlayThud(true);
		}
		switch(gp.getUi().getSubOptionState()) {
		case 0: gp.getUi().setMaxOptions(4); break;
		}
		if(code == KeyEvent.VK_UP) {
			gp.getUi().setCommandNum(gp.getUi().getCommandNum() - 1);
			if(gp.getUi().getCommandNum() < 0) gp.getUi().setCommandNum(gp.getUi().getMaxOptions() - 1);
			gp.playSE(13);
		}
		if(code == KeyEvent.VK_DOWN) {
			gp.getUi().setCommandNum(gp.getUi().getCommandNum() + 1);
			if(gp.getUi().getCommandNum() > gp.getUi().getMaxOptions() - 1) gp.getUi().setCommandNum(0);
			gp.playSE(13);
		}
		if(code == KeyEvent.VK_ENTER) {
			if(gp.getUi().getCommandNum() == 2) gp.gameEnd();
			if(gp.getUi().getCommandNum() == 3) gp.setGameState(gp.playState);
		}
		if(code == KeyEvent.VK_LEFT) {
			if(gp.getUi().getSubOptionState() == 0) {
				if(gp.getUi().getCommandNum() == 0){
					if(gp.getMusic().getVolumeScale() > 0) {
					gp.getMusic().decreaseVolumeScale(1);
					gp.getMusic().checkVolume();
					gp.playSE(13);
				}
				else gp.playSE(14);
				}
				else if(gp.getUi().getCommandNum() == 1) {
					if(gp.getSe().getVolumeScale() > 0) {
					gp.getSe().decreaseVolumeScale(1);
					gp.playSE(13);
				}
				else gp.playSE(14);
			}else gp.playSE(14);
		}
	}
		if(code == KeyEvent.VK_RIGHT) {
			if(gp.getUi().getSubOptionState() == 0) {
				if(gp.getUi().getCommandNum() == 0) {
					if(gp.getMusic().getVolumeScale() < 5) {
					gp.getMusic().increaseVolumeScale(1);
					gp.getMusic().checkVolume();
					gp.playSE(13);
				}
				else gp.playSE(14);
			}
				else if(gp.getUi().getCommandNum() == 1) {
					if(gp.getSe().getVolumeScale() < 5) {
					gp.getSe().increaseVolumeScale(1);
					gp.playSE(13);
				}
				else gp.playSE(14);
				}
				else gp.playSE(14);
			}
		}
	}
	public void dialogueState(int code) {
		gp.changePlayThud(false);
		if(code == KeyEvent.VK_ENTER) {
			gp.setGameState(gp.playState);
			gp.changePlayThud(true);
		}
	}
	public void characterState(int code) {
		gp.changePlayThud(false);
		if(code == KeyEvent.VK_K) {
			gp.setGameState(gp.playState);
			gp.changePlayThud(true);
		}
		if(gp.getPlayer().getInventory().size() > 0) {
		if(code == KeyEvent.VK_W) {
			previousValue = gp.getUi().getSlotRow();
			gp.getUi().setSlotRow(gp.getUi().getSlotRow() - 1);
			if(gp.getUi().getSlotRow() < 0) gp.getUi().setSlotRow(gp.getUi().getSlotsInAColumn() - 1);
			while(gp.getUi().getSlotRow() * gp.getUi().getSlotsInARow() + gp.getUi().getSlotCol() + 1 > gp.getPlayer().getInventory().size()) 
				gp.getUi().setSlotRow(gp.getUi().getSlotRow() - 1);
			if(gp.getUi().getSlotRow() == previousValue) gp.playSE(14);
			else gp.playSE(13);
		}
		if(code == KeyEvent.VK_S) {
			previousValue = gp.getUi().getSlotRow();
			gp.getUi().setSlotRow(gp.getUi().getSlotRow() + 1);
			if(gp.getUi().getSlotRow() > gp.getUi().getSlotsInAColumn() - 1 || gp.getUi().getSlotRow() * gp.getUi().getSlotsInARow() + gp.getUi().getSlotCol() + 1 > gp.getPlayer().getInventory().size()) gp.getUi().setSlotRow(0);
			if(gp.getUi().getSlotRow() == previousValue) gp.playSE(14);
				else gp.playSE(13);
		}
		if(code == KeyEvent.VK_A) {
			previousValue = gp.getUi().getSlotCol();
			gp.getUi().setSlotCol(gp.getUi().getSlotCol() - 1);
			if(gp.getUi().getSlotCol() < 0) gp.getUi().setSlotCol(gp.getUi().getSlotsInARow() - 1); 
			while(gp.getUi().getSlotRow() * gp.getUi().getSlotsInARow() + gp.getUi().getSlotCol() + 1 > gp.getPlayer().getInventory().size()) gp.getUi().setSlotCol(gp.getUi().getSlotCol() - 1);
			if(gp.getUi().getSlotCol() == previousValue) gp.playSE(14);
			else gp.playSE(13);
		}
		if(code == KeyEvent.VK_D) {
			previousValue = gp.getUi().getSlotCol();
			gp.getUi().setSlotCol(gp.getUi().getSlotCol() + 1);
			if(gp.getUi().getSlotCol() > gp.getUi().getSlotsInARow() - 1 || gp.getUi().getSlotRow() * gp.getUi().getSlotsInARow() + gp.getUi().getSlotCol() + 1 > gp.getPlayer().getInventory().size()) gp.getUi().setSlotCol(0);
			if(gp.getUi().getSlotCol() == previousValue) gp.playSE(14);
			else gp.playSE(13);
		}
		} else if(code == KeyEvent.VK_W || code == KeyEvent.VK_S || code == KeyEvent.VK_A || code == KeyEvent.VK_D) gp.playSE(14);
		if(code == KeyEvent.VK_ENTER) gp.getPlayer().selectItem();
	
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
	public boolean isUpPressed() {
		return upPressed;
	}
	public void setUpPressed(boolean upPressed) {
		this.upPressed = upPressed;
	}
	public boolean isDownPressed() {
		return downPressed;
	}
	public void setDownPressed(boolean downPressed) {
		this.downPressed = downPressed;
	}
	public boolean isLeftPressed() {
		return leftPressed;
	}
	public void setLeftPressed(boolean leftPressed) {
		this.leftPressed = leftPressed;
	}
	public boolean isRightPressed() {
		return rightPressed;
	}
	public void setRightPressed(boolean rightPressed) {
		this.rightPressed = rightPressed;
	}
	public boolean isEnterPressed() {
		return enterPressed;
	}
	public void setEnterPressed(boolean enterPressed) {
		this.enterPressed = enterPressed;
	}
	public boolean isAttackPressed() {
		return attackPressed;
	}
	public void setAttackPressed(boolean attackPressed) {
		this.attackPressed = attackPressed;
	}
	public boolean isMagicKeyPressed() {
		return magicKeyPressed;
	}
	public void setMagicKeyPressed(boolean magicKeyPressed) {
		this.magicKeyPressed = magicKeyPressed;
	}

}
