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
 * Representation of a triangle consisting of three indices. The indices
 * reference vertices in the vertex list in a triangle mesh.
 *
 * @author Philipp Jenke
 */
public class Triangle extends AbstractTriangle {

    /**
     * Indices of the vertices.
     */
    private int[] vertexIndices = {-1, -1, -1};

    public Triangle(int a, int b, int c) {
        this(a, b, c, -1, -1, -1, new Vector(1, 0, 0));
    }

    public Triangle(int a, int b, int c, int tA, int tB, int tC) {
        this(a, b, c, tA, tB, tC, new Vector(1, 0, 0));
    }

    public Triangle(int a, int b, int c, int tA, int tB, int tC, Vector normal) {
        super(tA, tB, tC, normal);
        vertexIndices[0] = a;
        vertexIndices[1] = b;
        vertexIndices[2] = c;
    }

    public Triangle(Triangle triangle) {
        super(triangle);
        vertexIndices[0] = triangle.vertexIndices[0];
        vertexIndices[1] = triangle.vertexIndices[1];
        vertexIndices[2] = triangle.vertexIndices[2];
    }

    @Override
    public String toString() {
        return String.format("Triangle");
    }

    public int getVertexIndex(int index) {
        return vertexIndices[index];
    }

    /**
     * Add an offset to all vertex indices.
     */
    public void addVertexIndexOffset(int offset) {
        for (int i = 0; i < 3; i++) {
            vertexIndices[i] += offset;
        }
    }
}
