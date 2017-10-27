package computergraphics.rendering;

import java.io.File;
import java.io.IOException;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.util.texture.TextureIO;

import computergraphics.misc.AssetPath;

public class Texture {

  private String filename;

  private int textureId = -1;

  public Texture(String filename) {
    this.filename = filename;
  }

  public Texture(int textureId) {
    this.textureId = textureId;
  }

  public boolean isLoaded() {
    return textureId >= 0;
  }

  /**
   * Load texture image from file and create GL texture object.
   */
  public void load(GL2 gl) {
    load(gl, filename);
  }

  /**
   * Load texture image from file and create GL texture object.
   */
  public void load(GL2 gl, String filename) {
    com.jogamp.opengl.util.texture.Texture texture = null;
    try {
      texture = TextureIO.newTexture(new File(AssetPath.getPathToAsset(filename)), true);
    } catch (GLException | IOException e) {
      System.out.println("Failed to load texture from image.");
      return;
    }
    if (texture == null) {
      System.out.println("Failed to load texture from image.");
      return;
    }
    textureId = texture.getTextureObject(gl);

    gl.glEnable(GL2.GL_TEXTURE_2D);
    gl.glBindTexture(GL2.GL_TEXTURE_2D, textureId);
    gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
    gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
    gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);
    gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);
    System.out.println("Texture " + filename + " loaded.");
  }

  /**
   * Bind the texture as current texture.
   */
  public void bind(GL2 gl) {
    gl.glBindTexture(GL2.GL_TEXTURE_2D, textureId);
  }

  public int getTextureId(GL2 gl) {
    return textureId;
  }
}
