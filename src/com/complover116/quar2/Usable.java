package com.complover116.quar2;

public interface Usable {
	public void keyPress(int key);
	public void keyRelease(int key);
	public void onUse(Player ply);
	public void onExit(Player ply);
}
