package com.complover116.quar2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.nio.ByteBuffer;

public class ClientFunctions {

	public static void receiveShipData(byte[] in) {
		if (ClientData.world.ships[in[1]] == null) {
			System.out.println("CLIENT: REQUESTING DATA FOR SHIP, ID:"+in[1]);
			
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
		}else
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
		if (ClientData.world.ships[in[1]] == null) {
			System.out.println("CLIENT: CREATING SHIP, ID:"+in[1]);
		ClientData.world.ships[in[1]] = new Ship(0,0,0, ClientData.world, in[1]);
		}
		byte hullInfo[] = new byte[60];
		ByteBuffer.wrap(in,4,60).get(hullInfo);
		ClientData.world.ships[in[1]].hull[in[2]][in[3]] = new Hull(hullInfo);
		ClientData.world.ships[in[1]].calcMass();
	}
	public static void receiveShipJect(byte[] in) {
		ShipJect obj = null;
		ByteBuffer b = ByteBuffer.wrap(in, 4, 60);
		switch(in[2]) {
		case 1:
			obj = new EnginesPanel(ClientData.world.ships[in[1]], b.getDouble(), b.getDouble(), b.getDouble());
			System.out.println("Received an EnginesPanel");
		break;
		case 2:
			obj = new Engine(ClientData.world.ships[in[1]], b.getDouble(), b.getDouble(), b.getDouble(), b.get());
			System.out.println("Received an Engine");
		break;
		case 3:
			obj = new WeaponsPanel(ClientData.world.ships[in[1]], b.getDouble(), b.getDouble(), b.getDouble());
			System.out.println("Received an WeaponsPanel");
		break;
		default:
			System.err.println("ERROR: Incorrect ShipJect id "+in[2]+"! (This is bad, probably means server's and client's versions differ)");
		return;
		}
		if(ClientData.world.ships[in[1]] == null) {
			System.err.println("WARNING: Invalid ship id "+in[1]);
			return;
		}
		ClientData.world.ships[in[1]].registerShipJect(obj, in[3]);
	}
	public static void receiveShipJectTick(byte[] in) {
		ByteBuffer b = ByteBuffer.wrap(in, 3, 60);
		ClientData.world.ships[in[1]].objects[in[2]].tickDataUp(b);
	}
	public static void receiveSpaceJectData(byte[] in) {
		if (ClientData.world.objects[in[1]] == null) {
			
			byte out[] = new byte[64];
			out[0] = 5;
			out[1] = in[1];
			DatagramPacket outgoing = new DatagramPacket(out, out.length,
					Config.server, 1141);
			try {
				ClientThread.socket.send(outgoing);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			if(in[2] == -127){
				ClientData.world.objects[in[1]].dead = true;
				ClientData.world.objects[in[1]] = null;
			} else {
				ClientData.world.objects[in[1]].upDate(ByteBuffer.wrap(in, 2, 62));
			}
		}
	}
	public static void receiveSpaceJectInfo(byte[] in) {
		SpaceJect obj = null;
		//STEP 1 - GET THE TYPE
		switch(in[2]) {
		case 1:
			//ZEROES ARE HERE TO SAVE 16 BYTES OF DATA!
			//WAT
			//CAN'T REMEMBER WHY
			obj = new Projectile(0,0,ClientData.world,in[1],0,0);
			//System.out.println("Received a Projectile");
		break;
		default:
			System.err.println("ERROR: Incorrect SpaceJect id "+in[2]+"! (This is bad, probably means server's and client's versions differ)");
		return;
		}
		//STEP 2 - CREATE IT
		//System.out.println("CLIENT: CREATING SPACEJECT, ID:"+in[1]);
		ClientData.world.objects[in[1]] = obj;
		//STEP 3 - GET THE INFO
		obj.infoUp(ByteBuffer.wrap(in, 3, 61));
	}
	//THAt's A DUMB NAME
	//BUT I'LL LIVE
	public static void receiveCLIEInfo(byte[] in) {
		if(Config.clientTickerMode == ClientTicker.MODE_NONE) return;
		//YOU SET MY SOUL ALIGHT
		ClientSideEnt obj = null;
		//STEP 1 - GET THE TYPE
		switch(in[2]) {
		case 1:
			//ZEROES ARE HERE TO SAVE 16 BYTES OF DATA!
			//WAT
			//CAN'T REMEMBER WHY
			obj = new Particle(0,0,ClientData.world,in[1]);
			//System.out.println("Received a Particle");
		break;
		default:
			System.err.println("ERROR: Incorrect CLIE id "+in[2]+"! (This is bad, probably means server's and client's versions differ)");
		return;
		}
		//STEP 2 - CREATE IT
		//System.out.println("CLIENT: CREATING CLIE, ID:"+in[1]);
		ClientData.cl_ents.add(obj);
		//STEP 3 - GET THE INFO
		obj.infoUp(ByteBuffer.wrap(in, 3, 61));
	}
	public static void receiveHull(byte[] in) {
		if(in[4] == -255){
			ClientData.world.ships[in[1]].hull[in[2]][in[3]] = null;
		} else {
			byte hullInfo[] = new byte[60];
			ByteBuffer.wrap(in,4,60).get(hullInfo);
			ClientData.world.ships[in[1]].hull[in[2]][in[3]] = new Hull(hullInfo);
		}
		ClientData.world.ships[in[1]].calcMass();
	}
	public static void playSound(byte[] in) {
		
		ByteBuffer b = ByteBuffer.wrap(in);
		b.get();
		int sound = b.getInt();
		boolean surface = b.get() == 1;
		
		
		String soundName = "";
		switch(sound) {
		case 1:
			soundName = "/sound/effects/explosions/hullboom_1.wav";
		break;
		case 2:
			soundName = "/sound/effects/weapons/basic/fire_1.wav";
		break;
		case 3:
			soundName = "/sound/effects/weapons/basic/fire_2.wav";
		break;
		case 4:
			soundName = "/sound/effects/weapons/basic/fire_3.wav";
		break;
		case 5:
			soundName = "/sound/effects/explosions/hullhit_1.wav";
		break;
		case 6:
			soundName = "/sound/effects/explosions/hullhit_2.wav";
		break;
		default:
			System.err.println("WARNING: Unregistered soundID "+sound);
		}
if(!surface) {
	
	float gain = 0;
	//float pan = 0;
	double x = b.getDouble();
	double y = b.getDouble();
	double res[] = ClientData.world.ships[ClientData.world.players[ClientData.controlledPlayer].shipid].transform(ClientData.world.players[ClientData.controlledPlayer].pos.x, ClientData.world.players[ClientData.controlledPlayer].pos.y);
	gain = 0 - ((float) new Pos(x,y).distance(new Pos(res[0], res[1])))/1000;
	SoundHandler.playModSound(soundName, 1f,gain);
	System.out.println("Playing positioned sound:"+soundName+" "+gain);
		} else {
			System.out.println("Playing surface sound:"+soundName);
			SoundHandler.playSound(soundName);
		}
		
	}

}
