/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

/**
 *
 * @author kieda
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//import com.sun.awt.AWTUtilities;
import core.AbstractCore;
import core.Core;
import util.constants.GConstants;
import graphics.Renderable;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.Arc2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import static util.constants.GConstants.*;
/**
 *
 * @author kieda
 */
public class Disp extends JFrame implements MouseListener, WindowListener{
    static int height;
    static int width;
    static Point mousePos = new Point();
//    GraphVisualize v;
    private ArrayList<Renderable> paints;//a list of things to paint

    @Override
    public void windowOpened(WindowEvent e) {}

    @Override
    public void windowClosing(WindowEvent e) {
        AbstractCore.setCoreState(AbstractCore.CoreState.EXIT);
    }

    @Override
    public void windowClosed(WindowEvent e) {}

    @Override
    public void windowIconified(WindowEvent e) {}

    @Override
    public void windowDeiconified(WindowEvent e) {}

    @Override
    public void windowActivated(WindowEvent e) {}

    @Override
    public void windowDeactivated(WindowEvent e) {}
    public enum MouseState{
        MOUSE_DOWN,
        MOUSE_UP
    }
    private static MouseState ms = MouseState.MOUSE_UP;
    public static MouseState getMouseState(){
        return ms;
    }
//    public Disp(Set<Integer> verts, Set<Edge> edges){
    BufferStrategy bf;
    BufferedImage bi;
    public Disp(){
        super(Core.project_name() + " "+ Core.project_version());
        setBounds(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        setUndecorated(true);
        height = getHeight();
        width = getWidth();
        
        paints = new ArrayList<Renderable>();//list of things to render
        Point p = getMousePosition();
        
        if(p!=null){
            mousePos = p;
        }
        else{
            mousePos.x = 200;//getMousePosition().x;
            mousePos.y = 200;//getMousePosition().y;
        }
//        AWTUtilities.setWindowOpacity(this, 0);
        setVisible(true);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        setIgnoreRepaint(true);
        bi = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB);
        addMouseListener(this);
        addWindowListener(this);
        createBufferStrategy(2);
        bf = this.getBufferStrategy();
        
    }
    public Disp(float opac){
        super(Core.project_name() + " "+ Core.project_version());
        setBounds(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        setUndecorated(true);
        height = getHeight();
        width = getWidth();
        
        paints = new ArrayList<Renderable>();//list of things to render
        Point p = getMousePosition();
        
        if(p!=null){
            mousePos = p;
        }
        else{
            mousePos.x = 200;//getMousePosition().x;
            mousePos.y = 200;//getMousePosition().y;
        }
//        AWTUtilities.setWindowOpacity(this, opac);
    }
    public void createG(){
        setVisible(true);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        setIgnoreRepaint(true);
        bi = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB);
        addMouseListener(this);
        addWindowListener(this);
        createBufferStrategy(2);
        bf = this.getBufferStrategy();
    }
    public synchronized void paints(){
        do{
            do{
                Graphics2D g2 = (Graphics2D) bf.getDrawGraphics();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.clearRect(0, 0, getSize().width, getSize().height);
                if (!bf.contentsLost()) {
                    for(Renderable r: paints){//updates all of the objects
                        r.update();
                    }
                    for(Renderable r: paints){//render the paints in order
                        r.render(g2);
                    }
                }
                
                g2.dispose();
            }while(bf.contentsRestored());
            bf.show();
        } while (bf.contentsLost());
        
    }
    
    public Graphics2D createDemoGraphics2D(Graphics g) {
        Graphics2D g2 = null;

        if (bi == null || bi.getWidth() != getSize().width
            || bi.getHeight() != getSize().height) {
        bi = (BufferedImage) createImage(getSize().width, getSize().height);
        }

        if (bi != null) {
            g2 = bi.createGraphics();
            g2.setBackground(getBackground());
        }

        // .. set attributes ..
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // .. clear canvas ..
        g2.clearRect(0, 0, getSize().width, getSize().height);

        return g2;
    }
    public void update(){
        height = getHeight();
        width = getWidth();
        Point p = getMousePosition();
        if(p!=null){
            mousePos = p;
        }
        paints();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        Point p = e.getPoint();
        
        mousePos.setLocation(p);// = p;
        ms = MouseState.MOUSE_DOWN;
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        Point p = e.getPoint();
        mousePos = p;
        ms = MouseState.MOUSE_UP;
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
    
    public void addPain(Renderable r){
        paints.add(r);
    }
    public void remPain(Renderable r){
        paints.remove(r);
    }
    public void clearPain(){
        paints.clear();
    }
}
