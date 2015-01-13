package com.complover116.quar2;

import java.awt.Graphics2D;
import java.nio.ByteBuffer;

public abstract class ClientSideEnt {
	public Pos pos = new Pos();
	public World world;
	public byte id;
	public boolean dead = false;
	public abstract void draw(Graphics2D g2d);
	public abstract void tick();
	public ClientSideEnt(double x, double y, World world, byte id) {
		pos.x = x;
		pos.y = y;
		this.world = world;
		this.id = id;
	}
	public ClientSideEnt(double x, double y, World world2) {
		pos.x = x;
		pos.y = y;
		this.world = world2;
	}
	/***
	 * There is 61 bytes of permanent data available for sending only one time
	 * @param b Data is to be put here
	 */
	public abstract void infoDown(ByteBuffer b);
	/***
	 * There is 61 bytes of permanent data available for receiving only one time
	 * @param b Data is to be received from here
	 */
	public abstract void infoUp(ByteBuffer b);
}
