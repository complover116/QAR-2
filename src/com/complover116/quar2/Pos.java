package com.complover116.quar2;

import java.io.Serializable;
import java.nio.ByteBuffer;


/**
 * Class representing a 2D Vector
 * @author complover116
 *
 */
public class Pos implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 238250628170350204L;
	public double x;
	public double y;
	
	public Pos() {
		
	}
	public Pos(double X, double Y) {
		x = X;
		y = Y;
	}
	public void put(ByteBuffer data) {
		data.putDouble(this.x);
		data.putDouble(this.y);
	}
	public void read(ByteBuffer data) {
		this.x = data.getDouble();
		this.y = data.getDouble();
	}
	public Pos(double X, double Y, boolean f) {
		x = X*16+8;
		y = Y*16+8;
	}
	public Pos(ByteBuffer data) {
		this.x = data.getDouble();
		this.y = data.getDouble();
	}
	public double distance(Pos pos2) {
		double deltaX = pos2.x - this.x;
		double deltaY = pos2.y - this.y;
		double res = Math.sqrt(deltaX*deltaX+deltaY*deltaY);
		return res;
	}
	public Pos sub(Pos pos2) {
		return new Pos(this.x - pos2.x,this.y - pos2.y);
	}
	public Pos add(Pos pos2) {
		return new Pos(this.x + pos2.x,this.y + pos2.y);
	}
	public Pos add2(Pos pos2) {
		return new Pos(this.x + pos2.y,this.y + pos2.x);
	}
	public Pos mul(double i ) {
		return new Pos(this.x*i,this.y*i);
	}
	public Pos normal() {
		double newX = Math.cos(Math.atan2(this.x, this.y));
		double newY = Math.sin(Math.atan2(this.x, this.y));
		return new Pos(newX, newY);
	}
}
