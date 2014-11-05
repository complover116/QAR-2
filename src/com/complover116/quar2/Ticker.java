package com.complover116.quar2;

public class Ticker {
	public static int ticktime = 0;
	public static void tick() {
		ServerData.world.tick();
		//NETWORKING STUFF
		if(ticktime > 0){
		//SENDING SHIP POSITION UPDATES
		for(int i = 0; i < Config.maxShips; i ++) {
			//IF IT EXISTS
			if(ServerData.world.ships[i] != null) {
				//SEND IT
				ServerFunctions.sendShipPos(ServerData.world.ships[i], i);
			}
		}
		//SENDING PLAYER INFO
		for(int i = 0; i < Config.maxShips; i ++) {
			//IF IT EXISTS
			if(ServerData.world.players[i] != null) {
				//SEND IT
				ServerFunctions.sendPlayerPos(ServerData.world.players[i], i);
			}
		}
		ticktime = 0;
		}
		ticktime ++;
	}
}
