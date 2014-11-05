package com.complover116.quar2;

public class World {
	public Ship[] ships = new Ship[Config.maxShips];
	public Player[] players = new Player[Config.maxShips];
	public void tick() {
		for(int i = 0; i < ships.length; i ++) {
			if(ships[i] != null)ships[i].tick();
		}
		for(int i = 0; i < players.length; i ++) {
			if(players[i] != null)players[i].tick();
		}
	}
}
