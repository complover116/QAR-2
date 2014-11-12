package com.complover116.quar2;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.sound.sampled.Clip;

public class ResourceContainer {
	public static HashMap<String, BufferedImage> images = new HashMap<String, BufferedImage>();
	public static HashMap<String, Clip> sounds = new HashMap<String, Clip>();
	public static void load() {
		ResourceLoader rsld = new ResourceLoader();
		rsld.execute();
		
	}
}
