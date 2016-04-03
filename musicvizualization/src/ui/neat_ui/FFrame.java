
package ui.neat_ui;

/*
 * BoxLayoutDemo.java requires no other files.
 */

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import util.constants.GConstants;

public class FFrame {
    public final float w_perc = .3f;
    public final float h_perc = .5f;
    public FFrame(){
       createAndShowGUI();
    }
    public static void addComponentsToPane(Container pane) {
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        
        addBB(new JButton("Dispose") {
            @Override
            protected void init(String text, Icon icon) {
                super.init(text, icon);
                addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //throw new UnsupportedOperationException("Not supported yet.");\
                        
                    }
                });
            }
        }, pane);
        addBB(new JButton("Exit") {
            @Override
            protected void init(String text, Icon icon) {
                super.init(text, icon);
                addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //throw new UnsupportedOperationException("Not supported yet.");\
                        System.exit(0);
                    }
                });
            }
        }, pane);
    }

    private static void addBB(JButton button, Container container) {
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(button);
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        int width  = (int)(GConstants.SCREEN_WIDTH*w_perc);
        int height = (int)(GConstants.SCREEN_HEIGHT*h_perc);
        
        addComponentsToPane(frame.getContentPane());
        frame.setBounds((GConstants.SCREEN_WIDTH - width)/2, (GConstants.SCREEN_HEIGHT - height)/2, width, height);
        //Display the window.
//        frame.pack();
        frame.setVisible(true);
    }

    
}
class MMain{
    public static void main(String[] args) {
        FFrame ff = new FFrame();
    }
}