package edu.cmu.ideate.zkieda.viz.ui;

import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import edu.cmu.ideate.zkieda.viz.core.Renderable;
import edu.cmu.ideate.zkieda.viz.core.Updateable;
/**
*
* @author kieda
*/
public class Display extends JFrame implements Updateable{
   private ArrayList<Renderable> paints;//a list of things to paint
   
   private BufferStrategy bf;
   
   private boolean fullscreen;
   
   public Display(boolean fullscreen){
	   Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
	   this.fullscreen = fullscreen;
	   if(fullscreen){
		   setUndecorated(true);
		   setResizable(false);
		   setBounds(0, 0, screenDim.width, screenDim.height);
		   GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		   if(gd.isFullScreenSupported()){
			   com.apple.eawt.FullScreenUtilities.setWindowCanFullScreen(this, true);
		   }
	   } else{
		   setBounds(screenDim.width/4, screenDim.height/4, (screenDim.width)/2, (screenDim.height)/2);
	   }
	   
	   paints = new ArrayList<>();
   }
   
   public void createGraphics(){
       setVisible(true);
       setIgnoreRepaint(true);
       createBufferStrategy(2);
       bf = this.getBufferStrategy();
       
       if(this.fullscreen){
    	   //I'm assuming you're gonna be running this on osx
    	   //if not you're fucked (can't compile)
	       com.apple.eawt.FullScreenUtilities.setWindowCanFullScreen(this, true);
	       com.apple.eawt.Application.getApplication().requestToggleFullScreen(this);
       }
   }
   
   public void update(float dt){
	   do{
           do{
               Graphics2D g2 = (Graphics2D) bf.getDrawGraphics();
//               g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
               g2.clearRect(0, 0, getSize().width, getSize().height);
               if (!bf.contentsLost()) {
                   for(Renderable r: paints){//render the paints in order
                       r.render(g2);
                   }
               }
               
               g2.dispose();
           }while(bf.contentsRestored());
           bf.show();
       } while (bf.contentsLost());
   }

   public List<Renderable> getRenderables() {
		return paints;
   }
}