package com.complover116.quar2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;

import javax.swing.JOptionPane;

public class ClientThread implements Runnable {
	public class TimeoutThread implements Runnable {

		@Override
		public void run() {
			System.out.println("Client Timeout Thread has started...");
			while(ClientData.run) {
				timeout ++;
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(timeout > ClientThread.timeoutHigh) {
					SoundHandler.playSound("/sound/effects/error1.wav");
					JOptionPane.showMessageDialog(null, "No message received for "+ClientThread.timeoutHigh/10+" seconds.\nConnection is considered terminated.", "Disconnected", JOptionPane.ERROR_MESSAGE);
					System.exit(0);
				}
			}
			System.out.println("Client Timeout Thread has stopped...");
		}
		
	}
	static DatagramSocket socket;
	public static int timeout = 0;
	public static final int timeoutLow = 10;
	public static final int timeoutHigh = 50;
	public ClientThread() {
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
			SoundHandler.playSound("/sound/effects/error1.wav");
			JOptionPane.showMessageDialog(null, "Client socket could not bind!\nMake sure that port 1142 is free!", "Connection error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}
	@Override
	public void run() {
		try{
		System.out.println("Client Networking Thread has started...");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		byte out[] = new byte[64];
		out[0] = 124;
		out[1] = (byte) (Math.random()*5);
		try {
			DatagramPacket outgoing = new DatagramPacket(out, out.length, Config.server, 1141);
			socket.send(outgoing);
			byte in[] = new byte[64];
			DatagramPacket incoming = new DatagramPacket(in, in.length);
			socket.setSoTimeout(2000);
			Render.loadStep = "Waiting for response from server...";
			socket.receive(incoming);
			if(in[1] == -1){
				JOptionPane.showMessageDialog(null, "Server is full!", "Connection error", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
			ClientData.controlledPlayer = in[2];
			Loader.initialized = true;
			socket.setSoTimeout(0);
		} catch(SocketTimeoutException e) {
			SoundHandler.playSound("/sound/effects/error1.wav");
			JOptionPane.showMessageDialog(null, "No response from host! Make sure that the server is running!", "Connection error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Unknown network error", "Network error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		System.out.println("Client Networking Thread has entered loopmode");
		new Thread(new TimeoutThread(), "Timeout Thread").start();
		while(ClientData.run) {
			byte in[] = new byte[64];
			DatagramPacket incoming = new DatagramPacket(in, in.length);
			try {
				socket.receive(incoming);
				timeout = 0;
				switch(in[0]) {
				case 1:
					ClientFunctions.receiveShipData(in);
				break;
				case 2:
					ClientFunctions.receivePlayerData(in);
				break;
				case 3:
					ClientFunctions.receivePlayerInfo(in);
				break;
				case 4:
					ClientFunctions.receiveSpaceJectData(in);
				break;
				case 5:
					ClientFunctions.receiveSpaceJectInfo(in);
				break;
				case 10:
					ClientFunctions.receiveShip(in);
				break;
				case -10:
					ClientFunctions.receiveHull(in);
				break;
				case 11:
					ClientFunctions.receiveShipJect(in);
				break;
				case -1:
					System.out.println("SpaapS");
					ClientData.world.players[in[1]] = new Player(ClientData.world, 0, 0);
					ClientData.world.players[in[1]].color = in[2];
				break;
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				Thread.sleep(Config.netTick);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}catch(Exception e) {
		e.printStackTrace();
		SoundHandler.playSound("/sound/effects/error1.wav");
		JOptionPane.showMessageDialog(null, "ClientThread has failed, system can not recover.\nShutting down...", "FATAL ERROR", JOptionPane.ERROR_MESSAGE);
		System.exit(0);
	}
		System.out.println("Client Networking Thread has stopped");
	}
	public static void sendKey(int key, boolean state) {
		byte out[] = new byte[64];
		out[0] = 1;
		if(state) {
			out[1] = 1;
		} else {
			out[1] = 0;
		}
		out[2] = (byte) ClientData.controlledPlayer;
		ByteBuffer.wrap(out, 3, 61).putInt(key);
			DatagramPacket outgoing = new DatagramPacket(out, out.length,
					Config.server, 1141);
			try {
				socket.send(outgoing);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Unknown network error", "Network error", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
	}
}
