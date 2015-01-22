package com.complover116.quar2;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.io.Serializable;
import java.nio.ByteBuffer;

public class Ship implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6330349971680345419L;
	public World world = null;
	public byte id;
	public Hull[][] hull = new Hull[256][256];
	public double x;
	public double y;
	public double rot;
	public Pos velocity = new Pos();
	public double mass = 0;
	public double velRot = 0;
	public double massX = 0;
	public double massY = 0;
	public double thrustX = 0;
	public double thrustY = 0;
	public double thrustRot = 0;
	public ShipJect objects[] = new ShipJect[256];
	public Ship(double x, double y, double rot, World world, byte id) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.rot = rot;
		this.id = id;
	}
	
	public void physReact(byte x, byte y, Pos velocity) {
		double[] res = this.realtransform(x*32+16, y*32+16);
		physReact(new Pos(res[0], res[1]), velocity, 1);
	}
	public void physReact(Pos impactpos, Pos velocity, double scale) {
		//GET PUSHED AWAY
		//double res[] = this.realtransform(massX, massY);
		//Pos push = new Pos(res[0], res[1]).sub(impactpos);
		//this.velocity.addOn(push.normal().mul(10));
		Pos res = velocity.sub(this.velocity).mul(10/this.mass);
		//res.mulOn(10);
		this.velocity.addOn(res.mul(scale));
		Pos deltaV = velocity.sub(this.velocity);
		//TURN
		double O[] = this.realdetransform(massX,massY);
		Pos o = new Pos(O[0], O[1]);
		double impact[] = this.realdetransform(impactpos.x,impactpos.y);
		Pos impos = new Pos(impact[0], impact[1]);
		//TURN CO-EFF
		//HERE GOES PHYSICS: https://en.wikipedia.org/wiki/Rotation_around_a_fixed_axis#Kinematics
		double r = o.distance(impos);
		double v = deltaV.length();
		double w = v/(r);
		double wDeg = Math.toDegrees(w);
		//double velres[] = this.realdetransform(deltaV.x,deltaV.y);
		
		Pos velToTemp = impos.rotate(impos.sub(new Pos(O[0],O[1])).direction()+90);
		this.velRot += wDeg*velToTemp.normal().y*1000*scale;
		//System.out.println(wDeg+":"+velToTemp.normal().y);
	}
	/***
	 * THIS FUNCTION IS SERVER ONLY!!!
	 */
	public void damageHull(byte x, byte y, byte damage) {
		if(this.hull[x][y] == null) return;
		if(this.hull[x][y].health <= damage){
		//STEP 1 - DO THE DAMAGE
		this.hull[x][y] = null;
		//STEP 2 - SEND HULL UPDATES
		ServerFunctions.sendHullBlockStatus(this, x, y);
		//STEP 3 - MAKE PARTICLES!
		double[] res = this.realtransform(x*32+16, y*32+16);
		for(int z = 0; z < 10; z ++) {
			ServerFunctions.sendClientSideObjectInfo(new Particle(res[0], res[1], ServerData.world, new Color(155,155,155)));
		}
		ServerSoundHandler.playPositionedSound("/sound/effects/explosions/hullboom_1.wav", res[0], res[1]);
		} else {
			this.hull[x][y].health -= damage;
			ServerFunctions.sendHullBlockStatus(this, x, y);
			double[] res = this.realtransform(x*32+16, y*32+16);
			for(int z = 0; z < (damage/10) + 1; z ++) {
				ServerFunctions.sendClientSideObjectInfo(new Particle(res[0], res[1], ServerData.world, new Color(155,155,155)));
			}
		}
	}
	public void tick() {
		if(!world.isRemote&&Config.collisionsEnabled) {
			for(byte i = 0; i < ServerData.world.ships.length; i ++) {
				
				Ship sh = ServerData.world.ships[i];
				if(sh != null&&sh.id!=this.id&&new Pos(sh.x, sh.y).distance(new Pos(this.x,this.y)) < 1000){
					for(byte x0 = 0; x0 < 127; x0++)
						for(byte y0 = 0; y0 < 127; y0++){
							if(this
									.hull[x0][y0]!= null) {
								double[] res0 = this.realtransform(x0*32+16, y0*32+16);
					for(byte x = 0; x < 127; x++)
						for(byte y = 0; y < 127; y++){
							if(i < 0)System.out.println(i+", "+x+":"+y);
							if(ServerData.world.ships[i]
									.hull[x][y]!= null) {
								double[] res = ServerData.world.ships[i].realtransform(x*32+16, y*32+16);
								if(new Pos(res0[0],res0[1]).distance(new Pos(res[0],res[1]))< 32) {
									//HERE COMES THE HARD PART:
									//STEP 1 - REMOVE SELF
									//dead = true;
									//STEP 2 - DO DAMAGE
									ServerData.world.ships[i].damageHull(x, y, (byte) 10);
									ServerData.world.ships[i].physReact(new Pos(res0[0],res0[1]), velocity, 3);
									this.damageHull(x0, y0, (byte) 10);
									this.physReact(new Pos(res[0],res[1]), sh.velocity, 3);
									//y = (byte) 255;
									//x = (byte) 255;
									//STEP 3 - MAKE PARTICLES!
									for(int z = 0; z < 5; z ++) {
										ServerFunctions.sendClientSideObjectInfo(new Particle(res0[0], res0[1], ServerData.world, new Color(255,155,0)));
									}
								}
							}
						}
							}
						}
				}
				}
			}
		velRot *= 0.995;
		this.x += velocity.x;
		this.y += velocity.y;
		velocity.mulOn(0.995);
		this.rot += velRot;
		for(int i =0 ;i <this.objects.length; i ++) if(objects[i] != null) this.objects[i].tick();
	}
	

	public void updatePos(ByteBuffer data) {
		this.x = data.getDouble();
		this.y = data.getDouble();
		this.rot = data.getDouble();
		velocity.x = data.getDouble();
		velocity.y = data.getDouble();
		this.velRot = data.getDouble();
		this.thrustX = data.getDouble();
	}
	public void downDatePos(ByteBuffer data) {
		data.putDouble(x);
		data.putDouble(y);
		data.putDouble(rot);
		data.putDouble(velocity.x);
		data.putDouble(velocity.y);
		data.putDouble(velRot);
		data.putDouble(thrustX);
		for(int i = 0; i < 127; i++){
			if(this.objects[i] != null) {
				ServerFunctions.sendShipJectTick(objects[i]);
			}
		}
	}
	public void calcMass() {
		mass = 0;
		double diver = 0;
		double divingX = 0;
		double divingY = 0;
		for(int i = 0; i < 256; i ++)
			for(int j = 0; j < 256; j ++){
				if(hull[i][j] != null) {
					mass ++;
					diver ++;
					divingX += i*32 + 16;
					divingY += j*32 + 16;
				}
				}
		massX = divingX / diver;
		massY = (divingY / diver) - 32;
		//System.out.println("("+massX+";"+massY+")");
	}
	public double[] transform(double x, double y) {
		//Get current degAndD
		double deltaX = x - massX;
		double deltaY = y - massY;
		double deg = Math.atan2(deltaX, deltaY);
		double distance = Math.sqrt(deltaX*deltaX+deltaY*deltaY);
		deg = Math.toDegrees(deg);
		//Change it
		deg += rot;
		//And convert back
		double relx = massX + Math.cos(Math.toRadians(deg-90))*distance;
		double rely = massX + Math.sin(Math.toRadians(deg-90))*distance;
		return new double[]{relx,rely};
	}
	public double[] detransform(double x, double y) {
		//Get current degAndD
		double deg = Math.atan2(x, y);
		double distance = Math.sqrt(x*x+y*y);
		deg = Math.toDegrees(deg);
		//Change it
		deg += rot;
		//And convert back
		double relx = Math.cos(Math.toRadians(deg-90))*distance;
		double rely = Math.sin(Math.toRadians(deg-90))*distance;
		return new double[]{relx,rely};
	}
	public double[] realtransform(double x, double y) {
		//Get current degAndD
				double deltaX = x - massX;
				double deltaY = y - massY;
				double deg = Math.atan2(deltaX, deltaY);
				double distance = Math.sqrt(deltaX*deltaX+deltaY*deltaY);
				deg = Math.toDegrees(deg);
				//Change it
				deg += rot;
				//And convert back
				double relx = massX + Math.cos(Math.toRadians(deg-90))*distance;
				double rely = massX + Math.sin(Math.toRadians(deg-90))*distance;
				return new double[]{relx+this.x,rely+this.y};
	}
	public double[] realdetransform(double x, double y) {
		x-=this.x;
		y-=this.y;
		//Get current degAndD
				double deltaX = x - massX;
				double deltaY = y - massY;
				double deg = Math.atan2(deltaX, deltaY);
				double distance = Math.sqrt(deltaX*deltaX+deltaY*deltaY);
				deg = Math.toDegrees(deg);
				//Change it
				deg += rot;
				//And convert back
				double relx = massX + Math.cos(Math.toRadians(deg-90))*distance;
				double rely = massX + Math.sin(Math.toRadians(deg-90))*distance;
				return new double[]{relx,rely};
	}
	public void draw(Graphics2D g2d) {
		for(int i = 0; i < 256; i ++)
			for(int j = 0; j < 256; j ++){
				if(hull[i][j] != null) {
					
					double[] transformed = transform(i*32,j*32);
					AffineTransform trans = AffineTransform.getTranslateInstance(transformed[0]+x, transformed[1]+y);
					trans.concatenate(AffineTransform.getRotateInstance(Math.toRadians(rot)));
					g2d.drawImage(ResourceContainer.images.get(hull[i][j].getImagePath()), trans,null);
					String img = "";
					if(hull[i][j].health > 75)
						img = "";
					else if (hull[i][j].health > 50)
						img =  "/img/hull/damage_1.png";
					else if(hull[i][j].health > 25)
						img =  "/img/hull/damage_2.png";
					else 
						img =  "/img/hull/damage_3.png";
					g2d.drawImage(ResourceContainer.images.get(img), trans,null);
				}
			}
		for(int i = 0; i < objects.length; i ++) {
			if(objects[i] != null)
			objects[i].draw(g2d);
		}
		double res[] = this.realtransform(massX, massY);
		g2d.fillRect((int)res[0], (int)res[1], 10, 10);
	}
	
	public byte registerShipJect(ShipJect obj) {
		for(byte i = 0; i < 256; i++) {
			if(objects[i] == null) {
				objects[i] = obj;
				obj.id = i;
				return i;
			}
		}
		return -1;
	}
	public boolean registerShipJect(ShipJect obj, byte id) {
		if(objects[id] != null) {
			System.err.println("Failed to register a ShipJect: id "+id+" is already taken!");
			return false;
		}
		objects[id] = obj;
		obj.id = id;
		System.out.println("Registered a ShipJect: id "+id);
		return true;
	}
}
