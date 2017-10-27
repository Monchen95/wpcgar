package computergraphics.rendering;

import java.util.Objects;

import computergraphics.math.Vector;

/**
 * Helping data structure to represent a render vertex in a @VertexBufferObject.
 * 
 * @author Philipp Jenke
 */
public class RenderVertex {

  public RenderVertex(Vector position, Vector normal, Vector color) {
    this(position, normal, color, new Vector(0,0));
  }
  
  public RenderVertex(Vector position, Vector normal, Vector color, Vector texCoords) {
    Objects.requireNonNull(position);
    Objects.requireNonNull(normal);
    Objects.requireNonNull(color);
    Objects.requireNonNull(texCoords);
    this.position = position;
    this.normal = normal;
    this.color = color;
    this.texCoords = texCoords;
  }

  /**
   * 3D position.
   */
  public Vector position;

  /**
   * 3D normal.
   */
  public Vector normal;

  /**
   * 4D color.
   */
  public Vector color;
  
  /**
   * 2D Texture coordinate
   */
  public Vector texCoords;

}
