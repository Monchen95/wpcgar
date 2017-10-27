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
package edu.hawhamburg.app.opengl;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;

import edu.hawhamburg.app.R;
import edu.hawhamburg.shared.misc.AssetPath;
import edu.hawhamburg.shared.misc.Scene;
import edu.hawhamburg.shared.rendering.GLView;
import edu.hawhamburg.shared.rendering.ObserverInteractionController;
import edu.hawhamburg.shared.rendering.OpenGLRenderer;
import edu.hawhamburg.shared.rendering.TextureManager;

/**
 * Base activity for an OpenGL ES application.
 */
public class OpenGLActivity extends Activity {

    // Our OpenGL view:
    private GLView mGlView;

    // Our renderer:
    private OpenGLRenderer mRenderer;

    private Scene scene;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_gl);

        // onSetup OpenGL
        AssetPath.getInstance().init(getAssets());

        // Application specific scene - must be set exactly here
        scene = new DefaultOpenGLScene();
        //scene = new LandscapeScene();
        //scene = new BallThrowScene();

        int depthSize = 16;
        int stencilSize = 8;
        mGlView = new GLView(this, scene, new ObserverInteractionController());
        mGlView.init(false, depthSize, stencilSize);
        mRenderer = new OpenGLRenderer(this, scene);
        mGlView.setRenderer(mRenderer);
        setContentView(mGlView);
        mGlView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

        TextureManager.getInstance().setup("edu.hawhamburg.pjenke", getApplicationContext());
        scene.onSetup(scene.getRoot());
    }

    // Called when the activity will start interacting with the user.
    @Override
    protected void onResume() {
        super.onResume();
        // Resume the GL view:
        if (mGlView != null) {
            mGlView.setVisibility(View.VISIBLE);
            mGlView.onResume();
        }
    }

    // Called when the system is about to start resuming a previous activity.
    @Override
    protected void onPause() {
        super.onPause();
        if (mGlView != null) {
            mGlView.setVisibility(View.INVISIBLE);
            mGlView.onPause();
        }
    }

}
