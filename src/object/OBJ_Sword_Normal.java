package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Sword_Normal extends Entity{
	
	public OBJ_Sword_Normal(GamePanel gp) {
		super(gp);
		
		name = "Normal Sword";
		category = type_sword;
		down1 = setup("/objects/sword_normal", gp.tileSize, gp.tileSize);
		attackValue = 1;
		description = "Mt 1\nA regular sword.";
		attackArea.width = gp.tileSize * 6 / 8; //36
		attackArea.height = gp.tileSize * 6 / 8;//36
	}
}
