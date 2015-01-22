package com.complover116.quar2;

import java.awt.Color;
import java.awt.Graphics2D;
import java.nio.ByteBuffer;

public class Projectile extends SpaceJect {
	public Pos velocity = new Pos(30,-10);
	public double targetX;
	public double targetY;
	public int lifetime;
	public Projectile(double x, double y, World world, byte id, double velX, double velY) {
		super(x, y, world, id);
		this.velocity.x = velX;
		this.velocity.y = velY;
		// TODO Auto-generated constructor stub
	}
	public Projectile(double x, double y, double velX, double velY) {
		super(x, y);
		this.velocity.x = velX;
		this.velocity.y = velY;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void draw(Graphics2D g2d) {
		g2d.setColor(new Color(255, (int)(Math.random()*255), 0));
		g2d.fillRect((int)pos.x-8, (int)pos.y-8, 16, 16);
		//g2d.drawRect((int)targetX, (int)targetY, 5, 5);
	}

	@Override
	public void tick() {
		
		//MOVE
		pos.addOn(velocity);
		
		
		//COLLISION CHECK (SERVER ONLY)
		if(!world.isRemote) {
			//MAKE PARTICLES
			ServerFunctions.sendClientSideObjectInfo(new Particle(this.pos.x, this.pos.y, ServerData.world, new Color(255,155,0), (byte) 1));
			this.lifetime++;
			if(this.lifetime>500) {
				this.dead = true;
			}
			for(int i = 0; i < ServerData.world.ships.length; i ++)
				if(ServerData.world.ships[i] != null) {
					for(byte x = 0; x < 127; x++)
						for(byte y = 0; y < 127; y++){
							if(i < 0)System.out.println(i+", "+x+":"+y);
							if(ServerData.world.ships[i]
									.hull[x][y]!= null) {
								double[] res = ServerData.world.ships[i].realtransform(x*32+16, y*32+16);
								targetX = res[0];
								targetY = res[1];
								if(pos.distance(new Pos(res[0],res[1]))< 32) {
									//HERE COMES THE HARD PART:
									//STEP 1 - REMOVE SELF
									dead = true;
									//STEP 2 - DO DAMAGE
									ServerData.world.ships[i].damageHull(x, y, (byte) 26);
									ServerData.world.ships[i].physReact(pos, velocity, 0.1);
									//y = (byte) 255;
									//x = (byte) 255;
									//STEP 3 - MAKE PARTICLES!
									for(int z = 0; z < 5; z ++) {
										ServerFunctions.sendClientSideObjectInfo(new Particle(this.pos.x, this.pos.y, ServerData.world, new Color(255,155,0)));
									}
								}
							}
						}
				}
		}
	}

	@Override
	public void dataDown(ByteBuffer b) {
		b.putDouble(targetX);
		b.putDouble(targetY);
	}

	@Override
	public void dataUp(ByteBuffer b) {
		targetX = b.getDouble();
		targetY = b.getDouble();
	}

	@Override
	public void infoDown(ByteBuffer b) {
		// TODO Auto-generated method stub bububbu
	}

	@Override
	public void infoUp(ByteBuffer b) {
		// TODO Auto-generated method stub
		
	}

}
