package com.complover116.quar2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class ServerThread implements Runnable {
	public static ArrayList<RemoteClient> clients = new ArrayList<RemoteClient>();
	public static DatagramSocket socket;

	public ServerThread() {
		try {
			socket = new DatagramSocket(1141);
		} catch (SocketException e) {
			SoundHandler.playSound("/sound/effects/error1.wav");
			JOptionPane.showMessageDialog(null, "Server socket could not bind!\nMake sure that port 1141 is free!", "Connection error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}

	@Override
	public void run() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		Loader.initialized = true;
		while (true) {
			byte in[] = new byte[64];
			DatagramPacket incoming = new DatagramPacket(in, in.length);
			try {
				socket.receive(incoming);
				if (in[0] == 124) {
					clients.add(new RemoteClient(incoming.getAddress(),
							incoming.getPort(), ServerData.world.players[0]));
					
					byte out[] = new byte[64];
					out[0] = 124;
					out[1] = 1;
					DatagramPacket response = new DatagramPacket(out, out.length, incoming.getSocketAddress());
					socket.send(response);
				}
				if(in[0] == 1) {
					if(in[1] == 1)
					ServerData.world.players[in[2]].keyPress(in);
					else
						ServerData.world.players[in[2]].keyRelease(in);
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

	public static void sendData() {
		
	}

	public static void sendBytes(byte[] out) {
		for (int i = 0; i < clients.size(); i++) {
			DatagramPacket outgoing = new DatagramPacket(out, out.length,
					clients.get(i).address, clients.get(i).port);
			try {
				socket.send(outgoing);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
