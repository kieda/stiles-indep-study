package edu.cmu.ideate.zkieda.viz.graphics;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Iterator;
import java.util.stream.IntStream;

import edu.cmu.ideate.zkieda.viz.core.Renderable;
import edu.cmu.ideate.zkieda.viz.core.Updateable;

public class Trip implements ImageProcessor, Updateable{
    
    private float alpha = 2f;//254.5f;
    private float deltaAlpha = 0f;//1f/255f;//1f;
    private float lambda = .35f;
    private float deltaLambda = 0.000f;
    
    private ImagePow ip;
    
    public enum GradientType{
    	QUADRATIC, 
    	LINEAR,
    	NONE
    }
    private GradientType gradient = GradientType.QUADRATIC;
    
    private boolean darken = false;
    
    @Override
    //todo opencl for optimization
    public void process(int[] pixels, int width, int height) {
        //creates an image the size the size of the screen
    	Point2D pmid = new Point2D.Float(width/2.0f, height/2.0f);
    	
    	
    	if(ip == null) {
        	 ip = new ImagePow((x, y) -> pmid.distanceSq(x, y), 14, width, height);
        }
    	
    	ip.refactor(width, height);
    	
        final float screenLength = (float)(pmid.distanceSq(0, 0));
        final double alphaPrime = alpha * Math.pow(screenLength, 1. - lambda);
        IntStream.range(0, width*height)
        	.parallel()
        	.forEach(idx -> {
        		int yPix = idx / width;
        		int xPix = idx % width;
//        		double x = (xPix - width/2.0)/ mag + width/2.0;
//        		double y = (yPix - height/2.0)/ mag + height/2.0;
        		double x = xPix;
        		double y = yPix;
        		
        		double dist = pmid.distanceSq(x, y);
        		
        		double sample = alphaPrime*Math.pow(dist, lambda) % 255;

        		switch(this.gradient) {
	        		case LINEAR:
	        			if(sample >= 127.5){
	            			sample = 255. - sample;
	            		}
	        			break;
	        		case QUADRATIC:
	        			sample = sample * (255 - sample) *4./255.;
	        			
	        			break;
	        		case NONE:
        		}

        		
        		if(darken){
        			float rel = (float)(1.0-pmid.distanceSq(x, y)/screenLength);
        			sample *= rel;
        		}
        		
        		byte sam = (byte)sample;
//        		if(y == 525){
//        			if(x == 840 + 11 || x == 840 + 12){
//        				System.out.println("x " + x + " "+ sample + " " + sam);
//        			}
//        		}
                byte a = (byte)0xFF;
                byte r = (byte)sam;
                byte g = (byte)(sam);
                byte b = (byte)(sample/1.5);

                int pix = (((((int)a)<<24) & 0xFF00_0000)|
                        ((((int)r)<<16) & 0x00FF_0000)|(b & 0x0000_00FF)|
                        (((int)g)<<8) & 0x0000_FF00);
                
                pixels[yPix*width + xPix] = pix;
        		
        	});
    }
    
    private double time = 0;
    @Override
    public void update(float dt) {
		alpha += deltaAlpha* dt * .1;
		time += dt;
    	lambda += deltaLambda; //(float)(1.5 + .5 * Math.sin(1.5 * time) + time * .05);
    }
    
    public float getAlpha() {
		return alpha;
	}
    
    public float getDeltaAlpha() {
		return deltaAlpha;
	}
    public float getDeltaLambda() {
		return deltaLambda;
	}
    public float getLambda() {
		return lambda;
	}
    
    
    public void setAlpha(float alpha) {
		this.alpha = alpha;
	}
    
    public void setDeltaAlpha(float deltaAlpha) {
		this.deltaAlpha = deltaAlpha;
	}
    
    public void setLambda(float lambda) {
		this.lambda = lambda;
	}
    
    public void setDeltaLambda(float deltaLambda) {
		this.deltaLambda = deltaLambda;
	}
    
    public void setDarken(boolean darken) {
		this.darken = darken;
	}
    
    public void setGradient(GradientType gradient) {
		this.gradient = gradient;
	}
}
