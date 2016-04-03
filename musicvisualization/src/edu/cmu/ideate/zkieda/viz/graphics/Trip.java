package edu.cmu.ideate.zkieda.viz.graphics;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import edu.cmu.ideate.zkieda.viz.core.Renderable;
import edu.cmu.ideate.zkieda.viz.core.Updateable;

public class Trip implements ImageProcessor, Updateable{
	private int j = 0;
    private boolean stage  = true;
    private float cc = .0001f;
    
    @Override
    public void process(int[] pixels, int width, int height) {
        //creates an image the size the size of the screen
        
    	Point2D pmid = new Point2D.Float(width/2.0f, height/2.0f);
        float screenLength = (float)(pmid.distanceSq(0, 0));
        
        for(int y = 0; y < height ; y++){ 
            for(int x = 0; x < width; x ++){
                float rel = (float)(1.0-pmid.distanceSq(x, y)/screenLength);
                
                //sam is the sample byte used
                float dist = (float)pmid.distanceSq(x, y);
                byte sam = (byte)(cc*dist);
               
                sam = (byte)(Math.max((sam)*rel, 0));
                
                byte a = (byte)0xFF;
                byte r = (byte)sam;
                byte g = (byte)(sam);
                byte b = (byte)(sam/1.5);

                int pix = ((((int)a)<<24)|
                        (((int)r)<<16)|(((int)b))|
                        (((int)g)<<8));
                
                pixels[y*width + x] = pix;
            }
        }
    }
    
    @Override
    public void update() {
    	if(stage){
            if(cc<.12){
                cc += .0005f;
            } else if(cc<.15){
                cc += .0001f;
            } else if(cc<.2){
                cc += .001f;
            } else cc += .00001f + .00001f*(j++);
            
            if(cc>2000d){
                stage = false;
            }
        } else{
            cc -= .00001f + .00001f*(j*j--);
            if(cc<-2000d) stage = true;
        }
    }
}
