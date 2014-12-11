package com.complover116.quar2;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.io.Serializable;

public class EnginesPanel extends Panel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2104368842861905993L;
	public byte rot = 0;
	public byte thrust = 0;
	public double goalThrust = 0;
	public EnginesPanel(Ship sh, double x, double y, double rot) {
		super(sh, x, y, rot);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void keyPress(int key) {
		// TODO Auto-generated method stub
		if (key == CharData.A)
			this.rot = -1;
		if (key == CharData.D)
			this.rot = 1;
		if (key == CharData.S&&this.goalThrust > 0){
			this.goalThrust -= 10;
		}
		if (key == CharData.W&&this.goalThrust < 100){
			this.goalThrust += 10;
		}
	}

	@Override
	public void keyRelease(int key) {
		// TODO Auto-generated method stub
		if (key == CharData.A&&this.rot == -1)
			this.rot = 0;
		if (key == CharData.D&&this.rot == 1)
			this.rot = 0;
	}

	public void draw(Graphics2D g2d) {
		double res[] = absPos();
		AffineTransform tr = AffineTransform.getTranslateInstance(res[0],
				res[1]);
		tr.concatenate(AffineTransform.getRotateInstance(Math.toRadians(res[2])));
		g2d.drawImage(
				ResourceContainer.images.get("/img/systems/panels/engines.png"),
				tr, null);
	}

	@Override
	public void tick() {
		ship.velRot+=(double)this.rot/ship.mass;
		if(ship.thrustX < this.goalThrust) {
			ship.thrustX += 0.5;
		}
		if(ship.thrustX > this.goalThrust) {
			ship.thrustX -= 0.5;
		}
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

}
