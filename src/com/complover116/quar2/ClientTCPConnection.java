package com.complover116.quar2;

import java.io.IOException;

import javax.swing.JOptionPane;

public class ClientTCPConnection implements Runnable {

	@Override
	public void run() {
		while(true) {
			byte in[] = new byte[128];
			try {
				int counter = 0;
				while((in[counter] = (byte) ClientThread.TCPSock.getInputStream().read()) != -126){
					counter ++;
				}
				
				for(int i = 0; i < 64; i ++) {
				//	System.out.print(in[i]+" ");
				}
				//System.out.println();
				ClientThread.reactToInput(in);
			} catch (IOException e) {
				SoundHandler.playSound("/sound/effects/error1.wav");
				JOptionPane.showMessageDialog(null, "TCP connection failed!", "Network error", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
			
		}
	}

}
