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

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import edu.hawhamburg.shared.misc.Constants;

/**
 * Represents an OpenGL ES texture.
 */
public class Texture {

    private Bitmap bitmap = null;

    private int textureId = -1;

    public Texture(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    /**
     * Bind the texture as current texture.
     */
    public void bind() {
        if (textureId < 0) {
            textureId = loadTexture();
        }
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        Shader.checkGlError();
    }

    /**
     * Load a texture from a resource id, return the texture id:
     *
     * @return Texture id
     */
    private int loadTexture() {
        final int[] textureHandle = new int[1];
        GLES20.glGenTextures(1, textureHandle, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        Log.i(Constants.LOGTAG, "Successfully created texture bitmap of size " + bitmap.getWidth() + "x" + bitmap.getHeight() + ".");
        Shader.checkGlError("loadTexture");
        return textureHandle[0];
    }
}
