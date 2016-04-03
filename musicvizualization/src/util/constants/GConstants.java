/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util.constants;

import java.awt.Dimension;
import java.awt.Toolkit;

public class GConstants {
    
    //simple universal graphics constants
    //you can extend this class to add on additional constants.
    public static final int SCREEN_WIDTH, SCREEN_HEIGHT;
    public static int BOUND_X, BOUND_Y, BOUND_W, BOUND_H;
    static {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        SCREEN_HEIGHT = d.height;
        SCREEN_WIDTH = d.width;
        //originally starts at the max screen width.
        BOUND_X = 0;
        BOUND_W = SCREEN_WIDTH;
        BOUND_Y = 0;
        BOUND_H = SCREEN_WIDTH;
    }
    /**centers the drawing frame at the X middle of the screen.*/
    public static void setWidth(int newWidth){
        BOUND_X = (SCREEN_WIDTH -newWidth)/2;
        BOUND_W = newWidth;
        //Y, height not changed
    }
    /**centers the drawing frame at the Y middle of the screen.*/
    public static void setHeight(int newHeight){
        BOUND_X = (SCREEN_HEIGHT -newHeight)/2;
        BOUND_W = newHeight;
        //Y, height not changed
    }
}
