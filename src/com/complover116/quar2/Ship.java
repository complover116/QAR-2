package com.complover116.quar2;

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
	public double velX = 0;
	public double velY = 0;
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
	public void tick() {
		velRot *= 0.995;
		this.x += velX;
		this.y += velY;
		this.velX *= 0.995;
		this.velY *= 0.995;
		this.rot += velRot;
		for(int i =0 ;i <this.objects.length; i ++) if(objects[i] != null) this.objects[i].tick();
	}
	public void updatePos(ByteBuffer data) {
		this.x = data.getDouble();
		this.y = data.getDouble();
		this.rot = data.getDouble();
		this.velX = data.getDouble();
		this.velY = data.getDouble();
		this.velRot = data.getDouble();
		this.thrustX = data.getDouble();
	}
	public void downDatePos(ByteBuffer data) {
		data.putDouble(x);
		data.putDouble(y);
		data.putDouble(rot);
		data.putDouble(velX);
		data.putDouble(velY);
		data.putDouble(velRot);
		data.putDouble(thrustX);
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
		System.out.println("("+massX+";"+massY+")");
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
	public void draw(Graphics2D g2d) {
		for(int i = 0; i < 256; i ++)
			for(int j = 0; j < 256; j ++){
				if(hull[i][j] != null) {
					
					double[] transformed = transform(i*32,j*32);
					AffineTransform trans = AffineTransform.getTranslateInstance(transformed[0]+x, transformed[1]+y);
					trans.concatenate(AffineTransform.getRotateInstance(Math.toRadians(rot)));
					g2d.drawImage(ResourceContainer.images.get(hull[i][j].getImagePath()), trans,null);
				}
			}
		for(int i = 0; i < objects.length; i ++) {
			if(objects[i] != null)
			objects[i].draw(g2d);
		}
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
