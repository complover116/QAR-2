package com.complover116.quar2;

import java.awt.Graphics2D;
import java.io.Serializable;
import java.nio.ByteBuffer;

public abstract class ShipJect implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -739488251719246204L;
	public Pos pos = new Pos();
	public double rot;
	public Ship ship;
	public byte id = -1;
	public ShipJect(Ship sh, double x, double y, double rot){
		this.ship = sh;
		this.pos.x = x;
		this.pos.y = y;
		this.rot = rot;
	}
	/**
	 * Return true and override tickData[Down/Up](ByteBuffer b) to send data to clients every tick
	 * @return whether data should be sent to clients this tick
	 */
	public boolean shouldSendTickData() {
		return false;
	}
	public void tickDataDown(ByteBuffer b) {}
	public void tickDataUp(ByteBuffer b) {}
	public byte[] downDate() {
		byte[] b = new byte[60];
		ByteBuffer bb = 
		ByteBuffer.wrap(b);
		bb.putDouble(pos.x);
		bb.putDouble(pos.y);
		bb.putDouble(rot);
		return b;
	}
	public double[] absPos() {
		double[] res = ship.transform(pos.x, pos.y);
		return new double[]{ship.x+res[0],ship.y+res[1],ship.rot};
	}
	public double[] absPos(double xOffset, double yOffset) {
		double[] res = ship.transform(pos.x+xOffset, pos.y+yOffset);
		return new double[]{ship.x+res[0],ship.y+res[1],ship.rot};
	}
	public abstract void draw(Graphics2D g2d);
	public abstract void tick();
}
