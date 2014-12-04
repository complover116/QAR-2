package com.complover116.quar2;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Leg extends Limb {
	boolean right = false;
	Rectangle dr = new Rectangle(0,0,1,1);
	Rectangle dr2 = new Rectangle(0,0,1,1);
	public double velY = 0;
	public byte state = 0;
	public Leg(Player ply, boolean right) {
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
	public void tick(){
		super.tick();

		x2 = player.pos.x+16;
		y2 = player.pos.y-40;
		face(player.looksright);
		if(player.anim == 0&&player.movX != 0){
		if(player.leganimstat == this.right){
			end.y = player.pos.y - 64;
		} else {
			end.y = player.pos.y - 54;
		}
		
		if(right) {
		end.x = player.pos.x -26+ player.leganim;
		} else {
			end.x = player.pos.x +52 - player.leganim;
		}
		} else {
			end.y = player.pos.y - 64;
			if(right) {
				end.x = player.pos.x + 24;
				} else {
					end.x = player.pos.x + 8;
				}
		}
	}
	public void draw(Graphics2D g2d){
		super.draw(g2d);
		g2d.setColor(new Color(255,0,0));
		rtsandr(new Rectangle((int)x1-2, (int) (y1-2), 4, 4), g2d);
		g2d.setColor(new Color(0,255,0));
		rtsandr(new Rectangle((int)x2-2, (int) (y2-2), 4, 4), g2d);
	}
	public void face(boolean trright) {
		faceright = trright;
	}
}
