package entity;


import main.GamePanel;
public class NPC_RedMage extends Entity{
	public NPC_RedMage(GamePanel gp) {
		super(gp);
		name = "Red Mage";
		direction = "down";
		speed = 1;
		getImage();
		setDialogue();
	}
	public void getImage() {
		up0 = setup("/npc/mageup0", gp.tileSize, gp.tileSize);
		up1 = setup("/npc/mageup1", gp.tileSize, gp.tileSize);
		up2 = setup("/npc/mageup1", gp.tileSize, gp.tileSize);
		up3 = setup("/npc/mageup1", gp.tileSize, gp.tileSize);
		down0 = setup("/npc/magedown0", gp.tileSize, gp.tileSize);
		down1 = setup("/npc/magedown1", gp.tileSize, gp.tileSize);
		down2 = setup("/npc/magedown1", gp.tileSize, gp.tileSize);
		down3 = setup("/npc/magedown1", gp.tileSize, gp.tileSize);
		left0 = setup("/npc/mageleft0", gp.tileSize, gp.tileSize);
		left1 = setup("/npc/mageleft1", gp.tileSize, gp.tileSize);
		left2 = setup("/npc/mageleft1", gp.tileSize, gp.tileSize);
		left3 = setup("/npc/mageleft1", gp.tileSize, gp.tileSize);
		right0 = setup("/npc/mageright0", gp.tileSize, gp.tileSize);
		right1 = setup("/npc/mageright1", gp.tileSize, gp.tileSize);
		right2 = setup("/npc/mageright1", gp.tileSize, gp.tileSize);
		right3 = setup("/npc/mageright1", gp.tileSize, gp.tileSize);
	}
	public void setDialogue() {
		dialogues[0] = "11111";
		dialogues[1] = "22222";
		dialogues[2] = "33333";
		dialogues[3] = "44444";
	}
	
	public void speak() {
		super.speak();
	}
}
