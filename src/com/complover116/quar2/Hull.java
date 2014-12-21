package com.complover116.quar2;

import java.io.Serializable;

public class Hull implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8975352098495523228L;
	public byte health = 100;
	public byte type = 0;
	public byte skin = 1;
	public Hull(int skinNum) {
		this.skin = (byte) ((byte)(Math.random()*skinNum) + 1);
	}
	public String getImagePath() {
		return "/img/hull/metal"+skin+".png";
	}
	public byte[] downDate() {
		byte b[] = new byte[60];
		b[0] = health;
		b[1] = type;
		b[2] = skin;
		return b;
	}
	public Hull(byte[] b) {
		this.health = b[0];
		this.type = b[1];
		this.skin = b[2];
	}
}
