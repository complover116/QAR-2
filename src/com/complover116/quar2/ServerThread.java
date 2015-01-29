package com.complover116.quar2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class ServerThread implements Runnable {
	public static ArrayList<RemoteClient> clients = new ArrayList<RemoteClient>();
	public static DatagramSocket socket;
	public static ServerSocket serverSocket;
	public static ArrayList<byte[]> outgoing = new ArrayList<byte[]>();
	public ServerThread() {
		try {
			socket = new DatagramSocket(1141);
			serverSocket = new ServerSocket(1141);
		} catch (SocketException e) {
			SoundHandler.playSound("/sound/effects/error1.wav");
			JOptionPane.showMessageDialog(null, "Server socket could not bind!\nMake sure that port 1141 is free!", "Connection error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		} catch (IOException e) {
			SoundHandler.playSound("/sound/effects/error1.wav");
			JOptionPane.showMessageDialog(null, "Severside TCP setup failed!", "Connection error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}

	@Override
	public void run() {
		try{
		Loader.initialized = true;
		System.out.println("Server Thread is running");
		new Thread(new MessageDispatchThread(), "MDT").start();
		while (true) {
			byte in[] = new byte[64];
			DatagramPacket incoming = new DatagramPacket(in, in.length);
			try {
				socket.receive(incoming);
				if (in[0] == 10) {
					ServerFunctions.sendShip(ServerData.world.ships[in[1]], in[1]);
				}
				if (in[0] == 124) {
					System.out.println("Incoming connection:"+incoming.getAddress());
					int i = 0;
					boolean success = true;
					try{
					for(i = 0; i < ServerData.world.players.length+1; i ++){
						if(ServerData.world.players[i] == null) {
							System.out.println("Player id "+i+" is free, allocating...");
							break;
						}
					}}
					catch(ArrayIndexOutOfBoundsException e){
						System.out.println("Server is full! Disconnecting the client...");
						success = false;
					}
					byte out[] = new byte[64];
					out[0] = 124;
					if(success){
					
					byte broadcast[] = new byte[64];
					broadcast[0] = -1;
					broadcast[1] = (byte) i;
					broadcast[2] = in[1];
					sendBytes(broadcast);
					
					out[1] = 1;
					out[2] = (byte) i;
					} else{
						out[1] = -1;
					}
					
					
					DatagramPacket response = new DatagramPacket(out, out.length, incoming.getSocketAddress());
					socket.send(response);
					
					
					System.out.println("Waiting for TCP connection...");
					try{
					serverSocket.setSoTimeout(2000);
					Socket sock = serverSocket.accept();
					//sock.getOutputStream().write(-126);
					ServerData.world.players[i] = new Player(ServerData.world, 256,128);
					ServerData.world.players[i].shipid = 5;
					ServerData.world.players[i].pos.x = 128;
					ServerData.world.players[i].color = in[1];
					clients.add(new RemoteClient(incoming.getAddress(),
							incoming.getPort(), ServerData.world.players[i], sock));
					} catch (SocketTimeoutException e) {
						System.err.println("TCP connection failed... :(");
					}
					//SEND THE WHOLE WORLD
					//We won't actually do that, this is stupid to do over UDP
					/*
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					ObjectOutput oout = null;
					try {
					  oout = new ObjectOutputStream(bos);   
					  oout.writeObject(ServerData.world);
					  byte[] yourBytes = bos.toByteArray();
					  System.out.println("Transferring "+yourBytes.length+" bytes");
					  
					  //TRANSFER INITIALIZE!
					  for(int trcn = 0; trcn < yourBytes.length/64; trcn ++){
						  byte 
					  }
					  DatagramPacket world = new DatagramPacket(yourBytes, yourBytes.length, incoming.getSocketAddress());
						socket.send(world);
					} finally {
					  try {
					    if (oout != null) {
					      oout.close();
					    }
					  } catch (IOException ex) {
					    // ignore close exception
					  }
					  try {
					    bos.close();
					  } catch (IOException ex) {
					    // ignore close exception
					  }
					}*/
				}
				if(in[0] == 3) {
					ServerFunctions.sendPlayerInfo(ServerData.world.players[in[1]], in[1]);
				}
				if(in[0] == 5) {
					ServerFunctions.sendObjectInfo(ServerData.world.objects[in[1]]);
				}
				if(in[0] == 1) {
					if(in[1] == 1)
					ServerData.world.players[in[2]].keyPress(in);
					else
						ServerData.world.players[in[2]].keyRelease(in);
				}
			} catch (IOException e1) {
				if(!e1.getMessage().equalsIgnoreCase("full"))
				throw new Exception();
			}
				Thread.sleep(Config.netTick);
		}
		}catch(Exception e) {
			e.printStackTrace();
			SoundHandler.playSound("/sound/effects/error1.wav");
			JOptionPane.showMessageDialog(null, "ServerThread has failed, system can not recover.\nShutting down...", "FATAL ERROR", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}

	

	public static void sendBytesOld(byte[] out) {
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
	public static void sendBytes(byte[] out) {
		synchronized(ServerThread.outgoing){
			outgoing.add(out);
		}
	}
}
