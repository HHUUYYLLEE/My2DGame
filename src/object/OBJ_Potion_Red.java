package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Potion_Red extends Entity{
	GamePanel gp;
	int healingValue = 5;
	public OBJ_Potion_Red(GamePanel gp) {
		super(gp);
		this.gp = gp;
		category = type_consumable;
		name = "Red Potion";
		moneyValue = 5;
		down1 = setup("/objects/potion_red", gp.tileSize, gp.tileSize);
		description = "Heals " + healingValue + " HP.";
		
	}
	public void update() {
		
	}
	public void use(Entity entity) {
		gp.gameState = gp.dialogueState;
		gp.ui.currentDialogue = "Drank the " + name + ".\nRecovered " + healingValue + " HP.";
		entity.life += healingValue;
		if(entity.life > entity.maxLife) entity.life = entity.maxLife;
		gp.ui.slotCol = 0;
		gp.ui.slotRow = 0;
		gp.playSE(15);
	}
}
