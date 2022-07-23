package main;

import java.util.Random;

import entity.monster.MON_DarkOrc;
import entity.npc.NPC_RedMage;
import object.OBJ_Coin;
import object.OBJ_Potion_Blue;
import object.OBJ_Potion_Red;
import object.OBJ_Sword_Fire;

public class AssetSetter {
	private GamePanel gp;
	private Random random = new Random();
	private int i,j;
	public AssetSetter(GamePanel gp) {
		this.gp = gp;
	}
	
	public void setObject() {
		int i = 0;
		gp.getObj()[i] = new OBJ_Coin(gp);
		gp.getObj()[i].setWorldX(gp.getTileSize() * 25);
		gp.getObj()[i].setWorldY(gp.getTileSize() * 23);
		i++;
		
		gp.getObj()[i] = new OBJ_Coin(gp);
		gp.getObj()[i].setWorldX(gp.getTileSize() * 26);
		gp.getObj()[i].setWorldY(gp.getTileSize() * 21);
		i++;
		
		gp.getObj()[i] = new OBJ_Potion_Red(gp);
		gp.getObj()[i].setWorldX(gp.getTileSize() * 21);
		gp.getObj()[i].setWorldY(gp.getTileSize() * 19);
		i++;
		
		gp.getObj()[i] = new OBJ_Sword_Fire(gp);
		gp.getObj()[i].setWorldX(gp.getTileSize() * 33);
		gp.getObj()[i].setWorldY(gp.getTileSize() * 21);
		i++;
		
		gp.getObj()[i] = new OBJ_Potion_Blue(gp);
		gp.getObj()[i].setWorldX(gp.getTileSize() * 30);
		gp.getObj()[i].setWorldY(gp.getTileSize() * 21);
		i++;
	}
	public void setNPC() {
		gp.getNpc()[0] = new NPC_RedMage(gp);
		gp.getNpc()[0].setWorldX(gp.getTileSize() * 22);
		gp.getNpc()[0].setWorldY(gp.getTileSize() * 21);
		
		gp.getNpc()[1] = new NPC_RedMage(gp);
		gp.getNpc()[1].setWorldX(gp.getTileSize() * 24);
		gp.getNpc()[1].setWorldY(gp.getTileSize() * 26);
	}
	public void setMonster() {
		gp.getMonster()[0] = new MON_DarkOrc(gp);
		gp.getMonster()[0].setWorldX(gp.getTileSize() * 20);
		gp.getMonster()[0].setWorldY(gp.getTileSize() * 37);
		
		gp.getMonster()[1] = new MON_DarkOrc(gp);
		gp.getMonster()[1].setWorldX(gp.getTileSize() * 23);
		gp.getMonster()[1].setWorldY(gp.getTileSize() * 37);
	}
	public void setRandomMonsters(int number) {
		for(int index = 0; index < number; ++index) {
		if(gp.getMonster()[index] == null) {
			gp.getMonster()[index] = new MON_DarkOrc(gp);
			do {
				i = random.nextInt(49);
				j = random.nextInt(49);
			}while(gp.getTileM().getTile()[gp.getTileM().getMapTileNum()[i][j]].isCollision()
					|| Math.abs(i * gp.getTileSize() - gp.getPlayer().getWorldX()) < 3 * gp.getTileSize() 
					|| Math.abs(j * gp.getTileSize() - gp.getPlayer().getWorldY()) < 3 * gp.getTileSize());
			
			gp.getMonster()[index].setWorldX(gp.getTileSize() * i);
			gp.getMonster()[index].setWorldY(gp.getTileSize() * j);
			
			if(gp.getcChecker().checkEntity(gp.getMonster()[index], gp.getMonster()) != 999 || gp.getcChecker().checkEntity(gp.getMonster()[index], gp.getNpc()) != 999) gp.getMonster()[index] = null;
				
		}
		}
	}
}
