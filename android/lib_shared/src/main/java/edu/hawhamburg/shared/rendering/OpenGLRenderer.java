/**
 * Diese Datei gehört zum Android/Java Framework zur Veranstaltung "Computergrafik für
 * Augmented Reality" von Prof. Dr. Philipp Jenke an der Hochschule für Angewandte
 * Wissenschaften (HAW) Hamburg. Weder Teile der Software noch das Framework als Ganzes dürfen
 * ohne die Einwilligung von Philipp Jenke außerhalb von Forschungs- und Lehrprojekten an der HAW
 * Hamburg verwendet werden.
 * <p>
 * This file is part of the Android/Java framework for the course "Computer graphics for augmented
 * reality" by Prof. Dr. Philipp Jenke at the University of Applied (UAS) Sciences Hamburg. Neither
 * parts of the framework nor the complete framework may be used outside of research or student
 * projects at the UAS Hamburg.
 */
package edu.hawhamburg.shared.rendering;

import android.app.Activity;
import android.content.res.Configuration;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import edu.hawhamburg.shared.math.Vector;
import edu.hawhamburg.shared.misc.Constants;
import edu.hawhamburg.shared.misc.Scene;
import edu.hawhamburg.shared.scenegraph.Camera;

/**
 * OpenGL-OpenGLRenderer for the application
 */
public class OpenGLRenderer implements GLSurfaceView.Renderer {
    protected Scene scene;
    protected boolean mIsPortrait = false;
    protected Activity mActivity;

    private Vector CLEAR_COLOR = new Vector(1, 1, 1, 1);

    public OpenGLRenderer(Activity activity, Scene scene) {
        mActivity = activity;
        this.scene = scene;
    }

    // Called when the surface is created or recreated.
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        scene.init();
    }

    // Called to draw the current frame.
    @Override
    public void onDrawFrame(GL10 gl) {

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // Set viewport for current view
        GLES20.glViewport(0, 0, Camera.getInstance().getWidth(), Camera.getInstance().getHeight());


        // TODO: Set scissor
        //GLES20.glScissor(viewport.getData()[0], viewport.getData()[1], viewport.getData()[2], viewport.getData()[3]);

        // Projection matrix
        float projectionMatrix[] = new float[16];
        Camera cam = Camera.getInstance();
        //Matrix.frustumM(projectionMatrix, 0, -cam.getAspectRatio(), cam.getAspectRatio(), -1, 1, cam.getZNear(), cam.getZFar());
        Matrix.perspectiveM(projectionMatrix, 0, cam.getFovyDegrees(), cam.getAspectRatio(), cam.getZNear(), cam.getZFar());

        // Renders video background replacing OpenGLRenderer.DrawVideoBackground()
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        // handle face culling, we need to detect if we are using reflection
        // to determine the direction of the culling
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glCullFace(GLES20.GL_BACK);
        edu.hawhamburg.shared.math.Matrix cgProjectionMatrix = new edu.hawhamburg.shared.math.Matrix(
                projectionMatrix[0], projectionMatrix[1], projectionMatrix[2], projectionMatrix[3],
                projectionMatrix[4], projectionMatrix[5], projectionMatrix[6], projectionMatrix[7],
                projectionMatrix[8], projectionMatrix[9], projectionMatrix[10], projectionMatrix[11],
                projectionMatrix[12], projectionMatrix[13], projectionMatrix[14], projectionMatrix[15]
        );
        Camera.getInstance().setProjectionMatrix(cgProjectionMatrix);
        scene.redraw();
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
    }

    // Called when the surface changed size.
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Log.i(Constants.LOGTAG, "Screen size: " + width + "x" + height);
        Camera.getInstance().setScreenSize(width, height);
        onConfigurationChanged();
        GLES20.glClearColor((float) CLEAR_COLOR.x(), (float) CLEAR_COLOR.y(), (float) CLEAR_COLOR.z(), (float) CLEAR_COLOR.w());
        scene.resize(width, height);
    }

    public void onConfigurationChanged() {
        updateActivityOrientation();
    }

    /**
     * Stores the orientation depending on the current resources configuration
     */
    protected void updateActivityOrientation() {
        Configuration config = mActivity.getResources().getConfiguration();

        switch (config.orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                mIsPortrait = true;
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                mIsPortrait = false;
                break;
            case Configuration.ORIENTATION_UNDEFINED:
            default:
                break;
        }

        Log.i(Constants.LOGTAG, "Activity is in "
                + (mIsPortrait ? "PORTRAIT" : "LANDSCAPE"));
    }
}
