package com.complover116.quar2;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

public class Render extends JPanel implements KeyListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1224246674901715075L;
	public static double lrot = 0;
	
	public static String loadStep = "Waiting for user";
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(new Color(0,0,0));
		if(!Loader.initialized) {
			lrot+=5;
			if(lrot > 360) lrot = lrot - 360;
			AffineTransform tr = AffineTransform.getTranslateInstance(350,300);
			tr.concatenate(AffineTransform.getRotateInstance(Math.toRadians(lrot), 64, 64));
			g2d.drawImage(ResourceContainer.images.get("/img/loadAnim.png"), tr, null);
			if(lrot <= 180) {
			g2d.setColor(new Color((int)((double)lrot/180*255), 0, 255 - (int)((double)lrot/180*255)));
			} else {
				g2d.setColor(new Color((int)((double)(360-lrot)/180*255), 0, 255 - (int)((double)(360-lrot)/180*255)));	
			}
			g2d.setFont(new Font("TimesRoman", Font.PLAIN, 30));
			g2d.drawString(loadStep, 280, 500);
		} else {
			
			Ship shish = ClientData.world.ships[ClientData.world.players[ClientData.controlledPlayer].shipid];
			//HUD RENDERING
			
			
			//USABLE HUD
			if(ServerData.world.players[ClientData.controlledPlayer].using != null){
				ServerData.world.players[ClientData.controlledPlayer].using.drawHUD(g2d);
			}
			
			try{
			//CAMERA POSITIONING
			
			double transformed[] = shish.transform(ClientData.world.players[ClientData.controlledPlayer].pos.x, ClientData.world.players[ClientData.controlledPlayer].pos.y);
			g2d.transform(AffineTransform.getRotateInstance(Math.toRadians(-ClientData.world.ships[ClientData.world.players[ClientData.controlledPlayer].shipid].rot), 400, 400));
			g2d.transform(AffineTransform.getTranslateInstance(-transformed[0] - shish.x + 400, -transformed[1] - shish.y+ 400));
			} catch (NullPointerException e) {
				System.out.println("Camera positioning failed, possibly no data from server yet");
			}
			
			//HERE GOES NOTHING
			//*No, really, it's about drawing the void of space
			//**And stars
			g2d.setBackground(new Color(0,0,0));
			for(int i = (int) (shish.x/100 - 10); i < shish.x/100 + 10; i ++) {
				for(int j = (int) (shish.y/100 - 10); j < shish.y/100 + 10; j ++) {
					try{
					if(ClientData.background[i][j] != null){
						ClientData.background[i][j].draw(g2d);
					} else {
						ClientData.background[i][j] = new BGObject(i*100, j*100);
					}
					} catch(ArrayIndexOutOfBoundsException e) {
						
					}
				}
			}
			
			//HERE GOES THE UNIVERSE
			for(int i = 0; i < Config.maxShips; i ++) {
				if(ClientData.world.ships[i] != null) {
					ClientData.world.ships[i].draw(g2d);
				}
			}
			
			for(int i = 0; i < Config.maxShips; i ++) {
				if(ClientData.world.players[i] != null) {
					ClientData.world.players[i].draw(g2d);
				}
			}
			
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		System.out.println(e.getKeyCode());
		ClientThread.sendKey(e.getKeyCode(), true);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		ClientThread.sendKey(e.getKeyCode(), false);
	}
}
