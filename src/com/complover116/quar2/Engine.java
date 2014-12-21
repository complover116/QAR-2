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
	public byte direction = 1;
	public Engine(Ship sh, double x, double y, double rot, byte direction) {
		super(sh, x, y, rot);
		if(sh.world.isRemote){
			sound = new Sound("/sound/effects/engine/active_loop.wav");
			
		}
		this.direction = direction;
	}
	public void draw(Graphics2D g2d) {
		double res[] = absPos();
		AffineTransform tr = AffineTransform.getTranslateInstance(res[0],
				res[1]);
		tr.concatenate(AffineTransform.getRotateInstance(Math.toRadians(res[2])));
		
		switch(this.direction){
		case 1:
		g2d.drawImage(ResourceContainer.images.get("/img/systems/engines/basic_left.png"),
				tr, null);
		break;
		case 2:
			g2d.drawImage(ResourceContainer.images.get("/img/systems/engines/basic_right.png"),
					tr, null);
			break;
		case 3:
			g2d.drawImage(ResourceContainer.images.get("/img/systems/engines/basic_up.png"),
					tr, null);
			break;
		case 4:
			g2d.drawImage(ResourceContainer.images.get("/img/systems/engines/basic_down.png"),
					tr, null);
			break;
		}
				
		
		double res2[] = absPos(-32, -24);
		AffineTransform tr2 = AffineTransform.getTranslateInstance(res2[0],
				res2[1]);
		tr2.concatenate(AffineTransform.getRotateInstance(Math.toRadians(res2[2])));
		g2d.transform(tr2);
		try {
			g2d.setColor(new Color(0,0,255));
			
			for(int i = 0; i < 15; i ++) {
				int l = (int) ((int)this.thisWayThrust() - Math.random()*this.thisWayThrust());
				int deviation = (int) ((10 - Math.random()*20)/100*this.thisWayThrust());
				switch(this.direction){
				case 1:
					g2d.drawLine(53 - l, i + deviation, 53, i);
					break;
				case 2:
					g2d.drawLine(140 - l, i - deviation, 140, i);
					break;
				}
				
			}
			g2d.transform(tr2.createInverse());
		} catch (NoninvertibleTransformException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//g2d.drawOval(x, y, width, height);
	}
	public double thisWayThrust() {
		if(this.ship.thrustX > 0&&this.direction == 1)return this.ship.thrustX;
		if(this.ship.thrustX < 0&&this.direction == 2)return this.ship.thrustX;
		return 0;
	}
	@Override 
	public void tick() {
		double res[] = ship.detransform(this.thrust*this.thisWayThrust()/ship.mass, 0);
		ship.velX += res[0];
		ship.velY += res[1];
		if(ship.world.isRemote){
			if(this.thisWayThrust() == 0&&sound.clip.isActive()){
				sound.stop();
			} else if(this.thisWayThrust() != 0&&!sound.clip.isActive()) {
				sound.startLoop();
			}
		}
		
		//System.out.println(this.thrust*ship.thrustX+"=>"+res[0]+":"+res[1]);
	}
}
