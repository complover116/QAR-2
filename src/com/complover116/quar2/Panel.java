package com.complover116.quar2;

public abstract class Panel extends ShipJect implements Usable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4215459859016303616L;
	public Panel(Ship sh, double x, double y, double rot) {
		super(sh, x, y, rot);
		// TODO Auto-generated constructor stub
	}

	public void onUse(Player ply) {
		ply.using = this;
		ply.pos.x = this.pos.x - 24;
		ply.pos.y = this.pos.y;
		ply.anim = 1;
	}
	public void onExit(Player ply) {
		ply.using = null;
		ply.anim = 0;
		ply.animation = 0;
	}

}
