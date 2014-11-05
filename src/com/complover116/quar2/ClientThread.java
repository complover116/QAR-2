package com.complover116.quar2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;

import javax.swing.JOptionPane;

public class ClientThread implements Runnable {
	static DatagramSocket socket;
	public ClientThread() {
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		byte out[] = new byte[64];
		out[0] = 124;
		try {
			DatagramPacket outgoing = new DatagramPacket(out, out.length, Config.server, 1141);
			socket.send(outgoing);
			byte in[] = new byte[64];
			DatagramPacket incoming = new DatagramPacket(in, in.length);
			socket.setSoTimeout(2000);
			socket.receive(incoming);
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
		
		while(true) {
			byte in[] = new byte[64];
			DatagramPacket incoming = new DatagramPacket(in, in.length);
			try {
				socket.receive(incoming);
				switch(in[0]) {
				case 1:
					ClientFunctions.receiveShipData(in);
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
	}
	public static void sendKey(int key, boolean state) {
		byte out[] = new byte[64];
		if(state) {
			out[0] = 100;
		} else {
			out[0] = 101;
		}
		ByteBuffer.wrap(out, 1, 63).putInt(key);
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
