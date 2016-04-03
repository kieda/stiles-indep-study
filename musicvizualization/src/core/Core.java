/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

//import com.sun.awt.AWTUtilities;
import graphics.graphs.PaintGraph;
import graphics.Renderable;
import graphics.aura.Drop;
import graphics.aura.Teardrop;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.Date;
import java.util.EventListener;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
//import sun.misc.GC;
//import sun.util.calendar.BaseCalendar;
import ui.Disp;
//import util.acceleration.CLRunner;
import util.console.Console;
import util.constants.GConstants;

/**
 * The core class. This class handles timers and events.
 * The core is also static, and creates and manages the main frame.
 * @author kieda
 */
public class Core extends AbstractCore{
    Disp r;
//    PaintGraph g = new PaintGraph();
    Renderable sd = new Renderable() {
        @Override
        public void render(Graphics2D a) {
            a.setPaint(Color.WHITE);
            a.drawString("FPS: "+fps+"", 20, 20);
//            a.drawString("NUM: "+g.size()+"", 20, 50);
//            a.drawString("CALCULATED CURRENT TIME: " + new Date().toString(), 20, 80);
        }
        @Override
        public void update() {}
    };
    Teardrop td ;//= new Teardrop();
    Renderable dark;
//    Drop d=new Drop();
    int count;
//    private int tot = 0;
    Random rand = new Random();
    boolean STAGE;
    final int MAX = 10 ;
    
    @Override
    public synchronized void update() { 
        if(state == CoreState.RUNNING){
//            if(STAGE){
//                if(count >= MAX) {STAGE = false;count--;}
//                if(time > tot*1f){
//                    g.addVertex(count++);
//
//                    g.addEdge(
//                            rand.nextInt(count),
//                            rand.nextInt(count));
//                }
//            } else{
//
//                if(count == 40) STAGE = true;
//                if(time > tot*1f){
//                    g.removeVertex(count);
//                    count--;
//                }
//            } tot++;
//            if(STAGE){
                
//                if(time > count*15f){
//                    g.addVertex(count);
////                    if(!STAGE && count >= MAX) STAGE = true;
////    //                if(count >= MAX) {
////                    if(STAGE){
////                        int e1 = count - rand.nextInt(MAX);
////                        
////                        
////                        while(Math.random()>.3f){
////                            int e2 = count - rand.nextInt(MAX);
////                            g.addEdge(e1,e2);
////                            e1 = e2;
////                        }
////                        g.removeVertex(count-MAX);
////                    }else{
////                        g.addEdge(
////                                rand.nextInt(count)
////                                ,
////                                rand.nextInt(count)
////                                );
////                    }
//                    count++;
//                    } 
//                else g.addEdge(
//                            rand.nextInt(count),
//                            rand.nextInt(count));
//                    count--;
//                }
//            } 
//            tot++;
//        }
        
        }
        r.update();
        
    }
    //noting to update, so far. The Disp auto-updates.
    final static float MAX_OPAC = 1f;
    
    float opac;
    long ll;
    //we want to have a thread that makes the screen darker over time.
    static Point origpos = new Point();
    final boolean SCREEN_SAVER = false;
    @Override
    public void initialize() {
        opac = 0.0f;
        ll = System.currentTimeMillis();
        count = 0;
        origpos = new Point(0,0);
        STAGE = false;
        td = new Teardrop();
        dark = new Renderable() {
            @Override
            public void render(Graphics2D g) {
                g.setPaint(Color.BLACK);
                g.fillRect(0, 0, GConstants.SCREEN_WIDTH, GConstants.SCREEN_HEIGHT);
            }
            @Override
            public void update() {}
        };
        r = new Disp(0);
        if(SCREEN_SAVER){ //immediate exits for the frame.
            r.addMouseMotionListener(new MouseMotionListener() {
                @Override public void mouseDragged(MouseEvent e)    {exit();}
                @Override public void mouseMoved(MouseEvent e)      {exit();
            }});
            r.addMouseListener(new MouseListener() {
                @Override public void mouseClicked(MouseEvent e)    {exit();}
                @Override public void mousePressed(MouseEvent e)    {exit();}
                @Override public void mouseReleased(MouseEvent e)   {exit(); }
                @Override public void mouseEntered(MouseEvent e)    {} 
                @Override public void mouseExited(MouseEvent e)     {}
            });
            r.addKeyListener(new KeyListener() {
                @Override public void keyTyped(KeyEvent e)          {}
                @Override public void keyPressed(KeyEvent e)        {exit();}
                @Override public void keyReleased(KeyEvent e)       {exit();}
            });
        }
        r.createG();
        
        
//        //r.createG();
//        //r.update();
//        r.setLocation(0, 0);
        
        r.addPain(dark);
//        Thread t = new Thread( new Runnable() {
//            @Override
//            public void run() {
        if(SCREEN_SAVER){
        Robot a;
        try {
            Point pp = MouseInfo.getPointerInfo().getLocation();
            if(pp!=null)
            origpos.setLocation(pp);
            a = new Robot();
            a.mouseMove(GConstants.SCREEN_WIDTH, GConstants.SCREEN_HEIGHT);
        } catch (AWTException ex) {
            Logger.getLogger(Core.class.getName()).log(Level.SEVERE, null, ex);
        }
                krupp:while(opac < MAX_OPAC){
                    r.update();
                    try {
                        Thread.currentThread().sleep(10);//quick refresh.
                    } catch (InterruptedException ex) {}
//                    AWTUtilities.setWindowOpacity(r, opac);
                    opac += .01f;
//                    opac  += .2*(opac)*(MAX_OPAC - opac) + .001f;//*((float)(System.currentTimeMillis() - ll))*.004f;
//                    System.out.println(opac);
                    if(!r.isVisible()) break krupp;
                }
        }
//                AWTUtilities.setWindowOpacity(r, MAX_OPAC);
//            }}); t.start();
//        r.remPain(dark);
//        Thread t = new Thread( new Runnable() {
//            @Override
//            public void run() {
//                while(opac < MAX_OPAC){
////                    r.update();
//                    AWTUtilities.setWindowOpacity(r, opac);
//                    //the more time that goes between an update, the more it should
//                    //add to the opacity.
//                    opac  += ((float)(System.currentTimeMillis() - ll))*.00001f;
//                    try {
//                        Thread.currentThread().sleep(10);//quick refresh.
//                    } catch (InterruptedException ex) {}
//                }
//            }
//        });
//        t.start();
        
//        r.createG();
      //  Thread t = new Thread(r);
        //t.start();
//        g.addVerts(1,2,3,4,5,6,7,8,10);
//        g.addEdge(1,2);
//        g.addEdge(2,3);
//        g.addEdge(10,4);
//        g.addVerts(1,2,3,4,5,6);
        
        
        //the opening darkening of the screen.
        
        
        
       
        
//        AWTUtilities.setWindowOpacity(r, MAX_OPAC);
        r.remPain(dark);//remove dark. add on our painting.
        r.addPain(td);//add on our main fx
        r.addPain(sd);
//        r.addPain(d);
//        r.addPain(sd);
//        r.addPain(sd);
//        g.addVertex(count++);//add two vertices for good measure
//        g.addVertex(count++);
//        g.addEdge(0, 1);
    }
    final static float sleep = 2f;//sleep for 1 second.
    @Override
    public void exit() {
        r.dispose();
        r.clearPain();
        setCoreState(CoreState.BROKEN);
        float tn = time;
        {
            try {
                Robot v = new Robot();
                v.mouseMove(origpos.x, origpos.y);
            } catch (AWTException ex) {}
        }
        switch(SCREEN_SAVER==true?1:0){
            case TRUE://hesitant to use rebooting code.
            case FALSE:
                r = null;
                Console.println("");
                Console.disableTabs();
                Console.println("[       bye~!       ]");
                Console.enableTabs();
                System.exit(0);
        }
//        if(SCREEN_SAVER){
//            core:while(true){
//                updateTime();
//                if(sleep<(time-tn)/1000f)
//                    break core;
//                try {
//                    Thread.currentThread().sleep(500);
//                } catch (InterruptedException ex) {}
//            }
//            initializeTime();//reset to the screensaver
//            Teardrop.reset();
//            setCoreState(CoreState.INITIALIZE);
//        }
    }

    @Override
    protected String projectName() {
        return "Mixx";
    }

    @Override
    protected String projectVersion() {
        return "v0.5";
    }
    final static int FALSE = 0;
    final static int TRUE  = 1;
}
