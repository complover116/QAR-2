package com.complover116.quar2;

import java.io.IOException;

import javax.swing.JOptionPane;

public class MessageDispatchThread implements Runnable {

	@Override
	public void run() {
		System.out.println("Server MessageDispatchThread is running!");
		while(true){
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(ServerThread.outgoing.size() > 100) {
				System.err.println("WARNING:MDT Buffer has "+ServerThread.outgoing.size()+" packets to send!");
			}
			synchronized(ServerThread.outgoing){
			while(ServerThread.outgoing.size() > 0) {
				if(ServerThread.outgoing.get(0).length == 64){
				ServerThread.sendBytesOld(ServerThread.outgoing.get(0));
				ServerThread.outgoing.remove(0);
				} else {
				//System.out.println("MDT:Sending a packet using TCP");
					for(int i = 0; i < ServerThread.clients.size(); i ++) {
						for(int j = 0; j < 64; j ++)
							try {
								ServerThread.clients.get(i).sock.getOutputStream().write(ServerThread.outgoing.get(0)[j]);
							} catch (IOException e) {
								e.printStackTrace();
								SoundHandler.playSound("/sound/effects/error1.wav");
								JOptionPane.showMessageDialog(null, "TCP sending error. The client may crash!", "Network error", JOptionPane.ERROR_MESSAGE);
								System.exit(0);
							}
						try {
							ServerThread.clients.get(i).sock.getOutputStream().write(-126);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					ServerThread.outgoing.remove(0);
					/*try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
				}
			}
			}
		}
	}
	
}
