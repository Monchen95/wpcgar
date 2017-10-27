package computergraphics.rendering;

import com.jogamp.opengl.GL2;

import computergraphics.math.Matrix;
import computergraphics.math.Vector;
import computergraphics.rendering.Shader.ShaderMode;

/**
 * Singleton class to provide access to the shader program locations.
 * 
 * @author Philipp Jenke
 */
public class ShaderAttributes {

  /**
   * Uniform and attribute parameter location in the shader program.
   */
  private static int locationShaderMode = -1;
  private static int locationCameraPosition = -1;
  private static int locationLightPosition = -1;
  private static int locationModelMatrix = -1;
  private static int locationViewMatrix = -1;
  private static int locationVertex = -1;
  private static int locationColor = -1;
  private static int locationNormal = -1;
  private static int locationTexCoords = -1;

  /**
   * Singleton instance
   */
  private static ShaderAttributes instance = null;

  private ShaderAttributes() {
  }

  /**
   * Getter for the singleton instance.
   */
  public static ShaderAttributes getInstance() {
    if (instance == null) {
      instance = new ShaderAttributes();
    }
    return instance;
  }

  /**
   * Read the current attribute locations from the shader, must be called after
   * shader link!
   */
  public void getAttributes(GL2 gl, int shaderProgram) {
    assert (shaderProgram >= 0);
    locationVertex = gl.glGetAttribLocation(shaderProgram, "inVertex");
    locationNormal = gl.glGetAttribLocation(shaderProgram, "inNormal");
    locationColor = gl.glGetAttribLocation(shaderProgram, "inColor");
    locationTexCoords = gl.glGetAttribLocation(shaderProgram, "inTexCoords");
    locationCameraPosition = gl.glGetUniformLocation(shaderProgram, "camera_position");
    locationShaderMode = gl.glGetUniformLocation(shaderProgram, "shaderMode");
    locationLightPosition = gl.glGetUniformLocation(shaderProgram, "lightPosition");
    locationModelMatrix = gl.glGetUniformLocation(shaderProgram, "modelMatrix");
    locationViewMatrix = gl.glGetUniformLocation(shaderProgram, "viewMatrix");

    Shader.checkGlError(gl);
  }

  public int getVertexLocation() {
    return locationVertex;
  }

  public int getNormalLocation() {
    return locationNormal;
  }

  public int getColorLocation() {
    return locationColor;
  }

  public int getTexCoordsLocation() {
    return locationTexCoords;
  }

  public void setCameraEyeParameter(GL2 gl, Vector eye) {
    if (locationCameraPosition >= 0) {
      gl.glUniform3f(locationCameraPosition, (float) eye.x(), (float) eye.y(), (float) eye.z());
      Shader.checkGlError(gl);
    }
  }

  public void setLightPositionParameter(GL2 gl, Vector lightPosition) {
    if (locationLightPosition >= 0) {
      gl.glUniform3f(locationLightPosition, (float) lightPosition.x(), (float) lightPosition.y(),
          (float) lightPosition.z());
      Shader.checkGlError(gl);
    }
  }

  public void setModelMatrixParameter(GL2 gl, Matrix modelMatrix) {
    if (locationModelMatrix >= 0) {
      gl.glUniformMatrix4fv(locationModelMatrix, 1, false, modelMatrix.floatData(), 0);
      // System.out.println("model matrix " + modelMatrix);
      Shader.checkGlError(gl);
    }
  }

  public void setViewMatrixParameter(GL2 gl, Matrix viewMatrix) {
    if (locationViewMatrix >= 0) {
      gl.glUniformMatrix4fv(locationViewMatrix, 1, false, viewMatrix.floatData(), 0);
      // System.out.println("view matrix " + viewMatrix);
      Shader.checkGlError(gl);
    }
  }

  public void setShaderModeParameter(GL2 gl, ShaderMode mode) {
    if (locationShaderMode < 0) {
      return;
    }
    int value = 0;
    switch (mode) {
    case PHONG:
      value = 0;
      break;
    case TEXTURE:
      value = 1;
      break;
    case NO_LIGHTING:
      value = 2;
      break;
    case AMBIENT_ONLY:
      value = 3;
      break;
    }
    gl.glUniform1i(locationShaderMode, value);
    Shader.checkGlError(gl);
  }
}
