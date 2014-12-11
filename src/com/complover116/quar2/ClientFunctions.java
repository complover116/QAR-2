package com.complover116.quar2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.nio.ByteBuffer;

public class ClientFunctions {

	public static void receiveShipData(byte[] in) {
		if (ClientData.world.ships[in[1]] == null) {
			System.out.println("CLIENT: CREATING SHIP, ID:"+in[1]);
			ClientData.world.ships[in[1]] = new Ship(0,0,0, ClientData.world);
			//REQUEST SHIP STRUCTURE
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

}
