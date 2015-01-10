package com.complover116.quar2;

import java.awt.Color;
import java.awt.Graphics2D;
import java.nio.ByteBuffer;

public class Particle extends SpaceJect {
	public double velX = Math.random()*10-5;
	public double velY = Math.random()*10-5;
	public int lifetime = 200;
	public Color color;
	public Particle(double x, double y, World world, byte id) {
		super(x, y, world, id);
	}

	public Particle(double x, double y, World world, Color color) {
		super(x, y, world);
		this.color = color;
	}

	@Override
	public void draw(Graphics2D g2d) {
		g2d.setColor(color);
		g2d.fillRect((int)pos.x-6, (int)pos.y-6, 12, 12);
	}

	@Override
	public void tick() {
		//MOVE
		pos.x += velX;
		pos.y += velY;
		lifetime --;
		if(lifetime < 0) this.dead = true;
	}

	@Override
	public void dataDown(ByteBuffer b) {
		
	}

	@Override
	public void dataUp(ByteBuffer b) {
		
	}

	@Override
	public void infoDown(ByteBuffer b) {
		b.putInt(color.getRed());
		b.putInt(color.getGreen());
		b.putInt(color.getBlue());
	}

	@Override
	public void infoUp(ByteBuffer b) {
		color = new Color(b.getInt(),b.getInt(),b.getInt());
		
	}

}
