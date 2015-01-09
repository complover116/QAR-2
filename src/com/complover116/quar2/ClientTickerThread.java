package com.complover116.quar2;

import javax.swing.JOptionPane;

public class ClientTickerThread implements Runnable {

	@Override
	public void run() {
		try{
		System.out.println("Client ticker thread has started...");
		while(true) {
			long tickstart = System.nanoTime();
			ClientTicker.tick();
			GUI.mainFrame.repaint();
			int ttMillis = (int) ((System.nanoTime()- tickstart)/1000000);
			if(ttMillis < Config.tickdelay) {
				try {
					Thread.sleep(Config.tickdelay - ttMillis);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}catch(Exception e) {
		e.printStackTrace();
		SoundHandler.playSound("/sound/effects/error1.wav");
		JOptionPane.showMessageDialog(null, "ClientTickerThread has failed, system can not recover.\nShutting down...", "FATAL ERROR", JOptionPane.ERROR_MESSAGE);
		System.exit(0);
	}
	}

}
