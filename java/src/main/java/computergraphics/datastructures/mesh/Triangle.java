/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * 
 * Base framework for "WP Computergrafik".
 */
package computergraphics.datastructures.mesh;

import computergraphics.math.Vector;

/**
 * Representation of a triangle consisting of three indices. The indices
 * reference vertices in the vertex list in a triangle mesh.
 * 
 * @author Philipp Jenke
 */
public class Triangle {

  /**
   * Indices of the vertices.
   */
  private int[] vertexIndices = { -1, -1, -1 };

  /**
   * Indices of the texture coordinates.
   */
  private int[] texCoordIndices = { -1, -1, -1 };

  /***
   * RGBA color.
   */
  private Vector color = new Vector(1, 1, 1, 1);

  /**
   * Normal of the triangle, initialized with a default direction.
   */
  private Vector normal;

  public Triangle(int a, int b, int c) {
    this(a, b, c, -1, -1, -1, new Vector(1, 0, 0));
  }

  public Triangle(int a, int b, int c, int tA, int tB, int tC) {
    this(a, b, c, tA, tB, tC, new Vector(1, 0, 0));
  }

  public Triangle(int a, int b, int c, int tA, int tB, int tC, Vector normal) {
    vertexIndices[0] = a;
    vertexIndices[1] = b;
    vertexIndices[2] = c;
    texCoordIndices[0] = tA;
    texCoordIndices[1] = tB;
    texCoordIndices[2] = tC;
    this.normal = normal;
  }

  @Override
  public String toString() {
    return String.format("(%d, %d, %d), (%d, %d, %d)", vertexIndices[0],
        vertexIndices[1], vertexIndices[2], texCoordIndices[0],
        texCoordIndices[1], texCoordIndices[2]);
  }

  public void setNormal(Vector normal) {
    this.normal.copy(normal);
  }

  public Vector getNormal() {
    return normal;
  }

  public int getVertexIndex(int index) {
    return vertexIndices[index];
  }

  public int getTexCoordIndex(int i) {
    return texCoordIndices[i];
  }

  public void setColor(Vector color) {
    this.color = color;
  }

  public Vector getColor() {
    return color;
  }
}
