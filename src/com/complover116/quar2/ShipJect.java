package com.complover116.quar2;

import java.awt.Graphics2D;

public abstract class ShipJect {
	public Pos pos = new Pos();
	public double rot;
	public Ship ship;
	public ShipJect(Ship sh, double x, double y, double rot){
		this.ship = sh;
		this.pos.x = x;
		this.pos.y = y;
		this.rot = rot;
	}
	public double[] absPos() {
		double[] res = ship.transform(pos.x, pos.y);
		return new double[]{ship.x+res[0],ship.y+res[1],ship.rot};
	}
	public abstract void draw(Graphics2D g2d);
	public abstract void tick();
}
