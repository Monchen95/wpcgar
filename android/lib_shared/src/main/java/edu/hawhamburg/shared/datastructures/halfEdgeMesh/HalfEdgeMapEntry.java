package edu.hawhamburg.shared.datastructures.halfEdgeMesh;

/**
 * Created by Devran on 29.10.2017.
 */

public class HalfEdgeMapEntry {
    protected int h1;
    protected int h2;

    public HalfEdgeMapEntry(int h1, int h2){
        this.h1=h1;
        this.h2=h2;
    }

    public boolean equals(Object o){
        if(o instanceof HalfEdgeMapEntry){
            if((h1==((HalfEdgeMapEntry) o).h1 || h1==((HalfEdgeMapEntry) o).h2 )&& (h2==((HalfEdgeMapEntry) o).h1 || h2==((HalfEdgeMapEntry) o).h2)){
                return true;
            }
        }
        return false;
    }
}
