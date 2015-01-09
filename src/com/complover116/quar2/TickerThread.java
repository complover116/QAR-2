package com.complover116.quar2;

import javax.swing.JOptionPane;

public class TickerThread implements Runnable {

	@Override
	public void run() {
		try{
		while(true) {
			long tickstart = System.nanoTime();
			Ticker.tick();
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
		JOptionPane.showMessageDialog(null, "TickerThread has failed, system can not recover.\nShutting down...", "FATAL ERROR", JOptionPane.ERROR_MESSAGE);
		System.exit(0);
	}
	}

}
