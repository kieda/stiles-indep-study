/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics.aura;

import java.awt.geom.Point2D;

/**
 *
 * @author kieda
 */
public class ptV {
    //a point with a magnitude
    public ptV(float x, float y, float size){
        this.p = new Point2D.Float(x, y); this.size = size;}
    public Point2D p;
    public float size;
}
