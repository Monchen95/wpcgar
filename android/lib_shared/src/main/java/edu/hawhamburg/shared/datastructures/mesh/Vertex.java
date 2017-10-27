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
package edu.hawhamburg.shared.datastructures.mesh;

import edu.hawhamburg.shared.math.Vector;

/**
 * Represents a vertex in 3-space with position and normal.
 *
 * @author Philipp Jenke
 */
public class Vertex {

    /**
     * Vertex position in 3-space.
     */
    protected Vector position = new Vector(0, 0, 0);

    /**
     * Vertex normal in 3-space.
     */
    protected Vector normal = new Vector(0, 1, 0);

    /**
     * Color in RBGA format.
     */
    protected Vector color = new Vector(0.5, 0.5, 0.5, 1);

    public Vertex(Vector position) {
        this(position, new Vector(0, 1, 0));
    }

    public Vertex(Vector position, Vector normal) {
        this.position.copy(position);
        this.normal.copy(normal);
    }

    public Vertex(Vertex vertex) {
        this.position = new Vector(vertex.position);
        this.normal = new Vector(vertex.normal);
        this.color = new Vector(vertex.color);
    }

    public Vector getPosition() {
        return position;
    }

    public Vector getNormal() {
        return normal;
    }

    public void setNormal(Vector normal) {
        this.normal = normal;
    }

    public Vector getColor() {
        return color;
    }

    public void setColor(Vector color) {
        this.color = color;
    }
}
