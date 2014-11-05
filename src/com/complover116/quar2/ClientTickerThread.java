package com.complover116.quar2;

public class ClientTickerThread implements Runnable {

	@Override
	public void run() {
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
	}

}
