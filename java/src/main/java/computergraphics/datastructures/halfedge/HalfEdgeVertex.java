/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * 
 * Base framework for "WP Computergrafik".
 */
package computergraphics.datastructures.halfedge;

import computergraphics.math.Vector;

/**
 * Representation of a vertex.
 * 
 * @author Philipp Jenke
 */
public class HalfEdgeVertex{

  /**
   * 3D position of the vertex.
   */
  private final Vector position = new Vector(0, 0, 0);

  /**
   * (Normalized) normal direction of the vertex.
   */
  private Vector normal = new Vector(1, 0, 0);

  /**
   * Color value at the vertex
   */
  private Vector color = new Vector(0, 0, 0);

  /**
   * Reference to one of the outgoing half edges.
   */
  private HalfEdge halfEgde = null;

  /**
   * Constructor.
   * 
   * @param position
   *          Initial value for position.
   */
  public HalfEdgeVertex(Vector position) {
    this.position.copy(position);
  }

  /**
   * Constructor.
   * 
   * @param position
   *          Initial value for position.
   * @param normal
   *          Initial value for normal.
   */
  public HalfEdgeVertex(Vector position, Vector normal) {
    this.position.copy(position);
    this.normal.copy(normal);
  }

  /**
   * Constructor.
   * 
   * @param position
   *          Initial value for position.
   * @param normal
   *          Initial value for normal.
   */
  public HalfEdgeVertex(Vector position, Vector normal, Vector color) {
    this.position.copy(position);
    this.normal.copy(normal);
    this.color.copy(color);
  }

  public Vector getPosition() {
    return position;
  }

  public Vector getNormal() {
    return normal;
  }

  public Vector getColor() {
    return color;
  }

  public void setNormal(Vector normal) {
    this.normal.copy(normal);
  }

  public void setColor(Vector color) {
    this.color.copy(color);
  }

  public HalfEdge getHalfEdge() {
    return halfEgde;
  }

  public void setHalfEgde(HalfEdge halfEgde) {
    this.halfEgde = halfEgde;
  }

  @Override
  public String toString() {
    return "Vertex";
  }
}