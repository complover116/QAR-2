package com.complover116.quar2;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class BasicWeapon extends Weapon {
	public BasicWeapon(WeaponsPanel panel) {
		super(panel);
		// TODO Auto-generated constructor stub
	}

	int cooldown = 0;
	
	
	@Override
	public void onFire() {
		//SAFEGUARD - MUST ONLY BE RUN ON SERVER
		if(attachedTo.ship.world.isRemote) return;
		if(cooldown == 0){
			cooldown = 50;
			//TODO - ANIMATIONS
			//BULLET FIRE
			Pos pos = attachedTo.getMountPos().add(Pos.normalFromDeg(rot).mul(60));
			Pos vel = Pos.normalFromDeg(rot).mul(6).add(attachedTo.ship.velocity);
			attachedTo.ship.world.regObject(new Projectile(pos.x,pos.y, vel.x,vel.y));
		}
	}

	@Override
	public void tick() {
		if(cooldown > 0) cooldown --;
	}

	@Override
	public void draw(Graphics2D g2d) {
		// TODO Auto-generated method stub
		Pos mountPos = this.attachedTo.getMountPos();
		AffineTransform tr = AffineTransform.getTranslateInstance(mountPos.x,
				mountPos.y-15.5);
		
		tr.concatenate(AffineTransform.getRotateInstance(Math.toRadians(this.rot + attachedTo.ship.rot),0,15.5));
		g2d.drawImage(
				ResourceContainer.images.get("/img/systems/weapons/basic/basic.png"),
				tr, null);
	}

}
