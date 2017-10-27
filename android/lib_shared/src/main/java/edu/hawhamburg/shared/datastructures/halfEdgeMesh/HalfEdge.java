package edu.hawhamburg.shared.datastructures.halfEdgeMesh;

/**
 * Created by Devran on 26.10.2017.
 */

public class HalfEdge {
    private HalfEdgeVertex startVertex;
    private HalfEdge oppositeHE;
    private HalfEdge successorHE;
    private HalfEdgeTriangle facet;

    public HalfEdge(HalfEdgeVertex startVertex) {
        this.startVertex = startVertex;
    }

    public HalfEdge(HalfEdgeVertex startVertex, HalfEdge oppositeHE, HalfEdge successorHE, HalfEdgeTriangle facet) {
        this.startVertex = startVertex;
        this.oppositeHE = oppositeHE;
        this.successorHE = successorHE;
        this.facet = facet;
    }

    public HalfEdgeVertex getStartVertex() {
        return startVertex;
    }

    public void setStartVertex(HalfEdgeVertex startVertex) {
        this.startVertex = startVertex;
    }

    public HalfEdge getOppositeHE() {
        return oppositeHE;
    }

    public void setOppositeHE(HalfEdge oppositeHE) {
        this.oppositeHE = oppositeHE;
    }

    public HalfEdge getSuccessorHE() {
        return successorHE;
    }

    public void setSuccessorHE(HalfEdge successorHE) {
        this.successorHE = successorHE;
    }

    public HalfEdgeTriangle getFacet() {
        return facet;
    }

    public void setFacet(HalfEdgeTriangle facet) {
        this.facet = facet;
    }

}
