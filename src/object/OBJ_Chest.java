package object;



import entity.Entity;
import main.GamePanel;

public class OBJ_Chest extends Entity{
	
	public OBJ_Chest(GamePanel gp) {
		super(gp);
		setName("Chest");
		setDown1(setup("/objects/chest", gp.getTileSize(), gp.getTileSize()));
		setDescription("A treasure chest.");
	}
}
