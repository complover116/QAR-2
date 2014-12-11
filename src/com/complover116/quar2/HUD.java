package com.complover116.quar2;

import java.awt.Color;
import java.awt.Graphics2D;

public enum HUD {
	PILOTING,
	DEFAULT;
	public void draw(Graphics2D g2d){
		switch(this) {
		case PILOTING:
			g2d.setColor(new Color(0,0,255));
			g2d.fillRect(0, 0, 200, 200);
		break;
		default:
		break;
		}
	}
	public int toNumber() {
		switch(this){
		case PILOTING:return 1;
		default:return 0;
		}
	}
	public static HUD fromNumber(int num) {
		switch(num){
		case 1:return PILOTING;
		default:return DEFAULT;
		}
	}
}
