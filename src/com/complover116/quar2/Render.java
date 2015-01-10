package com.complover116.quar2;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;

import javax.swing.JPanel;

public class Render extends JPanel implements KeyListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1224246674901715075L;
	public static double lrot = 0;
	public static double loadspeed = 5;
	public static String loadStep = "Waiting for user";
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(new Color(0,0,0));
		if(!Loader.initialized) {
			lrot+=loadspeed;
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
			
			Ship shish = null;
			
			
			try{
			//CAMERA POSITIONING
			shish = ClientData.world.ships[ClientData.world.players[ClientData.controlledPlayer].shipid];
			
			AffineTransform transform = null;
			if(ClientData.world.players[ClientData.controlledPlayer].hud == HUD.DEFAULT){
				double transformed[] = shish.transform(ClientData.world.players[ClientData.controlledPlayer].pos.x, ClientData.world.players[ClientData.controlledPlayer].pos.y);
				transform = AffineTransform.getRotateInstance(Math.toRadians(-ClientData.world.ships[ClientData.world.players[ClientData.controlledPlayer].shipid].rot), 400, 400);
				transform.concatenate(AffineTransform.getTranslateInstance(-transformed[0] - shish.x + 400, -transformed[1] - shish.y+ 400));
			}
			if(ClientData.world.players[ClientData.controlledPlayer].hud == HUD.PILOTING){
				double transformed[] = shish.transform(shish.massX-shish.velX*10, shish.massY-shish.velY*10);
				transform = AffineTransform.getScaleInstance(0.25,0.25);
				//transform.concatenate(AffineTransform.getRotateInstance(Math.toRadians(-ClientData.world.ships[ClientData.world.players[ClientData.controlledPlayer].shipid].rot), 400, 400));
				transform.concatenate(AffineTransform.getTranslateInstance(-transformed[0] - shish.x + 1600, -transformed[1] - shish.y + 1600));
			}
			g2d.transform(transform);
			
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
			g2d.setColor(new Color(0,255,0));
			/*//DEBUG GRID
			for(int i = 0; i < 100; i ++) {
				g2d.drawLine(0, i*100, 10000, i*100);
			}*/
			//HERE GOES THE UNIVERSE
			for(int i = 0; i < Config.maxShips; i ++) {
				if(ClientData.world.ships[i] != null) {
					ClientData.world.ships[i].draw(g2d);
				}
			}
			
			for(int i = 0; i < Config.maxPlayers; i ++) {
				if(ClientData.world.players[i] != null) {
					ClientData.world.players[i].draw(g2d);
				}
			}
			for(int i = 0; i < Config.maxObjects; i ++) {
				if(ClientData.world.objects[i] != null) {
					ClientData.world.objects[i].draw(g2d);
				}
			}
			//CAMERA DISPOSITIONING
			g2d.transform(transform.createInverse());
			//HUD RENDERING
			if(ClientThread.timeout > ClientThread.timeoutLow) {
				g2d.setColor(new Color(255,0,0));
				g2d.setFont(new Font("TimesRoman", Font.PLAIN, 30));
				g2d.drawString("WARNING:No message from server for "+ClientThread.timeout/10+" seconds", 100, 500);
			}
			
			//USABLE HUD
			ClientData.world.players[ClientData.controlledPlayer].hud.draw(g2d);
			} catch (NullPointerException e) {
				System.out.println("No data from server yet - skipping render frames");
			} catch (NoninvertibleTransformException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		//System.out.println(e.getKeyCode());
		ClientThread.sendKey(e.getKeyCode(), true);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		ClientThread.sendKey(e.getKeyCode(), false);
	}
}
