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
package edu.hawhamburg.shared.misc;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import edu.hawhamburg.shared.math.Matrix;
import edu.hawhamburg.shared.math.Vector;
import edu.hawhamburg.shared.rendering.Shader;
import edu.hawhamburg.shared.rendering.ShaderAttributes;
import edu.hawhamburg.shared.scenegraph.Camera;
import edu.hawhamburg.shared.scenegraph.InnerNode;
import edu.hawhamburg.shared.scenegraph.RootNode;
import edu.hawhamburg.shared.scenegraph.INode.RenderMode;

/**
 * Central frame for all applications - derive from this class.
 *
 * @author Philipp Jenke
 */
public abstract class Scene {
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
     * Set this flag if the projection matrix needs to be updated.
     */
    boolean needsUpdateProjectionMatrix = true;

    /**
     * Mapping between resource id and texture id;
     */
    Map<Integer, Integer> textureIds = new HashMap<Integer, Integer>();

    /**
     * Set this flag if the view matrix needs to be updated.
     */
    boolean needsUpdateViewMatrix = true;
    private List<Button> buttons = new ArrayList();

    public Scene() {
        this(100, RenderMode.REGULAR);
    }

    /**
     * Constructor
     */
    public Scene(int timerTimeout, RenderMode renderMode) {
        Log.i(Constants.LOGTAG, "Scene created.");
        Shader shader = new Shader();
        currentRenderMode = renderMode;
        root = new RootNode(shader);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timerUpdate = true;
                timerCounter++;
            }
        }, timerTimeout, timerTimeout);
    }


    // +++ THESE METHODS MUST BE IMPLEMENTED IN ALL CLASSE DERIVING FROM SCENE +++

    /**
     * Override this method with all the scene onSetup, e.g. setting up the scene graph
     */
    public abstract void onSetup(InnerNode rootNode);

    /**
     * This method is called at each timer tick.
     */
    public abstract void onTimerTick(int counter);

    /**
     * This method is called once for each scene redraw of OpenGL ES.
     */
    public abstract void onSceneRedraw();

    // +++ END: THESE METHODS MUST BE IMPLEMENTED IN ALL CLASSE DERIVING FROM SCENE +++

    /**
     * Add a button to the scene.
     */
    public void addButton(Button button) {
        buttons.add(button);
    }

    /**
     * This method is called once when the OpenGL context is created.
     */
    public void init() {
        Log.i(Constants.LOGTAG, "Init: setting OpenGL parameters.");
        String version_string = GLES20.glGetString(GLES20.GL_VERSION);
        Log.i(Constants.LOGTAG, "OpenGL-Version: " + version_string);
        int[] number = {0};
        GLES20.glGetIntegerv(GLES20.GL_STENCIL_BITS, number, 0);
        Log.i(Constants.LOGTAG, "Stencil buffer bits: " + number[0]);
        GLES20.glGetIntegerv(GLES20.GL_DEPTH_BITS, number, 0);
        Log.i(Constants.LOGTAG, "Depth buffer bits: " + number[0]);

        // Stencil test
        GLES20.glEnable(GLES20.GL_STENCIL_TEST);

        // Culling
        GLES20.glCullFace(GLES20.GL_BACK);
        GLES20.glFrontFace(GLES20.GL_CCW);

        // Depth Test
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        Shader.checkGlError("[before shader compile&link]");
        needsUpdateProjectionMatrix = true;
        root.getShader().compileAndLink();
        root.getShader().use();
        ShaderAttributes.getInstance().getAttributes(root.getShader().getProgram());
    }

    /**
     * Scene needs to be redrawn.
     */
    public void redraw() {

        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glEnable(GLES20.GL_BLEND);

        if (timerUpdate && root.isAnimated()) {
            root.timerTick(timerCounter);
            onTimerTick(timerCounter);
            timerUpdate = false;
        }


        // Inform inherited scene.
        onSceneRedraw();

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        if (currentRenderMode == RenderMode.REGULAR) {
            drawRegular();
        } else if (currentRenderMode == RenderMode.SHADOW_VOLUME) {
            drawShadowVolume();
        } else if (currentRenderMode == RenderMode.DEBUG_SHADOW_VOLUME) {
            drawDebugShadowVolume();
        } else {
            drawRegular();
        }

        // Draw buttons
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
       // GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        Camera.getInstance().setButtonCameraTransformation();
        ShaderAttributes.getInstance().setShaderModeParameter(Shader.ShaderMode.TEXTURE);
        for (Button button : buttons) {
            renderButton(button);
        }
        GLES20.glDisable(GLES20.GL_BLEND);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glDisable(GLES20.GL_CULL_FACE);

    }

    private void renderButton(Button button) {
        button.draw();
    }

    /**
     * Render scene regularly
     */
    private void drawRegular() {
        root.getShader().use();
        ShaderAttributes.getInstance().setCameraEyeParameter(new Vector(0, 0, 0));
        ShaderAttributes.getInstance().setLightPositionParameter(root.getLightPosition());
        Shader.checkGlError();

        // No change in stencil buffer
        GLES20.glStencilOp(GLES20.GL_KEEP, GLES20.GL_KEEP, GLES20.GL_KEEP);
        // Draw always
        GLES20.glStencilFunc(GLES20.GL_ALWAYS, 0, 255);

        getRoot().traverse(RenderMode.REGULAR, Matrix.createIdentityMatrix4());
    }

    /**
     * Render scene shadow volumes.
     */
    private void drawShadowVolume() {
        root.getShader().use();

        GLES20.glClearStencil(0);
        GLES20.glClear(GLES20.GL_STENCIL_BUFFER_BIT);

        // ************* ORIGINAL SCENE - NO LIGHTING *************

        // Stencil buffer never changed
        GLES20.glStencilOp(GLES20.GL_KEEP, GLES20.GL_KEEP, GLES20.GL_KEEP);
        // Stencil test always passed
        GLES20.glStencilFunc(GLES20.GL_ALWAYS, 0, 255);

        // onSetup shader
        ShaderAttributes.getInstance().setCameraEyeParameter(new Vector(0, 0, 0));
        ShaderAttributes.getInstance().setLightPositionParameter(root.getLightPosition());

        // Render scene w/o lighting
        root.traverse(RenderMode.DARK, Matrix.createIdentityMatrix4());


        // ************* SHADOW VOLUMES BACK FACES *************

        // Disable writes to the depth and color buffers.
        GLES20.glDepthMask(false);
        GLES20.glColorMask(false, false, false, false);

        // Use back-face culling.
        GLES20.glCullFace(GLES20.GL_BACK);

        // Stencil buffer always inc
        GLES20.glStencilOp(GLES20.GL_KEEP, GLES20.GL_KEEP, GLES20.GL_INCR);
        // Stencil test always passed
        GLES20.glStencilFunc(GLES20.GL_ALWAYS, 0, 255);

        // Render the shadow volumes (because of culling, only their front faces are
        // rendered).
        root.traverse(RenderMode.SHADOW_VOLUME, Matrix.createIdentityMatrix4());

        // ************* SHADOW VOLUMES FRONT FACES *************

        // Use front-face culling.
        GLES20.glCullFace(GLES20.GL_FRONT);

        // Set the stencil operation to decrement on depth pass.
        GLES20.glStencilOp(GLES20.GL_KEEP, GLES20.GL_KEEP, GLES20.GL_DECR);

        // Render the shadow volumes (only their back faces are rendered).
        root.traverse(RenderMode.SHADOW_VOLUME, Matrix.createIdentityMatrix4());

        // ************* ORIGINAL SCENE *************

        // No change in stencil buffer
        GLES20.glStencilOp(GLES20.GL_KEEP, GLES20.GL_KEEP, GLES20.GL_KEEP);
        // Draw if stencil value = 0
        GLES20.glStencilFunc(GLES20.GL_EQUAL, 0, 255);

        GLES20.glDepthMask(true);
        GLES20.glColorMask(true, true, true, true);
        GLES20.glCullFace(GLES20.GL_BACK);
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT);
        ShaderAttributes.getInstance().setShaderModeParameter(Shader.ShaderMode.PHONG);
        root.traverse(RenderMode.REGULAR, Matrix.createIdentityMatrix4());
    }

    /**
     * Render shadow volumes for debugging.
     */
    private void drawDebugShadowVolume() {
        root.getShader().use();
        GLES20.glEnable(GLES20.GL_BLEND);
        ShaderAttributes.getInstance().setCameraEyeParameter(new Vector(0, 0, 0));
        ShaderAttributes.getInstance().setLightPositionParameter(root.getLightPosition());
        Shader.checkGlError();

        // No change in stencil buffer
        GLES20.glStencilOp(GLES20.GL_KEEP, GLES20.GL_KEEP, GLES20.GL_KEEP);
        // Draw if stencil value is > 0
        GLES20.glStencilFunc(GLES20.GL_ALWAYS, 0, 255);

        // Regular scene
        getRoot().traverse(RenderMode.REGULAR, Matrix.createIdentityMatrix4());

        ShaderAttributes.getInstance().setShaderModeParameter(Shader.ShaderMode.NO_LIGHTING);
        getRoot().traverse(RenderMode.DEBUG_SHADOW_VOLUME, Matrix.createIdentityMatrix4());
    }

    /**
     * Return the root node of the scene graph.
     */
    public RootNode getRoot() {
        return root;
    }

    public void resize(int width, int height) {
        for (Button botton : buttons) {
            botton.invalidate();
        }
    }

    /**
     * A touch event occurred. Returns true, if the touch was handled.
     */
    public boolean onTouchDown(float x, float y) {
        // Convert to button coordinate system
        double buttonX = x / Camera.getInstance().getWidth() * 2 - 1;
        double buttonY = -y / Camera.getInstance().getHeight() * 2 + 1;
        for (Button button : buttons) {
            if (button.wasTouched(buttonX, buttonY)) {
                button.handleTouch();
                return true;
            }
        }
        return false;
    }
}
