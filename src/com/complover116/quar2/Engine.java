package com.complover116.quar2;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.io.Serializable;

public class Engine extends ShipJect implements Serializable {
	/**
	 * 
	 */
	public Sound sound;
	private static final long serialVersionUID = 3381261924780498053L;
	public double thrust = 0.1;
	public Engine(Ship sh, double x, double y, double rot) {
		super(sh, x, y, rot);
		if(sh.world.isRemote){
			sound = new Sound("/sound/effects/engine/idle_loop.wav");
			sound.startLoop();
		}
	}
	public void draw(Graphics2D g2d) {
		double res[] = absPos();
		AffineTransform tr = AffineTransform.getTranslateInstance(res[0],
				res[1]);
		tr.concatenate(AffineTransform.getRotateInstance(Math.toRadians(res[2])));
		g2d.drawImage(
				ResourceContainer.images.get("/img/systems/engines/basic.png"),
				tr, null);
		g2d.transform(tr);
		try {
			g2d.setColor(new Color(0,0,255));
			g2d.drawOval(0, 0, 20, 20);
			g2d.transform(tr.createInverse());
		} catch (NoninvertibleTransformException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//g2d.drawOval(x, y, width, height);
	}
	@Override 
	public void tick() {
		
		double res[] = ship.detransform(this.thrust*ship.thrustX/ship.mass, 0);
		ship.velX += res[0];
		ship.velY += res[1];
		if(ship.world.isRemote){
			sound.setVolume((float) (-25 + ship.thrustX/4));
		}
		
		//System.out.println(this.thrust*ship.thrustX+"=>"+res[0]+":"+res[1]);
	}
}
