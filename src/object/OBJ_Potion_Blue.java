package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Potion_Blue extends Entity{
	GamePanel gp;
	int healingValue = 5;
	public OBJ_Potion_Blue(GamePanel gp) {
		super(gp);
		this.gp = gp;
		category = type_consumable;
		name = "Blue Potion";
		moneyValue = 5;
		down1 = setup("/objects/potion_blue", gp.tileSize, gp.tileSize);
		description = "Heals " + healingValue + " mana.";
		
	}
	public void update() {
		
	}
	public void use(Entity entity) {
		gp.gameState = gp.dialogueState;
		gp.ui.currentDialogue = "Drank the " + name + ".\nRecovered " + healingValue + " mana.";
		entity.mana += healingValue;
		if(entity.mana > entity.maxMana) entity.mana = entity.maxMana;
		gp.ui.slotCol = 0;
		gp.ui.slotRow = 0;
		gp.playSE(15);
	}
}
