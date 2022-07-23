package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Shield_Wood extends Entity{

	public OBJ_Shield_Wood(GamePanel gp) {
		super(gp);
		
		setName("Wooden Shield");
		setDefenseValue(1);
	}
}
