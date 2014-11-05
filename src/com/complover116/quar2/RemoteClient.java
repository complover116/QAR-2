package com.complover116.quar2;

import java.net.InetAddress;

public class RemoteClient {
	
	public InetAddress address;
	public int port;
	public RemoteClient(InetAddress address2, int port2) {
		address = address2;
		port = port2;
	}
}
