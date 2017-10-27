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

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;

import edu.hawhamburg.shared.math.Vector;
import edu.hawhamburg.shared.misc.Constants;
import edu.hawhamburg.shared.misc.Scene;

/**
 * Android view to display OpenGL content, organized in a Scene.
 */
public class GLView extends GLSurfaceView {
    /**
     * The interaction controller handles the touch gestures.
     */
    private InteractionController interactionController;

    /**
     * Reference to the scene.
     */
    private Scene scene;

    /**
     * Remember the last touch position.
     */
    private Vector lastTouch = new Vector(-1, -1);


    // Constructor.
    public GLView(Context context, Scene scene, InteractionController interactionController) {
        super(context);
        Log.i(Constants.LOGTAG, "GLView created.");
        this.interactionController = interactionController;
        this.scene = scene;
    }

    // Initialization.
    public void init(boolean translucent, int depth, int stencil) {
        Log.i(Constants.LOGTAG, "Using OpenGL ES 2.0");
        Log.i(Constants.LOGTAG, "Using " + (translucent ? "translucent" : "opaque")
                + " GLView, depth buffer size: " + depth + ", stencil size: "
                + stencil);
        if (translucent) {
            this.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        }
        setEGLContextFactory(new ContextFactory());
        setEGLConfigChooser(translucent ? new ConfigChooser(8, 8, 8, 8, depth,
                stencil) : new ConfigChooser(5, 6, 5, 0, depth, stencil));

        interactionController.init();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        super.onTouchEvent(e);

        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (lastTouch.x() < 0 || lastTouch.y() < 0) {
                    lastTouch.set(0, x);
                    lastTouch.set(1, y);
                } else {
                    interactionController.touchMoved((int) (x - lastTouch.x()), (int) (y - lastTouch.y()));
                    lastTouch.set(0, x);
                    lastTouch.set(1, y);
                }
                break;
            case MotionEvent.ACTION_DOWN:
                lastTouch.set(0, x);
                lastTouch.set(1, y);
                scene.onTouchDown(x, y);
                break;
            case MotionEvent.ACTION_UP:
                lastTouch.set(0, -1);
                lastTouch.set(1, -1);
                break;
        }
        return true;
    }

    // Creates OpenGL contexts.
    private static class ContextFactory implements
            EGLContextFactory {
        private static int EGL_CONTEXT_CLIENT_VERSION = 0x3098;


        public EGLContext createContext(EGL10 egl, EGLDisplay display,
                                        EGLConfig eglConfig) {
            EGLContext context;

            Log.i(Constants.LOGTAG, "Creating OpenGL ES 2.0 context");
            checkEglError("Before eglCreateContext", egl);
            int[] attrib_list_gl20 = {EGL_CONTEXT_CLIENT_VERSION, 2,
                    EGL10.EGL_NONE};
            context = egl.eglCreateContext(display, eglConfig,
                    EGL10.EGL_NO_CONTEXT, attrib_list_gl20);

            checkEglError("After eglCreateContext", egl);
            return context;
        }


        public void destroyContext(EGL10 egl, EGLDisplay display,
                                   EGLContext context) {
            egl.eglDestroyContext(display, context);
        }
    }


    // Checks the OpenGL error.
    private static void checkEglError(String prompt, EGL10 egl) {
        int error;
        while ((error = egl.eglGetError()) != EGL10.EGL_SUCCESS) {
            Log.e(Constants.LOGTAG, String.format("%s: EGL error: 0x%x", prompt, error));
        }
    }
}
