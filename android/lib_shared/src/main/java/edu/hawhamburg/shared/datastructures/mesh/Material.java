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

import edu.hawhamburg.shared.math.Vector;

/**
 * Represents an OBJ-file material.
 */
public class Material {
    private String name;
    private String textureFilename = null;
    private Vector color = new Vector(1, 1, 1, 1);

    public Material(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Vector getColor() {
        return color;
    }

    public void setColor(Vector color) {
        this.color = color;
    }

    public String getTextureFilename() {
        return textureFilename;
    }

    public void setTextureFilename(String filename) {
        this.textureFilename = filename;
    }

    @Override
    public String toString() {
        return name + ": " + color + ", " + textureFilename;
    }
}
