/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * 
 * Base framework for "WP Computergrafik".
 */
package computergraphics.scenegraph;

import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL2;

import computergraphics.math.Matrix;
import computergraphics.math.Vector;
import computergraphics.rendering.RenderVertex;
import computergraphics.rendering.VertexBufferObject;

/**
 * Geometry node for a sphere with arbitary radius, centered at the origin.
 * 
 * @author Philipp Jenke
 */
public class SphereNode extends LeafNode {

  /**
   * Sphere radius.
   */
  private double radius;

  /**
   * Resolution (in one dimension) of the mesh.
   */
  private int resolution;

  private Vector color = new Vector(0.75, 0.25, 0.25, 1);

  /**
   * VBO.
   */
  private VertexBufferObject vbo;

  /**
   * Constructor.
   */
  public SphereNode(double radius, int resolution) {
    this.radius = radius;
    this.resolution = resolution;
    vbo = new VertexBufferObject();
    createVbo();
  }

  private void createVbo() {
    List<RenderVertex> renderVertices = new ArrayList<RenderVertex>();

    float dTheta = (float) (Math.PI / resolution);
    float dPhi = (float) (Math.PI * 2.0 / resolution);
    for (int i = 0; i < resolution; i++) {
      for (int j = 0; j < resolution; j++) {
        Vector p0 = evaluateSpherePoint(i * dTheta, j * dPhi);
        Vector p1 = evaluateSpherePoint(i * dTheta, (j + 1) * dPhi);
        Vector p2 = evaluateSpherePoint((i + 1) * dTheta, (j + 1) * dPhi);
        Vector p3 = evaluateSpherePoint((i + 1) * dTheta, j * dPhi);
        Vector normal = evaluateSpherePoint((i + 0.5f) * dTheta,
            (j + 0.5f) * dPhi).getNormalized();
        AddSideVertices(renderVertices, p0, p1, p2, p3, normal, color);
      }
    }
    vbo.Setup(renderVertices, GL2.GL_QUADS);
  }

  @Override
  public void drawGL(GL2 gl, RenderMode mode, Matrix modelMatrix) {
    if (mode == RenderMode.REGULAR) {
      vbo.draw(gl);
    }
  }

  /**
   * Compute a surface point for given sphere coordinates.
   */
  private Vector evaluateSpherePoint(float theta, float phi) {
    float x = (float) (radius * Math.sin(theta) * Math.cos(phi));
    float y = (float) (radius * Math.sin(theta) * Math.sin(phi));
    float z = (float) (radius * Math.cos(theta));
    return new Vector(x, y, z);
  }

  /**
   * Add 4 vertices to the array
   */
  private void AddSideVertices(List<RenderVertex> renderVertices, Vector p0,
      Vector p1, Vector p2, Vector p3, Vector normal, Vector color) {
    renderVertices.add(new RenderVertex(p3, normal, color));
    renderVertices.add(new RenderVertex(p2, normal, color));
    renderVertices.add(new RenderVertex(p1, normal, color));
    renderVertices.add(new RenderVertex(p0, normal, color));
  }

  public void setColor(Vector color) {
    this.color = color;
    createVbo();
  }
}
