package com.complover116.quar2;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.JOptionPane;

public class SoundHandler {
	public static boolean warned = false;
	public static boolean playSound(String name) {
		if(ResourceContainer.sounds.get(name)!= null){
		try {
			Clip clip = AudioSystem.getClip();
			clip.open(ResourceContainer.format, ResourceContainer.sounds.get(name), 0, ResourceContainer.sounds.get(name).length);
			clip.start();
		} catch (LineUnavailableException e) {
			if(!warned){
			JOptionPane.showMessageDialog(GUI.mainFrame, "AudioSystem Error!\nSounds may fail to play!", "Error", JOptionPane.ERROR_MESSAGE);
			warned = true;
			}
			System.err.println("Sound "+name+" could not be played!");
			
		}
		
		return true;
		}
		System.err.println("Sound "+name+" was to be played, but it was not loaded!");
		return false;
	}
	public static boolean playModSound(String name, float PAN, float GAIN) {
		if(ResourceContainer.sounds.get(name)!= null){
			try {
				if(GAIN>-80){
				Clip clip = AudioSystem.getClip();
				clip.open(ResourceContainer.format, ResourceContainer.sounds.get(name), 0, ResourceContainer.sounds.get(name).length);
				clip.start();
				if(clip.isControlSupported(FloatControl.Type.MASTER_GAIN))
		        {
		            FloatControl pan = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		            pan.setValue(GAIN);
		        }
				}
			} catch (LineUnavailableException e) {
				if(!warned){
				JOptionPane.showMessageDialog(GUI.mainFrame, "AudioSystem Error!\nSounds may fail to play!", "Error", JOptionPane.ERROR_MESSAGE);
				warned = true;
				}
				System.err.println("Sound "+name+" could not be played!");
				
			}
			
			return true;
			}
			System.err.println("Sound "+name+" was to be played, but it was not loaded!");
			return false;
		
	}
	/*public static boolean startLoop(String name) {
		if(ResourceContainer.sounds.get(name)!= null){
		ResourceContainer.sounds.get(name).loop(Clip.LOOP_CONTINUOUSLY);
		return true;
		}
		System.err.println("Sound "+name+" was to be played, but it was not loaded!");
		return false;
	}
	public static boolean stop(String name) {
		if(ResourceContainer.sounds.get(name)!= null){
			ResourceContainer.sounds.get(name).stop();
			return true;
			}
			System.err.println("Sound "+name+" was to be played, but it was not loaded!");
			return false;
	}*/
}
