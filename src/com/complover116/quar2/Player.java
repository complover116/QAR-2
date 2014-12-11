package com.complover116.quar2;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.io.Serializable;
import java.nio.ByteBuffer;

public class Player implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6679268673263726581L;
	public Pos pos = new Pos();
	public HUD hud = HUD.DEFAULT;
	public int color = 1;
	public Leg leftLeg;
	public Leg rightLeg;
	public Arm rightArm;
	public Arm leftArm;
	public Pos leg2pos = new Pos();
	public Pos leg1pos = new Pos();
	public double bodyYoffcet =0;
	public int shipid;
	public double velY = 0;
	public byte movX = 0;
	private byte speedX = 4;
	private int jumpsLeft = 100;
	public int animation = 0;
	public int anim = 0;
	public int leganim = 20;
	public boolean leganimstat = false;
	public int time = 0;
	public World world;
	public Usable using = null;
	public boolean onGround = false;
	public boolean looksright = true;
	public Usable getUsable() {
		System.out.println("Loooking for usables...");
		for(int i = 0; i < ServerData.world.ships[shipid].objects.size(); i ++){
			if(ServerData.world.ships[shipid].objects.get(i) instanceof Usable) {
				System.out.println("Found a usable!");
				if(ServerData.world.ships[shipid].objects.get(i).pos.distance(this.pos) < 32) return (Usable) ServerData.world.ships[shipid].objects.get(i);
			}
		}
		return null;
	}
	public void use(Usable toUse) {
		System.out.println("Using");
		toUse.onUse(this);
		this.using = toUse;
	}
	public Player(World wrld, double x, double y) {
		world = wrld;
		this.pos.x = x;
		this.pos.y = y;
		this.leftLeg = new Leg(this, false);
		this.rightLeg = new Leg(this, true);
		this.leftArm = new Arm(this, false);
		this.rightArm = new Arm(this, true);
	}
	public void unUse() {
		System.out.println("UnUsing");
		this.using.onExit(this);
		this.using = null;
	}
	void leganim(int offset) {
		if(world.isRemote)
		if(onGround){
		if(leganimstat){
			leganim -=offset;
		} else {
			leganim +=offset;
		}
		if(leganim>52||leganim<15){
			SoundHandler.playSound("/sound/effects/step/metal"+((int)(Math.random()*2)+1)+".wav");
			leganimstat = !leganimstat;
			if(leganim>52)leganim = 52;
			if(leganim<15)leganim = 15;
		}
		}
	}
	public void tick() {
		leganim(movX*speedX);
		time ++;
		boolean flag;
		boolean flag2 = true;
		if(!world.isRemote){
		// COLLISIONS
		double newY = pos.y + this.velY;
		double newX = pos.x + movX * speedX;
		this.velY -= 1;
		flag = true;
		for (int i = 0; i < 500; i++)
			for (int j = 0; j < 500; j++) {
				if(ServerData.world.ships[shipid].hull[i][j] != null){
				Rectangle r = new Rectangle(i * 32, j * 32 + 32, 32, 32);
				if (new Rectangle((int) newX, (int) pos.y, 32, 64).intersects(r)) {
					flag = false;
					if (movX == -1) {
						pos.x = r.getX() + r.getWidth();
					} else {
						pos.x = r.getX() - 32;
					}
					break;
				}
				
				
				if(new Rectangle((int) pos.x, (int)newY, 32,64).intersects(r)){
					flag2 = false;
					this.onGround = true;
					if(this.velY > 0){
					
					pos.y = r.getY() - 64;
					} else {
						pos.y = r.getY() + r.getHeight();
						this.jumpsLeft = 1;
					}
					this.velY = 0;
					break;
				}
				
				}
			}
		if (flag) {
			pos.x = newX;
		} else {

			movX = 0;
		}
		if(flag2){
			this.onGround = false;
			pos.y = newY;
			}
		}
		if(world.isRemote){
		//LIMBS TICK
		this.leftLeg.tick();
		this.rightLeg.tick();
		rightArm.tick();
		leftArm.tick();
		}
	}

	public void draw(Graphics2D g2d) {
		double transformed[] = ClientData.world.ships[shipid].transform(pos.x, pos.y);
		AffineTransform trans = AffineTransform.getTranslateInstance(
				transformed[0] + ClientData.world.ships[shipid].x,
				transformed[1] + ClientData.world.ships[shipid].y);
		trans.concatenate(AffineTransform.getRotateInstance(Math
				.toRadians(ClientData.world.ships[shipid].rot)));
		g2d.drawImage(ResourceContainer.images.get("/img/player/idle.png-"+ResourceLoader.colnames[color]),
				trans, null);
		leftLeg.draw(g2d);
		rightLeg.draw(g2d);
		rightArm.draw(g2d);
		leftArm.draw(g2d);
		/*if(hud == HUD.PILOTING) {
			double transformed2[] = ClientData.world.ships[shipid].transform(pos.x, pos.y+32);
			AffineTransform trans2 = AffineTransform.getTranslateInstance(
					transformed2[0] + ClientData.world.ships[shipid].x,
					transformed2[1] + ClientData.world.ships[shipid].y);
			trans.concatenate(AffineTransform.getRotateInstance(Math
					.toRadians(ClientData.world.ships[shipid].rot)));
			g2d.drawImage(ResourceContainer.images.get("/img/loadAnim_small.png"),
					trans2, null);
		}*/
	}

	public void downDatePos(ByteBuffer data) {
		pos.put(data);
		data.put(movX);
		data.putDouble(velY);
		data.putInt(shipid);
		data.putInt(anim);
		if(onGround){
		data.put((byte) 1);
		} else {
			data.put((byte) 0);
		}
		data.putInt(hud.toNumber());
	}

	public void upDatePos(ByteBuffer data) {
		pos.read(data);
		movX = data.get();
		velY = data.getDouble();
		shipid = data.getInt();
		anim = data.getInt();
		if(data.get() == 0){
			onGround = false;
		}else {
			onGround = true;
		}
		hud = HUD.fromNumber(data.getInt());
	}

	public void keyPress(byte[] in) {
		// First of all, let's get that integer we want
		int key = ByteBuffer.wrap(in, 3, 61).getInt();
		//Handle usables first - we don't want to be stuck in a panel, do we?
		if(key == CharData.Space) {
			if(this.using == null){
			Usable sos = this.getUsable();
			if(sos != null) {
				this.use(sos);
			}
			} else {
				this.unUse();
			}
		}
		//Then, if we are usiong something, let's channel our keys to the Usable
		if(this.using != null) {
			this.using.keyPress(key);
			return;
		}
		// Now, check whether this is a move key
		if (key == CharData.A){
			this.movX = -1;
			if(looksright){
		looksright = false;
		leftArm.offsetX = -15.5;
		rightArm.offsetX = 15.5;
		leftArm.offsetY = -48;
		rightArm.offsetY = -48;
			}
		}
		if (key == CharData.D){
			this.movX = 1;
			if(!looksright){
				looksright = true;
				leftArm.offsetX = -15.5;
				rightArm.offsetX = 15.5;
				leftArm.offsetY = -48;
				rightArm.offsetY = -48;
			}
		}
		if (key == CharData.W&&this.jumpsLeft > 0){
			this.jumpsLeft --;
			this.velY = 20;
		}
		
	}

	public void keyRelease(byte[] in) {
		// First of all, let's get that integer we want
		int key = ByteBuffer.wrap(in, 3, 61).getInt();
		//Then, if we are usiong something, let's channel our keys to the Usable
		if(this.using != null) {
			this.using.keyRelease(key);
			return;
		}
		// Now, check whether this is a move key
		if (key == CharData.A && this.movX == -1)
			this.movX = 0;
		if (key == CharData.D && this.movX == 1)
			this.movX = 0;
	}
}
