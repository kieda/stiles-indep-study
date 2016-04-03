/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.util.logging.Level;
import java.util.logging.Logger;
//import util.acceleration.CLRunner;
//import util.acceleration.CLRunner2;
import util.console.Console;
import util.constants.GConstants;

/**
 * No main loops necessary/threading. This is all handled on this end.
 * @author kieda
 */
public abstract class AbstractCore implements Runnable{
    {
        System.setProperty("project_name", projectName());
        System.setProperty("project_version", projectVersion());
        Console.open();
//        CLRunner2.openProgram();
        state = CoreState.INITIALIZE;//begin in initialization
        int size = GConstants.SCREEN_HEIGHT*GConstants.SCREEN_WIDTH;
        int[] xx = new int[size];
        int[] yy = new int[size];{
            int pos = 0;
            for(int y = 0; y < GConstants.SCREEN_HEIGHT ; y++){ 
                for(int x = 0; x < GConstants.SCREEN_WIDTH; x ++){
                    pos = y*GConstants.SCREEN_WIDTH + x;
                    xx[pos] = x;
                    yy[pos] = y;
            }}
        }
        
        Thread t = new Thread(this);
        t.start();
        
        
        //auto-initializes the thread. No need to initialze the thread outside 
        //of this class.
    }
    private static long currentTime;//the time we're at, or "the time past when we started".
    public static float dt;//the time between this frame and the last.
    public static float fps;//the time between this frame and the last.
    private static long startingTime;//the time when we started. Useful for operations. Non-final in case the client wants to "turn back time"
    public static float time;
    private final long PERIOD = 30;
    public void updateTime(){
        currentTime = System.currentTimeMillis();
        float tp = time;
        time = (float)(currentTime - startingTime);
        dt = time - tp;
        fps = 1/(dt/1000);
    }
    public abstract void update();
    public abstract void initialize();
    public abstract void exit();
    protected abstract String projectName();
    protected abstract String projectVersion();
    public static String project_name(){
        return System.getProperty("project_name");
    }
    public static String project_version(){
        return System.getProperty("project_version");
    }
    public static CoreState state;
    public enum CoreState{
        INITIALIZE,  //in the state of initialization. This state jumps into RUNNING immediately after it finishes.
        RUNNING, //the time is being updated and the canvas is being drawn
        SUSPENDED, //the canvas is being drawn but the time is not being updated
        BROKEN, //neither the canvas is being drawn nor the time is updated
        EXIT//exits.
    }
    public static void setCoreState(CoreState c){
        state = c;
    }
    public static void setCoreState(String state){}
    protected void initializeTime(){
        startingTime = System.currentTimeMillis();
        dt = 1f;//to prevent dividing by zero errors. Also, the 
                           //best time for a loop to come to completetion.
        time = 0f;
        currentTime = startingTime;
    }
    @Override public void run(){
        initializeTime();
        exit:while(true){ 
            sw:switch(state){
                case INITIALIZE:
                    state = CoreState.RUNNING;
                    //immediately set the state to running and begin running
                    initialize();
                    //initialize happens afterwords, in case if the user wants to
                    //jump into a different state after initialization.
                case RUNNING:
                    updateTime();
                case SUSPENDED:
                    update();
                    break sw;
                case BROKEN:
                    break sw;
                case EXIT:
                    
                    break exit;
            }
            try {
                
                Thread.currentThread().sleep(PERIOD);
            } catch (InterruptedException ex) {}
        }exit();
    }
}
