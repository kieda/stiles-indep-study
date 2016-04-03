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
import javax.swing.plaf.ColorUIResource;
import javax.swing.text.Segment;
import struct.cellular.CellAutomaton;
//import util.acceleration.CLRunner2;
import util.console.Console;
import util.constants.GConstants;

public class Teardrop implements Renderable{
    public static ptV p1 = new ptV(GConstants.SCREEN_WIDTH/2, GConstants.SCREEN_HEIGHT/2, 300);
    public static ptV p2 = new ptV(GConstants.SCREEN_WIDTH/2, GConstants.SCREEN_HEIGHT/2, 50);
    final float SCREEN_L = (float)(p1.p.distanceSq(0,0));//GConstants.SCREEN_HEIGHT*GConstants.SCREEN_HEIGHT/4);
    public static long exectime = 0;
//    final double dist = p1.size;
    /**
     * the C that prevents the d^2 from going to infinity
     */
    final double c = .1d;//.5*Math.pow(dist,2)/(126);
    public static float cc = .1f;//.00001f;
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
//                double xx   = 0;//CellAutomaton.generate_Next(x, y);
                float rel = (float)(1-p1.p.distanceSq(x, y)/SCREEN_L);
                
                byte sam = 0;
                if(rel<0) {
                    rel = 0;
                }else {

                

                //info[0] = p1size
                //info[1] = p2size
                //info[2] = p1x
                //info[3] = p1y
                //info[4] = p2x
                //info[5] = p2y
                //info[6] = px
                //info[7] = py
//                float p1s = p1.size; float p2s = p2.size;
                float p1x = (float)p1.p.getX(); float p1y = (float)p2.p.getY();
                float p2x = (float)p1.p.getX(); float p2y = (float)p2.p.getY();
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
//                float bb = cc*lenSq;
                if(lenSq < 0) {lenSq = 0; }
                //float dist = half_sqrt(lenSq);

                //((1-a)*p1.size+(1-b)(p2.size))/(Math.pow(l.ptSegDistSq(null))^2);
                float ret = cc*lenSq;
//                float res = ((aa)*p1.size+(bb)*(p2.size))/(lenSq+ .001f);                
//                byte ret = abs(res);
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
//                r = (byte)(ret + j%0xFF);
//                g = (byte)(ret+ (0xFF/3-j)%0xFF);
//                b = (byte)(ret + (0xFF*2/3-j)%0xFF);
//                byte res = mabs((byte)ret);
//                r = (byte)(ret);
//                g = (byte)(ret);
//                b = (byte)(ret);
                
                
                
                
                //sam is the sample byte used
                sam = (byte)ret;
                //rel is the multiplication that depends on the 
                //distance from the center of the screen
//                if( sam < 0){
//                    int i = (((sam&0x7F) >>> 1)<<1)|1;
//                    i = i<<1 |1;
//                    if(i<0)
//                        System.out.println("asdf");
//                }
//                sam = (byte)(((((sam&0x7F) >>> 1)<<1)|1) * rel);
//                sam = (byte)(sam * rel);
                  sam = (byte)(Math.max((
                        sam
                        )*rel, 0));
                }
                
                a = (byte)MAX;
                r = (byte)sam;
                g = (byte)(sam);
                b = (byte)(sam/1.5);
//                b = (byte)(sam);

//                System.out.println(xx);
                int pix = ((((int)a)<<24)|
                        (((int)r)<<16)|(((int)b))|
                        (((int)g)<<8));
                
                ss[y*GConstants.SCREEN_WIDTH + x] = pix;
                        
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
            
//            if(cc>0&&cc<.001){
//                cc += .000015f;
//            } 
//            else if(cc<.01){
//                cc += .00005f;
//            } 
//            else 
            
                if(cc<.12){
                cc += .0005f;
//                cc = .19f;
            }
            else if(cc<.15){
                cc += .0001f;
            }
            else if(cc<.2){
                cc += .001f;
            }
            else
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
    public static byte mabs(byte b){
        return (byte)(b<0 ? (-b)%127 : b%127);
    }
    public static int j = 0;
    final static double DIST = .09d;
    final static double C = 127*2;
    final static double K = Math.log(C)/DIST;
    static boolean stage  = true;
    public static void reset(){
        j = 0; cc = .00001f;
    }
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
    public static byte maxb(byte a, byte b){
        return (a >= b) ? a : b;
    }
}
