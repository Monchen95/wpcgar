package computergraphics.math;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Representation of a vector with arbitraty dimension.
 * 
 * @author Philipp Jenke
 */
public class Vector implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * Array contaising the values of the vector. Length of the array matches the
   * vector dimension.
   */
  private double[] values;

  /**
   * Create a vector.
   * 
   * @param dimension
   *          Dimension of the created vector.
   */
  public Vector(int dimension) {
    values = new double[dimension];
  }

  /**
   * Copy contructor
   * 
   * @param other
   *          Vector to be copied from.
   */
  public Vector(Vector other) {
    this(other.getDimension());
    for (int index = 0; index < getDimension(); index++) {
      set(index, other.get(index));
    }
  }

  /**
   * Convenience constructor for 2-dimensional vectors.
   * 
   * @param x
   *          x-coordinate.
   * @param y
   *          y-coordinate.
   */
  public Vector(double x, double y) {
    this(2);
    set(0, x);
    set(1, y);
  }

  /**
   * Convenience constructor for 3-dimensional vectors.
   * 
   * @param x
   *          x-coordinate.
   * @param y
   *          y-coordinate.
   * @param z
   *          z-coordinate.
   */
  public Vector(double x, double y, double z) {
    this(3);
    set(0, x);
    set(1, y);
    set(2, z);
  }

  /**
   * Convenience constructor for 3-dimensional vectors.
   * 
   * @param x
   *          x-coordinate.
   * @param y
   *          y-coordinate.
   * @param z
   *          z-coordinate.
   * @param w
   *          w-coordinate (forth coordinate).
   */
  public Vector(double x, double y, double z, double w) {
    this(4);
    set(0, x);
    set(1, y);
    set(2, z);
    set(3, w);
  }

  /**
   * Copy coordinates of other vector.
   * 
   * @param other
   *          Vector to copy from.
   */
  public void copy(Vector other) {
    if (other.getDimension() != getDimension()) {
      throw new IllegalArgumentException();
    }
    for (int i = 0; i < getDimension(); i++) {
      set(i, other.get(i));
    }
  }

  /**
   * Getter for the dimension.
   * 
   * @return Dimension of the vector.
   */
  public int getDimension() {
    return values.length;
  }

  /**
   * Getter for a specified coordinate.
   * 
   * @param index
   *          Index of the coordinate.
   * @return Coordinate value.
   */
  public double get(int index) {
    return values[index];
  }

  /**
   * Setter for value.
   *
   * @param index
   *          Coordinate index
   * @param value
   *          new value.
   */
  public void set(int index, double value) {
    values[index] = value;
  }

  /**
   * Subtract other vector, return result as new vector.
   * 
   * @param other
   *          Vector to be subtracted.
   * @return Vector containing the result.
   */
  public Vector subtract(Vector other) {
    if (other == null || other.getDimension() != getDimension()) {
      throw new IllegalArgumentException();
    }
    Vector result = new Vector(getDimension());
    for (int index = 0; index < getDimension(); index++) {
      result.set(index, get(index) - other.get(index));
    }
    return result;
  }

  /**
   * Add other vector, return result as new vector.
   * 
   * @param other
   *          Vector to be added.
   * @return Vector containing the result.
   */
  public Vector add(Vector other) {
    if (other == null || other.getDimension() != getDimension()) {
      throw new IllegalArgumentException();
    }
    Vector result = new Vector(getDimension());
    for (int index = 0; index < getDimension(); index++) {
      result.set(index, get(index) + other.get(index));
    }
    return result;
  }

  /**
   * Getter for norm of the vector.
   * 
   * @return Norm (length) of the vector.
   */
  public double getNorm() {
    return Math.sqrt(getSqrNorm());
  }

  /**
   * Getter for squared norm of the vector.
   * 
   * @return Squared norm (squared length).
   */
  public double getSqrNorm() {
    double norm = 0;
    for (int index = 0; index < getDimension(); index++) {
      norm += get(index) * get(index);
    }
    return norm;
  }

  /**
   * Multiply other vector, compute scalar product.
   * 
   * @param other
   *          Vector to be mutiplied.
   * @return Scalar product of the two vector.
   */
  public double multiply(Vector other) {
    if (other == null || other.getDimension() != getDimension()) {
      throw new IllegalArgumentException();
    }
    double result = 0;
    for (int index = 0; index < getDimension(); index++) {
      result += get(index) * other.get(index);
    }
    return result;
  }

  /**
   * Scale vector, return result as new vector.
   * 
   * @param factor
   *          Scaling factor.
   * @return Scaled vector.
   */
  public Vector multiply(double factor) {
    Vector result = new Vector(getDimension());
    for (int index = 0; index < getDimension(); index++) {
      result.set(index, get(index) * factor);
    }
    return result;
  }

  /**
   * Compute the inner product of the vector with another vector.
   * 
   * @param other
   *          Other vector
   * @return Resulting matrix.
   */
  public Matrix innerProduct(Vector other) {
    Matrix M = new Matrix(other.getDimension(), other.getDimension());
    for (int rowIndex = 0; rowIndex < other.getDimension(); rowIndex++) {
      for (int columnIndex = 0; columnIndex < other
          .getDimension(); columnIndex++) {
        M.set(rowIndex, columnIndex, get(rowIndex) * other.get(columnIndex));
      }
    }
    return M;
  }

  /**
   * Create a normalized version of the vector, return as result.
   * 
   * @return Normalized vector.
   */
  public Vector getNormalized() {
    final double d = getNorm();
    if (Math.abs(d) < MathHelpers.EPSILON) {
      System.out.println("Cannot normalize zero-vector!");
      return this;
    }
    return this.multiply(1.0 / d);
  }

  /**
   * Normalize the vector.
   */
  public void normalize() {
    double norm = getNorm();
    for (int i = 0; i < getDimension(); i++) {
      values[i] /= norm;
    }
  }

  /**
   * Compute the cross product of two vectors (only on 3-space).
   * 
   * @param other
   *          Vector to be computed with
   * @return Cross product result vector.
   */
  public Vector cross(final Vector other) {
    if (getDimension() != 3 || other.getDimension() != 3) {
      throw new IllegalArgumentException();
    }
    Vector result = new Vector(3);
    result.set(MathHelpers.INDEX_0,
        get(MathHelpers.INDEX_1) * other.get(MathHelpers.INDEX_2)
            - get(MathHelpers.INDEX_2) * other.get(MathHelpers.INDEX_1));
    result.set(MathHelpers.INDEX_1,
        get(MathHelpers.INDEX_2) * other.get(MathHelpers.INDEX_0)
            - get(MathHelpers.INDEX_0) * other.get(MathHelpers.INDEX_2));
    result.set(MathHelpers.INDEX_2,
        get(MathHelpers.INDEX_0) * other.get(MathHelpers.INDEX_1)
            - get(MathHelpers.INDEX_1) * other.get(MathHelpers.INDEX_0));
    return result;
  }

  /**
   * Add another vector to this-object, change this coordinates.
   * 
   * @param other
   *          Vector to be added.
   */
  public void addSelf(Vector other) {
    if (other.getDimension() != getDimension()) {
      throw new IllegalArgumentException();
    }
    for (int i = 0; i < getDimension(); i++) {
      set(i, get(i) + other.get(i));
    }
  }

  /**
   * Subtract another vector from this-object, change this coordinates.
   * 
   * @param other
   *          Vector to be added.
   */
  public void subtractSelf(Vector other) {
    if (other.getDimension() != getDimension()) {
      throw new IllegalArgumentException();
    }
    for (int i = 0; i < getDimension(); i++) {
      set(i, get(i) - other.get(i));
    }
  }

  /**
   * Scale vector, change this.
   * 
   * @param d
   *          Scaling factor.
   */
  public void multiplySelf(double d) {
    for (int i = 0; i < getDimension(); i++) {
      set(i, get(i) * d);
    }
  }

  /**
   * Create array of the coordinates in float format.
   * 
   * @return Coordinate array.
   */
  public float[] floatData() {
    float[] floatData = new float[values.length];
    for (int i = 0; i < values.length; i++) {
      floatData[i] = (float) values[i];
    }
    return floatData;
  }

  /**
   * Create array of the coordinates in double format.
   * 
   * @return Coordinate array.
   */
  public double[] data() {
    double[] data = new double[values.length];
    for (int i = 0; i < values.length; i++) {
      data[i] = values[i];
    }
    return data;
  }

  /**
   * Specialized version of the toString() method with given precision for the
   * coordinates.
   * 
   * @param precision
   *          Number of digits after the . or ,
   * @return String representation of the vector.
   */
  public String toString(int precision) {
    String result = "( ";
    for (int i = 0; i < getDimension(); i++) {
      result += String.format("%." + precision + "f ", values[i]);
    }
    result += ")\n";
    return result;
  }

  @Override
  public String toString() {
    String content = "( ";
    for (int index = 0; index < getDimension(); index++) {
      content += String.format("%4.3f ", get(index));
    }
    content += ")";
    return content;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + Arrays.hashCode(values);
    return result;
  }

  @Override
  public boolean equals(Object other) {
    if (other == null || !(other instanceof Vector)) {
      return false;
    }
    Vector otherVec = (Vector) other;
    if (getDimension() != otherVec.getDimension()) {
      return false;
    }
    for (int i = 0; i < getDimension(); i++) {
      if (!MathHelpers.equals(get(i), otherVec.get(i))) {
        return false;
      }
    }
    return true;
  }

  /**
   * Set the coordinates of a 3D vector.
   */
  public void set(double x, double y, double z) {
    set(0, x);
    set(1, y);
    set(2, z);
  }

  public double x() {
    return get(0);
  }

  public double y() {
    return get(1);
  }

  public double z() {
    return get(2);
  }

  public double w() {
    return get(3);
  }

  /**
   * Create a new vector from the first 3 coordinates.
   */
  public Vector xyz() {
    return new Vector(x(), y(), z());
  }
}
