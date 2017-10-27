package computergraphics.datastructures.halfedge;

/**
 * A half edge has references to the next edge within the current facet, the
 * opposite edge, its start vertex and the facet it belongs to.
 * 
 * @author Philipp Jenke
 *
 */
public class HalfEdge {

  /**
   * Reference to the next edge in the mesh.
   */
  private HalfEdge next;

  /**
   * Reference to the opposite edge in the mesh.
   */
  private HalfEdge opposite;

  /**
   * Start vertex of the half edge.
   */
  private HalfEdgeVertex startVertex;

  /**
   * The half edge belongs to this facet.
   */
  private HalfEdgeTriangle facet;

  public HalfEdge getNext() {
    return next;
  }

  public void setNext(HalfEdge next) {
    this.next = next;
  }

  public HalfEdge getOpposite() {
    return opposite;
  }

  public void setOpposite(HalfEdge opposite) {
    this.opposite = opposite;
  }

  public HalfEdgeVertex getStartVertex() {
    return startVertex;
  }

  public void setStartVertex(HalfEdgeVertex startVertex) {
    this.startVertex = startVertex;
  }

  public HalfEdgeTriangle getFacet() {
    return facet;
  }

  public void setFacet(HalfEdgeTriangle facet) {
    this.facet = facet;
  }

  @Override
  public String toString() {
    return "Half Edge";
  }
}
