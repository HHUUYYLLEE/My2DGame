package object;

import main.Entity;
import main.GamePanel;

public class OBJ_Potion_Red extends Entity{
	GamePanel gp;
	int healingValue = 5;
	public OBJ_Potion_Red(GamePanel gp) {
		super(gp);
		this.gp = gp;
		setCategory(type_consumable);
		setName("Red Potion");
		setMoneyValue(5);
		setDown1(setup("/objects/potion_red", gp.getTileSize(), gp.getTileSize()));
		setDescription("Heals " + healingValue + " HP.");
		
	}
	public void update() {
		
	}
	public void use(Entity entity) {
		gp.setGameState(gp.dialogueState);
		gp.getUi().setCurrentDialogue("Drank the " + getName() + ".\nRecovered " + healingValue + " HP.");
		entity.increaseLife(healingValue);
		if(entity.getLife() > entity.getMaxLife()) entity.setLife(entity.getMaxLife());
		gp.getUi().setSlotCol(0);
		gp.getUi().setSlotRow(0);
		gp.playSE(15);
	}
}
