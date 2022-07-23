package npc;

import main.Entity;
import main.GamePanel;

public class NPC_RedMage extends Entity{
	private GamePanel gp;
	public NPC_RedMage(GamePanel gp) {
		super(gp);
		this.gp = gp;
		setName("Red Mage");
		setDirection("down");
		setSpeed(1);
		getNPCImage();
		setDialogue();
	}
	public void getNPCImage() {
		setUp0(setup("/npc/mageup0", gp.getTileSize(), gp.getTileSize()));
		setUp1(setup("/npc/mageup1", gp.getTileSize(), gp.getTileSize()));
		setUp2(setup("/npc/mageup1", gp.getTileSize(), gp.getTileSize()));
		setUp3(setup("/npc/mageup1", gp.getTileSize(), gp.getTileSize()));
		setDown0(setup("/npc/magedown0", gp.getTileSize(), gp.getTileSize()));
		setDown1(setup("/npc/magedown1", gp.getTileSize(), gp.getTileSize()));
		setDown2(setup("/npc/magedown1", gp.getTileSize(), gp.getTileSize()));
		setDown3(setup("/npc/magedown1", gp.getTileSize(), gp.getTileSize()));
		setLeft0(setup("/npc/mageleft0", gp.getTileSize(), gp.getTileSize()));
		setLeft1(setup("/npc/mageleft1", gp.getTileSize(), gp.getTileSize()));
		setLeft2(setup("/npc/mageleft1", gp.getTileSize(), gp.getTileSize()));
		setLeft3(setup("/npc/mageleft1", gp.getTileSize(), gp.getTileSize()));
		setRight0(setup("/npc/mageright0", gp.getTileSize(), gp.getTileSize()));
		setRight1(setup("/npc/mageright1", gp.getTileSize(), gp.getTileSize()));
		setRight2(setup("/npc/mageright1", gp.getTileSize(), gp.getTileSize()));
		setRight3(setup("/npc/mageright1", gp.getTileSize(), gp.getTileSize()));
	}
	public void setDialogue() {
		getDialogues()[0] = "11111";
		getDialogues()[1] = "22222";
		getDialogues()[2] = "33333";
		getDialogues()[3] = "44444";
	}
	
	public void speak() {
		super.speak();
	}
}
