package edu.hawhamburg.shared.datastructures.halfEdgeMesh;

import edu.hawhamburg.shared.datastructures.mesh.AbstractTriangle;
import edu.hawhamburg.shared.datastructures.mesh.Triangle;

/**
 * Created by Devran on 26.10.2017.
 */

public class HalfEdgeTriangle extends AbstractTriangle {

    private HalfEdge halfEdge;

    public HalfEdgeTriangle(HalfEdge halfEdge){

        this.halfEdge = halfEdge;
    }



    public HalfEdge getHalfEdge() {
        return halfEdge;
    }

    public void setHalfEdge(HalfEdge halfEdge) {
        this.halfEdge = halfEdge;
    }

}
