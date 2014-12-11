package com.complover116.quar2;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.sound.sampled.AudioFormat;

public class ResourceContainer {
	public static AudioFormat format;
	public static HashMap<String, BufferedImage> images = new HashMap<String, BufferedImage>();
	public static HashMap<String, byte[]> sounds = new HashMap<String, byte[]>();
	public static void load() {
		ResourceLoader rsld = new ResourceLoader();
		rsld.execute();
		
	}
}
