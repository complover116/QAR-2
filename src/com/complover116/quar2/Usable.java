package com.complover116.quar2;

import java.awt.Graphics2D;

public interface Usable {
	public void keyPress(int key);
	public void keyRelease(int key);
	public void onUse(Player ply);
	public void onExit(Player ply);
	public void drawHUD(Graphics2D g2d);
}
