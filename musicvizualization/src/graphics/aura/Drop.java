/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics.aura;

import graphics.Renderable;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.awt.image.MemoryImageSource;
import java.awt.image.WritableRaster;
import java.util.Random;
import javax.swing.text.Segment;
import struct.cellular.CellAutomaton;
//import util.acceleration.CLRunner2;
import util.console.Console;
import util.constants.GConstants;

/**
 *
 * @author kieda
 */
public class Drop implements Renderable{
    public static ptV p1 = new ptV(GConstants.SCREEN_WIDTH/2-100, GConstants.SCREEN_HEIGHT/2 - 100, 0);
    public static ptV p2 = new ptV(GConstants.SCREEN_WIDTH/2, GConstants.SCREEN_HEIGHT/2, -100);
//    public static ptV p1 = new ptV(GConstants.SCREEN_WIDTH/2, GConstants.SCREEN_HEIGHT/2 - 100, 300);
//    public static ptV p2 = new ptV(GConstants.SCREEN_WIDTH/2, GConstants.SCREEN_HEIGHT/2 + 100, 50);
    public static long exectime = 0;
//    final double dist = p1.size;
    /**
     * the C that prevents the d^2 from going to infinity
     */
    final double c = .1d;//.5*Math.pow(dist,2)/(126);
    public static float cc = .00001f;
    /**
     * The K that we are using for the multiplication
     */
    final float k = 90;//Math.pow(dist,2)/(126) + Math.pow(dist,2);
//    final double dist1 = p2.size;
//    final double c1 = Math.pow(dist1,2)/(126);
//    final double k1 = Math.pow(dist1,2)/(126) + Math.pow(dist1,2);
    
    //point from p1 to p2
    //step 1 - just draw the shape.
    final byte MAX = (byte)0xFF;
    final int bytes = 4;
    final static int size = GConstants.SCREEN_HEIGHT*GConstants.SCREEN_WIDTH;
    static final Line2D l = new Line2D.Float(p1.p, p2.p);
    @Override
    public void render(Graphics2D gg) {
        long l1 = System.currentTimeMillis();
        //creates an image the size the size of the screen
        
//        byte[] r = new byte[size*4];
//        byte[] g = new byte[size*4];
//        byte[] b = new byte[size*4];
//        byte[] a = new byte[size*4];
        //4-bytes per pixel. Red, green, blue, and alpha.
        
//        System.out.println
//        Point2D p1 = new Point2D.Float(this.p1.x, this.p1.y);
//        Point2D p2 = new Point2D.Float(this.p2.x, this.p2.y);
        
        int ss[] = new int[size];
//        int count = 0;
        byte r,g,b,a;
        
        //System.out.println(p);
        for(int y = 0; y < GConstants.SCREEN_HEIGHT ; y++){ 
            for(int x = 0; x < GConstants.SCREEN_WIDTH; x ++){
                double xx   = 0;//CellAutomaton.generate_Next(x, y);
                

                

                //info[0] = p1size
                //info[1] = p2size
                //info[2] = p1x
                //info[3] = p1y
                //info[4] = p2x
                //info[5] = p2y
                //info[6] = px
                //info[7] = py
//                float p1s = p1.size; float p2s = p2.size;
//                float p1x = (float)p1.p.getX(); float p1y = (float)p2.p.getY();
//                float p2x = (float)p1.p.getX(); float p2y = (float)p2.p.getY();
                float p1x = (float)p1.p.getX(); float p1y = (float)p1.p.getY();
                float p2x = (float)p2.p.getX(); float p2y = (float)p2.p.getY();
                float ppx = (float)x;           float ppy = (float)y;
                //<vector>
                    p2x -= p1x;
                    p2y -= p1y;
                //</vector>
                    
                //<vector>
                    ppx -= p1x;
                    ppy -= p1y;
                //</vector>
                float aa = 0;//a affects p1, b affects p2
                float dot_prod = ppx*p2x + ppy*p2y;//dot(pp, p2);
                float projlenSq;
                if(dot_prod<=0.0){
                    projlenSq = 0.0f;
                    //closest to p1
                    aa = 1f;
                }else{
                    //<vector>
                        ppx = p2x - ppx;
                        ppy = p2y - ppy;
                    //</vector>
                        
                    dot_prod = (ppx*p2x + ppy*p2y);
                    if(dot_prod<=0.0f){
                        projlenSq = 0.0f;
                        aa = 0;//closest to p2
                    }else{
                        float dot_prode = p2y*p2y + (p2x*p2x);
                        projlenSq = (dot_prod*dot_prod)/dot_prode;
                        aa = (float)Math.sqrt(projlenSq/dot_prode);
                        //root(projection length squared /  length of segment squared )
                    }
                }
                float lenSq = ppx*ppx + ppy*ppy - projlenSq;
                float bb = 1 - aa;
                if(lenSq < 0) lenSq = 0;
                //float dist = half_sqrt(lenSq);


                //((1-a)*p1.size+(1-b)(p2.size))/(Math.pow(l.ptSegDistSq(null))^2);
//                float ret = cc*lenSq;                GOOD
                float res = ((aa)*p1.size+(bb)*(p2.size))/(lenSq+ .001f);                
                byte ret = abs(res);
            //    ret[gid] = (((int)a)<<24)|(((int)r)<<16)|(b)|(((int)g)<<8);
             
//                pos = y*GConstants.SCREEN_WIDTH + x;
//                double dist = (
//                        l.ptSegDist(x, y) + p1.p.distance(x, y)*this.p1.size + this.p2.size*p2.p.distance(x, y)
//                        
//                        )/1000d;
//                l.ptSegDist(xx, xx, xx, xx, xx, xx);
//                float ret = k*CLRunner2.executeFX(
//                        //SHOULD BE the given density for the given point.
//                        p1.size,     p2.size,
//                        (float)p1.p.getX(), (float)p1.p.getY(), 
//                        (float)p2.p.getX(), (float)p2.p.getY(),
//                        (float)x, (float)y);
                
//                System.out.println(ret);
//                k*((1-a)*p1.size+(1-b)(p2.size))/(Math.pow(l.ptSegDistSq(null))^2);
                //we pass in:   NO://(1-a), (1-b), 
                        
                        //size_a (p1.size), size_b (p2.size), point1x, point1y; point2x, point2y; pointx, pointy;
                //we get out: double ((1-a)*p1.size+ (1-b)(p2.size))/(Math.pow(l.ptSegDistSq(null))^2);
                //
//                
//                double dis1 = 5*k/(Math.pow(dist,2)+c);
//                
////                double xx = 1;//CellAutomaton.generate_Next(x, y);
////                double avg = ((dis1 - 10 + xx));//+dis2)/2.0;
//                
//                double avg  = ((dis1 - 10));//+dis2)/2.0;
//                
////                r = abs(3*avg+xx);
//                r = abs(4*avg+xx);
//                g = abs(avg  +xx);
//                b = abs(avg  -xx);
//                a = (byte)MAX;
                //byte avg  = (byte)ret;//abs(1400.0/(Math.pow(dist,2)+10.0));//+dis2)/2.0;
//                byte avg  = abs(Math.pow(l.ptSegDist(x, y),2)*xx);//+dis2)/2.0;
//                avg  = abs(Math.pow(l.ptSegDist(x, y),2)*xx);//+dis2)/2.0;
//                avg  = abs(Math.pow(l.ptSegDist(x, y),2)*xx);//+dis2)/2.0;
//                avg  = abs(Math.pow(l.ptSegDist(x, y),2)*xx);//+dis2)/2.0;
                r = (byte)(127);
                g = (byte)(127);
                b = (byte)(127);
                a = (byte)(ret);
//                System.out.println(xx);
                ss[y*GConstants.SCREEN_WIDTH + x] = 
                        ((((int)a)<<24)|
                        (((int)r)<<16)|(((int)b))|
                        (((int)g)<<8));
            }
        }
        exectime = l1 - System.currentTimeMillis();
//        System.out.println(count);
//        System.out.println(pos+3);
//        System.out.println(b.length);
        // {0 1  2  3} ( 4  5  6  7)
        // {8 9 10 11} (12 13 14 15)
        Image i = getImageFromArray(ss, GConstants.SCREEN_WIDTH, GConstants.SCREEN_HEIGHT);
//                Toolkit.getDefaultToolkit().createImage(new MemoryImageSource (GConstants.SCREEN_WIDTH, GConstants.SCREEN_HEIGHT,
//            new IndexColorModel(bytes*4, r.length, r, g, b, a),
//            g, 0, GConstants.SCREEN_WIDTH)
//        );
        gg.drawImage(i, 0, 0, null);
        if(stage){
            cc += .00001f + .00001f*(j++);
            if(cc>2000d){
                stage = false;
            }
        } else{
            cc -= .00001f + .00001f*(j*j--);
            if(cc<-2000d) stage = true;
        }
//        p1.p.setLocation(GConstants.SCREEN_WIDTH/2 + 100*Math.cos((j%120)/20d), GConstants.SCREEN_HEIGHT/2+ 100*Math.sin((j%120)/20d));
    }
    static int j = 0;
    final static double DIST = .09d;
    final static double C = 127*2;
    final static double K = Math.log(C)/DIST;
    static boolean stage  = false;
    public static byte abs(double b) {
        //logistic curve
        double s = 127-C/(1+Math.exp(-K*b));
//        return (byte)s;
        if(s<0) return 0;
        byte a = (byte)s;
        return (byte)((a < 0) ? -a : a);
    }
    public static Image getImageFromArray(int[] pixels, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        //WritableRaster raster = (WritableRaster) image.getData();
        //raster.setPixels(0,0,width,height,pixels);
        image.setRGB(0, 0, width, height, pixels, 0, width);
        return image;
    }
    @Override
    public void update() {}
    
}
