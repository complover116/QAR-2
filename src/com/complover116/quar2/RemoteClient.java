package com.complover116.quar2;

import java.net.InetAddress;
import java.net.Socket;

public class RemoteClient {
	
	public InetAddress address;
	public int port;
	public Socket sock;
	public Player player;
	public RemoteClient(InetAddress address2, int port2) {
		address = address2;
		port = port2;
	}
	public RemoteClient(InetAddress address2, int port2, Player player2, Socket sock) {
		address = address2;
		port = port2;
		player = player2;
		this.sock = sock;
	}
}
