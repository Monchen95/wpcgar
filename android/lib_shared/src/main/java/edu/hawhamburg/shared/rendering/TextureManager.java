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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import edu.hawhamburg.shared.misc.Constants;

/**
 * This managers keeps track of all registered textures which can be used in a scene.
 *
 * @author Philipp Jenke
 */
public class TextureManager {

    private Map<String, Texture> textures = new HashMap<String, Texture>();

    private static TextureManager instance = null;

    private Context context = null;

    private String modulePackageName = null;

    private TextureManager() {

    }

    public static TextureManager getInstance() {
        if (instance == null) {
            instance = new TextureManager();
        }
        return instance;
    }

    public void setup(String modulePackageName, Context context) {
        this.modulePackageName = modulePackageName;
        this.context = context;
    }

    /**
     * Return the texure for the texture name.
     */
    public Texture getTexture(String textureName) {
        if (context == null) {
            Log.i(Constants.LOGTAG, "Context must be set first!");
            return null;
        }

        if (modulePackageName == null) {
            Log.i(Constants.LOGTAG, "Module package name must be set first.");
            return null;
        }

        String noExtensionFilename = textureName;
        if (noExtensionFilename.trim().toUpperCase().contains(".JPG") ||
                noExtensionFilename.trim().toUpperCase().contains(".PNG") ||
                noExtensionFilename.trim().toUpperCase().contains(".JPEG")) {
            int index = noExtensionFilename.lastIndexOf('.');
            noExtensionFilename = noExtensionFilename.substring(0, index).trim();
        }

        if (!textures.containsKey(textureName)) {
            // Try to create the texture
            int resourceId = context.getResources().getIdentifier(noExtensionFilename, Constants.TEXURE_DIR, modulePackageName);

            if (resourceId == 0) {
                Log.i(Constants.LOGTAG, "Invalid resource id, did not load texture " + textureName);
                return null;
            }

            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;   // No pre-scaling
            final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);
            Log.i(Constants.LOGTAG, "Successfully read texture bitmap from " + textureName + ".");
            Texture texture = new Texture(bitmap);
            textures.put(textureName, texture);
        }
        return textures.get(textureName);
    }
}
