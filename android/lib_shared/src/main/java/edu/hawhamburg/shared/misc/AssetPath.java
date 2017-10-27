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
package edu.hawhamburg.shared.misc;

import android.content.res.AssetManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * Computes the relative path to an asset.
 * 
 * @author Philipp Jenke
 */
public class AssetPath {
  private static AssetPath instance;

  private AssetManager assetManager;

  private AssetPath(){

  }

  public static AssetPath getInstance() {
    if (instance == null) {
      instance = new AssetPath();
    }
    return instance;
  }

  public void init(AssetManager assetManager){
    this.assetManager = assetManager;
  }

  public String readTextFileToString(String relativeFilename){

    if ( assetManager == null){
      Log.i(Constants.LOGTAG, "AssetManager must be set first!");
    }

    try {
      InputStream stream = assetManager.open(relativeFilename);
      Scanner s = new Scanner(stream).useDelimiter("\\A");
      String result = s.hasNext() ? s.next() : "";
      stream.close();
      return result;
    } catch (IOException e) {
      Log.e(Constants.LOGTAG, "Failed to open file " + relativeFilename + " in assets folder.");
      return null;
    }
  }

  public InputStream readTextFileToStream(String relativeFilename){
    try {
      InputStream stream = assetManager.open(relativeFilename);
      return stream;
    } catch (IOException e) {
      Log.e(Constants.LOGTAG, "Failed to open file " + relativeFilename + " in assets folder.");
      return null;
    }
  }
}
