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
package edu.hawhamburg.shared.datastructures.mesh;

/**
 * Represents an edge in a triangle mesh.
 */
public class Edge {
  /**
   * Index of teh first vertex.
   */
  public int a;

  /**
   * Index of the second vertex.
   */
  public int b;

  public Edge(int a, int b) {
    this.a = a;
    this.b = b;
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof Edge)) {
      return false;
    }
    Edge otherEdge = (Edge) other;
    return (a == otherEdge.a && b == otherEdge.b)
        || (a == otherEdge.b && b == otherEdge.a);
  }

  @Override
  public int hashCode() {
    return a + b;
  }

  @Override
  public String toString() {
    return "(" + a + ", " + b + ")";
  }

  /**
   * Flip end vertices.
   */
  public void Flip() {
    int tmp = b;
    b = a;
    a = tmp;
  }
}
