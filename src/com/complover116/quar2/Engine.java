package com.complover116.quar2;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.io.Serializable;

public class Engine extends ShipJect implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3381261924780498053L;
	public double thrust = 0.1;
	public Engine(Ship sh, double x, double y, double rot) {
		super(sh, x, y, rot);
		// TODO Auto-generated constructor stub
	}
	public void draw(Graphics2D g2d) {
		double res[] = absPos();
		AffineTransform tr = AffineTransform.getTranslateInstance(res[0],
				res[1]);
		tr.concatenate(AffineTransform.getRotateInstance(Math.toRadians(res[2])));
		g2d.drawImage(
				ResourceContainer.images.get("/img/systems/engines/basic.png"),
				tr, null);
		//g2d.drawOval(x, y, width, height);
	}
	@Override 
	public void tick() {
		double res[] = ship.detransform(this.thrust*ship.thrustX, 0);
		ship.velX += res[0];
		ship.velY += res[1];
		//System.out.println(this.thrust*ship.thrustX+"=>"+res[0]+":"+res[1]);
	}
}
