package computergraphics.datastructures.bsp;

import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;

import computergraphics.datastructures.bsp.BspTreeNode.Orientation;
import computergraphics.math.Matrix;
import computergraphics.math.Vector;
import computergraphics.rendering.RenderVertex;
import computergraphics.rendering.VertexBufferObject;
import computergraphics.scenegraph.LeafNode;

/**
 * Scene graph node to display a BSP tree.
 * 
 * @author Philipp Jenke
 *
 */
public class BspNode extends LeafNode {

  /**
   * List of points used to create the tree.
   */
  private List<Vector> points;

  /**
   * Observer position
   */
  private Vector eye;

  /**
   * Flags controlling the visualized content.
   */
  public boolean showPoints = true;
  public boolean showPlanes = true;
  public boolean showBackToFront = true;
  public boolean showElements = true;

  private VertexBufferObject vboPoints = new VertexBufferObject();
  private VertexBufferObject vboPlanes = new VertexBufferObject();
  private VertexBufferObject vboBack2FrontPath = new VertexBufferObject();
  private VertexBufferObject vboElements = new VertexBufferObject();

  public BspNode(BspTreeNode rootNode, List<Vector> points, List<Integer> back2FrontSorted, Vector eye) {
    this.points = points;
    this.eye = eye;
    vboPoints.Setup(CreateVBOPoints(), GL.GL_POINTS);
    vboBack2FrontPath.Setup(CreateVBOBack2Front(back2FrontSorted), GL.GL_LINE_STRIP);
    vboPlanes.Setup(CreateVBOPlanes(rootNode, 0.7f), GL.GL_LINES);
    vboElements.Setup(CreateVBOElements(rootNode), GL.GL_LINES);
  }

  @Override
  public void drawGL(GL2 gl, RenderMode mode, Matrix modelMatrix) {
    if (mode == RenderMode.REGULAR) {
      if (showPlanes) {
        vboPlanes.draw(gl);
      }

      if (showPoints) {
        vboPoints.draw(gl);
      }

      if (showBackToFront) {
        vboBack2FrontPath.draw(gl);
      }

      if (showElements) {
        vboElements.draw(gl);
      }
    }
  }

  public void TimerTick(int counter) {
  }

  /**
   * Create the VBO render vertices for the data points.
   */
  private List<RenderVertex> CreateVBOPoints() {
    List<RenderVertex> renderVertices = new ArrayList<RenderVertex>();
    for (Vector p : points) {
      renderVertices.add(new RenderVertex(p, new Vector(0, 0, 1), new Vector(0, 1, 0, 1)));
    }
    renderVertices.add(new RenderVertex(eye, new Vector(0, 0, 1), new Vector(1, 1, 0, 1)));
    return renderVertices;
  }

  /**
   * Create the VBO render vertices for the back-to-front sorting lines.
   */
  private List<RenderVertex> CreateVBOBack2Front(List<Integer> sortedPoints) {
    List<RenderVertex> renderVertices = new ArrayList<RenderVertex>();
    for (int index : sortedPoints) {
      renderVertices.add(new RenderVertex(points.get(index), new Vector(0, 0, 1), new Vector(1, 1, 0, 1)));
    }
    renderVertices.add(new RenderVertex(eye, new Vector(0, 0, 1), new Vector(1, 1, 0, 1)));
    return renderVertices;
  }

  /**
   * Create the VBO render vertices for the BSP plane lines.
   */
  private List<RenderVertex> CreateVBOPlanes(BspTreeNode node, float scale) {
    List<RenderVertex> renderVertices = new ArrayList<RenderVertex>();
    if (node == null) {
      return renderVertices;
    }
    Vector tangent = new Vector(node.getN().get(1), -node.getN().get(0), 0).multiply(scale);
    renderVertices.add(new RenderVertex(node.getP().add(tangent), new Vector(0, 0, 1), new Vector(1, 1, 1, 1)));
    renderVertices.add(new RenderVertex(node.getP().subtract(tangent), new Vector(0, 0, 1), new Vector(1, 1, 1, 1)));
    renderVertices.add(new RenderVertex(node.getP(), new Vector(0, 0, 1), new Vector(1, 1, 1, 1)));
    renderVertices.add(new RenderVertex(node.getP().add(node.getN().multiply(scale * 0.3f)), new Vector(0, 0, 1),
        new Vector(1, 1, 1, 1)));

    renderVertices.addAll(CreateVBOPlanes(node.GetChild(BspTreeNode.Orientation.POSITIVE), scale * 0.5f));
    renderVertices.addAll(CreateVBOPlanes(node.GetChild(BspTreeNode.Orientation.NEGATIVE), scale * 0.5f));

    return renderVertices;
  }

  /**
   * Create the VBO for the data points in the different BSP regions.
   */
  private List<RenderVertex> CreateVBOElements(BspTreeNode node) {
    List<RenderVertex> renderVertices = new ArrayList<RenderVertex>();
    if (node == null) {
      return renderVertices;
    }
    for (int or = 0; or < 2; or++) {
      Orientation orientation = Orientation.values()[or];
      Vector color = (orientation == Orientation.NEGATIVE) ? new Vector(0, 1, 1, 1) : new Vector(1, 0, 1, 1);
      for (int i = 0; i < node.getNumberOfElements(orientation); i++) {
        int index = node.getElement(orientation, i);
        renderVertices.add(new RenderVertex(node.getP(), new Vector(0, 0, 1), color));
        renderVertices.add(new RenderVertex(points.get(index), new Vector(0, 0, 1), color));
      }
    }
    renderVertices.addAll(CreateVBOElements(node.GetChild(BspTreeNode.Orientation.POSITIVE)));
    renderVertices.addAll(CreateVBOElements(node.GetChild(BspTreeNode.Orientation.NEGATIVE)));
    return renderVertices;
  }
}
