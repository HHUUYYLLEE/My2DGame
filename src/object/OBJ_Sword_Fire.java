package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Sword_Fire extends Entity{
	
	public OBJ_Sword_Fire(GamePanel gp) {
		super(gp);
		
		setName("Fire Sword");
		setCategory(type_sword);
		setDown1(setup("/objects/sword_fire", gp.getTileSize(), gp.getTileSize()));
		setAttackValue(3);
		setDescription("Mt 3\nA sword engulfed in flames.");
		getAttackArea().width = gp.getTileSize() * 6 / 8; //36
		getAttackArea().height = gp.getTileSize() * 6 / 8;//36
	}
	public void update() {
		
	}
}
