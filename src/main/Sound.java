package main;

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {
	Clip clip;
	URL soundURL[] = new URL[30];
	public Sound() {
		soundURL[0] = getClass().getResource("/sound/Travel.wav");
		soundURL[1] = getClass().getResource("/sound/coin.wav");
		soundURL[2] = getClass().getResource("/sound/powerup.wav");
		soundURL[3] = getClass().getResource("/sound/unlock.wav");
		soundURL[4] = getClass().getResource("/sound/fanfare.wav");
		soundURL[5] = getClass().getResource("/sound/THUD.wav");
		soundURL[6] = getClass().getResource("/sound/Title.wav");
		soundURL[7] = getClass().getResource("/sound/swing.wav");
		soundURL[8] = getClass().getResource("/sound/Hit1.wav");
		soundURL[9] = getClass().getResource("/sound/Hit2.wav");
		soundURL[10] = getClass().getResource("/sound/dead.wav");
		soundURL[11] = getClass().getResource("/sound/disappear.wav");
		soundURL[12] = getClass().getResource("/sound/levelup.wav");
		soundURL[13] = getClass().getResource("/sound/movingcursor.wav");
		soundURL[14] = getClass().getResource("/sound/cursornotmoving.wav");
		soundURL[15] = getClass().getResource("/sound/heal.wav");
		soundURL[16] = getClass().getResource("/sound/fireswing.wav");
		soundURL[17] = getClass().getResource("/sound/firecasting.wav");
		soundURL[18] = getClass().getResource("/sound/firehit.wav");
	}
	
	public void setFile(int i) {
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
			clip = AudioSystem.getClip();
			clip.open(ais);
		}catch(Exception e) {
			
		}
	}
	public void play() {
		clip.start();
	}
	public void loop() {
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	public void stop() {
		if(clip != null) clip.stop();
	}
}