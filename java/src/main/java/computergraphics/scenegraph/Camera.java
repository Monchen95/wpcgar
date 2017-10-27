/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * 
 * Base framework for "WP Computergrafik".
 */
package computergraphics.scenegraph;

import computergraphics.math.Matrix;
import computergraphics.math.Vector;

/**
 * Represents a camera.
 * 
 * @author Philipp Jenke
 */
public class Camera {

  /**
   * Near clipping distance.
   */
  private double zNear;

  /**
   * Far clipping distance.
   */
  private double zFar;

  /**
   * Opening angle of the camera (in degrees).
   */
  private double fovy;

  /**
   * Position of the eye.
   */
  private Vector eye;

  /**
   * Reference point of the camera (look at)
   */
  private Vector ref;

  /**
   * Up-vector of the camera.
   */
  private Vector up;

  /**
   * Window aspect ratio.
   */
  private float aspectRatio;

  /**
   * OpenGL view matrix.
   */
  private Matrix viewMatrix;

  public Camera() {
    eye = new Vector(0, 0, 5);
    ref = new Vector(0, 0, 0);
    up = new Vector(0, 1, 0);
    aspectRatio = 1;
    fovy = 45.0f;
    zNear = 0.1;
    zFar = 10.0;
    viewMatrix = makeLookUpMatrix();
  }

  /**
   * Create an OpenGL lookup-matrix.
   */
  private Matrix makeLookUpMatrix() {
    Vector y = up.getNormalized();
    Vector z = eye.subtract(ref).getNormalized();
    Vector x = y.cross(z).getNormalized();
    y = z.cross(x).getNormalized();
    Matrix R = new Matrix(x.x(), y.x(), z.x(), 0, x.y(), y.y(), z.y(), 0, x.z(),
        y.z(), z.z(), 0, 0, 0, 0, 1);
    Matrix T = new Matrix(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, -eye.x(),
        -eye.y(), -eye.z(), 1);
    return T.multiply(R);
  }

  /**
   * Update the camera eye point by rotation angles.
   */
  public void updateExtrinsicCameraParams(float alpha, float beta) {
    Vector dir = eye.subtract(ref);
    // Rotate around up-vector
    eye = Matrix.createRotationMatrix3(up, alpha).multiply(dir).add(ref);
    // Rotate around side-vector
    dir = eye.subtract(ref);
    Vector side = dir.cross(up);
    side.normalize();
    eye = Matrix.createRotationMatrix3(side, -beta).multiply(dir).add(ref);
    // Fix up-vector
    dir = ref.subtract(eye);
    side = dir.cross(up);
    side.normalize();
    up = side.cross(dir);
    up.normalize();
    // Update LookAt
    viewMatrix = makeLookUpMatrix();
  }

  /**
   * Zoom in/out.
   */
  public void zoom(int factor) {
    Vector dir = ref.subtract(eye);
    eye = eye.add(dir.multiply(0.002f * factor));
    viewMatrix = makeLookUpMatrix();
  }

  public Vector getEye() {
    return eye;
  }

  public Vector getRef() {
    return ref;
  }

  public Vector getUp() {
    return up;
  }

  public void setEye(Vector e) {
    eye = new Vector(e);
  }

  public void setRef(Vector e) {
    ref = new Vector(e);
  }

  public void setUp(Vector e) {
    up = new Vector(e);
  }

  public double getAspectRatio() {
    return aspectRatio;
  }

  public void setAspectRatio(float aspectRatio) {
    this.aspectRatio = aspectRatio;
  }

  public Matrix getViewMatrix() {
    return viewMatrix;
  }

  public double getFovy() {
    return fovy;
  }

  public double getZNear() {
    return zNear;
  }

  public double getZFar() {
    return zFar;
  }

  @Override
  public String toString() {
    return "eye: " + eye + "\nref: " + ref + "\nup: " + up;
  }
}
