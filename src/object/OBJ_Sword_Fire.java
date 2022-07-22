package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Sword_Fire extends Entity{
	
	public OBJ_Sword_Fire(GamePanel gp) {
		super(gp);
		
		name = "Fire Sword";
		category = type_sword;
		down1 = setup("/objects/sword_fire", gp.tileSize, gp.tileSize);
		attackValue = 3;
		description = "Mt 3\nA sword engulfed in flames.";
		attackArea.width = gp.tileSize * 6 / 8; //36
		attackArea.height = gp.tileSize * 6 / 8;//36
	}
	public void update() {
		
	}
}
