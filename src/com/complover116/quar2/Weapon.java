package com.complover116.quar2;

import java.awt.Graphics2D;

public abstract class Weapon {
	public WeaponsPanel attachedTo;
	double rot = 45;
	public Weapon(WeaponsPanel panel) {
		attachedTo = panel;
	}
	public abstract void onFire();
	public abstract void tick();
	public abstract void draw(Graphics2D g2d);
}
