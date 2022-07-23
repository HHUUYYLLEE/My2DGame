package object;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import entity.Entity;
import entity.Projectile;
import main.GamePanel;

public class OBJ_Fireball extends Projectile{
	GamePanel gp;

	public OBJ_Fireball(GamePanel gp) {
		super(gp);
		this.gp = gp;
		
		setName("Fireball");
		setSpeed(7);
		setMaxLife(80);
		setLife(getMaxLife());
		setAttack(2);
		setUseCost(1);
		setAlive(false);
		getFireballImage();
	}
	public void getFireballImage() {
		setCast1(setup("/projectile/firecast1", gp.getTileSize(), gp.getTileSize()));
		setCast2(setup("/projectile/firecast2", gp.getTileSize(), gp.getTileSize()));
		setCast3(setup("/projectile/firecast3", gp.getTileSize(), gp.getTileSize()));
		setCast4(setup("/projectile/firecast4", gp.getTileSize(), gp.getTileSize()));
		setCast5(setup("/projectile/firecast5", gp.getTileSize(), gp.getTileSize()));
		setImpact1(setup("/projectile/fireimpact1", gp.getTileSize(), gp.getTileSize()));
		setImpact2(setup("/projectile/fireimpact2", gp.getTileSize(), gp.getTileSize()));
		setImpact3(setup("/projectile/fireimpact3", gp.getTileSize(), gp.getTileSize()));
		setUp0(setup("/projectile/firetravel1up", gp.getTileSize(), gp.getTileSize()));
		setUp1(setup("/projectile/firetravel2up", gp.getTileSize(), gp.getTileSize()));
		setUp2(setup("/projectile/firetravel3up", gp.getTileSize(), gp.getTileSize()));
		setUp3(setup("/projectile/firetravel4up", gp.getTileSize(), gp.getTileSize()));
		setUp4(setup("/projectile/firetravel5up", gp.getTileSize(), gp.getTileSize()));
		setUp5(setup("/projectile/firetravel6up", gp.getTileSize(), gp.getTileSize()));
		setUp6(setup("/projectile/firetravel7up", gp.getTileSize(), gp.getTileSize()));
		setUp7(setup("/projectile/firetravel8up", gp.getTileSize(), gp.getTileSize()));
		setDown0(setup("/projectile/firetravel1down", gp.getTileSize(), gp.getTileSize()));
		setDown1(setup("/projectile/firetravel2down", gp.getTileSize(), gp.getTileSize()));
		setDown2(setup("/projectile/firetravel3down", gp.getTileSize(), gp.getTileSize()));
		setDown3(setup("/projectile/firetravel4down", gp.getTileSize(), gp.getTileSize()));
		setDown4(setup("/projectile/firetravel5down", gp.getTileSize(), gp.getTileSize()));
		setDown5(setup("/projectile/firetravel6down", gp.getTileSize(), gp.getTileSize()));
		setDown6(setup("/projectile/firetravel7down", gp.getTileSize(), gp.getTileSize()));
		setDown7(setup("/projectile/firetravel8down", gp.getTileSize(), gp.getTileSize()));
		setLeft0(setup("/projectile/firetravel1left", gp.getTileSize(), gp.getTileSize()));
		setLeft1(setup("/projectile/firetravel2left", gp.getTileSize(), gp.getTileSize()));
		setLeft2(setup("/projectile/firetravel3left", gp.getTileSize(), gp.getTileSize()));
		setLeft3(setup("/projectile/firetravel4left", gp.getTileSize(), gp.getTileSize()));
		setLeft4(setup("/projectile/firetravel5left", gp.getTileSize(), gp.getTileSize()));
		setLeft5(setup("/projectile/firetravel6left", gp.getTileSize(), gp.getTileSize()));
		setLeft6(setup("/projectile/firetravel7left", gp.getTileSize(), gp.getTileSize()));
		setLeft7(setup("/projectile/firetravel8left", gp.getTileSize(), gp.getTileSize()));
		setRight0(setup("/projectile/firetravel1right", gp.getTileSize(), gp.getTileSize()));
		setRight1(setup("/projectile/firetravel2right", gp.getTileSize(), gp.getTileSize()));
		setRight2(setup("/projectile/firetravel3right", gp.getTileSize(), gp.getTileSize()));
		setRight3(setup("/projectile/firetravel4right", gp.getTileSize(), gp.getTileSize()));
		setRight4(setup("/projectile/firetravel5right", gp.getTileSize(), gp.getTileSize()));
		setRight5(setup("/projectile/firetravel6right", gp.getTileSize(), gp.getTileSize()));
		setRight6(setup("/projectile/firetravel7right", gp.getTileSize(), gp.getTileSize()));
		setRight7(setup("/projectile/firetravel8right", gp.getTileSize(), gp.getTileSize()));
	}
	public boolean haveResources(Entity user) {
		return user.getMana() >= getUseCost();
	}
	public void subtractResources(Entity user) {
		user.decreaseMana(getUseCost());
	}
	public void update() {
		if(getUser().isCasting()) {
			setSpriteCounter(getSpriteCounter() + 1);
			if((getSpriteCounter() + 1) % 4 == 0) {
				if(getSpriteCastNum() == 0) setSpriteCastNum(1);
				else if(getSpriteCastNum() == 1) setSpriteCastNum(2);
				else if(getSpriteCastNum() == 2) setSpriteCastNum(3);
				else if(getSpriteCastNum() == 3) setSpriteCastNum(4);
				else setSpriteCastNum(0);
			}
			if(getSpriteCounter() == 20) {
				getUser().setCasting(false);
				setSpriteCounter(0);
			}
		}else if(!getUser().isImpact()){
		setSpriteCastNum(0);
		switch(getDirection()) {
		case "up": setWorldY(getWorldY() - getSpeed()); break;
		case "down": setWorldY(getWorldY() + getSpeed()); break;
		case "left": setWorldX(getWorldX() - getSpeed()); break;
		case "right": setWorldX(getWorldX() + getSpeed()); break;
		}
		setSpriteCounter(getSpriteCounter() + 1);
		if(getSpriteCounter() > 7) {
			if(getSpriteNum() == 0) setSpriteNum(1);
			else if(getSpriteNum() == 1) setSpriteNum(2);
			else if(getSpriteNum() == 2) setSpriteNum(3);
			else if(getSpriteNum() == 3) setSpriteNum(4);
			else if(getSpriteNum() == 4) setSpriteNum(5);
			else if(getSpriteNum() == 5) setSpriteNum(6);
			else if(getSpriteNum() == 6) setSpriteNum(7);
			else if(getSpriteNum() == 7) setSpriteNum(0);
			setSpriteCounter(0);
		}
		if(getUser() == gp.getPlayer()) {
			int monsterIndex = gp.getcChecker().checkEntity(this, gp.getMonster());
			if(monsterIndex != 999) {
				getUser().setImpact(true);
				gp.getPlayer().damageMonster(monsterIndex);
				getUser().setCasting(false);
				setSpriteCounter(0);
			}
		}else {
			boolean contactPlayer = gp.getcChecker().checkPlayer(this);
			if(!gp.getPlayer().isInvincible()&& contactPlayer) {
				damagePlayer(getAttack());
				setAlive(false);
			}
		}
		decreaseLife(1);
		if(getLife() <= 0) {
			getUser().setCasting(false);
			setAlive(false);
		}
	}else {
		setSpriteCounter(getSpriteCounter() + 1);
		if(getSpriteCounter() % 6 == 0) {
		if(getSpriteImpactNum() == 0) setSpriteImpactNum(1);
		else if(getSpriteImpactNum() == 1) setSpriteImpactNum(2);
		else setSpriteImpactNum(0);
	}if(getSpriteCounter() == 18) {
		getUser().setImpact(false);
		setSpriteCounter(0);
		setAlive(false);
		setCasting(false);
	}
	}

	}
	public void draw(Graphics2D g2) {

		BufferedImage image = null;
		int screenX, screenY;
		screenX = getWorldX() - gp.getPlayer().getWorldX() + gp.getPlayer().getX();
		screenY = getWorldY() - gp.getPlayer().getWorldY() + gp.getPlayer().getY();
		//Improve rendering efficiency by not render out of screen stuff (or dying off screen)
		if(getWorldX() + gp.getTileSize() > gp.getPlayer().getWorldX() - gp.getPlayer().getScreenX() &&
		   getWorldX() - gp.getTileSize() < gp.getPlayer().getWorldX() + gp.getPlayer().getScreenX() &&
		   getWorldY() + gp.getTileSize() > gp.getPlayer().getWorldY() - gp.getPlayer().getScreenY() &&
		   getWorldY() - gp.getTileSize() < gp.getPlayer().getWorldY() + gp.getPlayer().getScreenY()){
		if(gp.getPlayer().isCasting()) {
			if(getSpriteCastNum() == 0) image = getCast1();
			if(getSpriteCastNum() == 1) image = getCast2();
			if(getSpriteCastNum() == 2) image = getCast3();
			if(getSpriteCastNum() == 3) image = getCast4();
			if(getSpriteCastNum() == 4) image = getCast5();
		}else if(!gp.getPlayer().isImpact()) {
			
			switch(getDirection()) {
			
			case "up":
				if(getSpriteNum() == 0) image = getUp0();
				if(getSpriteNum() == 1) image = getUp1();
				if(getSpriteNum() == 2) image = getUp2();
				if(getSpriteNum() == 3) image = getUp3();
				if(getSpriteNum() == 4) image = getUp4();
				if(getSpriteNum() == 5) image = getUp5();
				if(getSpriteNum() == 6) image = getUp6();
				if(getSpriteNum() == 7) image = getUp7();
				break;
			case "down":
				if(getSpriteNum() == 0) image = getDown0();
				if(getSpriteNum() == 1) image = getDown1();
				if(getSpriteNum() == 2) image = getDown2();
				if(getSpriteNum() == 3) image = getDown3();
				if(getSpriteNum() == 4) image = getDown4();
				if(getSpriteNum() == 5) image = getDown5();
				if(getSpriteNum() == 6) image = getDown6();
				if(getSpriteNum() == 7) image = getDown7();
				break;
			case "left":
				if(getSpriteNum() == 0) image = getLeft0();
				if(getSpriteNum() == 1) image = getLeft1();
				if(getSpriteNum() == 2) image = getLeft2();
				if(getSpriteNum() == 3) image = getLeft3();
				if(getSpriteNum() == 4) image = getLeft4();
				if(getSpriteNum() == 5) image = getLeft5();
				if(getSpriteNum() == 6) image = getLeft6();
				if(getSpriteNum() == 7) image = getLeft7();
				break;
			case "right":
				if(getSpriteNum() == 0) image = getRight0();
				if(getSpriteNum() == 1) image = getRight1();
				if(getSpriteNum() == 2) image = getRight2();
				if(getSpriteNum() == 3) image = getRight3();
				if(getSpriteNum() == 4) image = getRight4();
				if(getSpriteNum() == 5) image = getRight5();
				if(getSpriteNum() == 6) image = getRight6();
				if(getSpriteNum() == 7) image = getRight7();
				break;
			}
	}else {
		if(getSpriteImpactNum() == 0) image = getImpact1();
		else if(getSpriteImpactNum() == 1) image = getImpact2();
		else image = getImpact3();
		}
	
		g2.drawImage(image, screenX, screenY, null);
		}
	}
}
