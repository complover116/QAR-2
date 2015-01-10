package com.complover116.quar2;

import java.io.Serializable;

public class World implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5152246757662289849L;
	public boolean isRemote = true;
	public Ship[] ships = new Ship[Config.maxShips];
	public SpaceJect[] objects = new SpaceJect[Config.maxObjects];
	public Player[] players = new Player[Config.maxPlayers];
	public void tick() {
		for(int i = 0; i < ships.length; i ++) {
			if(ships[i] != null)ships[i].tick();
		}
		for(int i = 0; i < players.length; i ++) {
			if(players[i] != null)players[i].tick();
		}
		for(int i = 0; i < objects.length; i ++) {
			if(objects[i] != null){
				if(objects[i].dead){
					objects[i] = null;
				} else
				objects[i].tick();
			}
		}
	}
}
