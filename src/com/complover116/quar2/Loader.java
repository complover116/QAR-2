package com.complover116.quar2;

import java.io.IOException;
import java.net.InetAddress;

import javax.swing.JOptionPane;

public class Loader {
	public static boolean isServer = false;
	public static boolean initialized = false;
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		GUI.init();
		System.out.println("...SOS...");
		String in = GUI.askString("Connection setup", "To join a game, type an ip address. To host one, type \"host\"");
		Render.loadStep = "Loading resources...";
		new Thread(new ClientTickerThread()).start();
		ResourceContainer.load();
		while(!initialized) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		initialized = false;
		if(in.equalsIgnoreCase("host")) {
			Render.loadStep = "Hosting a game...";
			new Thread(new ServerThread()).start();
			isServer = true;
			ServerData.world.isRemote = false;
			while(!initialized) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			initialized = false;
			ServerData.world.ships[5] = new Ship(400,500,0,ServerData.world);
			ServerData.world.ships[5].velX = 0;
			ServerData.world.ships[5].velRot = 0;
			
			SoundHandler.playSound("/sound/effects/blip1.wav");
			new Thread(new TickerThread()).start();
			Config.address = "localhost";
		} else {
			Config.address = in;
		}
		
			Render.loadStep = "Connecting to "+Config.address+"...";
			
			try{
			
			Config.server = InetAddress.getByName(Config.address);
			new Thread(new ClientThread()).start();
			while(!initialized) {
				Render.loadspeed+=0.25;
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			SoundHandler.playSound("/sound/effects/blip1_space.wav");
			
			} catch (Exception e) {
				SoundHandler.playSound("/sound/effects/error1.wav");
				JOptionPane.showMessageDialog(null, "Could not connect to "+in+", maybe you mistyped?", "Connection error", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
		
		
	}

}
