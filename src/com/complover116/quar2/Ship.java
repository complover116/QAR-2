package com.complover116.quar2;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class Ship {
	public Hull[][] hull = new Hull[500][500];
	public double x;
	public double y;
	public double rot;
	public double velX = 0;
	public double velY = 0;
	public double velRot = 0;
	public double massX = 0;
	public double massY = 0;
	public double thrustX = 0;
	public double thrustY = 0;
	public double thrustRot = 0;
	public ArrayList<ShipJect> objects = new ArrayList<ShipJect>();
	public Ship(double x, double y, double rot) {
		this.x = x;
		this.y = y;
		this.rot = rot;
		hull[0][0] = new Hull(3);
		hull[1][0] = new Hull(3);
		hull[2][0] = new Hull(3);
		hull[3][0] = new Hull(3);
		hull[4][0] = new Hull(3);
		hull[5][0] = new Hull(3);
		hull[6][0] = new Hull(3);
		hull[7][0] = new Hull(3);
		hull[8][0] = new Hull(3);
		hull[9][0] = new Hull(3); 
		hull[10][0] = new Hull(3);
		hull[11][0] = new Hull(3);
		hull[12][0] = new Hull(3);
		hull[13][0] = new Hull(3);
		hull[14][0] = new Hull(3);
		hull[15][0] = new Hull(3);
		hull[16][0] = new Hull(3);
		hull[17][0] = new Hull(3);
		hull[18][0] = new Hull(3);
		hull[19][0] = new Hull(3);
		hull[20][0] = new Hull(3);
		
		hull[0][13] = new Hull(3);
		hull[1][13] = new Hull(3);
		hull[2][13] = new Hull(3);
		hull[3][13] = new Hull(3);
		hull[4][13] = new Hull(3);
		hull[5][13] = new Hull(3);
		hull[6][13] = new Hull(3);
		hull[7][13] = new Hull(3);
		hull[8][13] = new Hull(3);
		hull[9][13] = new Hull(3);
		hull[10][13] = new Hull(3);
		hull[11][13] = new Hull(3);
		hull[12][13] = new Hull(3);
		hull[13][13] = new Hull(3);
		hull[14][13] = new Hull(3);
		hull[15][13] = new Hull(3);
		hull[16][13] = new Hull(3);
		hull[17][13] = new Hull(3);
		hull[18][13] = new Hull(3);
		hull[19][13] = new Hull(3);
		hull[20][13] = new Hull(3);
		
		hull[0][1] = new Hull(3);
		hull[0][2] = new Hull(3);
		hull[0][3] = new Hull(3);
		hull[0][4] = new Hull(3);
		hull[0][5] = new Hull(3);
		hull[0][6] = new Hull(3);
		hull[0][7] = new Hull(3);
		hull[0][8] = new Hull(3);
		hull[0][9] = new Hull(3);
		hull[0][10] = new Hull(3);
		hull[0][11] = new Hull(3);
		hull[0][12] = new Hull(3);
		
		/*hull[9][1] = new Hull(3);
		hull[9][2] = new Hull(3);
		hull[9][3] = new Hull(3);
		hull[9][4] = new Hull(3);
		hull[9][5] = new Hull(3);
		hull[9][6] = new Hull(3);
		hull[9][7] = new Hull(3);
		hull[9][8] = new Hull(3);
		hull[9][9] = new Hull(3);*/
		calcMass();
		objects.add(new EnginesPanel(this, 128,64,0));
		objects.add(new Engine(this, -64,64,0));
	}
	public void tick() {
		this.x += velX;
		this.y += velY;
		this.rot += velRot;
		for(int i =0 ;i <this.objects.size(); i ++) this.objects.get(i).tick();
	}
	public void updatePos(ByteBuffer data) {
		this.x = data.getDouble();
		this.y = data.getDouble();
		this.rot = data.getDouble();
		this.velX = data.getDouble();
		this.velY = data.getDouble();
		this.velRot = data.getDouble();
	}
	public void downDatePos(ByteBuffer data) {
		data.putDouble(x);
		data.putDouble(y);
		data.putDouble(rot);
		data.putDouble(velX);
		data.putDouble(velY);
		data.putDouble(velRot);
	}
	public void calcMass() {
		double diver = 0;
		double divingX = 0;
		double divingY = 0;
		for(int i = 0; i < 500; i ++)
			for(int j = 0; j < 500; j ++){
				if(hull[i][j] != null) {
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
		for(int i = 0; i < 500; i ++)
			for(int j = 0; j < 500; j ++){
				if(hull[i][j] != null) {
					
					double[] transformed = transform(i*32,j*32);
					AffineTransform trans = AffineTransform.getTranslateInstance(transformed[0]+x, transformed[1]+y);
					trans.concatenate(AffineTransform.getRotateInstance(Math.toRadians(rot)));
					g2d.drawImage(ResourceContainer.images.get(hull[i][j].getImagePath()), trans,null);
				}
			}
		for(int i = 0; i < objects.size(); i ++) {
			objects.get(i).draw(g2d);
		}
	}
}
