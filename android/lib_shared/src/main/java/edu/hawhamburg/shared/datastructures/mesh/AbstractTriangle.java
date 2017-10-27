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

import android.util.Log;

import edu.hawhamburg.shared.math.Vector;
import edu.hawhamburg.shared.misc.Constants;

/**
 * Generic triangle representation
 *
 * @author Philipp Jenke
 */
public class AbstractTriangle {

    /**
     * Triangle color in RGBA format.
     */
    protected Vector color = new Vector(0.5, 0.5, 0.5, 1);

    /**
     * Facet normal
     */
    private Vector normal = new Vector(0, 1, 0);

    /**
     * Indices of the texture coordinates.
     */
    private int[] texCoordIndices = {-1, -1, -1};

    public AbstractTriangle() {
    }

    public AbstractTriangle(AbstractTriangle triangle) {
        this.color = new Vector(triangle.color);
        this.normal = new Vector(triangle.normal);
        texCoordIndices[0] = triangle.texCoordIndices[0];
        texCoordIndices[1] = triangle.texCoordIndices[1];
        texCoordIndices[2] = triangle.texCoordIndices[2];
    }

    public AbstractTriangle(int tA, int tB, int tC, Vector normal) {
        this.normal = normal;
        texCoordIndices[0] = tA;
        texCoordIndices[1] = tB;
        texCoordIndices[2] = tC;
    }

    public AbstractTriangle(Vector normal) {
        this(-1, -1, -1, normal);
    }

    public Vector getColor() {
        return color;
    }

    /**
     * Color must be a 4D vector in RGBA format.
     */
    public void setColor(Vector color) {
        if (color.getDimension() != 4) {
            Log.i(Constants.LOGTAG, "Color must be given in RGBA format!");
        }
        this.color = color;
    }

    public void setNormal(Vector normal) {
        this.normal.copy(normal);
    }

    public Vector getNormal() {
        return normal;
    }

    /**
     * i must be in 0, 1, 2
     */
    public int getTexCoordIndex(int i) {
        return texCoordIndices[i];
    }

    /**
     * Add an offset to all texture coordinates.
     */
    public void addTexCoordOffset(int offset) {
        for (int i = 0; i < 3; i++) {
            texCoordIndices[i] += offset;
        }
    }
}
