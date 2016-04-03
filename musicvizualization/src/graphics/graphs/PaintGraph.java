/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics.graphs;

import java.awt.*;
import java.util.Iterator;
import struct.graphs.Graph;
import util.constants.GConstants;
import util.error.InvalidInputException;

public class PaintGraph extends PaintableGraph{//default paint graph
    float scale;
    private int cycle_count = 0;//used so vertices know when they're being 
                                //painted
    @Override public synchronized void render(Graphics2D g) {
        synchronized(this){
            cycle_count=0;
            for(Vertex v: vertices.values()){
                if(v!=null)
                ((PaintableVert)v).update();//we know that the type will be of a paintable vertex, as given in the addVertex.
                cycle_count++;
            }cycle_count=0;
            for(Edge e: edges){
                ((PaintableEdge)e).update();
                cycle_count++;
            }cycle_count = 0;
        }
        synchronized(this){
            for(Edge e: edges){
                ((PaintableEdge)e).render(g);
                cycle_count++;
            }cycle_count=0;
            for(Vertex v: vertices.values()){
                if(v!=null)((PaintableVert)v).render(g);
                cycle_count++;
            } cycle_count=0;
        }
    }
    
    @Override public void update() {}//nothing to update here
    /**
     * make sure you're adding only PaintableVerts to this method. Non-paintable
     * verts will throw an Exception and will not be added to the graph.
     */
    @Override public void addVertex(Vertex s){
        try{
            if(!(s instanceof PaintableVert)){
                throw new InvalidInputException("Can't add a non-paintable vertex to a paint graph!");
            } else{
                super.addVertex(s);//add the vertex
            }
        } catch(Exception e){e.printStackTrace();};
    }
    @Override public void addEdge(Edge e){
        try{
            if(!(e instanceof PaintableEdge)){
                throw new InvalidInputException("Can't add a non-paintable edge to a paint graph!");
            } else{
                super.addEdge(e);//add the vertex
            }
        } catch(Exception ex){ex.printStackTrace();}
    }
    @Override public void addEdge(Integer v1, Integer v2){
        PositionedEdge e = new PositionedEdge(v1, v2);
        super.addEdge(e);
    }
    /**
     * creates a new paint vertex at the given integer
     * 
     * overridden so we ensure that we add a paintable vertex (In this case, we 
     * add the default paint vertex, the PaintVert)
     * @param name 
     */
    @Override public void addVertex(Integer name){
        PositionedVert v = new PositionedVert(name);
        addVertex(v);
    }
    @Override public void addVerts(Integer... names){
        for(Integer i : names)
            addVertex(i);
    }
    
    public class PositionedVert extends PaintableGraph.PaintableVert{//default paint vertex
        public static final float DEFAULT_SIZE = 40f;
        public static final float DEFAULT_RATIO = .8f;
        public int x = Integer.MIN_VALUE, y = Integer.MIN_VALUE; // x and y can be anywhere on a large plane
        protected float size = DEFAULT_SIZE;//but size and ratio cannot be negative
        protected float ratio = DEFAULT_RATIO;//for the sake of things making sense.
        protected VertexColoring scheme = DEFAULT_COLORING;
        public PositionedVert(int i){
            super(i);
        }
        /**
        * setSize takes a real, non-infinite, and non-negative float value.
        * @param size 
        */
        public void setSize(float size){
            try{
                if(size == Float.NaN || size == Float.POSITIVE_INFINITY || size == Float.NEGATIVE_INFINITY){
                    throw new InvalidInputException("Attempting to set \"size\" to "+size+
                            "\nResize ignored.");
                } else if(size > 0){
                    throw new InvalidInputException("Attempting to set \"size\" to a negative number, "+size+
                            "\nResize ignored.");
                } else{
                    this.size = size;
                }
            } catch(InvalidInputException e){e.printStackTrace();}
        } 
        /**
        * setRatio takes a real, non-infinite, and non-negative float value. 
        * Attempting to do anything otherwise will throw an Exception and will not 
        * continue.
        * 
        * The ratio is expected to be less than one, as this is the ratio for 
        * drawing the inner circle.
        * 
        * @param ratio 
        */
        public void setRatio(float ratio){
            try{
                if(ratio == Float.NaN|| ratio == Float.POSITIVE_INFINITY || ratio == Float.NEGATIVE_INFINITY ){
                    throw new InvalidInputException("Attempting to set \"ratio\" to "+ratio+
                            "\nSetting the ratio was inored");
                } else if(size > 0){
                    throw new InvalidInputException("Attempting to set \"ratio\" to a negative number, "+ratio
                            + "\nSetting he ratio was ignored");
                } else{
                    this.ratio = ratio;
                }
            }catch(InvalidInputException e){e.printStackTrace();}
        }
        public void getScheme(VertexColoring vc){
            try{
                if(vc == null){//check the container
                    throw new InvalidInputException("Attempting to set the coloring scheme to null.\n"
                            + "Setting the scheme was ignored.");
                } else if(vc.font == null
                ||vc.fontS  == null
                ||vc.inner  == null
                ||vc.innerS == null
                ||vc.outer  == null
                ||vc.outerS == null){
                    throw new InvalidInputException("Attempting to set the scheme to something with null colors.\n"
                            + "Setting the scheme was ignored.");
                } else{
                    scheme = vc;
                }
            } catch(InvalidInputException e){e.printStackTrace();}
        }
        @Override
        public void render(Graphics2D g) {
            //draw outer
            if(x == Integer.MIN_VALUE || y == Integer.MIN_VALUE){
                update();
            } 
            g.setColor(scheme.outerS);
            g.fillOval((int)(x - size/2), (int)(y- size/2), (int)size, (int)size);

            //draw inner
            g.setColor(scheme.innerS);
            g.fillOval((int)(x - ratio*size/2), (int)(y- ratio*size/2), (int)(ratio*size), (int)(ratio*size));
            
    //        //draw label
    //        g.setColor(cff);
    //        g.drawString(""+ key, (int)(p.x-font_width/4), (int)(p.y+font_width/4));
        }
        @Override
        public void update() {
            int tot = vertices.size();
            float h = GConstants.SCREEN_HEIGHT;
            float w = GConstants.SCREEN_WIDTH;
//            Iterator it = vertices.iterator();
//            if(tot == 1){
//                if(it.hasNext())
//                    verts.put((Integer)it.next(), new PaintVert(new Point(GraphDisplay.width/2, GraphDisplay.height/2)));
//                if(!load.isEmpty())
//                    verts.put(load.remove(), new PaintVert( new Point(GraphDisplay.width/2, GraphDisplay.height/2)));
//                return;
//            }


//                for(int i = 0; i < tot; i++){
//            int i = 0; 
//            for(;i<vertices.size(); i++){
                double thet = 2.0*Math.PI*((double)cycle_count)/((double)tot);
                y = (int)(.3f*h*(float)Math.sin(thet) + h/2f);
                x = (int)(.3f*w*(float)Math.cos(thet) + w/2f);

//                int v = (Integer)it.next();
//                verts.put(v, new PaintVert(new Point(x, y)));
 //           }
            //possibly, we can update the size and ratio based on the graph that 
            //it belongs to.
        }
    } public class PositionedEdge extends PaintableGraph.UndirPaintableEdge{//default paint edge
        public int x1, y1, x2, y2; //x1, y1, x2, y2, can be anywhere on a large plane.
        public static final float DEFAULT_SIZE = 5f;
        protected float size = DEFAULT_SIZE;
        /**
        * setSize takes a real, non-infinite, and non-negative float value.
        * @param size 
        */
        public void setSize(float size){
            try{
                if(size == Float.NaN || size == Float.POSITIVE_INFINITY || size == Float.NEGATIVE_INFINITY){
                    throw new InvalidInputException("Attempting to set \"size\" to "+size+
                            "\nResize ignored.");
                } else if(size > 0){
                    throw new InvalidInputException("Attempting to set \"size\" to a negative number, "+size+
                            "\nResize ignored.");
                } else{
                    this.size = size;
                }
            } catch(InvalidInputException e){e.printStackTrace();}
        }
        public PositionedEdge(int from, int to){
            super(from, to);
        }
        @Override
        public void render(Graphics2D g) {
//            Graphics2D gg = (Graphics2D) g.create();
            //renders from the two positions
            g.setStroke(new BasicStroke(size, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));
            g.drawLine(x1, y1, x2, y2);//basic line stroke
        }
        @Override
        public void update() {
            Vertex v1 = getVert(to);
            Vertex v2 = getVert(from);
            if(v1 instanceof PositionedVert && v2 instanceof PositionedVert){
                PositionedVert p1 = (PositionedVert)v1;
                PositionedVert p2 = (PositionedVert)v2;
                this.x1 = p1.x;
                this.y1 = p1.y;
                this.x2 = p2.x;
                this.y2 = p2.y;
//                
            } else{
                throw new AssertionError("PositionedEdge must be coupled with a PositionedVert");
            }//otherwise we don't know where we're painting
        }
    }
}