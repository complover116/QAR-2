package com.complover116.quar2;

import java.util.ArrayList;


public class ClientData {
	public static World world = new World();
	public static int controlledPlayer = 0;
	public static BGObject[][] background = new BGObject[100][100];
	public static ArrayList<ClientSideEnt> cl_ents = new ArrayList<ClientSideEnt>();
	public static boolean run = true;
	public static HUD hud = HUD.DEFAULT;
}
