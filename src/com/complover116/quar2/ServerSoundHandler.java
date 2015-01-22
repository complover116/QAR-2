package com.complover116.quar2;

import java.nio.ByteBuffer;

public class ServerSoundHandler {
	public static void playSurfaceSound(String name) {
		int soundID = 0;
		switch(name) {
		case "/sound/effects/explosions/hullboom_1.wav": soundID = 1; break;
		default:
			System.err.println("WARNING: Unregistered sound "+name);
		}
		
		byte out[] = new byte[64];
		ByteBuffer b = ByteBuffer.wrap(out);
		b.put((byte) -25);
		b.putInt(soundID);
		b.put((byte) 1);
		ServerThread.sendBytes(out);
	}
	public static void playPositionedSound(String name, double x, double y) {
		int soundID = 0;
		switch(name) {
		case "/sound/effects/explosions/hullboom_1.wav": soundID = 1; break;
		case "/sound/effects/weapons/basic/fire_1.wav": soundID = 2; break;
		case "/sound/effects/weapons/basic/fire_2.wav": soundID = 3; break;
		case "/sound/effects/weapons/basic/fire_3.wav": soundID = 4; break;
		default:
			System.err.println("WARNING: Unregistered sound "+name);
		}
		
		byte out[] = new byte[64];
		ByteBuffer b = ByteBuffer.wrap(out);
		b.put((byte) -25);
		b.putInt(soundID);
		b.put((byte) 0);
		b.putDouble(x);
		b.putDouble(y);
		ServerThread.sendBytes(out);
	}
}
