package com.complover116.quar2;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class EnginesPanel extends Panel {

	public EnginesPanel(Ship sh, double x, double y, double rot) {
		super(sh, x, y, rot);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void keyPress(int key) {
		// TODO Auto-generated method stub
		if (key == CharData.A)
			this.ship.velRot -= 0.2;
		if (key == CharData.D)
			this.ship.velRot += 0.2;
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
				ResourceContainer.images.get("/img/systems/panels/engines.png"),
				tr, null);
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

}
