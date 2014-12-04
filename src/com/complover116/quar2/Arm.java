package com.complover116.quar2;

import java.awt.Rectangle;

public class Arm extends Limb{
	int time = 0;
	boolean right = false;
	double goalx;
	double goaly;
	double offsetX;
	double offsetY;
	double moveSpeed = 1;
	Rectangle dr = new Rectangle(0,0,1,1);
	Rectangle dr2 = new Rectangle(0,0,1,1);
	public double velY = 0;
	public byte state = 0;
	public Arm(Player ply, boolean right) {
		super(ply);
		if(right){
		end.y = 160;
		end.x = ply.pos.x +24;
		} else {
			end.y = 160;
			end.x = ply.pos.x +8;
		}
		this.right = right;
	}
	public void tick() {
		super.tick();
		
		x2 = player.pos.x+15.5;
		y2 = player.pos.y-18;
		if(offsetY>goaly){
			offsetY -= moveSpeed;
		}
		if(offsetX>goalx){
			offsetX -= moveSpeed;
		}
		
		if(offsetY<goaly){
			offsetY += moveSpeed;
		}
		if(offsetX<goalx){
			offsetX += moveSpeed;
		}
		
		if(player.anim == 0) {
			this.moveSpeed = 2;
			if(player.movX == 1){
				goaly = -28;
				if(this.right==player.leganimstat) {
					goalx = 16;
				} else {
					goalx = 0;
				}
			} else if(player.movX == -1){
				goaly = -28;
				if(this.right==player.leganimstat) {
					goalx = -16;
				} else {
					goalx = 0;
				}
			} else {
				goaly = -48;
				if(right){
					goalx = 15.5;
				} else {
					goalx = - 15.5;
				}
			}
		} else if (player.anim == 1){
			this.moveSpeed = 1;
			time ++;
			if(time > 30&&Math.random()>0.95){
				time = 0;
				double offset = Math.random()*20;
				goaly = -50+offset;
				goalx = 32.5+offset;
			}
			if(time == 15) {
				goalx = 10;
				goaly = -30;
			}
			
		}
		end.x = player.pos.x + 15.5 + offsetX;
		end.y = player.pos.y + offsetY;
	}
}
