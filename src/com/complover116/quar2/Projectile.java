package com.complover116.quar2;

import java.awt.Color;
import java.awt.Graphics2D;
import java.nio.ByteBuffer;

public class Projectile extends SpaceJect {
	public double velX;
	public double velY;
	public Projectile(double x, double y, World world, byte id) {
		super(x, y, world, id);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void draw(Graphics2D g2d) {
		g2d.setColor(new Color(255, (int)(Math.random()*255), 0));
		g2d.fillRect((int)pos.x-6, (int)pos.y-6, 12, 12);
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dataDown(ByteBuffer b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dataUp(ByteBuffer b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void infoDown(ByteBuffer b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void infoUp(ByteBuffer b) {
		// TODO Auto-generated method stub
		
	}

}
