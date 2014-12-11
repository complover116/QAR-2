package com.complover116.quar2;

public class MessageDispatchThread implements Runnable {

	@Override
	public void run() {
		System.out.println("Server MessageDispatchThread is running!");
		while(true){
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(ServerThread.outgoing.size() > 20) {
				System.out.println("Out buffer is overflowing! Resetting...");
				ServerThread.outgoing.clear();
			}
			while(ServerThread.outgoing.size() > 0) {
				ServerThread.sendBytesOld(ServerThread.outgoing.get(0));
				ServerThread.outgoing.remove(0);
			}
		}
	}
	
}
