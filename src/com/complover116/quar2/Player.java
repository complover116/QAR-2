package com.complover116.quar2;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.nio.ByteBuffer;

public class Player {
	public double x;
	public double y;
	public int shipid;
	public double velY = 0;
	public byte movX = 0;
	private byte speedX = 8;
	private int jumpsLeft = 1;
	
	public void tick() {
		boolean flag;
		boolean flag2 = true;
		// COLLISIONS
		double newY = y + this.velY;
		double newX = x + movX * speedX;
		this.velY -= 1;
		flag = true;
		for (int i = 0; i < 500; i++)
			for (int j = 0; j < 500; j++) {
				if(ServerData.world.ships[shipid].hull[i][j] != null){
				Rectangle r = new Rectangle(j * 32, i * 32 + 32, 32, 32);
				if (new Rectangle((int) newX, (int) y, 32, 64).intersects(r)) {
					flag = false;
					if (movX == -1) {
						this.x = r.getX() + r.getWidth();
					} else {
						this.x = r.getX() - 32;
					}
					break;
				}
				
				
				if(new Rectangle((int) x, (int)newY, 32,64).intersects(r)){
					flag2 = false;
					
					if(this.velY > 0){
					
					this.y = r.getY() - 64;
					} else {
						this.y = r.getY() + r.getHeight();
						this.jumpsLeft = 1;
					}
					this.velY = 0;
					break;
				}
				
				}
			}
		if (flag) {
			x = newX;
		} else {

			movX = 0;
		}
		if(flag2){
			y = newY;
			}
	}

	public void draw(Graphics2D g2d) {

		double transformed[] = ClientData.world.ships[shipid].transform(x, y);
		AffineTransform trans = AffineTransform.getTranslateInstance(
				transformed[0] + ClientData.world.ships[shipid].x,
				transformed[1] + ClientData.world.ships[shipid].y);
		trans.concatenate(AffineTransform.getRotateInstance(Math
				.toRadians(ClientData.world.ships[shipid].rot)));
		g2d.drawImage(ResourceContainer.images.get("/img/player/idle.png"),
				trans, null);
	}

	public void downDatePos(ByteBuffer data) {
		data.putDouble(x);
		data.putDouble(y);
		data.put(movX);
		data.putDouble(velY);
		data.putInt(shipid);
	}

	public void upDatePos(ByteBuffer data) {
		x = data.getDouble();
		y = data.getDouble();
		movX = data.get();
		velY = data.getDouble();
		shipid = data.getInt();
	}

	public void keyPress(byte[] in) {
		// First of all, let's get that integer we want
		int key = ByteBuffer.wrap(in, 3, 61).getInt();
		// Now, check whether this is a move key
		if (key == CharData.A)
			this.movX = -1;
		if (key == CharData.D)
			this.movX = 1;
		if (key == CharData.W&&this.jumpsLeft > 0){
			this.jumpsLeft --;
			this.velY = 20;
		}
	}

	public void keyRelease(byte[] in) {
		// First of all, let's get that integer we want
		int key = ByteBuffer.wrap(in, 3, 61).getInt();
		// Now, check whether this is a move key
		if (key == CharData.A && this.movX == -1)
			this.movX = 0;
		if (key == CharData.D && this.movX == 1)
			this.movX = 0;
	}
}
