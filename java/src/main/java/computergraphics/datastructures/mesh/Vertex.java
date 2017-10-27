package computergraphics.datastructures.mesh;

import computergraphics.math.Vector;

/**
 * Represents a vertex in 3-space with position and normal.
 * 
 * @author Philipp Jenke
 */
public class Vertex {

  /**
   * Vertex position in 3-space.
   */
  private Vector position = new Vector(0, 0, 0);

  /**
   * Vertex normal in 3-space.
   */
  private Vector normal = new Vector(0, 1, 0);

  public Vertex(Vector position) {
    this(position, new Vector(0, 1, 0));
  }

  public Vertex(Vector position, Vector normal) {
    this.position.copy(position);
    this.normal.copy(normal);
  }

  public Vector getPosition() {
    return position;
  }

  public Vector getNormal() {
    return normal;
  }
}
