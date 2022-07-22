package monster;

import java.util.Random;

import entity.Entity;
import main.GamePanel;
import object.OBJ_Rock;

public class MON_DarkOrc extends Entity{

	GamePanel gp;
	public MON_DarkOrc(GamePanel gp) {
		super(gp);
		this.gp = gp;
		
		category = type_monster;
		name = "Dark Orc";
		attack = 5;
		defense = 0;
		speed = 1;
		maxLife = 80;
		life = maxLife;
		exp = 3;
		projectile = new OBJ_Rock(gp);
		solidArea.x = 2;
		solidArea.y = 1;
		solidArea.width = 42;
		solidArea.height = 40;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		
		getImage();
		
	}
	public void getImage() {
		up0 = setup("/monster/Enemy1Up0", gp.tileSize, gp.tileSize);
		up1 = setup("/monster/Enemy1Up1", gp.tileSize, gp.tileSize);
		up2 = setup("/monster/Enemy1Up0", gp.tileSize, gp.tileSize);
		up3 = setup("/monster/Enemy1Up2", gp.tileSize, gp.tileSize);
		down0 = setup("/monster/Enemy1Down0", gp.tileSize, gp.tileSize);
		down1 = setup("/monster/Enemy1Down1", gp.tileSize, gp.tileSize);
		down2 = setup("/monster/Enemy1Down0", gp.tileSize, gp.tileSize);
		down3 = setup("/monster/Enemy1Down2", gp.tileSize, gp.tileSize);
		left0 = setup("/monster/Enemy1Left0", gp.tileSize, gp.tileSize);
		left1 = setup("/monster/Enemy1Left1", gp.tileSize, gp.tileSize);
		left2 = setup("/monster/Enemy1Left0", gp.tileSize, gp.tileSize);
		left3 = setup("/monster/Enemy1Left2", gp.tileSize, gp.tileSize);
		right0 = setup("/monster/Enemy1Right0", gp.tileSize, gp.tileSize);
		right1 = setup("/monster/Enemy1Right1", gp.tileSize, gp.tileSize);
		right2 = setup("/monster/Enemy1Right0", gp.tileSize, gp.tileSize);
		right3 = setup("/monster/Enemy1Right2", gp.tileSize, gp.tileSize);
	}
	public void setAction() {
		actionLockCounter++;
		if(actionLockCounter == 120) {
		Random random = new Random();
		int i = random.nextInt(100) + 1; //random number 1-100 for npc to move
		if(i <= 20) {
			moving = true;
			direction = "up";
		}
		else if(i <= 40) {
			moving = true;
			direction = "down";
		}
		else if(i <= 60) {
			moving = true;
			direction = "left";
		}
		else if(i <= 80) {
			moving = true;
			direction = "right";
		}else moving = false;
		actionLockCounter = 0;
		}
		int i = new Random().nextInt(200) + 1;
		if(i > 199 && projectile.alive == false && shotCooldown == 80) {
			projectile.set(worldX, worldY, direction, true, this);
			gp.projectileList.add(projectile);
			shotCooldown = 0;
		}
	}
	
}
