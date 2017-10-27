/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * 
 * Base framework for "WP Computergrafik".
 */
package computergraphics.misc;

import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.glu.GLU;

import computergraphics.math.Matrix;
import computergraphics.rendering.Shader;
import computergraphics.rendering.ShaderAttributes;
import computergraphics.rendering.Shader.ShaderMode;
import computergraphics.scenegraph.Camera;
import computergraphics.scenegraph.RootNode;
import computergraphics.scenegraph.INode.RenderMode;

import javax.swing.JFrame;

/**
 * Central frame for all applications - derive from this class.
 * 
 * @author Philipp Jenke
 */
public abstract class Scene extends JFrame {
  private static final long serialVersionUID = 2322862744369751274L;

  /**
   * 3D view object.
   */
  private final ComputergraphicsWindow view;

  /**
   * Timer object to create a game loop.
   */
  private Timer timer = new Timer();

  /**
   * Root node of the scene graph
   */
  private RootNode root;

  /**
   * Time.
   */
  private int timerCounter = 0;

  /**
   * Current render mode.
   */
  private RenderMode currentRenderMode;

  /**
   * Indicates a timer tick.
   */
  private boolean timerUpdate = false;

  /**
   * GLU instance
   */
  private GLU glu = null;

  /**
   * Set this flag if the projection matrix needs to be updated.
   */
  boolean needsUpdateProjectionMatrix = true;

  /**
   * Set this flag if the view matrix needs to be updated.
   */
  boolean needsUpdateViewMatrix = true;

  /**
   * Constructor
   */
  public Scene(int timerTimeout, Shader.ShaderMode shaderMode, RenderMode renderMode) {
    Shader shader = new Shader(shaderMode);
    Camera camera = new Camera();

    glu = new GLU();
    currentRenderMode = renderMode;

    // Work-in-progress: Use OpenGL 4
    // GLProfile glp = GLProfile.get(GLProfile.GL4);
    // final GLCapabilitiesImmutable glcaps = (GLCapabilitiesImmutable) new
    // GLCapabilities(glp);
    // final GLCapabilities tGLCapabilities = new GLCapabilities(glp);

    GLCapabilities capabilities = new GLCapabilities(GLProfile.getDefault());
    capabilities.setStencilBits(8);
    view = new ComputergraphicsWindow(capabilities, this);
    root = new RootNode(shader, camera);

    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        timerUpdate = true;
        timerCounter++;
      }
    }, timerTimeout, timerTimeout);

    getContentPane().add(view);
    view.requestFocusInWindow();

    setAlwaysOnTop(true);

    // Setup JFrame
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setTitle("WP Computergrafik");
    setSize(640, 480);
    setVisible(true);
  }

  /**
   * This method is called once when the OpenGL context is created.
   */
  public void init(GL2 gl) {
    System.out.println("Initializing OpenGL.");

    String version_string = gl.glGetString(GL2.GL_VERSION);
    System.out.println("OpenGL-Version: " + version_string);
    int[] number = { 0 };
    gl.glGetIntegerv(GL2.GL_STENCIL_BITS, number, 0);
    System.out.println("Stencil buffer bits: " + number[0]);
    gl.glGetIntegerv(GL2.GL_DEPTH_BITS, number, 0);
    System.out.println("Depth buffer bits: " + number[0]);

    // Stencil test
    gl.glEnable(GL2.GL_STENCIL_TEST);

    // Culling
    gl.glEnable(GL2.GL_CULL_FACE);
    gl.glCullFace(GL2.GL_BACK);
    gl.glFrontFace(GL2.GL_CCW);

    // Depth Test
    gl.glEnable(GL2.GL_DEPTH_TEST);

    // Blending
    gl.glEnable(GL2.GL_BLEND);
    gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);

    Shader.checkGlError(gl);

    needsUpdateProjectionMatrix = true;

    root.getShader().compileAndLink(gl);
    root.getShader().use(gl);

    ShaderAttributes.getInstance().getAttributes(gl, root.getShader().getProgram());

    setupScenegraph(gl);
  }

  /**
   * Use this method to setup the content of the scene.
   */
  public abstract void setupScenegraph(GL2 gl);

  /**
   * Event-Handler: surface changed. (non-Javadoc)
   */
  public void resize(GL2 gl, int width, int height) {
    root.getCamera().setAspectRatio((float) width / (float) height);
    gl.glViewport(0, 0, width, height);
    needsUpdateProjectionMatrix = true;
  }

  /**
   * Scene needs to be redrawn.
   */
  public void redraw(GL2 gl) {
    // define the color we want to be displayed as the "clipping wall"
    gl.glClearColor((float) root.getBackgroundColor().x(), (float) root.getBackgroundColor().y(),
        (float) root.getBackgroundColor().z(), 1.0f);

    // clear the color buffer and the depth buffer
    gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

    updateProjectionMatrix(gl);
    updateViewMatrix(gl);

    if (timerUpdate && root.isAnimated()) {
      root.timerTick(timerCounter);
      timerTick(timerCounter);
      timerUpdate = false;
    }

    if (currentRenderMode == RenderMode.REGULAR) {
      drawRegular(gl);
    } else if (currentRenderMode == RenderMode.SHADOW_VOLUME) {
      drawShadowVolume(gl);
    } else if (currentRenderMode == RenderMode.DEBUG_SHADOW_VOLUME) {
      drawDebugShadowVolume(gl);
    } else {
      drawRegular(gl);
    }
  }

  /**
   * This callback is called each time a key is pressed; can be used to handle
   * key events.
   */
  public void keyPressed(KeyEvent keyEvent) {
  }

  /**
   * This method is called at each timer tick.
   */
  public void timerTick(int counter) {
  }

  /**
   * Render scene regularly
   */
  private void drawRegular(GL2 gl) {
    root.getShader().use(gl);
    ShaderAttributes.getInstance().setCameraEyeParameter(gl, root.getCamera().getEye());
    ShaderAttributes.getInstance().setShaderModeParameter(gl, root.getShader().getMode());
    ShaderAttributes.getInstance().setLightPositionParameter(gl, root.getLightPosition());
    Shader.checkGlError(gl);

    // No change in stencil buffer
    gl.glStencilOp(GL2.GL_KEEP, GL2.GL_KEEP, GL2.GL_KEEP);
    // Draw always
    gl.glStencilFunc(GL2.GL_ALWAYS, 0, 255);

    getRoot().traverse(gl, RenderMode.REGULAR, Matrix.createIdentityMatrix4());
  }

  /**
   * Render scene shadow volumes.
   */
  private void drawShadowVolume(GL2 gl) {
    root.getShader().use(gl);

    gl.glClearStencil(0);
    gl.glClear(GL2.GL_STENCIL_BUFFER_BIT);

    // ************* ORIGINAL SCENE - NO LIGHTING *************

    // Stencil buffer never changed
    gl.glStencilOp(GL2.GL_KEEP, GL2.GL_KEEP, GL2.GL_KEEP);
    // Stencil test always passed
    gl.glStencilFunc(GL2.GL_ALWAYS, 0, 255);

    // Setup shader
    root.getShader().setMode(Shader.ShaderMode.AMBIENT_ONLY);
    ShaderAttributes.getInstance().setCameraEyeParameter(gl, root.getCamera().getEye());
    ShaderAttributes.getInstance().setShaderModeParameter(gl, root.getShader().getMode());
    ShaderAttributes.getInstance().setLightPositionParameter(gl, root.getLightPosition());

    // Render scene w/o lighting
    root.traverse(gl, RenderMode.REGULAR, Matrix.createIdentityMatrix4());

    // ************* SHADOW VOLUMS BACK FACES *************

    // Disable writes to the depth and color buffers.
    gl.glDepthMask(false);
    gl.glColorMask(false, false, false, false);

    // Use back-face culling.
    gl.glCullFace(GL2.GL_BACK);

    // Stencil buffer always inc
    gl.glStencilOp(GL2.GL_KEEP, GL2.GL_KEEP, GL2.GL_INCR);
    // Stencil test always passed
    gl.glStencilFunc(GL2.GL_ALWAYS, 0, 255);

    // Render the shadow volumes (because of culling, only their front faces are
    // rendered).
    root.traverse(gl, RenderMode.SHADOW_VOLUME, Matrix.createIdentityMatrix4());

    // ************* SHADOW VOLUMS FRONT FACES *************

    // gl.glBegin (PrimitiveType.Quads);
    // gl.glVertex3 (-0.5f, 0.5, -0.5f);
    // gl.glVertex3 (-0.5f, 0.5, 0.5f);
    // gl.glVertex3 (0.5f, 0.5, 0.5f);
    // gl.glVertex3 (0.5f, 0.5, -0.5f);
    // gl.glEnd ();
    //
    // Use front-face culling.
    gl.glCullFace(GL2.GL_FRONT);

    // Set the stencil operation to decrement on depth pass.
    gl.glStencilOp(GL2.GL_KEEP, GL2.GL_KEEP, GL2.GL_DECR);

    // Render the shadow volumes (only their back faces are rendered).
    root.traverse(gl, RenderMode.SHADOW_VOLUME, Matrix.createIdentityMatrix4());

    // ************* ORIGINAL SCENE *************

    // No change in stencil buffer
    gl.glStencilOp(GL2.GL_KEEP, GL2.GL_KEEP, GL2.GL_KEEP);
    // Draw if stencil value = 0
    gl.glStencilFunc(GL2.GL_EQUAL, 0, 255);

    gl.glDepthMask(true);
    gl.glColorMask(true, true, true, true);
    gl.glCullFace(GL2.GL_BACK);
    gl.glClear(GL2.GL_DEPTH_BUFFER_BIT);
    root.getShader().setMode(Shader.ShaderMode.PHONG);
    ShaderAttributes.getInstance().setShaderModeParameter(gl, root.getShader().getMode());
    root.traverse(gl, RenderMode.REGULAR, Matrix.createIdentityMatrix4());
  }

  /**
   * Render shadow volumes for debugging.
   */
  private void drawDebugShadowVolume(GL2 gl) {

    root.getShader().use(gl);
    ShaderAttributes.getInstance().setCameraEyeParameter(gl, root.getCamera().getEye());
    ShaderAttributes.getInstance().setShaderModeParameter(gl, root.getShader().getMode());
    ShaderAttributes.getInstance().setLightPositionParameter(gl, root.getLightPosition());
    Shader.checkGlError(gl);

    // No change in stencil buffer
    gl.glStencilOp(GL2.GL_KEEP, GL2.GL_KEEP, GL2.GL_KEEP);
    // Draw if stencil value is > 0
    gl.glStencilFunc(GL2.GL_ALWAYS, 0, 255);

    getRoot().traverse(gl, RenderMode.REGULAR, Matrix.createIdentityMatrix4());

    ShaderMode oldMode = getRoot().getShader().getMode();
    getRoot().getShader().setMode(ShaderMode.NO_LIGHTING);
    ShaderAttributes.getInstance().setShaderModeParameter(gl, root.getShader().getMode());
    getRoot().traverse(gl, RenderMode.DEBUG_SHADOW_VOLUME, Matrix.createIdentityMatrix4());
    getRoot().getShader().setMode(oldMode);
    ShaderAttributes.getInstance().setShaderModeParameter(gl, root.getShader().getMode());
  }

  /**
   * Checks if the projection matrix needs to be updated (e.g. because of a
   * window resize event).
   */
  private void updateProjectionMatrix(GL2 gl) {
    if (needsUpdateProjectionMatrix) {
      // System.out.println("Set projection matrix");
      gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
      gl.glLoadIdentity();
      glu.gluPerspective(root.getCamera().getFovy(), root.getCamera().getAspectRatio(), root.getCamera().getZNear(),
          root.getCamera().getZFar());
      needsUpdateProjectionMatrix = false;
      // System.out.println("Updating projection matrix.");
    }
  }

  /**
   * Set the current model view matrix.
   */
  private void updateViewMatrix(GL2 gl) {
    if (needsUpdateViewMatrix) {
      // System.out.println("Set modelview matrix");
      Camera cam = root.getCamera();

      gl.glMatrixMode(GL2.GL_MODELVIEW);
      gl.glLoadIdentity();

      glu.gluLookAt(cam.getEye().x(), cam.getEye().y(), cam.getEye().z(), cam.getRef().x(), cam.getRef().y(),
          cam.getRef().z(), cam.getUp().x(), cam.getUp().y(), cam.getUp().z());

      needsUpdateViewMatrix = false;
    }
  }

  /**
   * Rotate the camera left <-> right and up <-> down.
   */
  public void rotateCamera(double angleAroundUp, double angleUpDown) {
    root.getCamera().updateExtrinsicCameraParams((float) angleAroundUp, (float) angleUpDown);
    needsUpdateViewMatrix = true;
  }

  public void zoom(int factor) {
    root.getCamera().zoom(factor);
    needsUpdateViewMatrix = true;
  }

  /**
   * Return the root node of the scene graph.
   */
  protected RootNode getRoot() {
    return root;
  }

  public void keyPressedEvent(KeyEvent keyEvent) {
    int key = Character.toUpperCase(keyEvent.getKeyChar());
    switch (key) {
    case 'I':
      getRoot().getCamera().zoom(40);
      break;
    case 'O':
      getRoot().getCamera().zoom(-40);
      break;
    case 'A':
      getRoot().setAnimated(!getRoot().isAnimated());
      break;
    case 'R':
      currentRenderMode = RenderMode.REGULAR;
      break;
    case 'S':
      currentRenderMode = RenderMode.SHADOW_VOLUME;
      break;
    case 'D':
      currentRenderMode = RenderMode.DEBUG_SHADOW_VOLUME;
      break;
    }
    keyPressed(keyEvent);
  }

}
