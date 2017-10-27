/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * 
 * Base framework for "WP Computergrafik".
 */
package computergraphics.misc;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.FPSAnimator;

import computergraphics.math.Vector;

/**
 * This class represents a view for 3D content.
 * 
 * @author Philipp Jenke
 */
public class ComputergraphicsWindow extends GLJPanel implements GLEventListener, MouseListener,
    MouseMotionListener, KeyListener {

  /**
     * 
     */
  private static final long serialVersionUID = 1L;

  /**
   * Last coordinates of the mouse
   */
  private Vector lastMouseCoordinates = new Vector(-1, -1, 0);

  /**
   * Remember the current button.
   */
  private int currentButton = -1;

  private final Scene scene;

  /**
   * Constructor
   */
  public ComputergraphicsWindow(GLCapabilities capabilities, Scene scene) {
    super(capabilities);
    this.scene = scene;

    addGLEventListener(this);
    addMouseListener(this);
    addMouseMotionListener(this);
    addKeyListener(this);

    // Start the Gl loop.
    FPSAnimator animator = new FPSAnimator(this, 60, true);
    animator.start();
  }

  @Override
  public void display(GLAutoDrawable drawable) {
    GL2 gl = drawable.getGL().getGL2();
    scene.redraw(gl);
  }

  @Override
  public void dispose(GLAutoDrawable arg0) {
  }

  @Override
  public void init(GLAutoDrawable drawable) {
    scene.init(drawable.getGL().getGL2());
  }

  @Override
  public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
    scene.resize(drawable.getGL().getGL2(), w, h);
  }

  @Override
  public void mouseClicked(MouseEvent event) {
  }

  @Override
  public void mouseEntered(MouseEvent e) {
  }

  @Override
  public void mouseExited(MouseEvent e) {
  }

  @Override
  public void mousePressed(MouseEvent event) {
    currentButton = event.getButton();
    if (event.getButton() == MouseEvent.BUTTON1) {
      lastMouseCoordinates.set(0, event.getX());
      lastMouseCoordinates.set(1, event.getY());
    }
  }

  @Override
  public void mouseReleased(MouseEvent event) {
    lastMouseCoordinates = new Vector(event.getX(), event.getY(), 0);
    currentButton = -1;
  }

  @Override
  public void mouseDragged(MouseEvent event) {
    if (currentButton == MouseEvent.BUTTON1) {
      if ((lastMouseCoordinates.get(0) > 0)
          && (lastMouseCoordinates.get(1) > 0)) {
        double deltaX =
            (float) (event.getX() - lastMouseCoordinates.get(0)) / 200.0f;
        double deltaY =
            (float) (event.getY() - lastMouseCoordinates.get(1)) / 200.0f;
        scene.rotateCamera(deltaX, deltaY);
      }
      lastMouseCoordinates.set(0, event.getX());
      lastMouseCoordinates.set(1, event.getY());
    } else if (currentButton == MouseEvent.BUTTON3) {
      if ((lastMouseCoordinates.get(0) > 0)
          && (lastMouseCoordinates.get(1) > 0)) {
        int deltaY = (int) (event.getY() - lastMouseCoordinates.get(1));
        scene.zoom(deltaY);
      }
      lastMouseCoordinates.set(0, event.getX());
      lastMouseCoordinates.set(1, event.getY());
    }
  }

  @Override
  public void mouseMoved(MouseEvent event) {

  }

  @Override
  public void keyPressed(KeyEvent e) {
  }

  @Override
  public void keyReleased(KeyEvent e) {
    scene.keyPressedEvent(e);
  }

  @Override
  public void keyTyped(KeyEvent e) {
  }

}