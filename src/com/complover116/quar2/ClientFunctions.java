package com.complover116.quar2;

import java.nio.ByteBuffer;

public class ClientFunctions {

	public static void receiveShipData(byte[] in) {
		if (ClientData.world.ships[in[1]] == null) {
			System.out.println("CLIENT: CREATING SHIP, ID:"+in[1]);
			ClientData.world.ships[in[1]] = new Ship(0,0,0);
			//REQUEST SHIP STRUCTURE
		}
		ClientData.world.ships[in[1]].updatePos(ByteBuffer.wrap(in, 2, 62));
	}
	public static void receivePlayerData(byte[] in) {
		if (ClientData.world.players[in[1]] == null) {
			System.out.println("CLIENT: CREATING PLAYER, ID:"+in[1]);
			ClientData.world.players[in[1]] = new Player();
		}
		ClientData.world.players[in[1]].upDatePos(ByteBuffer.wrap(in, 2, 62));
	}

}
