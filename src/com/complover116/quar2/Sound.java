package com.complover116.quar2;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;

public class Sound {
	public Clip clip;
	public Pos pos;
	public void setVolume(float volume) {
		FloatControl gainControl = 
			    (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(volume);
	}
	public void startLoop() {
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	public void stop() {
		clip.stop();
	}
	public Sound(String name){
		try {
			clip = AudioSystem.getClip();
			clip.open(ResourceContainer.format, 
					ResourceContainer.sounds.get(name), 
					0,
					ResourceContainer.sounds.get(name).length);
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
