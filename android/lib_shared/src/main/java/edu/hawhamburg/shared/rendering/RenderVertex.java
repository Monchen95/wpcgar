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

import edu.hawhamburg.shared.math.Vector;

/**
 * Helping data structure to represent a render vertex in a @VertexBufferObject.
 * 
 * @author Philipp Jenke
 */
public class RenderVertex {

  public RenderVertex(Vector position, Vector normal, Vector color) {
    this(position, normal, color, new Vector(0,0));
  }
  
  public RenderVertex(Vector position, Vector normal, Vector color, Vector texCoords) {
    this.position = position;
    this.normal = normal;
    this.color = color;
    this.texCoords = texCoords;
  }

  /**
   * 3D position.
   */
  public Vector position;

  /**
   * 3D normal.
   */
  public Vector normal;

  /**
   * 4D color.
   */
  public Vector color;
  
  /**
   * 2D Texture coordinate
   */
  public Vector texCoords;

}
