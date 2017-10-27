package computergraphics.scenegraph;

import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL2;

import computergraphics.math.Matrix;
import computergraphics.math.Vector;
import computergraphics.rendering.RenderVertex;
import computergraphics.rendering.VertexBufferObject;

/**
 * Representation of a coordinate frame with lines for the three axis in RBG.
 */
public class CoordinateFrameNode extends LeafNode {
  private VertexBufferObject vbo = new VertexBufferObject();

  public CoordinateFrameNode() {
    this(1);
  }

  public CoordinateFrameNode(float scale) {
    List<RenderVertex> renderVertices = new ArrayList<RenderVertex>();
    renderVertices.add(new RenderVertex(new Vector(0, 0, 0), new Vector(0, 1, 0), new Vector(1, 0, 0, 1)));
    renderVertices.add(new RenderVertex(new Vector(scale, 0, 0), new Vector(0, 1, 0), new Vector(1, 0, 0, 1)));
    renderVertices.add(new RenderVertex(new Vector(0, 0, 0), new Vector(0, 1, 0), new Vector(0, 1, 0, 1)));
    renderVertices.add(new RenderVertex(new Vector(0, scale, 0), new Vector(0, 1, 0), new Vector(0, 1, 0, 1)));
    renderVertices.add(new RenderVertex(new Vector(0, 0, 0), new Vector(0, 1, 0), new Vector(0, 0, 1, 1)));
    renderVertices.add(new RenderVertex(new Vector(0, 0, scale), new Vector(0, 1, 0), new Vector(0, 0, 1, 1)));
    vbo.Setup(renderVertices, GL2.GL_LINES);
  }

  @Override
  public void drawGL(GL2 gl, RenderMode mode, Matrix modelMatrix) {
    gl.glLineWidth(6);
    vbo.draw(gl);
  }

  @Override
  public void timerTick(int counter) {
  }
}
