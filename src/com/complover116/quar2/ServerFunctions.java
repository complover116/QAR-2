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

	public static void sendShip(Ship sh, int num) {
		System.out.println("Broadcasting data for ship "+num);
		// SEND THE HULL STRUCTURE
		for (int i = 0; i < 256; i++)
			for (int j = 0; j < 256; j++) {
				if (sh.hull[i][j] != null) {
						byte out[] = new byte[64];
						out[0] = 10;
						out[1] = (byte) num;
						out[2] = (byte) i;
						out[3] = (byte) j;
						ByteBuffer data = ByteBuffer.wrap(out, 4, 60);
						data.put(sh.hull[i][j].downDate());
						ServerThread.sendBytes(out);
				}
			}
		// SEND THE SHIPJECTS
		for(int i = 0; i < sh.objects.length; i ++) {
			if(sh.objects[i] != null)
				ServerFunctions.sendShipJect(sh.objects[i]);
		}
	}
	public static void sendHullBlockStatus(Ship sh, byte x, byte y) {
		byte out[] = new byte[64];
		out[0] = -10;
		out[1] = (byte) sh.id;
		out[2] = (byte) x;
		out[3] = (byte) y;
		ByteBuffer data = ByteBuffer.wrap(out, 4, 60);
		try{
			data.put(sh.hull[x][y].downDate());
		}catch (NullPointerException e) {
			data.put((byte) -255);
		}
		ServerThread.sendBytes(out);
	}
	public static void sendShipJect(ShipJect obj) {
		byte out[] = new byte[64];
		out[0] = 11;
		out[1] = obj.ship.id;
		out[3] = obj.id;
		ByteBuffer b = ByteBuffer.wrap(out, 4,60);
		if(obj.getClass() == EnginesPanel.class){
			out[2] = 1;
			b.putDouble(obj.pos.x);
			b.putDouble(obj.pos.y);
			b.putDouble(obj.rot);
		}
		else if(obj.getClass() == Engine.class){
			out[2] = 2;
			b.putDouble(obj.pos.x);
			b.putDouble(obj.pos.y);
			b.putDouble(obj.rot);
			b.put(((Engine)obj).direction);
		}
		else {
			System.err.println("ERROR: Undefined ShipJect type "+obj.getClass()+"! (This is bad, register this type RIGHT NOW!)");
			return;
		}
		ServerThread.sendBytes(out);
	}

	public static void sendObjectData(SpaceJect spaceJect, int i) {
		byte out[] = new byte[64];
		out[0] = 4;
		out[1] = (byte) i;
		if(spaceJect.dead){
			out[2] = (byte) -127;
		} else {
		ByteBuffer data = ByteBuffer.wrap(out, 2, 62);
		spaceJect.downDate(data);
		}
		ServerThread.sendBytes(out);
	}
	public static void sendObjectInfo(SpaceJect obj) {
		//STEP 1 - METADATA
		byte out[] = new byte[64];
		out[0] = 5;
		out[1] = obj.id;
		
		//STEP 2 - OBJECT TYPE
		if(obj.getClass() == Projectile.class){
			out[2] = 1;
		}
		else {
			System.err.println("ERROR: Undefined ShipJect type "+obj.getClass()+"! (This is bad, register this type RIGHT NOW!)");
			return;
		}
		//STEP 3 - INFO
		obj.infoDown(ByteBuffer.wrap(out, 3,61));
		ServerThread.sendBytes(out);
	}
}
