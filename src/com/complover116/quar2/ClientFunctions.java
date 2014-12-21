package com.complover116.quar2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.nio.ByteBuffer;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;

public class ClientFunctions {

	public static void receiveShipData(byte[] in) {
		if (ClientData.world.ships[in[1]] == null) {
			System.out.println("CLIENT: CREATING SHIP, ID:"+in[1]);
			ClientData.world.ships[in[1]] = new Ship(0,0,0, ClientData.world);
			//REQUEST SHIP STRUCTURE
			byte out[] = new byte[64];
			out[0] = 10;
			out[1] = in[1];
			DatagramPacket outgoing = new DatagramPacket(out, out.length,
					Config.server, 1141);
			try {
				ClientThread.socket.send(outgoing);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		ClientData.world.ships[in[1]].updatePos(ByteBuffer.wrap(in, 2, 62));
	}
	public static void receivePlayerData(byte[] in) {
		if (ClientData.world.players[in[1]] == null) {
			System.out.println("CLIENT: CREATING PLAYER, ID:"+in[1]);
			Pos pos = new Pos(ByteBuffer.wrap(in, 2, 62));
			ClientData.world.players[in[1]] = new Player(ClientData.world, pos.x, pos.y);
			
			byte out[] = new byte[64];
			out[0] = 3;
			out[1] = in[1];
			DatagramPacket outgoing = new DatagramPacket(out, out.length,
					Config.server, 1141);
			try {
				ClientThread.socket.send(outgoing);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		ClientData.world.players[in[1]].upDatePos(ByteBuffer.wrap(in, 2, 62));
	}
	public static void receivePlayerInfo(byte[] in) {
		// TODO Auto-generated method stub
		
		ClientData.world.players[in[1]].color = ByteBuffer.wrap(in, 2, 62).getInt();
	}
	public static void receiveShip(byte[] in) {
		byte hullInfo[] = new byte[60];
		ByteBuffer.wrap(in,4,60).get(hullInfo);
		ClientData.world.ships[in[1]].hull[in[2]][in[3]] = new Hull(hullInfo);
		ClientData.world.ships[in[1]].calcMass();
	}

}
