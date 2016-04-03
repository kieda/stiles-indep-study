package struct.cellular;
public class ModularAutomaton {
    final float MOD = 127;
    ModCell this_cell;
    ModCell[] neigh;
    public ModularAutomaton(){
        this_cell = new ModCell();
        this_cell.density = 0;
        neigh = (ModCell[])this_cell.getNeighborCells();
        neigh = (ModCell[])this_cell.constrain_cell_outer(neigh);
    }
    class ModCell extends Cell{
        float density;
        @Override
        public void updateNeighboringCells() {
            for(ModCell c: neigh){
                c.addDensity(this_cell);
            }
        }
        public float getDensity(){
            return density;
        }
        void addDensity(ModCell c1){
            c1.density = (c1.density + this_cell.density)%MOD;
            //add c2 to c1
        }
    }
}
