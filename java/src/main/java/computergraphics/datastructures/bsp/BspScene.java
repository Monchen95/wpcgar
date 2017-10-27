/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * 
 * Base framework for "WP Computergrafik".
 */

package computergraphics.datastructures.bsp;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL2;

import computergraphics.datastructures.bsp.BspNode;
import computergraphics.datastructures.bsp.BspTreeNode;
import computergraphics.math.Vector;
import computergraphics.misc.Scene;
import computergraphics.rendering.Shader;
import computergraphics.scenegraph.INode.RenderMode;

/**
 * Scene and application for the BSP tree example.
 * 
 * @author Philipp Jenke
 */
public class BspScene extends Scene {

  private static final long serialVersionUID = 6506789797991105075L;

  /**
   * Scene graph BSP node
   */
  private BspNode node;

  public BspScene() {
    super(100, Shader.ShaderMode.PHONG, RenderMode.REGULAR);
  }

  @Override
  public void setupScenegraph(GL2 gl) {
    getRoot().setLightPosition(new Vector(1, 1, 1));
    getRoot().setAnimated(true);
    getRoot().setBackgroundColor(new Vector(0.25, 0.25, 0.25, 1));
    gl.glLineWidth(5);
    gl.glPointSize(5);

    // Create data
    int numberOfPoints = 10;
    List<Vector> points = new ArrayList<Vector>();
    List<Integer> pointIndices = new ArrayList<Integer>();
    for (int i = 0; i < numberOfPoints; i++) {
      points.add(new Vector((float) (2 * Math.random() - 1), (float) (2 * Math.random() - 1), 0));
      pointIndices.add(i);
    }

    // Create tree
    BspTreeToolsDummy tools = new BspTreeToolsDummy();
    BspTreeNode rootNode = tools.createBspTree(null, points, pointIndices);

    // Add result to scne graph
    if (rootNode != null) {
      Vector observer = new Vector(1, 1, 0);
      List<Integer> back2FrontSorted = tools.getBackToFront(rootNode, points, observer);
      node = new BspNode(rootNode, points, back2FrontSorted, observer);
      getRoot().addChild(node);
    }
  }

  @Override
  public void keyPressed(KeyEvent keyEvent) {
    char keyCode = keyEvent.getKeyChar();
    switch (keyCode) {
    case 'p':
      node.showPoints = !node.showPoints;
      break;
    case 'e':
      node.showElements = !node.showElements;
      break;
    case 'l':
      node.showPlanes = !node.showPlanes;
      break;
    case 'b':
      node.showBackToFront = !node.showBackToFront;
      break;
    }
  }

  @Override
  public void timerTick(int counter) {
  }

  public static void main(String[] args) {
    new BspScene();
  }
}
