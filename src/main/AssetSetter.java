package main;

import java.util.Random;

import entity.NPC_RedMage;
import monster.MON_DarkOrc;
import object.OBJ_Coin;
import object.OBJ_Potion_Blue;
import object.OBJ_Potion_Red;
import object.OBJ_Sword_Fire;

public class AssetSetter {
	GamePanel gp;
	Random random = new Random();
	int i,j;
	public AssetSetter(GamePanel gp) {
		this.gp = gp;
	}
	
	public void setObject() {
		int i = 0;
		gp.obj[i] = new OBJ_Coin(gp);
		gp.obj[i].worldX = gp.tileSize * 25;
		gp.obj[i].worldY = gp.tileSize * 23;
		i++;
		
		gp.obj[i] = new OBJ_Coin(gp);
		gp.obj[i].worldX = gp.tileSize * 26;
		gp.obj[i].worldY = gp.tileSize * 21;
		i++;
		
		gp.obj[i] = new OBJ_Potion_Red(gp);
		gp.obj[i].worldX = gp.tileSize * 21;
		gp.obj[i].worldY = gp.tileSize * 19;
		i++;
		
		gp.obj[i] = new OBJ_Sword_Fire(gp);
		gp.obj[i].worldX = gp.tileSize * 33;
		gp.obj[i].worldY = gp.tileSize * 21;
		i++;
		
		gp.obj[i] = new OBJ_Potion_Blue(gp);
		gp.obj[i].worldX = gp.tileSize * 30;
		gp.obj[i].worldY = gp.tileSize * 21;
		i++;
	}
	public void setNPC() {
		gp.npc[0] = new NPC_RedMage(gp);
		gp.npc[0].worldX = gp.tileSize * 22;
		gp.npc[0].worldY = gp.tileSize * 21;
		
		gp.npc[1] = new NPC_RedMage(gp);
		gp.npc[1].worldX = gp.tileSize * 24;
		gp.npc[1].worldY = gp.tileSize * 26;
	}
	public void setMonster() {
		gp.monster[0] = new MON_DarkOrc(gp);
		gp.monster[0].worldX = gp.tileSize * 20;
		gp.monster[0].worldY = gp.tileSize * 37;
		
		gp.monster[1] = new MON_DarkOrc(gp);
		gp.monster[1].worldX = gp.tileSize * 23;
		gp.monster[1].worldY = gp.tileSize * 37;
	}
	public void setRandomMonsters(int number) {
		for(int index = 0;index < number; ++index) {
		if(gp.monster[index] == null) {
			gp.monster[index] = new MON_DarkOrc(gp);
			do {
				i = random.nextInt(49);
				j = random.nextInt(49);
			}while(gp.tileM.tile[gp.tileM.mapTileNum[i][j]].collision == true 
					|| Math.abs(i * gp.tileSize - gp.player.worldX) < 3 * gp.tileSize 
					|| Math.abs(j * gp.tileSize - gp.player.worldY) < 3 * gp.tileSize);
			
			gp.monster[index].worldX = gp.tileSize * i;
			gp.monster[index].worldY = gp.tileSize * j;
			
			if(gp.cChecker.checkEntity(gp.monster[index], gp.monster) != 999 || gp.cChecker.checkEntity(gp.monster[index], gp.npc) != 999) gp.monster[index] = null;
				
		}
		}
	}
}
