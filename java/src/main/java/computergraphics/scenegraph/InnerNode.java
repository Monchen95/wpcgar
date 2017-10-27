package computergraphics.scenegraph;

import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL2;

import computergraphics.math.Matrix;

public class InnerNode extends INode {

  /**
   * List of child nodes.
   */
  private List<INode> children = new ArrayList<INode>();

  @Override
  public void traverse(GL2 gl, RenderMode mode, Matrix modelMatrix) {
    for (INode child : children) {
      child.traverse(gl, mode, modelMatrix);
    }
  }

  @Override
  public void timerTick(int counter) {
    for (INode child : children) {
      child.timerTick(counter);
    }
  }

  /**
   * Add new child node.
   **/
  public void addChild(INode child) {
    child.setParentNode(this);
    children.add(child);
  }

}
