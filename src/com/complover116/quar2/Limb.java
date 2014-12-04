package com.complover116.quar2;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

public class Limb {
	public Player player;
	public byte forcebend = 0;
	public Pos end = new Pos();
	boolean faceright = true;
	public double x1 = 40;
	public double y1 = 20;
	public double x2 = 40;
	public double y2 = 20;
	public final int segLength = 13;
	public Limb(Player ply) {
		player = ply;
	}
	public void tick() {

	}
	public void draw(Graphics2D g2d) {
		  
		  double dx = end.x - x1;
		  double dy = end.y - y1;
		  double angle1 = Math.atan2(dy, dx);  
		  
		  double tx = end.x - Math.cos(angle1) * segLength;
		  double ty = end.y - Math.sin(angle1) * segLength;
		  dx = tx - x2;
		  dy = ty - y2;
		  double angle2 = Math.atan2(dy, dx);
		  if(angle2>angle1&&!faceright) {
			  angle2-=Math.toRadians(10);
		  }
		  if(angle2<angle1&&faceright) {
			  angle2+=Math.toRadians(10);
		  }
		  x1 = x2 + Math.cos(angle2) * segLength;
		  y1 = y2 + Math.sin(angle2) * segLength;
		  
		  segment(x1, y1, angle1, g2d); 
		  segment(x2, y2, angle2, g2d); 
		  g2d.setColor(new Color(255,255,0));
		 // rtsandr(new Rectangle((int)end.x,(int)end.y,4,4), g2d);
		 }
	void segment(double x, double y, double angle, Graphics2D g2d){
		double transformed[] = ClientData.world.ships[player.shipid].transform(x, y);
		AffineTransform trans = AffineTransform.getTranslateInstance(
				transformed[0] + ClientData.world.ships[player.shipid].x,
				transformed[1] + ClientData.world.ships[player.shipid].y);
		trans.concatenate(AffineTransform.getRotateInstance(Math
				.toRadians(ClientData.world.ships[player.shipid].rot)));
		trans.concatenate(AffineTransform.getRotateInstance(-angle, 0, 1.5));
		g2d.drawImage(ResourceContainer.images.get("/img/player/limb2.png"),
				trans, null);
	}
	void rtsandr(Rectangle r, Graphics2D g2d) {
		
		  double transformed[] = ClientData.world.ships[player.shipid].transform(r.x, r.y);
		  g2d.fillRect((int)(transformed[0]+ClientData.world.ships[player.shipid].x), (int)(transformed[1]+ClientData.world.ships[player.shipid].y), r.width, r.height);
	}
}
