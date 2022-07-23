package object;



import main.Entity;
import main.GamePanel;

public class OBJ_Door extends Entity{
	
	public OBJ_Door(GamePanel gp) {
		super(gp);
		setName("Door");
		setDown1(setup("/objects/door", gp.getTileSize(), gp.getTileSize()));
		setCollision(true);
		
		getSolidArea().x = 0;
		getSolidArea().y = 16;
		getSolidArea().width = 48;
		getSolidArea().height = 32;
		setSolidAreaDefaultX(getSolidArea().x);
		setSolidAreaDefaultY(getSolidArea().y);
	}
}
