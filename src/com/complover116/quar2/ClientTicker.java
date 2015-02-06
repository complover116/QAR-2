package com.complover116.quar2;

public class ClientTicker {
	public static int ticktime = 0;
	/***
	 * Only to be used on the slowest computers. 
	 * Basically, the client only renders data from the server.
	 * No clientside objects, animations, and, of course, no client predictions.
	 * Might look weird, but it will work
	 */
	public static final byte MODE_NONE = -1;
	/***
	 * To be used if the client is slow. Disables client predictions.
	 */
	public static final byte MODE_MINIMAL = 0;
	/***
	 * Default mode. 
	 * All the clientside stuff works, and the client will automatically predict the next frame if no data from the server.
	 */
	public static final byte MODE_NORMAL = 1;
	/***
	 * To be used with unreliable connection to the host. 
	 * The client will predict the next frame, even if there is data, and then read it.
	 * Will help with laggy frames, but might cause clientside stuff to go a bit weird.
	 */
	public static final byte MODE_FORCED = 2;
	
	public static int sinceLastTick = 0;
	public static void tick() {
		sinceLastTick = 0;
		if(Config.clientTickerMode > ClientTicker.MODE_NONE){
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
}
