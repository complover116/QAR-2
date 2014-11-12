package com.complover116.quar2;

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
	
}
