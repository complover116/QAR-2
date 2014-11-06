package com.complover116.quar2;

public class ClientTicker {
	public static int ticktime = 0;
	public static void tick() {
		try {
		ClientData.world.tick();
		} catch (NullPointerException e) {
			
		}
	}
}
