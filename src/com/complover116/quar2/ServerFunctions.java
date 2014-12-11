package com.complover116.quar2;

import java.nio.ByteBuffer;

public class ServerFunctions {

	public static void sendShipPos(Ship ship, int id) {
		byte out[] = new byte[64];
		out[0] = 1;
		out[1] = (byte) id;
		ByteBuffer data = ByteBuffer.wrap(out, 2, 62);
		ship.downDatePos(data);
		ServerThread.sendBytes(out);
	}

	public static void sendPlayerPos(Player player, int i) {
		byte out[] = new byte[64];
		out[0] = 2;
		out[1] = (byte) i;
		ByteBuffer data = ByteBuffer.wrap(out, 2, 62);
		player.downDatePos(data);
		ServerThread.sendBytes(out);
	}
	public static void disconnect(RemoteClient client) {
		
	}
	public static void sendPlayerInfo(Player player, int i) {
		byte out[] = new byte[64];
		out[0] = 3;
		out[1] = (byte) i;
		ByteBuffer data = ByteBuffer.wrap(out, 2, 62);
		data.putInt(player.color);
		ServerThread.sendBytes(out);
	}
	public static void sendHUDType(Player player, int i) {
		byte out[] = new byte[64];
		out[0] = 2;
		out[1] = (byte) i;
		ByteBuffer data = ByteBuffer.wrap(out, 2, 62);
		player.downDatePos(data);
		ServerThread.sendBytes(out);
	}
}
