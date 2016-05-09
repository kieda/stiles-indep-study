package edu.cmu.ideate.zkieda.viz.graphics;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import edu.cmu.ideate.zkieda.viz.core.Renderable;

public class ImageRenderable implements Renderable{
	private ImageProcessor processor;
	private int[] buffer;
	private int width;
	private int height;
	private BufferedImage image;
	
	public ImageRenderable(ImageProcessor processor, int w, int h){
		this.processor = processor;
		this.width = w;
		this.height = h;
		this.buffer = new int[w*h];
		this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}
	
	@Override
	public void render(Graphics2D g) {
		processor.process(buffer, width, height);
		image.setRGB(0, 0, width, height, buffer, 0, width);
		g.drawImage(image, 0, 0, null);
	}
	
	public BufferedImage getImage(){
		return image;
	}
}
