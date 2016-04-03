package edu.cmu.ideate.zkieda.viz.core;

import java.awt.Graphics2D;

/**
 * update called before render every frame. 
 * @author kieda
 */
public interface Renderable {
    public void render(Graphics2D g);
}