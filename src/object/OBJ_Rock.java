package object;

import main.Entity;
import main.GamePanel;
import main.Projectile;

public class OBJ_Rock extends Projectile{
	private GamePanel gp;

	public OBJ_Rock(GamePanel gp) {
		super(gp);
		this.gp = gp;
		setName("rock");
		setSpeed(6);
		setMaxLife(80);
		setLife(getMaxLife());
		setAttack(2);
		setUseCost(1);
		setAlive(false);
		getRockImage();
	}
	public boolean haveResources(Entity user) {
		return user.getAmmo() >= getUseCost();
	}
	public void subtractResources(Entity user) {
		user.decreaseAmmo(getUseCost());
	}
	public void getRockImage() {
	
		setUp0(setup("/projectile/rockprojectile", gp.getTileSize(), gp.getTileSize()));
		setUp1(setup("/projectile/rockprojectile", gp.getTileSize(), gp.getTileSize()));
		setUp2(setup("/projectile/rockprojectile", gp.getTileSize(), gp.getTileSize()));
		setUp3(setup("/projectile/rockprojectile", gp.getTileSize(), gp.getTileSize()));
		setDown0(setup("/projectile/rockprojectile", gp.getTileSize(), gp.getTileSize()));
		setDown1(setup("/projectile/rockprojectile", gp.getTileSize(), gp.getTileSize()));
		setDown2(setup("/projectile/rockprojectile", gp.getTileSize(), gp.getTileSize()));
		setDown3(setup("/projectile/rockprojectile", gp.getTileSize(), gp.getTileSize()));
		setLeft0(setup("/projectile/rockprojectile", gp.getTileSize(), gp.getTileSize()));
		setLeft1(setup("/projectile/rockprojectile", gp.getTileSize(), gp.getTileSize()));
		setLeft2(setup("/projectile/rockprojectile", gp.getTileSize(), gp.getTileSize()));
		setLeft3(setup("/projectile/rockprojectile", gp.getTileSize(), gp.getTileSize()));
		setRight0(setup("/projectile/rockprojectile", gp.getTileSize(), gp.getTileSize()));
		setRight1(setup("/projectile/rockprojectile", gp.getTileSize(), gp.getTileSize()));
		setRight2(setup("/projectile/rockprojectile", gp.getTileSize(), gp.getTileSize()));
		setRight3(setup("/projectile/rockprojectile", gp.getTileSize(), gp.getTileSize()));
}
}
