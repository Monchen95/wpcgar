package edu.hawhamburg.shared.datastructures.halfEdgeMesh;


import edu.hawhamburg.shared.datastructures.mesh.Vertex;
import edu.hawhamburg.shared.math.Vector;

/**
 * Created by Devran on 26.10.2017.
 */

public class HalfEdgeVertex extends Vertex {


    private HalfEdge outGoingHE;

    public HalfEdgeVertex(Vector position){
        super(position);
    }

    public HalfEdgeVertex(Vector position, HalfEdge outGoingHE){
        super(position);
        this.outGoingHE = outGoingHE;
    }

    public HalfEdge getOutGoingHE() {
        return outGoingHE;
    }

    public void setOutGoingHE(HalfEdge outGoingHE) {
        this.outGoingHE = outGoingHE;
    }

    public boolean equals(HalfEdgeVertex other){
        if(this.getPosition().equals(other.getPosition())){
            return true;
        }
        return false;
    }

}
