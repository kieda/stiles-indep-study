/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package struct.graphs;

import java.util.*;

/**
 *
 * @author kieda
 */
public class Graph{
//    protected Set<Vertex> vertices;
//    protected ArrayList<Vertex> vertices;
    protected Map<Integer,Vertex> vertices;//map from the hashcode to a position on the arraylist
    protected HashSet<Edge> edges;
    public Graph(){
//        vertices = new ArrayList<Vertex>();
        vertices = new HashMap<Integer, Vertex>();
        edges = new HashSet<Edge>();
    }
    @SuppressWarnings("") public void addEdge(Edge e){
        assert vertices.containsKey(e.from)&&vertices.containsKey(e.to): "Invalid Edge " + e;
        edges.add(e);
    }
    /**
     * by default, adds an undirected edge between p1 to p2
     */
    @SuppressWarnings("") public void addEdge(Integer p1, Integer p2){
        assert vertices.containsKey(p1)&&vertices.containsKey(p2): "Invalid Edge {"+p1+", "+p2+"}";
        edges.add(new UndirEdge(p1, p2));
    }
    /**
     * removes an edge. Direction matters here, i.e. removing an undirected edge 
     * would remove all directed and undirected edges between the two points.
     * Removing an directed edge will remove all undirected edges between the two 
     * specified vertices, and will remove all directed edges with the same 
     * beginning and end.
     */
    @SuppressWarnings("") public void removeEdge(Edge e){
        assert vertices.containsKey(e.from)&&vertices.containsKey(e.to): "Invalid Edge " + e;
        edges.remove(e);
    }
    
    @SuppressWarnings("") public void removeEdge(Integer p1, Integer p2){
        assert vertices.containsKey(p1)&&vertices.containsKey(p2): "Invalid Edge {"+p1+", "+p2+"}";
        edges.remove(new UndirEdge(p1, p2));
    }
    //override this method if you need specific typing
    public void removeVertex(Vertex v){
        //get v name
        //v name -> map -> position
        if(v!=null)
            removeVertex(v.name);
    }
//    private final int MAX_RESTORE = 10;
//    private ArrayDeque<Integer> restore;
    public void removeVertex(int v){
        //get v name
        //v name -> map -> position
        
        Vertex pos = vertices.remove(new Integer(v));
        //System.out.print(" pos "+ pos+"\n");
        if(pos!=null){
//            vertices.set(pos, null); 
//            restore.add(pos);
//            if(restore.size() >= MAX_RESTORE)
//                restore();
//                    remove(
//                (int)pos //the position in the arraylist according to the map.
//                );
            Object[] n = edges.toArray();
            for(Object c : n){
                Edge nn = (Edge)c;
                if(nn.to==v || nn.from == v){
                    edges.remove(nn);
                }
            }
        }
    }
    //override this method if you need specific typing
    public void addVertex(Vertex v){
        //get v name
        //put v ono the map
        //add v on the map's position
//        Vertex k = vertices.get(v.name);
        if(v!= null && v.name!=null)
        vertices.put(v.name, v);
//        if(k!=null){
//            //if we already have the vertex mapped to
//            vertices.put(k.name, v);//replace the vertex at the given index
//        } else{//append the vertex to the end of the list
//            vertices.put(k.name, v);
//                //adds the potsition on to the map.
//                //puts it one after the last current index.
//            
//        }
    }
    public void addVerts(Integer... verts){
        for(Integer i : verts){
            addVertex(i);
        }
    }
    /**
     * default for creating a new vertex. Just adds a basic vertex at the name
     * @param v 
     */
    public void addVertex(Integer v){
        Vertex c = new Vertex(v);
        //get v name
        //put v ono the map
        //add v on the map's position
        addVertex(c);
    }
    public int size(){
        return vertices.size();
    }
    /**
     * please don't do shit you shouldn't do with this method (i.e setting the
     * vals to things that they shouldn't be.)
     */
    public ArrayList<Vertex> getVerts(){
        return new ArrayList<Vertex>(vertices.values());
    }
    public Vertex getVert(int i){
        //vertex name input
//        System.out.println((int)positions.get(i));
        return vertices.get(
                    i 
                    //gets the index for which the name is at
                );
    }
    public Set<Edge> getEdges(){
        return edges;
    }
    public class Vertex { //wrapper class that stores info.
        Integer name;
        @Override public boolean equals(Object other){
            if(other instanceof Integer) return (Integer)other == name;
            else if(other instanceof Vertex) return ((Vertex)other).name == name;
            return false;
        } public Vertex(Integer name){this.name = name;}
        @Override public int hashCode(){
            return name;
        } @Override public String toString(){
            return ""+ name;
        }
    }
    /**in case if the client wants to make a new way for the Edges to function*/
    public interface hqeq{
        public boolean eq(Object other);
        public int hc();
    }
    public abstract class Edge implements hqeq{
        public Integer from, to;  //an edge FROM x TO y
        public Edge(Integer from, Integer to){this.from = from; this.to = to;}
//        protected abstract boolean eq(Object other);
//        protected abstract int hc();
        @Override public boolean equals(Object other){
            return eq(other);
        } 
        @Override public int hashCode(){
            return hc();
        } @Override public String toString(){
            return "{"+from+", "+to+"}";
        }
    } 
    /*Basic class used for directed edges*/
    public class DirEdge extends Edge{
        public DirEdge(Integer from, Integer to){super(from, to);}
//        /**
//        * Hash code depends on which edge is which, unlike the {@Code UndirEdge} 
//        * class.
//        */
//        @Override protected int hc(){
//            return (from<<20)^to;
//        }
        /**
        * Hash code does not depend on which edge is which. 
        * This is because it is important that an undirected edge can remove a
        * directed edge. This is sub-optimal, as edges in both directions are
        * placed in the same bucket, but that's OK I guess.
        */
        @Override public int hc(){
            return (Math.max(from, to)<<20)^Math.min(from, to);
        }
        /**
        * Non-neutral equal. Two edges are equal if and only if the edges are the
        * same, and have the same order.
        */
        @Override public boolean eq(Object other){
            //in the case we are comparing undirected edges
            if(other instanceof UndirEdge)
                return (((Edge)other).from == from 
                    &&((Edge)other).to == to)
                    ||(((Edge)other).to == from 
                    &&((Edge)other).from == to);
            if(other instanceof Edge) 
                return (((Edge)other).from == from 
                    &&((Edge)other).to == to);
            return false;
        }
    }
    /*Basic class used for undirected edges*/
    public class UndirEdge extends Edge{
        public UndirEdge(Integer from, Integer to){super(from, to);}

        /**
        * Hash code does not depend on which edge is which. It's directionless.
        */
        @Override public int hc(){
            return (Math.max(from, to)<<20)^Math.min(from, to);
        }
        /**
        * A neutral equal - two edges can be the same with the edges backwards.
        */
        @Override public boolean eq(Object other){
            if(other instanceof Edge) 
                return (((Edge)other).from == from 
                    &&((Edge)other).to == to)
                    ||(((Edge)other).to == from 
                    &&((Edge)other).from == to);
            return false;
        }
    }
}
