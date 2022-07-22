package entity;


import main.GamePanel;

public class Projectile extends Entity{
	public Entity user;
	public Projectile(GamePanel gp) {
		super(gp);
	}
	public void set(int worldX, int worldY, String direction, boolean alive, Entity user) {
		this.worldX = worldX;
		this.worldY = worldY;
		this.direction = direction;
		this.alive = alive;
		this.user = user;
		this.life = this.maxLife;
	}
	
	public boolean haveResources(Entity user) {
		//Unused, only for inheritance
		return false;
	}
	public void subtractResources(Entity user) {
		//Unused, only for inheritance
	}
	public void update() {
		switch(direction) {
		case "up": worldY -= speed; break;
		case "down": worldY += speed; break;
		case "left": worldX -= speed; break;
		case "right": worldX += speed; break;
		}
		spriteCounter++;
		if(spriteCounter > 12) {
			if(spriteNum == 0) spriteNum = 1;
			else if(spriteNum == 1) spriteNum = 2;
			else if(spriteNum == 2) spriteNum = 3;
			else spriteNum = 0;
			spriteCounter = 0;
		}
		if(user == gp.player) {
			int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
			if(monsterIndex != 999) {
				gp.player.damageMonster(monsterIndex);
				alive = false;
			}
		}else {
			//Enemies' projecttiles
			boolean contactPlayer = gp.cChecker.checkPlayer(this);
			if(gp.player.invincible == false && contactPlayer == true) {
				damagePlayer(attack);
				alive = false;
			}
		}
		life--;
		if(life <= 0) alive = false;
	}
	
}

