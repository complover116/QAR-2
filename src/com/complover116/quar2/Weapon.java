package com.complover116.quar2;

import java.awt.Graphics2D;
import java.nio.ByteBuffer;


public abstract class Weapon {
	public WeaponsPanel attachedTo;
	double rot = 45;
	double lastRot = 0;
	public Weapon(WeaponsPanel panel) {
		attachedTo = panel;
	}
	public void downDate(ByteBuffer b) {
		b.putDouble(rot);
	}
	public void upDate(ByteBuffer b) {
		rot = b.getDouble();
	}
	public boolean shouldSendData() {
		if(this.rot!=this.lastRot) {
			lastRot = rot;
			return true;
		}
		return false;
	}
	public abstract void onFire();
	public abstract void tick();
	public abstract void draw(Graphics2D g2d);
}
