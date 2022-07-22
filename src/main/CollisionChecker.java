package main;

import entity.Entity;

public class CollisionChecker {
	GamePanel gp;
	
	public CollisionChecker(GamePanel gp) {
		this.gp = gp;
	}
	
	public void checkTile(Entity entity) {
		//Check for collisions
		int entityLeftWorldX = entity.worldX + entity.solidArea.x;
		int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
		int entityTopWorldY = entity.worldY + entity.solidArea.y;
		int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;
		
		int entityLeftCol = entityLeftWorldX / gp.tileSize;
		int entityRightCol = entityRightWorldX / gp.tileSize;
		int entityTopRow = entityTopWorldY / gp.tileSize;
		int entityBottomRow = entityBottomWorldY / gp.tileSize;
		
		int tileNum1, tileNum2;
		
		switch(entity.direction) {
		case "up":
			entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
			if(entityLeftCol <= 49 && entityRightCol <= 49 && entityTopRow <= 49) {
			tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
			tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
			if(gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) entity.collisionOn = true;
			}
			break;
		case "down":
			entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
			if(entityLeftCol <= 49 && entityRightCol <= 49 && entityBottomRow <= 49) {
			tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
			tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
			if(gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) entity.collisionOn = true;
			}
			break;
		case "left":
			entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
			if(entityLeftCol <= 49 && entityBottomRow <= 49 && entityTopRow <= 49) {
			tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
			tileNum2 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
			if(gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) entity.collisionOn = true;
			}
			break;
		case "right":
			entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
			if(entityBottomRow <= 49 && entityRightCol <= 49 && entityTopRow <= 49) {
			tileNum1 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
			tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
			if(gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) entity.collisionOn = true;
			}
			break;
		}
	}
	public int checkObject(Entity entity, boolean player) {
		//Objects like door can be turned into non-solid
		int index = 999;
		for (int i = 0; i < gp.obj.length; ++i) {
			if(gp.obj[i] != null) {
				//Get entity's solid area position
				entity.solidArea.x += entity.worldX;
				entity.solidArea.y += entity.worldY;
				//Get the object's solid area position
				gp.obj[i].solidArea.x += gp.obj[i].worldX;
				gp.obj[i].solidArea.y += gp.obj[i].worldY;
				
				switch(entity.direction) {
				case "up": entity.solidArea.y -= entity.speed; break;
				case "down": entity.solidArea.y += entity.speed; break;
				case "left": entity.solidArea.x -= entity.speed; break;
				case "right": entity.solidArea.x += entity.speed; break;
				}
				if(entity.solidArea.intersects(gp.obj[i].solidArea)) {
					if(gp.obj[i].collision == true) entity.collisionOn = true;
					if(player == true) index = i;
				}
				entity.solidArea.x = entity.solidAreaDefaultX;
				entity.solidArea.y = entity.solidAreaDefaultY;
				gp.obj[i].solidArea.x = gp.obj[i].solidAreaDefaultX;
				gp.obj[i].solidArea.y = gp.obj[i].solidAreaDefaultY;
			}
			
		}
		return index;
	}
	//COLLISION WITH NPC OR MONSTERS
	public int checkEntity(Entity entity, Entity[] target) {
		int index = 999;
		for (int i = 0; i < target.length; ++i) {
			if(target[i] != null) {
				//Get entity's solid area position
				entity.solidArea.x += entity.worldX;
				entity.solidArea.y += entity.worldY;
				//Get the object's solid area position
				target[i].solidArea.x += target[i].worldX;
				target[i].solidArea.y += target[i].worldY;
				
				switch(entity.direction) {
				case "up": entity.solidArea.y -= entity.speed; break;
				case "down": entity.solidArea.y += entity.speed; break;
				case "left": entity.solidArea.x -= entity.speed; break;
				case "right": entity.solidArea.x += entity.speed; break;
				}
				if(entity.solidArea.intersects(target[i].solidArea) && target[i] != entity && target[i].dead == false) {
					entity.collisionOn = true;
					index = i;
				}
				
				entity.solidArea.x = entity.solidAreaDefaultX;
				entity.solidArea.y = entity.solidAreaDefaultY;
				target[i].solidArea.x = target[i].solidAreaDefaultX;
				target[i].solidArea.y = target[i].solidAreaDefaultY;
			}
			
		}
		return index;
	}
	//Monster contacting player, player will take damage
	public boolean checkPlayer(Entity entity) {
		boolean contactPlayer = false;
			//Get entity's solid area position
			entity.solidArea.x += entity.worldX;
			entity.solidArea.y += entity.worldY;
			//Get the object's solid area position
			gp.player.solidArea.x += gp.player.worldX;
			gp.player.solidArea.y += gp.player.worldY;
			
			switch(entity.direction) {
			case "up": entity.solidArea.y -= entity.speed; break;
			case "down": entity.solidArea.y += entity.speed; break;
			case "left": entity.solidArea.x -= entity.speed; break;
			case "right": entity.solidArea.x += entity.speed; break;
			}
			if(entity.solidArea.intersects(gp.player.solidArea)) {
				entity.collisionOn = true;
				contactPlayer = true;
			}
			entity.solidArea.x = entity.solidAreaDefaultX;
			entity.solidArea.y = entity.solidAreaDefaultY;
			gp.player.solidArea.x = gp.player.solidAreaDefaultX;
			gp.player.solidArea.y = gp.player.solidAreaDefaultY;
			return contactPlayer;
	}
}