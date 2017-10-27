/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * 
 * Base framework for "WP Computergrafik".
 */
package computergraphics.scenegraph;

import com.jogamp.opengl.GL2;

import computergraphics.math.Matrix;
import computergraphics.math.Vector;

/**
 * Translate all child nodes.
 * 
 * @author Philipp Jenke
 */
public class TranslationNode extends InnerNode {

  /**
   * Translation matrix (model matrix)
   */
  private Matrix translation;

  public TranslationNode(Vector translation) {
    this.translation = Matrix.createTranslationMatrixGl(translation);
  }

  public void setTranslation(Vector t) {
    this.translation = Matrix.createTranslationMatrixGl(t);
  }

  public void traverse(GL2 gl, RenderMode mode, Matrix modelMatrix) {
    super.traverse(gl, mode, translation.multiply(modelMatrix));
  }

  public void timerTick(int counter) {
    super.timerTick(counter);
  }

}
