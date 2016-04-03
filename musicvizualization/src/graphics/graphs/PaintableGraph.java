/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics.graphs;

import graphics.Renderable;
import java.awt.Color;
import struct.graphs.Graph;
//import struct.graphs.Vertex;

/**
 *
 * @author kieda
 */
public abstract class PaintableGraph extends Graph implements Renderable{
    public abstract class PaintableVert extends Graph.Vertex implements Renderable{
        public PaintableVert(Integer name){super(name);}
        public final VertexColoring DEFAULT_COLORING = new VertexColoring(
            Color.LIGHT_GRAY,
            Color.DARK_GRAY,

            new Color(192, 122, 48),
            Color.BLACK,


            new Color(28, 54, 84),
            new Color(84, 45, 28)
        );
        VertexColoring g = DEFAULT_COLORING;
        class VertexColoring{
            public Color inner;
            public Color outer;
            public Color innerS;
            public Color outerS;
            public Color font;
            public Color fontS;
            public VertexColoring(
                    Color inner,
                    Color outer,
                    Color innerS,
                    Color outerS,
                    Color font,
                    Color fontS){
                this.inner = inner;
                this.outer = outer;
                this.outerS = outerS;
                this.innerS = innerS;
                this.font = font;
                this.fontS = fontS;
            }
        }
    } 
    public interface PaintableEdge extends hqeq, Renderable{} 
    public abstract class DirPaintableEdge extends DirEdge implements PaintableEdge {
        public DirPaintableEdge(Integer from, Integer to){super(from, to);}
    } public abstract class UndirPaintableEdge extends DirEdge implements PaintableEdge{
        public UndirPaintableEdge(Integer from, Integer to){super(from, to);}
    }
}
