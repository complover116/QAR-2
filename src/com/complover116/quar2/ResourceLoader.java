package com.complover116.quar2;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

public class ResourceLoader extends SwingWorker<Void, ProgressUpdate> {
	public static String[] colnames = {"r","g","b","o","y"};
	static double colRCoff[] = {255,0,0,255,255};
	static double colGCoff[] = {0,255,0,150,255};
	static double colBCoff[] = {0,0,255,0,0};
	@Override
	protected Void doInBackground() {
		try{
		System.out.println("===LOADING===");
		ArrayList<String> images = new ArrayList<String>();
		BufferedReader in = null;
		in = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/ImgList")));
		String sees;
		while((sees = in.readLine())!=null){
			images.add(sees);
			System.out.println(sees);
		}
		in.close();
		ArrayList<String> sounds = new ArrayList<String>();
		BufferedReader in2 = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/SoundList")));
		while((sees = in2.readLine())!=null){
			sounds.add(sees);
			System.out.println(sees);
		}
		in2.close();
		//GUI.progressMonitor(0, images.size()+sounds.size(), "Loading stuff");
		//LOAD IMAGES
		for(int i = 0; i < images.size(); i ++){
			ResourceContainer.images.put(images.get(i), ImageIO.read(this.getClass().getResourceAsStream(images.get(i))));
			Thread.sleep(20);
			publish(new ProgressUpdate(i, images.get(i)));
		}
		//LOAD SOUNDS
		int failed = 0;
				for(int i = 0; i < sounds.size(); i ++){
					try{
					AudioInputStream audioIn = AudioSystem.getAudioInputStream(new BufferedInputStream(this.getClass().getResourceAsStream(sounds.get(i))));
					ResourceContainer.format = audioIn.getFormat();
					byte data[] = new byte[audioIn.available()];
					audioIn.read(data, 0, 20971520);
					ResourceContainer.sounds.put(sounds.get(i), data);
					Thread.sleep(20);
					publish(new ProgressUpdate(i+images.size()-1, sounds.get(i)));
					} catch(IOException e) {
						failed ++;
					}
				}
				if(failed > 0){
					if(failed >= sounds.size())
						JOptionPane.showMessageDialog(GUI.mainFrame, "No sounds were able to load - there must be something wrong!", "Error", JOptionPane.ERROR_MESSAGE);
					else
					JOptionPane.showMessageDialog(GUI.mainFrame, "Could not load "+failed+" sounds!\nThere must be a mistake in SoundList!", "Error", JOptionPane.WARNING_MESSAGE);
				}
				
				
				//PREPARE GENERATED IMAGES
				System.out.println("===PREPARING GENERATED IMAGES===");
				String[] itg = {"/img/player/idle.png","/img/player/limb2.png","/img/player/limb1.png"};
				
				for(int i = 0; i < itg.length; i ++){
					for(int col = 0; col < colnames.length; col ++){
					BufferedImage srcImg = ResourceContainer.images.get(itg[i]);
					BufferedImage img = new BufferedImage(srcImg.getWidth(), srcImg.getHeight(), srcImg.getType());
					img.setData(srcImg.copyData(null));
					WritableRaster rast = img.getRaster();
					for(int x = 0; x < rast.getWidth(); x ++)
						for(int y = 0; y < rast.getHeight(); y ++) {
							int pixel[] = new int[4];
							pixel[3] = 255;
							rast.getPixel(x, y, pixel);
							if(pixel[1] == 0&&pixel[2] == 0&&pixel[0] != 0&&pixel[3] != 0) {
								
								double k = pixel[0];
								
								pixel[0] = (int) (k*colRCoff[col]/255);
								pixel[1] = (int) (k*colGCoff[col]/255);
								pixel[2] = (int) (k*colBCoff[col]/255);
								
								rast.setPixel(x, y, pixel);
							}
						}
					publish(new ProgressUpdate(sounds.size() + images.size() - 1, "GENERATING:"+itg[i]+"-"+colnames[col]));
					Thread.sleep(2);
					ResourceContainer.images.put(itg[i]+"-"+colnames[col], img);
					System.out.println(itg[i]+"-"+colnames[col]);
					}
				}		
		if(!SoundHandler.playSound("/sound/effects/loaded.wav")){
			System.err.println("SOUNDS FAILED TO LOAD!");
		}
		return null;
		} catch(Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(GUI.mainFrame, "Resources failed to load. You cannot play with no images, sorry...", "It broke...", JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		}
		return null;
	}
	@Override
	public void done() {
		//GUI.closeProgressMonitor();
		Loader.initialized = true;
	}
	@Override
	protected void process(List<ProgressUpdate> prc) {
		//GUI.updateProgress(prc.get(prc.size() - 1).progress, prc.get(prc.size() - 1).message);
	}
}
