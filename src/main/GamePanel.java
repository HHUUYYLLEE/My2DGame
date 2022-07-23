package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.JPanel;

import tile.TileManager;

public class GamePanel extends JPanel implements Runnable{
	// SCREEN SETTINGS
	private final int originalTileSize = 16; //16x16 tile
	private final int scale = 3;
	private final int tileSize = originalTileSize * scale; //48x48 tile
	
	private final int maxScreenCol = 18;
	private final int maxScreenRow = 14;
	private final int screenWidth = tileSize * maxScreenCol; //48x20 pixels
	private final int screenHeight = tileSize * maxScreenRow; //48x15 pixels
	
	//WORLD SETTINGS
	private final int maxWorldCol = 50;
	private final int maxWorldRow = 50;
	private final int worldWidth = tileSize * maxWorldCol;
	private final int worldHeight = tileSize * maxWorldRow;
	
	//FPS
	private int FPS = 60;
	
	//public Connect database = new Connect();
	private Thread gameThread;
	
	//ENTITY AND OBJECT
	private TileManager tileM = new TileManager(this, "/maps/worldV2.txt");
	private KeyHandler keyH = new KeyHandler(this);
	private Sound music = new Sound();
	private Sound se = new Sound();
	private CollisionChecker cChecker = new CollisionChecker(this);
	private AssetSetter aSetter = new AssetSetter(this);
	private UI ui = new UI(this);
	private EventHandler eHandler = new EventHandler(this);
	private Player player = new Player(this, keyH);
	private Entity obj[] = new Entity[20];
	private Entity npc[] = new Entity[10];
	private Entity monster[] = new Entity[20];
	private ArrayList<Entity> projectileList = new ArrayList<>();
	private ArrayList<Entity> entityList = new ArrayList<>();
	private boolean playThud = true;
	
	//GAME STATE
	private int gameState;
	public final int titleState = 0;
	public final int playState = 1;
	public final int pauseState = 2;
	public final int dialogueState = 3;
	public final int characterState = 4;
	
	
	private int spawnCooldown = 0;
	/*public Thread gameThread2 = new Thread() {
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
	};*/
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
		//gameThread2.start();
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
			if(deltaTHUD >= 50 && player.getThud() == 1 && player.isCollisionOn()) {
				if(playThud) playSE(5);
				player.setThud(0);
				deltaTHUD = 0;
			}
			if(delta >= 1) {
				spawnCooldown++;
				if(spawnCooldown >= 2 * FPS && gameState == playState) {
					aSetter.setRandomMonsters(5);
					spawnCooldown = 0;
				}
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
			if(player.getLife() <= 0) {
				//game over
				gameEnd();
			}
		//PLAYER
			player.update();
			
		//NPC
			for (int i = 0; i < npc.length; ++i) if(npc[i] != null) npc[i].update();
			
		//Object
			for (int i = 0; i < obj.length; ++i) if(obj[i] != null) obj[i].update();
			
			
		//MONSTERS	
			for(int i = 0; i < monster.length; ++i) {
				if(monster[i] != null) {
					if(monster[i].isDead()) {
						monster[i].increaseDyingCounter(1);
						if(monster[i].getDyingCounter() == 60) playSE(11);
						if(monster[i].getDyingCounter() > 120) monster[i].setAlive(false);
					}
					if(monster[i].isAlive() && !monster[i].isDead()) monster[i].update();
				if(!monster[i].isAlive()) {
					monster[i].checkDrop();
					spawnCooldown = 0;
					player.gainEXP(monster[i]);
					monster[i] = null;
					
				}
			}
		}
			
			//Projectiles
			for(int i = 0; i< projectileList.size(); ++i)
				if(projectileList.get(i) != null) {
					if(projectileList.get(i).isAlive()) projectileList.get(i).update();
				if(!projectileList.get(i).isAlive()) {
					projectileList.remove(i);
				}
			}
			
		}
	}
	public void gameEnd() {
		gameState = titleState;
		stopMusic();
		player.setCurrentWeapon(null);
		player.setCurrentShield(null);
		player.setDefaultValues();
		player.getPlayerAttackImage();
		player.setItems();
		player.setInvincible(false);
		aSetter.setNPC();
		aSetter.setMonster();
		aSetter.setObject();
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
				int result = Integer.compare(e1.getWorldY(), e2.getWorldY());
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

	
	
	
	//Encapsulation
	public int getTileSize() {
		return tileSize;
	}
	public int getFPS() {
		return FPS;
	}
	public boolean isPlayThud() {
		return playThud;
	}
	public void changePlayThud(boolean playThud) {
		this.playThud = playThud;
	}
	public int getGameState() {
		return gameState;
	}
	public void setGameState(int gameState) {
		this.gameState = gameState;
	}
	public int getMaxScreenCol() {
		return maxScreenCol;
	}
	public int getMaxScreenRow() {
		return maxScreenRow;
	}
	public int getScreenWidth() {
		return screenWidth;
	}
	public int getScreenHeight() {
		return screenHeight;
	}
	public int getMaxWorldCol() {
		return maxWorldCol;
	}
	public int getMaxWorldRow() {
		return maxWorldRow;
	}
	public int getWorldWidth() {
		return worldWidth;
	}
	public int getWorldHeight() {
		return worldHeight;
	}
	public Player getPlayer() {
		return player;
	}
	public TileManager getTileM() {
		return tileM;
	}

	public void setTileM(TileManager tileM) {
		this.tileM = tileM;
	}


	public CollisionChecker getcChecker() {
		return cChecker;
	}


	public AssetSetter getaSetter() {
		return aSetter;
	}

	public UI getUi() {
		return ui;
	}

	public EventHandler geteHandler() {
		return eHandler;
	}

	public Entity[] getObj() {
		return obj;
	}

	public Entity[] getNpc() {
		return npc;
	}

	public Entity[] getMonster() {
		return monster;
	}

	public ArrayList<Entity> getProjectileList() {
		return projectileList;
	}


	public ArrayList<Entity> getEntityList() {
		return entityList;
	}

	public KeyHandler getKeyH() {
		return keyH;
	}

	public Sound getMusic() {
		return music;
	}

	public Sound getSe() {
		return se;
	}





}

