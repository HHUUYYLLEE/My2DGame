package tile;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class TileManager {
	GamePanel gp;
	private Tile[] tile;
	private int mapTileNum[][];
	public TileManager(GamePanel gp, String mapFile) {
		this.gp = gp;
		tile = new Tile[50];
		mapTileNum = new int[gp.getMaxWorldCol()][gp.getMaxWorldRow()];
		getTileImage();
		loadMap(mapFile);
		
	}
	public void getTileImage() {
			setup(0,"grass00", false, false);
			setup(1,"grass00", false, false);
			setup(2,"grass00", false, false);
			setup(3,"grass00", false, false);
			setup(4,"grass00", false, false);
			setup(5,"grass00", false, false);
			setup(6,"grass00", false, false);
			setup(7,"grass00", false, false);
			setup(8,"grass00", false, false);
			setup(9,"grass00", false, false);
			
			//UNUSED
			setup(10,"grass00", false, false);
			setup(11,"grass01", false, false);
			setup(12,"water00", true, false);
			setup(13,"water01", true, false);
			setup(14,"water02", true, false);
			setup(15,"water03", true, false);
			setup(16,"water04", true, false);
			setup(17,"water05", true, false);
			setup(18,"water06", true, false);
			setup(19,"water07", true, false);
			setup(20,"water08", true, false);
			setup(21,"water09", true, false);
			setup(22,"water10", true, false);
			setup(23,"water11", true, false);
			setup(24,"water12", true, false);
			setup(25,"water13", true, false);
			setup(26,"road00", false, false);
			setup(27,"road01", false, false);
			setup(28,"road02", false, false);
			setup(29,"road03", false, false);
			setup(30,"road04", false, false);
			setup(31,"road05", false, false);
			setup(32,"road06", false, false);
			setup(33,"road07", false, false);
			setup(34,"road08", false, false);
			setup(35,"road09", false, false);
			setup(36,"road10", false, false);
			setup(37,"road11", false, false);
			setup(38,"road12", false, false);
			setup(39,"earth", false, false);
			setup(40,"wall", true, false);
			setup(41,"tree", true, false);
			setup(42,"spikeGrass", false, true);
			setup(43,"spikeRoad", false, true);
	}
	public void setup(int index, String imageName, boolean collision, boolean damageTile) {
		UtilityTool uTool = new UtilityTool();
		try {
			tile[index] = new Tile();
			tile[index].setImage(ImageIO.read(getClass().getResourceAsStream("/tiles/" + imageName + ".png")));
			tile[index].setImage(uTool.scaleImage(tile[index].getImage(), gp.getTileSize(), gp.getTileSize()));
			tile[index].setCollision(collision);
			tile[index].setDamageTile(damageTile);
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	public void loadMap(String filePath) {
		try {
			InputStream is = getClass().getResourceAsStream(filePath);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			int col = 0;
			int row = 0;
			
			while(col < gp.getMaxWorldCol() && row < gp.getMaxWorldRow()) {
				
				String line = br.readLine();
				
				while(col < gp.getMaxWorldCol()) {
					
					String numbers[] = line.split(" ");
					int num = Integer.parseInt(numbers[col]);
					mapTileNum[col][row] = num;
					col++;
				}
				if (col == gp.getMaxWorldCol()) {
					col = 0;
					row++;
				}
			}
			br.close();
			
		}catch(Exception e) {
			
		}
	}
	public void draw(Graphics2D g2) {

		int worldCol = 0;
		int worldRow = 0;
		
		
		while(worldCol < gp.getMaxWorldCol() && worldRow < gp.getMaxWorldRow()) {
			int tileNum = mapTileNum[worldCol][worldRow];
			
			int worldX = worldCol * gp.getTileSize();
			int worldY = worldRow * gp.getTileSize();
			int screenX = worldX - gp.getPlayer().getWorldX() + gp.getPlayer().getScreenX();
			int screenY = worldY - gp.getPlayer().getWorldY() + gp.getPlayer().getScreenY();
			
			//Stop moving camera at edge
			if(gp.getPlayer().getScreenX() > gp.getPlayer().getWorldX()) screenX = worldX;
			if(gp.getPlayer().getScreenY() > gp.getPlayer().getWorldY()) screenY = worldY;
			
			int rightOffset = gp.getScreenWidth() - gp.getPlayer().getScreenX();
			if(rightOffset > gp.getWorldWidth() - gp.getPlayer().getWorldX()) screenX = gp.getScreenWidth() - (gp.getWorldWidth() - worldX);
			
			int bottomOffset = gp.getScreenHeight() - gp.getPlayer().getScreenY();
			if(bottomOffset > gp.getWorldHeight() - gp.getPlayer().getWorldY()) screenY = gp.getScreenHeight() - (gp.getWorldHeight() - worldY);
			
			
			//Draw tiles when player is at the map's edge
			if(worldX + gp.getTileSize() > gp.getPlayer().getWorldX() - gp.getPlayer().getScreenX() &&
			   worldX - gp.getTileSize() < gp.getPlayer().getWorldX() + gp.getPlayer().getScreenX() &&
			   worldY + gp.getTileSize() > gp.getPlayer().getWorldY() - gp.getPlayer().getScreenY() &&
			   worldY - gp.getTileSize() < gp.getPlayer().getWorldY() + gp.getPlayer().getScreenY()){
			g2.drawImage(tile[tileNum].getImage(), screenX, screenY, null);
		}else if(gp.getPlayer().getScreenX() > gp.getPlayer().getWorldX() ||
				 gp.getPlayer().getScreenY() > gp.getPlayer().getWorldY() ||
				 rightOffset > gp.getWorldWidth() - gp.getPlayer().getWorldX()||
				 bottomOffset > gp.getWorldHeight() - gp.getPlayer().getWorldY()) g2.drawImage(tile[tileNum].getImage(), screenX, screenY, null);
			
			worldCol++;
			if(worldCol == gp.getMaxWorldCol()) {
				worldCol = 0;
				worldRow++;
			}
		}
		
	}
	public Tile[] getTile() {
		return tile;
	}
	public void setTile(Tile[] tile) {
		this.tile = tile;
	}
	public int[][] getMapTileNum() {
		return mapTileNum;
	}
	public void setMapTileNum(int[][] mapTileNum) {
		this.mapTileNum = mapTileNum;
	}
}
