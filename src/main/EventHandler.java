package main;
import java.util.ArrayList;
import java.util.Arrays;

public class EventHandler {
	GamePanel gp;
	EventRect eventRect[][];
	int previousEventX, previousEventY, spikeTileTotal = 0;
	boolean canTouchEvent = true;
	ArrayList<ArrayList<Integer>> spikeTile = new ArrayList<ArrayList<Integer>>();
	
	public EventHandler(GamePanel gp) {
		this.gp = gp;
		eventRect = new EventRect[gp.maxWorldCol][gp.maxWorldRow];
		
		int col = 0;
		int row = 0;
		while(col < gp.maxWorldCol && row < gp.maxWorldRow) {
		
		eventRect[col][row] = new EventRect();
		eventRect[col][row].x = 8; //Place to trigger event;
		eventRect[col][row].y = 8;
		eventRect[col][row].width = 23;
		eventRect[col][row].height = 23;
		eventRect[col][row].eventRectDefaultX = eventRect[col][row].x;
		eventRect[col][row].eventRectDefaultY = eventRect[col][row].y;
		if(gp.tileM.tile[gp.tileM.mapTileNum[col][row]].damageTile == true) {
			spikeTile.add(spikeTileTotal, new ArrayList<>(Arrays.asList(col, row)));
			spikeTileTotal++;
		}
		col++;
		if(col == gp.maxWorldCol) {
			col = 0;
			row++;
		}
		
	}
}
	public void checkEvent() {
		//Check if player is more than 1 tile away from last event
		int xDistance = Math.abs(gp.player.worldX - previousEventX);
		int yDistance = Math.abs(gp.player.worldY - previousEventY);
		int distance = Math.max(xDistance, yDistance);
		int i;
		if(distance > gp.tileSize) canTouchEvent = true;
		
		
		if(canTouchEvent == true) {
			
			
		for(i = 0;i < spikeTileTotal; ++i)
			if(hit(spikeTile.get(i).get(0), spikeTile.get(i).get(1), "any") == true) 
				damageSpike(spikeTile.get(i).get(0), spikeTile.get(i).get(1), gp.playState);
		
		
		if(hit(23,12,"up") == true) healingPool(23, 12, gp.dialogueState);
		}
			
	}
	public boolean hit(int col, int row, String reqDirection) {
		 boolean hit = false;
		 
		 gp.player.solidArea.x += gp.player.worldX;
		 gp.player.solidArea.y += gp.player.worldY;
		 eventRect[col][row].x += col * gp.tileSize;
		 eventRect[col][row].y += row * gp.tileSize;
		 //if collides with event tiles
		 if(gp.player.solidArea.intersects(eventRect[col][row]) && eventRect[col][row].eventDone == false) {
			 if(gp.player.direction.contentEquals(reqDirection) || reqDirection.contentEquals("any")) {
				 hit = true;
				 
				 previousEventX = gp.player.worldX;
				 previousEventY = gp.player.worldY;
			 }
		 }
		 // Undo all changed values
		 gp.player.solidArea.x = gp.player.solidAreaDefaultX;
		 gp.player.solidArea.y = gp.player.solidAreaDefaultY;
		 eventRect[col][row].x = eventRect[col][row].eventRectDefaultX;
		 eventRect[col][row].y = eventRect[col][row].eventRectDefaultY;
		 
		 
		 return hit;
	}
	public void teleport(int gameState) {
		gp.gameState = gameState;
		gp.player.worldX = gp.tileSize * 37;
		gp.player.worldY = gp.tileSize * 10;
	}
	public void damageSpike(int col, int row, int gameState) {
		if(gp.player.invincible == false) {
				if(gp.player.life > 1) gp.playSE(8);
				else gp.playSE(10);
			gp.gameState = gameState;
			//gp.ui.currentDialogue = "Take damage.";
			gp.player.life --;
			//eventRect[col][row].eventDone = true;
			gp.player.invincible = true;
		}
	}
	public void healingPool(int col, int row, int gameState) {
		if(gp.keyH.enterPressed == true) {
			gp.playSE(15);
			gp.gameState = gameState;
			gp.ui.currentDialogue = "Drunk the water. Life and mana recovered.";
			gp.player.life = gp.player.maxLife;
			gp.player.mana = gp.player.maxMana;
		}
	}
}
