package edu.cmu.ideate.zkieda.viz.drivers;

import edu.cmu.ideate.zkieda.viz.core.Core;
import edu.cmu.ideate.zkieda.viz.core.Core.CoreState;
import edu.cmu.ideate.zkieda.viz.graphics.ImageRenderable;
import edu.cmu.ideate.zkieda.viz.graphics.Trip;
import edu.cmu.ideate.zkieda.viz.ui.Display;

public class Main {
	private Display display;
	private Trip trippy;
	private Core core;
	
	private Main(){
		this.core = new Core(() -> {
			display = new Display(true);
			trippy = new Trip();
	    	
			display.createGraphics();
			ImageRenderable ir = new ImageRenderable(trippy, display.getWidth(), display.getHeight());
	    	
	    	display.getRenderables().add(ir);
	    	core.getUpdateables().add(trippy);
	    	core.getUpdateables().add(display);
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
