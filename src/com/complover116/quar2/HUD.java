package com.complover116.quar2;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public enum HUD {
	PILOTING,
	DEFAULT;
	public void draw(Graphics2D g2d){
		switch(this) {
		case PILOTING:
			g2d.setColor(new Color(0,0,255));
			g2d.drawRect(0, 775, 800, 25);
			g2d.fillRect(0, 775, (int) (ClientData.world.ships[ClientData.world.players[ClientData.controlledPlayer].shipid].thrustX*8), 25);
			g2d.setColor(new Color(255,255,0));
			g2d.fillRect((int) (ClientData.world.ships[ClientData.world.players[ClientData.controlledPlayer].shipid].thrustX*8), 775, 10, 25);
			g2d.setFont(new Font("TimesRoman", Font.PLAIN, 30));
			g2d.setColor(new Color(255,255,255));
			g2d.drawString("Thrust:"+Math.round(ClientData.world.ships[ClientData.world.players[ClientData.controlledPlayer].shipid].thrustX)+"%", 300, 775);
			
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
