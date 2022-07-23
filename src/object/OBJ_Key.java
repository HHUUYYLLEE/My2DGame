package object;



import entity.Entity;
import main.GamePanel;

public class OBJ_Key extends Entity{
	public OBJ_Key(GamePanel gp) {
		super(gp);
		setName("Key");
		setDown1(setup("/objects/key", gp.getTileSize(), gp.getTileSize()));
		setDescription("Opens a door or chest.");
	}
}
