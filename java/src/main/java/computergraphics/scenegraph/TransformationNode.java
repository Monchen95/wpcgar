package computergraphics.scenegraph;

import com.jogamp.opengl.GL2;

import computergraphics.math.Matrix;

public class TransformationNode extends InnerNode {
  /**
   * Transformation matrix (model matrix), 4x4
   */
  private Matrix transformation;

  public TransformationNode(Matrix transformation) {
    this.transformation = transformation;
  }
  
  public TransformationNode() {
    this.transformation = Matrix.createIdentityMatrix4();
  }

  public void setTransformation(Matrix t) {
    this.transformation = t;
  }

  public void traverse(GL2 gl, RenderMode mode, Matrix modelMatrix) {
    super.traverse(gl, mode, transformation.multiply(modelMatrix));
  }

  public void timerTick(int counter) {
    super.timerTick(counter);
  }
}
