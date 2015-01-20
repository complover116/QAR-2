package com.complover116.quar2;

import java.awt.Graphics2D;

public abstract class Weapon {
	public abstract void onFire();
	public abstract void tick();
	public abstract void draw(Graphics2D g2d);
}
