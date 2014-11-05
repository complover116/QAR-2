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

}
