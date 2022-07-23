package main;

import entity.Entity;

public class CollisionChecker {
	private GamePanel gp;
	public CollisionChecker(GamePanel gp) {
		this.gp = gp;
	}
	
	public void checkTile(Entity entity) {
		//Check for collisions
		int entityLeftWorldX = entity.getWorldX() + entity.getSolidArea().x;
		int entityRightWorldX = entity.getWorldX() + entity.getSolidArea().x + entity.getSolidArea().width;
		int entityTopWorldY = entity.getWorldY() + entity.getSolidArea().y;
		int entityBottomWorldY = entity.getWorldY() + entity.getSolidArea().y + entity.getSolidArea().height;
		
		int entityLeftCol = entityLeftWorldX / gp.getTileSize();
		int entityRightCol = entityRightWorldX / gp.getTileSize();
		int entityTopRow = entityTopWorldY / gp.getTileSize();
		int entityBottomRow = entityBottomWorldY / gp.getTileSize();
		
		int tileNum1, tileNum2;
		
		switch(entity.getDirection()) {
		case "up":
			entityTopRow = (entityTopWorldY - entity.getSpeed()) / gp.getTileSize();
			if(entityLeftCol <= 49 && entityRightCol <= 49 && entityTopRow <= 49) {
			tileNum1 = gp.getTileM().getMapTileNum()[entityLeftCol][entityTopRow];
			tileNum2 = gp.getTileM().getMapTileNum()[entityRightCol][entityTopRow];
			if(gp.getTileM().getTile()[tileNum1].isCollision() || gp.getTileM().getTile()[tileNum2].isCollision()) entity.setCollisionOn(true);
			}
			break;
		case "down":
			entityBottomRow = (entityBottomWorldY + entity.getSpeed()) / gp.getTileSize();
			if(entityLeftCol <= 49 && entityRightCol <= 49 && entityBottomRow <= 49) {
			tileNum1 = gp.getTileM().getMapTileNum()[entityLeftCol][entityBottomRow];
			tileNum2 = gp.getTileM().getMapTileNum()[entityRightCol][entityBottomRow];
			if(gp.getTileM().getTile()[tileNum1].isCollision() || gp.getTileM().getTile()[tileNum2].isCollision()) entity.setCollisionOn(true);
			}
			break;
		case "left":
			entityLeftCol = (entityLeftWorldX - entity.getSpeed()) / gp.getTileSize();
			if(entityLeftCol <= 49 && entityBottomRow <= 49 && entityTopRow <= 49) {
			tileNum1 = gp.getTileM().getMapTileNum()[entityLeftCol][entityTopRow];
			tileNum2 = gp.getTileM().getMapTileNum()[entityLeftCol][entityBottomRow];
			if(gp.getTileM().getTile()[tileNum1].isCollision() || gp.getTileM().getTile()[tileNum2].isCollision()) entity.setCollisionOn(true);
			}
			break;
		case "right":
			entityRightCol = (entityRightWorldX + entity.getSpeed()) / gp.getTileSize();
			if(entityBottomRow <= 49 && entityRightCol <= 49 && entityTopRow <= 49) {
			tileNum1 = gp.getTileM().getMapTileNum()[entityRightCol][entityTopRow];
			tileNum2 = gp.getTileM().getMapTileNum()[entityRightCol][entityBottomRow];
			if(gp.getTileM().getTile()[tileNum1].isCollision() || gp.getTileM().getTile()[tileNum2].isCollision()) entity.setCollisionOn(true);
			}
			break;
		}
	}
	public int checkObject(Entity entity, boolean isPlayer) {
		//Objects like door can be turned into non-solid
		int index = 999;
		for (int i = 0; i < gp.getObj().length; ++i) {
			if(gp.getObj()[i] != null) {
				//Get entity's solid area position
				entity.getSolidArea().x += entity.getWorldX();
				entity.getSolidArea().y += entity.getWorldY();
				//Get the object's solid area position
				gp.getObj()[i].getSolidArea().x += gp.getObj()[i].getWorldX();
				gp.getObj()[i].getSolidArea().y += gp.getObj()[i].getWorldY();
				
				switch(entity.getDirection()) {
				case "up": entity.getSolidArea().y -= entity.getSpeed(); break;
				case "down": entity.getSolidArea().y += entity.getSpeed(); break;
				case "left": entity.getSolidArea().x -= entity.getSpeed(); break;
				case "right": entity.getSolidArea().x += entity.getSpeed(); break;
				}
				if(entity.getSolidArea().intersects(gp.getObj()[i].getSolidArea())) {
					if(gp.getObj()[i].isCollision()) entity.setCollisionOn(true);
					if(isPlayer) index = i;
				}
				entity.getSolidArea().x = entity.getSolidAreaDefaultX();
				entity.getSolidArea().y = entity.getSolidAreaDefaultY();
				gp.getObj()[i].getSolidArea().x = gp.getObj()[i].getSolidAreaDefaultX();
				gp.getObj()[i].getSolidArea().y = gp.getObj()[i].getSolidAreaDefaultY();
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
				entity.getSolidArea().x += entity.getWorldX();
				entity.getSolidArea().y += entity.getWorldY();
				//Get the object's solid area position
				target[i].getSolidArea().x += target[i].getWorldX();
				target[i].getSolidArea().y += target[i].getWorldY();
				
				switch(entity.getDirection()) {
				case "up": entity.getSolidArea().y -= entity.getSpeed(); break;
				case "down": entity.getSolidArea().y += entity.getSpeed(); break;
				case "left": entity.getSolidArea().x -= entity.getSpeed(); break;
				case "right": entity.getSolidArea().x += entity.getSpeed(); break;
				}
				if(entity.getSolidArea().intersects(target[i].getSolidArea()) && target[i] != entity && !(target[i].isDead())) {
					entity.setCollisionOn(true);
					index = i;
				}
				
				entity.getSolidArea().x = entity.getSolidAreaDefaultX();
				entity.getSolidArea().y = entity.getSolidAreaDefaultY();
				target[i].getSolidArea().x = target[i].getSolidAreaDefaultX();
				target[i].getSolidArea().y = target[i].getSolidAreaDefaultY();
			}
			
		}
		return index;
	}
	//Monster contacting player, player will take damage
	public boolean checkPlayer(Entity entity) {
		boolean contactPlayer = false;
			//Get entity's solid area position
			entity.getSolidArea().x += entity.getWorldX();
			entity.getSolidArea().y += entity.getWorldY();
			//Get the object's solid area position
			gp.getPlayer().getSolidArea().x += gp.getPlayer().getWorldX();
			gp.getPlayer().getSolidArea().y += gp.getPlayer().getWorldY();
			
			switch(entity.getDirection()) {
			case "up": entity.getSolidArea().y -= entity.getSpeed(); break;
			case "down": entity.getSolidArea().y += entity.getSpeed(); break;
			case "left": entity.getSolidArea().x -= entity.getSpeed(); break;
			case "right": entity.getSolidArea().x += entity.getSpeed(); break;
			}
			if(entity.getSolidArea().intersects(gp.getPlayer().getSolidArea())) {
				entity.setCollisionOn(true);
				contactPlayer = true;
			}
			entity.getSolidArea().x = entity.getSolidAreaDefaultX();
			entity.getSolidArea().y = entity.getSolidAreaDefaultY();
			gp.getPlayer().getSolidArea().x = gp.getPlayer().getSolidAreaDefaultX();
			gp.getPlayer().getSolidArea().y = gp.getPlayer().getSolidAreaDefaultY();
			return contactPlayer;
	}
}
