package main;

public class Projectile extends Entity{
	private Entity user;
	public Projectile(GamePanel gp) {
		super(gp);
	}
	public void set(int worldX, int worldY, String direction, boolean alive, Entity user) {
		this.setWorldX(worldX);
		this.setWorldY(worldY);
		this.setDirection(direction);
		this.setAlive(alive);
		this.user = user;
		this.setLife(this.getMaxLife());
	}
	
	public boolean haveResources(Entity user) {
		//Unused, only for inheritance
		return false;
	}
	public void subtractResources(Entity user) {
		//Unused, only for inheritance
	}
	public void update() {
		switch(getDirection()) {
		case "up": setWorldY(getWorldY() - getSpeed()); break;
		case "down": setWorldY(getWorldY() + getSpeed()); break;
		case "left": setWorldX(getWorldX() - getSpeed()); break;
		case "right": setWorldX(getWorldX() + getSpeed()); break;
		}
		setSpriteCounter(getSpriteCounter() + 1);
		if(getSpriteCounter() > 12) {
			if(getSpriteNum() == 0) setSpriteNum(1);
			else if(getSpriteNum() == 1) setSpriteNum(2);
			else if(getSpriteNum() == 2) setSpriteNum(3);
			else setSpriteNum(0);
			setSpriteCounter(0);
		}
		if(user == gp.getPlayer()) {
			int monsterIndex = gp.getcChecker().checkEntity(this, gp.getMonster());
			if(monsterIndex != 999) {
				gp.getPlayer().damageMonster(monsterIndex);
				setAlive(false);
			}
		}else {
			//Enemies' projecttiles
			boolean contactPlayer = gp.getcChecker().checkPlayer(this);
			if(!gp.getPlayer().isInvincible() && contactPlayer) {
				damagePlayer(getAttack());
				setAlive(false);
			}
		}
		decreaseLife(1);
		if(getLife() <= 0) setAlive(false);
	}
	public Entity getUser() {
		return user;
	}
	
}

