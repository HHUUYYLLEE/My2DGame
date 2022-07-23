package object;

import main.Entity;
import main.GamePanel;

public class OBJ_Sword_Normal extends Entity{
	
	public OBJ_Sword_Normal(GamePanel gp) {
		super(gp);
		
		setName("Normal Sword");
		setCategory(type_sword);
		setDown1(setup("/objects/sword_normal", gp.getTileSize(), gp.getTileSize()));
		setAttackValue(1);
		setDescription("Mt 1\nA regular sword.");
		getAttackArea().width = gp.getTileSize() * 6 / 8; //36
		getAttackArea().height = gp.getTileSize() * 6 / 8;//36
	}
}
