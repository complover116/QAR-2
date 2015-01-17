package com.complover116.quar2;

import java.awt.Color;
import java.awt.Graphics2D;
import java.nio.ByteBuffer;

public class Particle extends ClientSideEnt {
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
		if(lifetime>0)
		g2d.fillRect((int)pos.x-6, (int)pos.y-6, 12, 12);
		else 
			g2d.fillRect((int)pos.x-6-lifetime, (int)pos.y-6 - lifetime, 12 + lifetime, 12 + lifetime);
	}

	@Override
	public void tick() {
		//MOVE
		pos.x += velX;
		pos.y += velY;
		lifetime --;
		if(lifetime < -6) this.dead = true;
	}

	@Override
	public void infoDown(ByteBuffer b) {
		b.putInt(color.getRed());
		b.putInt(color.getGreen());
		b.putInt(color.getBlue());
		b.putDouble(this.pos.x);
		b.putDouble(this.pos.y);
	}

	@Override
	public void infoUp(ByteBuffer b) {
		color = new Color(b.getInt(),b.getInt(),b.getInt());
		pos.x = b.getDouble();
		pos.y = b.getDouble();
	}

}
