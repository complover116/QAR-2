package com.complover116.quar2;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.io.Serializable;
import java.nio.ByteBuffer;

public class WeaponsPanel extends Panel implements Serializable {
	public Weapon weapon = new BasicWeapon(this);
	public byte turn = 0;
	/**
	 * 
	 */
	private static final long serialVersionUID = -2104368842861905993L;
	public WeaponsPanel(Ship sh, double x, double y, double rot) {
		super(sh, x, y, rot);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void keyPress(int key) {
		if(key == CharData.D) {
			if(weapon!=null) {
				this.weapon.onFire();
			}
		}
		if(key == CharData.W){
			turn = -1;
		}
		if(key == CharData.S){
			turn = 1;
		}
	}

	@Override
	public void keyRelease(int key) {
		if(key == CharData.W&&turn == -1){
			turn = 0;
		}
		if(key == CharData.S&&turn == 1){
			turn = 0;
		}
	}

	public void draw(Graphics2D g2d) {
		double res[] = absPos();
		AffineTransform tr = AffineTransform.getTranslateInstance(res[0],
				res[1]);
		tr.concatenate(AffineTransform.getRotateInstance(Math.toRadians(res[2])));
		g2d.drawImage(
				ResourceContainer.images.get("/img/systems/weapons/mk1.png"),
				tr, null);
		if(weapon!=null) {
			this.weapon.draw(g2d);
		}
	}

	@Override
	public void tick() {
		if(weapon!=null) {
			this.weapon.tick();
		}
		if(weapon.rot > -90&&turn == -1) weapon.rot --;
		if(weapon.rot < 90&&turn == 1) weapon.rot ++;
	}

	@Override
	public void drawHUD(Graphics2D g2d) {
		if(ship.thrustX > 0) {
			g2d.setColor(new Color(0,0,255));
			g2d.drawRect(400, 760, 40, (int) (400*ship.thrustX));
		}
	}
	
	public void onUse(Player ply) {
		super.onUse(ply);
		ply.hud = HUD.PILOTING;
	}
	public void onExit(Player ply) {
		super.onExit(ply);
		ply.hud = HUD.DEFAULT;
	}
	@Override
	public boolean shouldSendTickData() {
		if(this.weapon.shouldSendData()) return true;
		return false;
	}
	@Override
	public void tickDataDown(ByteBuffer b) {
		this.weapon.downDate(b);
	}
	@Override
	public void tickDataUp(ByteBuffer b) {
		this.weapon.upDate(b);
	}
	public Pos getMountPos() {
		double res[] = this.ship.realtransform(pos.x+64, pos.y-30);
		return new Pos(res[0],res[1]);
	}
}
