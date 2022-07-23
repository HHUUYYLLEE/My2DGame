package object;



import entity.Entity;
import main.GamePanel;

public class OBJ_Heart extends Entity{
	
	public OBJ_Heart(GamePanel gp) {
		super(gp);
		setName("Heart");
		setImage(setup("/objects/heart_full", gp.getTileSize(), gp.getTileSize()));
		setImage1(setup("/objects/heart_half", gp.getTileSize(), gp.getTileSize()));
		setImage2(setup("/objects/heart_blank", gp.getTileSize(), gp.getTileSize()));
	}

}
