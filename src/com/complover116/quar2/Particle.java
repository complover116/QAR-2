package com.complover116.quar2;

import java.awt.Color;
import java.awt.Graphics2D;
import java.nio.ByteBuffer;

public class Particle extends ClientSideEnt {
	public double velX;
	public double velY;
	public int lifetime;
	public byte type = 0;
	public Color color;
	public Particle(double x, double y, World world, byte id) {
		this(x, y, world, new Color(255,0,0), (byte) 0);
		
	}

	public Particle(double x, double y, World world, Color color) {
		this(x, y, world, color, (byte) 0);
	}
	public Particle(double x, double y, World world, Color color, byte type) {
		super(x, y, world);
		this.color = color;
		this.type = type;
		if(type == 1) {
			this.velX = 0;
			this.velY = 0;
			this.lifetime = 0;
		} else {
			this.velX = Math.random()*10-5;
			this.velY = Math.random()*10-5;
			this.lifetime = 0;
		}
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
		b.put(type);
	}

	@Override
	public void infoUp(ByteBuffer b) {
		color = new Color(b.getInt(),b.getInt(),b.getInt());
		pos.x = b.getDouble();
		pos.y = b.getDouble();
		type = b.get();
		if(type == 1) {
			this.velX = 0;
			this.velY = 0;
			this.lifetime = 0;
		} else {
			this.velX = Math.random()*10-5;
			this.velY = Math.random()*10-5;
			this.lifetime = 100;
		}
	}

}
