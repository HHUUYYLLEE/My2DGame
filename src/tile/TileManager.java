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
	public Tile[] tile;
	public int mapTileNum[][];
	public TileManager(GamePanel gp, String mapFile) {
		this.gp = gp;
		tile = new Tile[50];
		mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];
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
			tile[index].image = ImageIO.read(getClass().getResourceAsStream("/tiles/" + imageName + ".png"));
			tile[index].image = uTool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
			tile[index].collision = collision;
			tile[index].damageTile = damageTile;
			
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
			
			while(col < gp.maxWorldCol && row < gp.maxWorldRow) {
				
				String line = br.readLine();
				
				while(col < gp.maxWorldCol) {
					
					String numbers[] = line.split(" ");
					int num = Integer.parseInt(numbers[col]);
					mapTileNum[col][row] = num;
					col++;
				}
				if (col == gp.maxWorldCol) {
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
		
		
		while(worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
			int tileNum = mapTileNum[worldCol][worldRow];
			
			int worldX = worldCol * gp.tileSize;
			int worldY = worldRow * gp.tileSize;
			int screenX = worldX - gp.player.worldX + gp.player.screenX;
			int screenY = worldY - gp.player.worldY + gp.player.screenY;
			
			//Stop moving camera at edge
			if(gp.player.screenX > gp.player.worldX) screenX = worldX;
			if(gp.player.screenY > gp.player.worldY) screenY = worldY;
			
			int rightOffset = gp.screenWidth - gp.player.screenX;
			if(rightOffset > gp.worldWidth - gp.player.worldX) screenX = gp.screenWidth - (gp.worldWidth - worldX);
			
			int bottomOffset = gp.screenHeight - gp.player.screenY;
			if(bottomOffset > gp.worldHeight - gp.player.worldY) screenY = gp.screenHeight - (gp.worldHeight - worldY);
			
			
			//Draw tiles when player is at the map's edge
			if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
			   worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
			   worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
			   worldY - gp.tileSize < gp.player.worldY + gp.player.screenY){
			g2.drawImage(tile[tileNum].image, screenX, screenY, null);
		}else if(gp.player.screenX > gp.player.worldX ||
				 gp.player.screenY > gp.player.worldY ||
				 rightOffset > gp.worldWidth - gp.player.worldX||
				 bottomOffset > gp.worldHeight - gp.player.worldY) g2.drawImage(tile[tileNum].image, screenX, screenY, null);
			
			worldCol++;
			if(worldCol == gp.maxWorldCol) {
				worldCol = 0;
				worldRow++;
			}
		}
		
	}
}
