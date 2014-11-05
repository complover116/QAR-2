package com.complover116.quar2;

public class TickerThread implements Runnable {

	@Override
	public void run() {
		while(true) {
			long tickstart = System.nanoTime();
			Ticker.tick();
			int ttMillis = (int) ((System.nanoTime()- tickstart)/1000000);
			if(ttMillis < 20) {
				try {
					Thread.sleep(20 - ttMillis);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
