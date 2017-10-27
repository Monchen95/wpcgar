/**
 * Diese Datei gehört zum Android/Java Framework zur Veranstaltung "Computergrafik für
 * Augmented Reality" von Prof. Dr. Philipp Jenke an der Hochschule für Angewandte
 * Wissenschaften (HAW) Hamburg. Weder Teile der Software noch das Framework als Ganzes dürfen
 * ohne die Einwilligung von Philipp Jenke außerhalb von Forschungs- und Lehrprojekten an der HAW
 * Hamburg verwendet werden.
 *
 * This file is part of the Android/Java framework for the course "Computer graphics for augmented
 * reality" by Prof. Dr. Philipp Jenke at the University of Applied (UAS) Sciences Hamburg. Neither
 * parts of the framework nor the complete framework may be used outside of research or student
 * projects at the UAS Hamburg.
 */
package edu.hawhamburg.shared.rendering;

import android.opengl.GLES20;

import edu.hawhamburg.shared.math.Matrix;
import edu.hawhamburg.shared.math.Vector;
import edu.hawhamburg.shared.rendering.Shader.ShaderMode;

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
  private static int locationProjectionMatrix = -1;
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
  public void getAttributes(int shaderProgram) {
    assert (shaderProgram >= 0);
    locationVertex = GLES20.glGetAttribLocation(shaderProgram, "inVertex");
    Shader.checkGlError("shader location error: inVertex");
    locationNormal = GLES20.glGetAttribLocation(shaderProgram, "inNormal");
    Shader.checkGlError("shader location error: inNormal");
    locationColor = GLES20.glGetAttribLocation(shaderProgram, "inColor");
    Shader.checkGlError("shader location error: inColor");
    locationTexCoords = GLES20.glGetAttribLocation(shaderProgram, "inTexCoords");
    Shader.checkGlError("shader location error: inTexCoords");
    locationCameraPosition = GLES20.glGetUniformLocation(shaderProgram, "camera_position");
    Shader.checkGlError("shader location error: camera_position");
    locationShaderMode = GLES20.glGetUniformLocation(shaderProgram, "shaderMode");
    Shader.checkGlError("shader location error: shaderMode");
    locationLightPosition = GLES20.glGetUniformLocation(shaderProgram, "lightPosition");
    Shader.checkGlError("shader location error: lightPosition");
    locationModelMatrix = GLES20.glGetUniformLocation(shaderProgram, "modelMatrix");
    Shader.checkGlError("shader location error: modelMatrix");
    locationViewMatrix = GLES20.glGetUniformLocation(shaderProgram, "viewMatrix");
    Shader.checkGlError("shader location error: viewMatrix");
    locationProjectionMatrix = GLES20.glGetUniformLocation(shaderProgram, "projectionMatrix");
    Shader.checkGlError("shader location error: projectionMatrix");
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

  public void setCameraEyeParameter(Vector eye) {
    if (locationCameraPosition >= 0) {
      GLES20.glUniform3f(locationCameraPosition, (float) eye.x(), (float) eye.y(), (float) eye.z());
      Shader.checkGlError();
    }
  }

  public void setLightPositionParameter(Vector lightPosition) {
    if (locationLightPosition >= 0) {
      GLES20.glUniform3f(locationLightPosition, (float) lightPosition.x(), (float) lightPosition.y(),
          (float) lightPosition.z());
      Shader.checkGlError();
    }
  }

  public void setModelMatrixParameter(Matrix modelMatrix) {
    if (locationModelMatrix >= 0) {
      GLES20.glUniformMatrix4fv(locationModelMatrix, 1, false, modelMatrix.floatData(), 0);
      // System.out.println("model matrix " + modelMatrix);
      Shader.checkGlError();
    }
  }

  public void setViewMatrixParameter(Matrix viewMatrix) {
    if (locationViewMatrix >= 0) {
      GLES20.glUniformMatrix4fv(locationViewMatrix, 1, false, viewMatrix.floatData(), 0);
      // System.out.println("view matrix " + viewMatrix);
      Shader.checkGlError();
    }
  }

  public void setProjectionMatrixParameter(Matrix projectionMatrix) {
    if (locationProjectionMatrix >= 0) {
      GLES20.glUniformMatrix4fv(locationProjectionMatrix, 1, false, projectionMatrix.floatData(), 0);
      // System.out.println("projection matrix " + projectionMatrix);
      Shader.checkGlError();
    }
  }

  public void setShaderModeParameter(ShaderMode mode) {
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
    GLES20.glUniform1i(locationShaderMode, value);
    Shader.checkGlError();
  }
}