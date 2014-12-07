package com.complover116.quar2;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.Serializable;

public class BGObject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7795050939649665915L;
	public int x;
	public int y;
	public BGObject(int x, int y) {
		//System.out.println("New BGObject created at x:"+x+" y:"+y);
		this.x = (int) (x + Math.random() * 200 - 100);
		this.y = (int) (y + Math.random() * 200 - 100);
	}
	public void draw(Graphics2D g2d) {
		g2d.setColor(new Color(255,255,255));
		int radius = (int)(Math.random()*2.5+2.5);
		g2d.fillOval(x - radius, y - radius, radius*2, radius*2);
	}
}
