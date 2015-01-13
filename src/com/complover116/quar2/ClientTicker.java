package com.complover116.quar2;

public class ClientTicker {
	public static int ticktime = 0;
	public static void tick() {
		ClientData.world.tick();
		//CONCURRENT MODIFICATION!!!
		//THAT NEEDS TO BE REVERSED
		for(int i = ClientData.cl_ents.size() - 1; i > -1; i --) {
			if(ClientData.cl_ents.get(i).dead) {
				ClientData.cl_ents.remove(i);
			} else {
				ClientData.cl_ents.get(i).tick();
			}
		}
	}
}
