package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_ManaCrystal extends Entity{
	GamePanel gp;
	public OBJ_ManaCrystal(GamePanel gp) {
		super(gp);
		this.gp = gp;
		
		setName("Mana Crystal");
		setImage(setup("/objects/manacrystal_full", gp.getTileSize(), gp.getTileSize()));
		setImage2(setup("/objects/manacrystal_blank", gp.getTileSize(), gp.getTileSize()));
	}

}
