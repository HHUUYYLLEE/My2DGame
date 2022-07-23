package monster;

import java.util.Random;

import main.Entity;
import main.GamePanel;
import object.OBJ_Coin;
import object.OBJ_Rock;

public class MON_DarkOrc extends Entity{

	private GamePanel gp;
	public MON_DarkOrc(GamePanel gp) {
		super(gp);
		this.gp = gp;
		
		setCategory(type_monster);
		setName("Dark Orc");
		setAttack(5);
		setDefense(0);
		setSpeed(1);
		setMaxLife(80);
		setLife(getMaxLife());
		setExp(3);
		setProjectile(new OBJ_Rock(gp));
		getSolidArea().x = 2;
		getSolidArea().y = 1;
		getSolidArea().width = 42;
		getSolidArea().height = 40;
		setSolidAreaDefaultX(getSolidArea().x);
		setSolidAreaDefaultY(getSolidArea().y);
		
		getMonsterImage();
		
	}
	public void getMonsterImage() {
		setUp0(setup("/monster/Enemy1Up0", gp.getTileSize(), gp.getTileSize()));
		setUp1(setup("/monster/Enemy1Up1", gp.getTileSize(), gp.getTileSize()));
		setUp2(setup("/monster/Enemy1Up0", gp.getTileSize(), gp.getTileSize()));
		setUp3(setup("/monster/Enemy1Up2", gp.getTileSize(), gp.getTileSize()));
		setDown0(setup("/monster/Enemy1Down0", gp.getTileSize(), gp.getTileSize()));
		setDown1(setup("/monster/Enemy1Down1", gp.getTileSize(), gp.getTileSize()));
		setDown2(setup("/monster/Enemy1Down0", gp.getTileSize(), gp.getTileSize()));
		setDown3(setup("/monster/Enemy1Down2", gp.getTileSize(), gp.getTileSize()));
		setLeft0(setup("/monster/Enemy1Left0", gp.getTileSize(), gp.getTileSize()));
		setLeft1(setup("/monster/Enemy1Left1", gp.getTileSize(), gp.getTileSize()));
		setLeft2(setup("/monster/Enemy1Left0", gp.getTileSize(), gp.getTileSize()));
		setLeft3(setup("/monster/Enemy1Left2", gp.getTileSize(), gp.getTileSize()));
		setRight0(setup("/monster/Enemy1Right0", gp.getTileSize(), gp.getTileSize()));
		setRight1(setup("/monster/Enemy1Right1", gp.getTileSize(), gp.getTileSize()));
		setRight2(setup("/monster/Enemy1Right0", gp.getTileSize(), gp.getTileSize()));
		setRight3(setup("/monster/Enemy1Right2", gp.getTileSize(), gp.getTileSize()));
	}
	public void setAction() {
		setActionLockCounter(getActionLockCounter() + 1);
		if(getActionLockCounter() == 120) {
		Random random = new Random();
		int i = random.nextInt(100) + 1; //random number 1-100 for npc to move
		if(i <= 20) {
			setMoving(true);
			setDirection("up");
		}
		else if(i <= 40) {
			setMoving(true);
			setDirection("down");
		}
		else if(i <= 60) {
			setMoving(true);
			setDirection("left");
		}
		else if(i <= 80) {
			setMoving(true);
			setDirection("right");
		}else setMoving(false);
		setActionLockCounter(0);
		}
		int i = new Random().nextInt(200) + 1;
		if(i > 199 && !getProjectile().isAlive() && getShotCooldown() == 80) {
			getProjectile().set(getWorldX(), getWorldY(), getDirection(), true, this);
			gp.getProjectileList().add(getProjectile());
			setShotCooldown(0);
		}
	}
	public void checkDrop() {
		int i = new Random().nextInt(100) + 1;
		if(i < 50) dropItem(new OBJ_Coin(gp));
	}
}
