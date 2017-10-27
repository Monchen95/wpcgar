/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * 
 * Base framework for "WP Computergrafik".
 */
package computergraphics.datastructures.mesh;

import computergraphics.math.Vector;
import computergraphics.rendering.Texture;

/**
 * Shared interface for all triangle mesh implementations.
 * 
 * @author Philipp Jenke
 */
public interface ITriangleMesh {
  /**
   * Add a new vertex (given by position) to the vertex list. The new vertex is
   * appended to the end of the list.
   */
  public int addVertex(Vector position);
  
  public Vertex getVertex(int index);
  
  public int getNumberOfVertices();

  /**
   * Add a new triangle to the mesh with the vertex indices a, b, c. The index
   * of the first vertex is 0.
   */
  public void addTriangle(int vertexIndex1, int vertexIndex2, int vertexIndex3);
  
  /**
   * Add a new triangle to the mesh with the vertex indices a, b, c. The index
   * of the first vertex is 0.
   */
  public void addTriangle(Triangle t);

  /**
   * Add triangle by vertex indices and corresponding texture coordinate
   * indices.
   */
  public void addTriangle(int vertexIndex1, int vertexIndex2, int vertexIndex3,
      int texCoordIndex1, int texCoordIndex2, int texCoordIndex3);

  public int getNumberOfTriangles();

  public Triangle getTriangle(int triangleIndex);

  /**
   * Clear mesh - remove all triangles and vertices.
   */
  public void clear();

  /**
   * Compute the triangles normals.
   */
  public void computeTriangleNormals();
  
  /**
   * Returns a color (RGBA) for the mesh.
   * */
  public Vector getColor();
  
  /**
   * Sets the (RGBA) color.
   * */
  public void setColor(Vector color);
  

  public Vector getTextureCoordinate(int index);
  
  /**
   * Add texture coordinate to mesh.
   */
  public void addTextureCoordinate(Vector t);

  /**
   * Set a texture object for the mesh.
   */
  public void setTexture(Texture texture);

  public Texture getTexture();
  
  /**
   * Create a mesh of the shadow polygons.
   * 
   * lightPosition: Position of the light source. extend: Length of the polygons
   * shadowPolygonMesh: Result is put in there
   */
  public void createShadowPolygons(Vector lightPosition, float extend,
      ITriangleMesh shadowPolygonMesh);

  public int getNumberOfTextureCoordinates();
}
