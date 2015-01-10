package com.complover116.quar2;

import java.awt.Graphics2D;
import java.nio.ByteBuffer;

public abstract class SpaceJect {
	public Pos pos = new Pos();
	public World world;
	public abstract void draw(Graphics2D g2d);
	public abstract void tick();
	public SpaceJect(double x, double y, World world) {
		pos.x = x;
		pos.y = y;
		this.world = world;
	}
	public void downDate(ByteBuffer b){
		b.putDouble(pos.x);
		b.putDouble(pos.y);
		dataDown(b);
	}
	public void upDate(ByteBuffer b){
		pos.x = b.getDouble();
		pos.y = b.getDouble();
		dataUp(b);
	}
	/***
	 * There is 46 bytes of data available for sending every tick
	 * @param b Data is to be put here
	 */
	public abstract void dataDown(ByteBuffer b);
	/***
	 * There is 46 bytes of data available for receiving every tick
	 * @param b Data is to be received from here
	 */
	public abstract void dataUp(ByteBuffer b);
	/***
	 * There is 62 bytes of permanent data available for sending only one time
	 * @param b Data is to be put here
	 */
	public abstract void infoDown(ByteBuffer b);
	/***
	 * There is 62 bytes of permanent data available for receiving only one time
	 * @param b Data is to be received from here
	 */
	public abstract void infoUp(ByteBuffer b);
}
