/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 *
 * @author kieda
 */
public interface Renderable {
    public void render(Graphics2D g);
    public void update();
}