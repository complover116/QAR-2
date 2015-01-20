package com.complover116.quar2;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.io.Serializable;

public class WeaponsPanel extends Panel implements Serializable {

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
		// TODO Auto-generated method stub
	}

	@Override
	public void keyRelease(int key) {
		// TODO Auto-generated method stub
	}

	public void draw(Graphics2D g2d) {
		double res[] = absPos();
		AffineTransform tr = AffineTransform.getTranslateInstance(res[0],
				res[1]);
		tr.concatenate(AffineTransform.getRotateInstance(Math.toRadians(res[2])));
		g2d.drawImage(
				ResourceContainer.images.get("/img/systems/weapons/mk1.png"),
				tr, null);
	}

	@Override
	public void tick() {
		
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
		//ply.hud = HUD.PILOTING;
	}
	public void onExit(Player ply) {
		super.onExit(ply);
		//ply.hud = HUD.DEFAULT;
	}

}
