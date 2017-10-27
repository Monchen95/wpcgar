package computergraphics.datastructures.bsp;

import java.util.ArrayList;
import java.util.List;

import computergraphics.math.Vector;

/**
 * A node in a BSP tree.
 * 
 * @author Philipp Jenke
 *
 */
public class BspTreeNode {

  /**
   * Enum for front and back orientation.
   */
  public enum Orientation {
    POSITIVE, NEGATIVE
  }

  /**
   * Child node on positive half space (in normal direction).
   */
  private BspTreeNode[] children = new BspTreeNode[2];

  /**
   * Plane point.
   */
  private Vector p;

  /**
   * Element indices in positive and negative subspace.
   */
  private List<Integer> elementsPos = new ArrayList<Integer>();
  private List<Integer> elementsNeg = new ArrayList<Integer>();

  /**
   * Plane normal
   */
  private Vector n;

  public BspTreeNode() {
  }

  /**
   * Returns true if the point is on the positive side of the plane, false
   * otherwise.
   */
  public boolean IsPositive(Vector p) {
    return p.multiply(n) - this.p.multiply(n) > 0;
  }

  public void SetChild(Orientation orientation, BspTreeNode childNode) {
    children[orientation.ordinal()] = childNode;
  }

  public BspTreeNode GetChild(Orientation orientation) {
    return children[orientation.ordinal()];
  }

  /**
   * Add an element to a specified subset (front or back).
   */
  public void AddElement(Orientation orientation, int index) {
    if (orientation == Orientation.NEGATIVE) {
      elementsNeg.add(index);
    } else {
      elementsPos.add(index);
    }
  }

  /**
   * Get the number of elements in a specified subset (front or back).
   */
  public int getNumberOfElements(Orientation orientation) {
    if (orientation == Orientation.NEGATIVE) {
      return elementsNeg.size();
    } else {
      return elementsPos.size();
    }
  }

  /**
   * Get an element in a specified subset (front or back).
   */
  public int getElement(Orientation orientation, int index) {
    if (orientation == Orientation.NEGATIVE) {
      return elementsNeg.get(index);
    } else {
      return elementsPos.get(index);
    }
  }

  public void setP(Vector p) {
    this.p = p;
  }

  public void setN(Vector n) {
    this.n = n;
  }
  
  public Vector getP() {
    return p;
  }

  public Vector getN() {
    return n;
  }
}
