package object;

import entity.Entity;
import entity.Projectile;
import main.GamePanel;

public class OBJ_Rock extends Projectile{
	GamePanel gp;

	public OBJ_Rock(GamePanel gp) {
		super(gp);
		this.gp = gp;
		
		name = "rock";
		speed = 6;
		maxLife = 80;
		life = maxLife;
		attack = 2;
		useCost = 1;
		alive = false;
		getImage();
	}
	public boolean haveResources(Entity user) {
		return user.ammo >= useCost;
	}
	public void subtractResources(Entity user) {
		user.ammo -= useCost;
	}
	public void getImage() {
	
		up0 = setup("/projectile/rockprojectile", gp.tileSize, gp.tileSize);
		up1 = setup("/projectile/rockprojectile", gp.tileSize, gp.tileSize);
		up2 = setup("/projectile/rockprojectile", gp.tileSize, gp.tileSize);
		up3 = setup("/projectile/rockprojectile", gp.tileSize, gp.tileSize);
		down0 = setup("/projectile/rockprojectile", gp.tileSize, gp.tileSize);
		down1 = setup("/projectile/rockprojectile", gp.tileSize, gp.tileSize);
		down2 = setup("/projectile/rockprojectile", gp.tileSize, gp.tileSize);
		down3 = setup("/projectile/rockprojectile", gp.tileSize, gp.tileSize);
		left0 = setup("/projectile/rockprojectile", gp.tileSize, gp.tileSize);
		left1 = setup("/projectile/rockprojectile", gp.tileSize, gp.tileSize);
		left2 = setup("/projectile/rockprojectile", gp.tileSize, gp.tileSize);
		left3 = setup("/projectile/rockprojectile", gp.tileSize, gp.tileSize);
		right0 = setup("/projectile/rockprojectile", gp.tileSize, gp.tileSize);
		right1 = setup("/projectile/rockprojectile", gp.tileSize, gp.tileSize);
		right2 = setup("/projectile/rockprojectile", gp.tileSize, gp.tileSize);
		right3 = setup("/projectile/rockprojectile", gp.tileSize, gp.tileSize);
}
}
