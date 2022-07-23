package main;
import java.util.ArrayList;
import java.util.Arrays;

public class EventHandler {
	private GamePanel gp;
	private EventRect eventRect[][];
	private int previousEventX, previousEventY, spikeTileTotal = 0;
	private boolean canTouchEvent = true;
	private ArrayList<ArrayList<Integer>> spikeTile = new ArrayList<ArrayList<Integer>>();
	
	public EventHandler(GamePanel gp) {
		this.gp = gp;
		eventRect = new EventRect[gp.getMaxWorldCol()][gp.getMaxWorldRow()];
		
		updateAllDamageTiles();
		
	}
private void updateAllDamageTiles() {
	int col = 0;
	int row = 0;
	while(col < gp.getMaxWorldCol() && row < gp.getMaxWorldRow()) {
	
	eventRect[col][row] = new EventRect();
	eventRect[col][row].x = 8; //Place to trigger event;
	eventRect[col][row].y = 8;
	eventRect[col][row].width = 23;
	eventRect[col][row].height = 23;
	eventRect[col][row].setEventRectDefaultX(eventRect[col][row].x);
	eventRect[col][row].setEventRectDefaultY(eventRect[col][row].y);
	
	if(gp.getTileM().getTile()[gp.getTileM().getMapTileNum()[col][row]].isDamageTile()) {
		spikeTile.add(spikeTileTotal, new ArrayList<>(Arrays.asList(col, row)));
		spikeTileTotal++;
	}
	
	col++;
	if(col == gp.getMaxWorldCol()) {
		col = 0;
		row++;
	}
	}
}
	public void checkEvent() {
		canTouchEventOrNot();
		allEvents();
	}
	private void allEvents() {
		int i;
		if(canTouchEvent == true) {
		for(i = 0;i < spikeTileTotal; ++i)
			if(hit(spikeTile.get(i).get(0), spikeTile.get(i).get(1), "any") == true) 
				damageSpike(spikeTile.get(i).get(0), spikeTile.get(i).get(1), gp.playState);
		
		
		if(hit(23,12,"up") == true) healingPool(23, 12, gp.dialogueState);
		}
	}
	private void canTouchEventOrNot() {
		//Check if player is more than 1 tile away from last event
				int xDistance = Math.abs(gp.getPlayer().getWorldX() - previousEventX);
				int yDistance = Math.abs(gp.getPlayer().getWorldY() - previousEventY);
				int distance = Math.max(xDistance, yDistance);
				if(distance > gp.getTileSize()) canTouchEvent = true;
	}
	public boolean hit(int col, int row, String reqDirection) {
		 boolean hit = false;
		 
		 gp.getPlayer().getSolidArea().x += gp.getPlayer().getWorldX();
		 gp.getPlayer().getSolidArea().y += gp.getPlayer().getWorldY();
		 eventRect[col][row].x += col * gp.getTileSize();
		 eventRect[col][row].y += row * gp.getTileSize();
		 //if collides with event tiles
		 if(gp.getPlayer().getSolidArea().intersects(eventRect[col][row]) && !eventRect[col][row].isEventDone()) {
			 if(gp.getPlayer().getDirection().contentEquals(reqDirection) || reqDirection.contentEquals("any")) {
				 hit = true;
				 
				 previousEventX = gp.getPlayer().getWorldX();
				 previousEventY = gp.getPlayer().getWorldY();
			 }
		 }
		 // Undo all changed values
		 gp.getPlayer().getSolidArea().x = gp.getPlayer().getSolidAreaDefaultX();
		 gp.getPlayer().getSolidArea().y = gp.getPlayer().getSolidAreaDefaultY();
		 eventRect[col][row].x = eventRect[col][row].getEventRectDefaultX();
		 eventRect[col][row].y = eventRect[col][row].getEventRectDefaultY();
		 return hit;
	}
	public void teleport(int gameState) {
		gp.setGameState(gameState);
		gp.getPlayer().setWorldX(gp.getTileSize() * 37);
		gp.getPlayer().setWorldY(gp.getTileSize() * 10);
	}
	public void damageSpike(int col, int row, int gameState) {
		if(!gp.getPlayer().isInvincible()) {
				if(gp.getPlayer().getLife() > 1) gp.playSE(8);
				else gp.playSE(10);
			gp.setGameState(gameState);
			//gp.ui.currentDialogue = "Take damage.";
			gp.getPlayer().decreaseLife(1);
			//eventRect[col][row].eventDone = true;
			gp.getPlayer().setInvincible(true);
		}
	}
	public void healingPool(int col, int row, int gameState) {
		if(gp.getKeyH().isEnterPressed()) {
			gp.playSE(15);
			gp.setGameState(gameState);
			gp.getUi().setCurrentDialogue("Drunk the water. Life and mana recovered.");
			gp.getPlayer().setLife(gp.getPlayer().getMaxLife());
			gp.getPlayer().setMana(gp.getPlayer().getMaxMana());
		}
	}
}
