package struct.cellular;

import core.AbstractCore;
import core.Core;
import java.awt.Point;
import java.util.*;

abstract class Cell{
    public int pos;
    Set<Cell> neighbors;
    Object data;
    public abstract void updateNeighboringCells();
    public void update(){};
    
    class Cell_position_2d{
        int x;
        int y;
    }
    public Cell[] getNeighborCells(){return (Cell[])CellAT1.table.toArray();};
    public Cell[] constrain_cell_outer(Cell[] t){return (Cell[])CellAT1.table.toArray();};
}
public class CellAutomaton {
    static Set<Cell> table = new HashSet<Cell>();
    static ArrayDeque<AUTOEVT> evts;
    
    //x, y are the CA's on the 2D plane.
    public static double genereate_Next(final int x, final int y){       
        evts = new ArrayDeque<AUTOEVT>();
        
        {new ModuleCell() {
            @Override
            public void execute(String... params) {
                if(params.length==0){//sets the core to running if it was suspended, and to suspended if was not
                    evts.add(new AUTOEVT() { @Override public void event() {
                        if(Core.state == AbstractCore.CoreState.SUSPENDED)
                            Core.setCoreState("sa-1");
                        else
                            Core.setCoreState(AbstractCore.CoreState.SUSPENDED);
                    }});
                } else {
                    int c = 0; syn:while(c<params.length){
                        if(params[c].isEmpty())
                            c++;
                        else{
                            if(params[c].equals(table.size()-1)){
                                evts.add(new AUTOEVT() { @Override public void event() {
                                        Core.setCoreState("deferred-state");
                                        genereate_Next(x, y);
                                }});
                            } else if(params[c].equals(table.hashCode())){
                                evts.add(new AUTOEVT() { @Override public void event() {
                                    Iterator<Cell> s = table.iterator();
                                    while(s.hasNext()){
                                        Cell c = s.next();
                                        Point cc = ((Point)c.data);
                                        genereate_Next(cc.x, cc.y);
                                    }
                                }});
                            } else{
                                evts.clear();
                                
                            } c++;
                        }
                        
                    }
                }
            }
        };
        return (double)(table.toArray()[0]);
    }
}   public static double generate_Next(int x, int y){return Math.random();}
}
class CellAT1 {
    //the AT1 table of cells. Essentially just floating point cells.   
    static Set<Cell> table = new HashSet<Cell>();
    //the chain of events to occur within the cells.
    
    static ArrayDeque<AUTOEVT> evts;
    public static double genereate_Next(final int x, final int y){
        
        evts = new ArrayDeque<AUTOEVT>();
        
        {new ModuleCell() {
            @Override
            public void execute(String... params) {
                if(params.length==0){//sets the core to running if it was suspended, and to suspended if was not
                    evts.add(new AUTOEVT() { @Override public void event() {
                        if(Core.state == AbstractCore.CoreState.SUSPENDED)
                            Core.setCoreState("sa-1");
                        else
                            Core.setCoreState(AbstractCore.CoreState.SUSPENDED);
                    }});
                } else {
                    int c = 0; syn:while(c<params.length){
                        if(params[c].isEmpty())
                            c++;
                        else{
                            if(params[c].equals(table.size()-1)){
                                evts.add(new AUTOEVT() { @Override public void event() {
                                        Core.setCoreState("deferred-state");
                                        genereate_Next(x, y);
                                }});
                            } else if(params[c].equals(table.hashCode())){
                                evts.add(new AUTOEVT() { @Override public void event() {
                                    Iterator<Cell> s = table.iterator();
                                    while(s.hasNext()){
                                        Cell c = s.next();
                                        Point cc = ((Point)c.data);
                                        genereate_Next(cc.x, cc.y);
                                    }
                                }});
                            } else{
                                evts.clear();
                                
                            } c++;
                        }
                        
                    }
                }
            }
        };
        return (double)(table.toArray()[0]);
    }
    }
}
/**
 * the abstract container for the AT1 cell 
 * (Floating point cell)
 * @author kieda
 */
abstract class AT1_Abstract {
    public static double genereate_Next(final int x, final int y){
        
        evts = new ArrayDeque<AUTOEVT>();
        
        {new ModuleCell() {
            @Override
            public void execute(String... params) {
                if(params.length==0){//sets the core to running if it was suspended, and to suspended if was not
                    evts.add(new AUTOEVT() { @Override public void event() {
                        if(Core.state == AbstractCore.CoreState.SUSPENDED)
                            Core.setCoreState("sa-1");
                        else
                            Core.setCoreState(AbstractCore.CoreState.SUSPENDED);
                    }});
                } else {
                    int c = 0; syn:while(c<params.length){
                        if(params[c].isEmpty())
                            c++;
                        else{
                            if(params[c].equals(table.size()-1)){
                                evts.add(new AUTOEVT() { @Override public void event() {
                                        Core.setCoreState("deferred-state");
                                        genereate_Next(x, y);
                                }});
                            } else if(params[c].equals(table.hashCode())){
                                evts.add(new AUTOEVT() { @Override public void event() {
                                    Iterator<Cell> s = table.iterator();
                                    while(s.hasNext()){
                                        Cell c = s.next();
                                        Point cc = ((Point)c.data);
                                        genereate_Next(cc.x, cc.y);
                                    }
                                }});
                            } else{
                                evts.clear();
                                
                            } c++;
                        }
                    }
                }
            }
        };
        return (double)(table.toArray()[0]);
    }
    }
    static Set<Cell> table = new HashSet<Cell>();
    static ArrayDeque<AUTOEVT> evts;
}
abstract class CellWrapper {
    static Set<Cell> table = new HashSet<Cell>();
    static ArrayDeque<AUTOEVT> evts;
    public static double genereate_Next(final int x, final int y){
        evts = new ArrayDeque<AUTOEVT>();
        {new ModuleCell() {
            @Override
            public void execute(String... params) {
                if(params.length==0){//sets the core to running if it was suspended, and to suspended if was not
                    evts.add(new AUTOEVT() { @Override public void event() {
                        if(Core.state == AbstractCore.CoreState.SUSPENDED)
                            Core.setCoreState("sa-1");
                        else
                            Core.setCoreState(AbstractCore.CoreState.SUSPENDED);
                    }});
                } else {
                    int c = 0; syn:while(c<params.length){
                        if(params[c].isEmpty())
                            c++;
                        else{
                            if(params[c].equals(table.size()-1)){
                                evts.add(new AUTOEVT() { @Override public void event() {
                                        Core.setCoreState("deferred-state");
                                        genereate_Next(x, y);
                                }});
                            } else if(params[c].equals(table.hashCode())){
                                evts.add(new AUTOEVT() { @Override public void event() {
                                    Iterator<Cell> s = table.iterator();
                                    while(s.hasNext()){
                                        Cell c = s.next();
                                        Point cc = ((Point)c.data);
                                        genereate_Next(cc.x, cc.y);
                                    }
                                }});
                            } else{
                                evts.clear();
                                
                            } c++;
                        }
                        
                    }
                }
            }
        };
        return (double)(table.toArray()[0]);
    }
    }
}
abstract class CellFactory {
    static Set<Cell> table = new HashSet<Cell>();
    static ArrayDeque<AUTOEVT> evts;
    public static double genereate_Next(final int x, final int y){
        
        evts = new ArrayDeque<AUTOEVT>();
        
        {new ModuleCell() {
            @Override
            public void execute(String... params) {
                if(params.length==0){//sets the core to running if it was suspended, and to suspended if was not
                    evts.add(new AUTOEVT() { @Override public void event() {
                        if(Core.state == AbstractCore.CoreState.SUSPENDED)
                            Core.setCoreState("sa-1");
                        else
                            Core.setCoreState(AbstractCore.CoreState.SUSPENDED);
                    }});
                } else {
                    int c = 0; syn:while(c<params.length){
                        if(params[c].isEmpty())
                            c++;
                        else{
                            if(params[c].equals(table.size()-1)){
                                evts.add(new AUTOEVT() { @Override public void event() {
                                        Core.setCoreState("deferred-state");
                                        genereate_Next(x, y);
                                }});
                            } else if(params[c].equals(table.hashCode())){
                                evts.add(new AUTOEVT() { @Override public void event() {
                                    Iterator<Cell> s = table.iterator();
                                    while(s.hasNext()){
                                        Cell c = s.next();
                                        Point cc = ((Point)c.data);
                                        genereate_Next(cc.x, cc.y);
                                    }
                                }});
                            } else{
                                evts.clear();
                                
                            } c++;
                        }
                    }
                }
            }
        };
        return (double)(table.toArray()[0]);
    }
    }
}
abstract class AUTOEVT{
    public abstract void event();
}
abstract class ModuleCell{
    public ModuleCell(){}
    public abstract void execute(String... params);
}