package com.complover116.quar2;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class Player {
	public double x;
	public double y;
	public Ship ship;
	public double velY = 0;
	public void tick() {
		// TODO Auto-generated method stub
		
	}
	public void draw(Graphics2D g2d) {
		double transformed[] = ship.transform(x, y);
		AffineTransform trans = AffineTransform.getTranslateInstance(transformed[0]+ship.x, transformed[1]+ship.y);
		trans.concatenate(AffineTransform.getRotateInstance(Math.toRadians(ship.rot)));
		g2d.drawImage(ResourceContainer.images.get("/img/player/idle.png"), trans, null);
	}
}
