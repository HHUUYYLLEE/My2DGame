package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.JPanel;
import entity.Entity;
import entity.Player;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable{
	// SCREEN SETTINGS
	final int originalTileSize = 16; //16x16 tile
	final int scale = 3;
	public final int tileSize = originalTileSize * scale; //48x48 tile
	
	public final int maxScreenCol = 18;
	public final int maxScreenRow = 14;
	public final int screenWidth = tileSize * maxScreenCol; //48x20 pixels
	public final int screenHeight = tileSize * maxScreenRow; //48x15 pixels
	
	//WORLD SETTINGS
	public final int maxWorldCol = 50;
	public final int maxWorldRow = 50;
	public final int worldWidth = tileSize * maxWorldCol;
	public final int worldHeight = tileSize * maxWorldRow;
	
	//FPS
	public int FPS = 60;
	TileManager tileM = new TileManager(this, "/maps/worldV2.txt");
	public KeyHandler keyH = new KeyHandler(this);
	public Sound music = new Sound();
	public Sound se = new Sound();
	public CollisionChecker cChecker = new CollisionChecker(this);
	public AssetSetter aSetter = new AssetSetter(this);
	public UI ui = new UI(this);
	public EventHandler eHandler = new EventHandler(this);
	
	//public Connect database = new Connect();
	public Thread gameThread;
	
	//ENTITY AND OBJECT
	public Player player = new Player(this, keyH);
	public Entity obj[] = new Entity[10];
	public Entity npc[] = new Entity[10];
	public Entity monster[] = new Entity[20];
	public ArrayList<Entity> projectileList = new ArrayList<>();
	ArrayList<Entity> entityList = new ArrayList<>();
	public boolean playThud = true;
	
	//GAME STATE
	public int gameState;
	public final int titleState = 0;
	public final int playState = 1;
	public final int pauseState = 2;
	public final int dialogueState = 3;
	public final int characterState = 4;
	
	
	int spawnCooldown = 0;
	public Thread gameThread2 = new Thread() {
		public void run() {
			double drawInterval = 1000000000/FPS; // 0.0166 seconds
			double delta = 0;
			long lastTime = System.nanoTime();
			long currentTime;
			while(gameThread2 != null) {
				currentTime = System.nanoTime();
				delta += (currentTime - lastTime)/drawInterval;
				lastTime = currentTime;
				if(delta >= 1) {
					update();
					spawnCooldown++;
					if(spawnCooldown >= 2 * FPS && gameState == playState) {
						aSetter.setRandomMonsters(5);
						spawnCooldown = 0;
					}
				delta--;
				}
			}
		}
		public void update() {
			//At title screen
			if(gameState == playState && player.life <= 0) {
					//game over
					gameState = titleState;
					stopMusic();
					player.currentWeapon = null;
					player.currentShield = null;
					player.setDefaultValues();
					player.getPlayerAttackImage();
					player.setItems();
					player.invincible = false;
					aSetter.setNPC();
					aSetter.setMonster();
					aSetter.setObject();
				}
				
	}
	};
	public GamePanel() {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		this.setFocusable(true);
	}
	
	public void setupGame() {
		
		aSetter.setNPC();
		aSetter.setMonster();
		aSetter.setObject();
		gameState = titleState;
	}
	
	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
		gameThread2.start();
	}

	@Override
	/*public void run() {
		double drawInterval = 1000000000/FPS; // 0.0166 seconds
		double nextDrawTime = System.nanoTime() + drawInterval;
		double remainingTime;
		while(gameThread != null) {
			//System.out.println("The game loop is running");
			
			// 1. Update: Update information such as characters' positions
			update();
			// 2. Draw: Draw the screen with the updated information
			repaint();
			
			 
			 try {
				remainingTime = nextDrawTime - System.nanoTime();
				remainingTime /= 1000000;
				if(remainingTime < 0) {
					remainingTime = 0;
				}
				Thread.sleep((long) remainingTime);
				nextDrawTime += drawInterval;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}*/
	public void run() {
		double drawInterval = 1000000000/FPS; // 0.0166 seconds
		double delta = 0;
		double deltaTHUD = 0;
		long lastTime = System.nanoTime();
		long lastTimeTHUD = System.nanoTime();
		long currentTime, currentTimeTHUD;
		while(gameThread != null) {
			currentTime = System.nanoTime();
			currentTimeTHUD = System.nanoTime();
			delta += (currentTime - lastTime)/drawInterval;
			lastTime = currentTime;
			deltaTHUD += (currentTimeTHUD - lastTimeTHUD)/drawInterval;
			lastTimeTHUD = currentTimeTHUD;
			if(deltaTHUD >= 1000) deltaTHUD -= 900;
			if(deltaTHUD >= 50 && player.thud == 1 && player.collisionOn == true) {
				if(playThud == true) playSE(5);
				player.thud = 0;
				deltaTHUD = 0;
			}
			if(delta >= 1) {
			
			// 1. Update: Update information such as characters' positions
			update();
			// 2. Draw: Draw the screen with the updated information
			repaint();
			delta--;
			}
		}
		
	}
	public void update() {
		if(gameState == playState) {
		//PLAYER
			player.update();
			
		//NPC
			for (int i = 0; i < npc.length; ++i) if(npc[i] != null) npc[i].update();
			
		//Object
			for (int i = 0; i < obj.length; ++i) if(obj[i] != null) obj[i].update();
			
			
		//MONSTERS	
			for(int i = 0; i < monster.length; ++i) {
				if(monster[i] != null) {
					if(monster[i].dead == true) {
						monster[i].dyingCounter++;
						if(monster[i].dyingCounter == 60) playSE(11);
						if(monster[i].dyingCounter > 120) monster[i].alive = false;
					}
					if(monster[i].alive == true && monster[i].dead == false) monster[i].update();
				if(monster[i].alive == false) {
					player.exp += monster[i].exp;
					ui.addMessage("Gained " + monster[i].exp + " Experience Points.");
					monster[i] = null;
					spawnCooldown = 0;
					player.checkLevelUp();
				}
			}
		}
			
			//Projectiles
			for(int i = 0; i< projectileList.size(); ++i)
				if(projectileList.get(i) != null) {
					if(projectileList.get(i).alive == true) projectileList.get(i).update();
				if(projectileList.get(i).alive == false) {
					projectileList.remove(i);
				}
			}
			
		}
		if(gameState == pauseState) {
			
		}
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		
		//At title screen
		if(gameState == titleState) ui.draw(g2);
		else {
		
		//TILES
		tileM.draw(g2);
		
		//OBJECT
		for(int i = 0;i < obj.length; ++i)  if(obj[i] != null)  entityList.add(obj[i]);
		
		//PLAYER
		entityList.add(player);
		
		//NPC
		for(int i = 0;i < npc.length; ++i) if(npc[i] != null) entityList.add(npc[i]);
		
		
		//MONSTER
		for(int i = 0;i < monster.length; ++i) if(monster[i] != null) entityList.add(monster[i]);
		
		//Projectile
		for(int i = 0;i < projectileList.size(); ++i) if(projectileList.get(i) != null) entityList.add(projectileList.get(i));
		
		//SORT
		Collections.sort(entityList, new Comparator<Entity>() {
			
			@Override
			public int compare(Entity e1, Entity e2) {
				int result = Integer.compare(e1.worldY, e2.worldY);
				return result;
			}
		});
		
		//DRAW ENTITIES
		for(int i = 0;i < entityList.size(); ++i) entityList.get(i).draw(g2);
		
		entityList.clear();
		// UI
		ui.draw(g2);
		}
		g2.dispose();
	}
	
	public void playMusic(int i) {
		music.setFile(i);
		music.play();
		music.loop();
	}
	public void stopMusic() {
		music.stop();
	}
	public void playSE(int i) {
		se.setFile(i);
		se.play();
	}
	public void stopSE() {
		se.stop();
	}
}

