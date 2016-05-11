package edu.cmu.ideate.zkieda.viz.drivers;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import java.util.Timer;

import edu.cmu.ideate.zkieda.viz.core.Core;
import edu.cmu.ideate.zkieda.viz.core.Core.CoreState;
import edu.cmu.ideate.zkieda.viz.core.Updateable;
import edu.cmu.ideate.zkieda.viz.core.Renderable;
import edu.cmu.ideate.zkieda.viz.graphics.ImageRenderable;
import edu.cmu.ideate.zkieda.viz.graphics.Trip;
import edu.cmu.ideate.zkieda.viz.ui.ControllerUI;
import edu.cmu.ideate.zkieda.viz.ui.Display;

public class Main {
	private Display display;
	private Trip trippy;
	private Core core;
	
	//change this if you want to display the fps 
	//on screen
	private final boolean displayFps = false;
	
	//makes a controller window that we can use 
	//to change the realtime settings of our visualization program.
	private final boolean useController = false;
	
	private final File saveDir = new File(System.getProperty("user.home"), "Desktop"); 
	
	private Main(){
		this.core = new Core(() -> {
			//make and set up our main display
			display = new Display(true);
			trippy = new Trip();
	    	
			display.createGraphics();
			
			//add renderables
			ImageRenderable ir = new ImageRenderable(trippy, display.getWidth(), display.getHeight());
	    	
	    	display.getRenderables().add(ir);
	    	core.getUpdateables().add(trippy);
	    	core.getUpdateables().add(display);
	    	
	    	//option to display fps
	    	if(displayFps) {
		    	display.getRenderables().add(g2 -> {
		    		float fps = core.getCurrentFps();
		    		g2.setColor(Color.WHITE);
		    		g2.drawString("" + fps, 10, 10);
		    	});
	    	}
	    	
	    	
	    	//initialize, create controller, and hook it up
	    	if(useController){
		    	ControllerUI controller = new ControllerUI();
		    	
		    	controller.getAlphaChangePanel().setValueChangeListener(d -> {
		    		trippy.setDeltaAlpha((float)d);
		    	});
		    	
		    	controller.getAlphaPanel().setValueChangeListener(d -> {
		    		trippy.setAlpha((float)d);
		    	});
		    	
		    	controller.getLambdaPanel().setValueChangeListener(d -> {
		    		trippy.setLambda((float)d);
		    	});
		    	
		    	controller.getLambdaChangePanel().setValueChangeListener(d -> {
		    		trippy.setDeltaLambda((float)d);
		    	});
		    	
		    	controller.getDarkenPanel().setItemListener(i -> {
		    		switch(i){
			    		case "On":
			    			trippy.setDarken(true);
			    			break;
			    		case "Off":
			    			trippy.setDarken(false);
			    			break;
		    		}
		    	});
		    	
		    	controller.getSmoothingPanel().setItemListener(i -> {
		    		switch(i){
			    		case "Quadratic":
			    			trippy.setGradient(Trip.GradientType.QUADRATIC);
			    			break;
			    		case "Linear":
			    			trippy.setGradient(Trip.GradientType.LINEAR);
			    			break;
			    		case "None":
			    			trippy.setGradient(Trip.GradientType.NONE);
			    			break;
		    		}
		    	});
		    	
		    	controller.setVisible(true);
		    	
		    	
		    	//this thread updates the controller's alpha value
		    	new Thread(()-> {
		    		while(true){
		    			try{
		    				EventQueue.invokeLater(() -> {
		    					controller.getAlphaDisplayPanel().setAlpha(trippy.getAlpha());
		    				});
		    				Thread.currentThread().sleep(100);
		    			} catch(InterruptedException e){}
		    		}
		    	}).start();
	    	}
	    	
	    	display.addKeyListener(new KeyListener() {
	    		private final Timer displayTimer = new Timer();
	    		
				@Override
				public void keyTyped(KeyEvent e) {}
				
				@Override
				public void keyReleased(KeyEvent e) {
					if(e.isMetaDown() && e.getKeyCode() == KeyEvent.VK_S){
						String[] fileInteral = saveDir.list();
						Set<String> internalFiles = new HashSet<>();
						Collections.addAll(internalFiles, fileInteral);
						
						int fileNo = 0;
						String name = "Screenshot-";
						while(internalFiles.contains(makeFileName(fileNo)))
							fileNo++;
							
						String fileName = makeFileName(fileNo);
						EventQueue.invokeLater(() -> {
							try {
								ImageIO.write(ir.getImage(), "png", new File(saveDir, fileName));
							} catch (IOException e1) {
								e1.printStackTrace();
							}
//							final long startTime = System.currentTimeMillis();
//							Renderable saveFileDisplay = g -> {
//								float opacity = (1000 - System.currentTimeMillis() - startTime) / 1000.0f;
//								opacity = Math.min(0, opacity);
//								Color colorBefore = g.getColor();
//								g.setColor(new Color(1f, 1f, 1f, opacity));
//								g.drawString("File " + name + " Saved.", 10, 40);
//								g.setColor(colorBefore);
//							};
//							
//							display.getRenderables().add(saveFileDisplay);
//							
//							//ensure there's no race condition...
//							EventQueue.invokeLater(() -> {
//								displayTimer.schedule(new TimerTask() {
//									@Override
//									public void run() {
//										display.getRenderables().remove(saveFileDisplay);
//									}
//								}, 1000);
//							});
						});
							
						
					}
				}
				
				private String makeFileName(int fileNo){
					return "Screenshot-" + fileNo + ".png";
				}
				
				@Override
				public void keyPressed(KeyEvent e) {}
			});
	    	
		}, () -> {
			display.dispose();
			display.getRenderables().clear();
	        core.setCoreState(CoreState.EXIT);
		});
	}
	
	
	public static void main(String[] args) {
		Main m = new Main();
		Thread t = new Thread(m.core);
		t.start();
	}
}
