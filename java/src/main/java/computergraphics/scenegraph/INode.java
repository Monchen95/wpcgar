/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * 
 * Base framework for "WP Computergrafik".
 */
package computergraphics.scenegraph;

import com.jogamp.opengl.GL2;

import computergraphics.math.Matrix;

/**
 * Parent class for all scene graph nodes.
 * 
 * @author Philipp Jenke
 */
public abstract class INode {

  private INode parentNode = null;

  /**
   * This enum allows to pass different render mode states to the drawing
   * routines. The default state is REGULAR.
   */
  public enum RenderMode {
    REGULAR, SHADOW_VOLUME, DEBUG_SHADOW_VOLUME
  };

  /**
   * This method is called to draw the node using OpenGL commands. Override in
   * implementing nodes. Do not forget to call the same method for the children.
   */
  public abstract void traverse(GL2 gl, RenderMode mode, Matrix modelMatrix);

  /**
   * Timer tick event.
   */
  public abstract void timerTick(int counter);

  /**
   * Every node must know its root node
   */
  public RootNode getRootNode() {
    return parentNode.getRootNode();
  }

  /**
   * Every node must know its root node
   */
  public void setParentNode(INode parentNode) {
    this.parentNode = parentNode;
  }

}
