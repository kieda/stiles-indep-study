/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.cmu.ideate.zkieda.viz.core;

import java.util.ArrayList;

/**
 * No main loops necessary/threading. This is all handled on this end.
 * @author kieda
 */
public class Core implements Runnable{
	private Runnable initialize, exit;
	private ArrayList<Updateable> updates;//a list of things to update before each frame
	
	/**
	 * does nothing on exit
	 */
	public Core(Runnable initialize){
		this(initialize, () -> {});
	}
	
	/**
	 * callbacks for when the core is initializing, updating, or exiting
	 */
	public Core(Runnable initialize, Runnable exit){
		state = CoreState.INITIALIZE;//begin in initialization
		this.initialize = initialize;
		this.exit = exit;
		this.updates = new ArrayList<>();
	}
    
    
    private long currentTime;//the time we're at, or "the time past when we started".
    private float dt;//the time between this frame and the last.
    private float fps;//the time between this frame and the last.
    private long startingTime;//the time when we started. Useful for operations. Non-final in case the client wants to "turn back time"
    private float previousTime; //time in the previous frame
    
    private final long PERIOD = 10;//how long we wait per frame
    
    private void updateTime(){
        currentTime = System.currentTimeMillis();
        float tp = previousTime;
        previousTime = (float)(currentTime - startingTime);
        dt = previousTime - tp;
        fps = 1000f/dt;
    }
    
    private CoreState state;
    
    public long getCurrentTime(){
    	return currentTime;
    }
    
    public float getCurrentFps() {
		return fps;
	}
    
    public float getCurrentDt() {
		return dt;
	}
    
    public long getStartingTime() {
		return startingTime;
	}
    
    public enum CoreState{
        INITIALIZE,  //in the state of initialization. This state jumps into RUNNING immediately after it finishes.
        RUNNING, //the time is being updated and the canvas is being drawn
        SUSPENDED, //the canvas is being drawn but the time is not being updated
        BROKEN, //neither the canvas is being drawn nor the time is updated
        EXIT//exits.
    }
    
    public void setCoreState(CoreState c){
        state = c;
    }
    
    private void initializeTime(){
        startingTime = System.currentTimeMillis();
        dt = 1f;//to prevent dividing by zero errors. Also, the 
                           //best time for a loop to come to completetion.
        previousTime = 0f;
        currentTime = startingTime;
    }
    
    @Override public void run(){
        initializeTime();
    	
        exit:while(true) {
            sw:switch(state){
                case INITIALIZE:
                    state = CoreState.RUNNING;
                    //immediately set the state to running and begin running
                    initialize.run();
                    //initialize happens afterwords, in case if the user wants to
                    //jump into a different state after initialization.
                case RUNNING:
                    updateTime();
                case SUSPENDED:
                    for(Updateable u : updates) {u.update();}
                    break sw;
                case BROKEN:
                    break sw;
                case EXIT:
                    break exit;
            } try {
                Thread.currentThread().sleep(PERIOD);
            } catch (InterruptedException ex) {}
        }
        exit.run();
    }
    
    public CoreState getState() {
		return state;
	}
    
    public ArrayList<Updateable> getUpdateables() {
		return updates;
	}
}
