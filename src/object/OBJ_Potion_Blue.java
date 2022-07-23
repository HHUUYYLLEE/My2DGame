package object;

import main.Entity;
import main.GamePanel;

public class OBJ_Potion_Blue extends Entity{
	private GamePanel gp;
	int healingValue = 5;
	public OBJ_Potion_Blue(GamePanel gp) {
		super(gp);
		this.gp = gp;
		setCategory(type_consumable);
		setName("Blue Potion");
		setMoneyValue(5);
		setDown1(setup("/objects/potion_blue", gp.getTileSize(), gp.getTileSize()));
		setDescription("Heals " + healingValue + " mana.");
		
	}
	public void update() {
		
	}
	public void use(Entity entity) {
		gp.setGameState(gp.dialogueState);
		gp.getUi().setCurrentDialogue("Drank the " + getName() + ".\nRecovered " + healingValue + " mana.");
		entity.increaseMana(healingValue);
		if(entity.getMana() > entity.getMaxMana()) entity.setMana(entity.getMaxMana());
		gp.getUi().setSlotCol(0);
		gp.getUi().setSlotRow(0);
		gp.playSE(15);
	}
}
