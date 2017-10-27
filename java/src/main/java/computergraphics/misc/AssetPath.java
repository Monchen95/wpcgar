package computergraphics.misc;

/**
 * Computes the relative path to an asset.
 * 
 * @author Philipp Jenke
 *
 */
public class AssetPath {
  /**
   * Current implementation: working-dir = project directory
   */
  private static String assetPath = "assets/";

  /**
   * Returns the relative path to the asset filename.
   */
  public static String getPathToAsset(String assetFilename) {
    return assetPath + assetFilename;
  }
}
