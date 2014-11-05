package com.complover116.quar2;

public class Hull {
	public int health = 100;
	public int type = 0;
	public int skin = 1;
	public Hull(int skinNum) {
		this.skin = (int)(Math.random()*skinNum) + 1;
	}
	public String getImagePath() {
		return "/img/hull/metal"+skin+".png";
	}
}
