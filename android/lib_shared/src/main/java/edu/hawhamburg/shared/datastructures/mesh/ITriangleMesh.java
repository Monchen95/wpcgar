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

import edu.hawhamburg.shared.math.AxisAlignedBoundingBox;
import edu.hawhamburg.shared.math.Vector;
import edu.hawhamburg.shared.rendering.Texture;

/**
 * Shared interface for all triangle mesh implementations.
 *
 * @author Philipp Jenke
 */
public interface ITriangleMesh {
    /**
     * Add a new vertex (given by position) to the vertex list. The new vertex is
     * appended to the end of the list.
     */
    public int addVertex(Vector position);

    /**
     * Index in vertex list.
     */
    public Vertex getVertex(int index);

    /**
     * Index in triangle, must be in 0, 1, 2.
     */
    public Vertex getVertex(AbstractTriangle triangle, int index);

    public int getNumberOfVertices();

    /**
     * Add a new triangle to the mesh with the vertex indices a, b, c. The index
     * of the first vertex is 0.
     */
    public void addTriangle(int vertexIndex1, int vertexIndex2, int vertexIndex3);

    /**
     * Add a new triangle to the mesh with the vertex indices a, b, c. The index
     * of the first vertex is 0.
     */
    public void addTriangle(AbstractTriangle t);

    /**
     * Add triangle by vertex indices and corresponding texture coordinate
     * indices.
     */
    public void addTriangle(int vertexIndex1, int vertexIndex2, int vertexIndex3,
                            int texCoordIndex1, int texCoordIndex2, int texCoordIndex3);

    public int getNumberOfTriangles();

    public AbstractTriangle getTriangle(int triangleIndex);

    /**
     * Clear mesh - remove all triangles and vertices.
     */
    public void clear();

    /**
     * Compute the triangles normals.
     */
    public void computeTriangleNormals();


    public Vector getTextureCoordinate(int index);

    /**
     * Add texture coordinate to mesh.
     */
    public void addTextureCoordinate(Vector t);

    public Texture getTexture();

    /**
     * Create a mesh of the shadow polygons.
     * <p>
     * lightPosition: Position of the light source. extend: Length of the polygons
     * shadowPolygonMesh: Result is put in there
     */
    public void createShadowPolygons(Vector lightPosition, float extend,
                                     ITriangleMesh shadowPolygonMesh);

    /**
     * Return the number of texture coordinates in the mesh.
     */
    public int getNumberOfTextureCoordinates();

    /**
     * Returns true if the mesh has a texture assigned.
     */
    public boolean hasTexture();


    /**
     * Return the bounding box of the mesh.
     */
    public AxisAlignedBoundingBox getBoundingBox();

    void setTextureName(String textureFilename);

    /**
     * Set color to all triangles and all vertices of the mesh.
     */
    public void setColor(Vector color);

    /**
     * Sets the alpha (blendding/trasparency) value for all triangles.
     */
    void setTransparency(double alpha);
}
