package com.complover116.quar2;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class ShipJect {
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
	public void draw(Graphics2D g2d) {
		double res[] = absPos();
		AffineTransform tr = AffineTransform.getTranslateInstance(res[0], res[1]);
		tr.concatenate(AffineTransform.getRotateInstance(Math.toRadians(res[2])));
		g2d.drawImage(ResourceContainer.images.get("/img/systems/panels/engines.png"), tr, null);
	}
}
