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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.hawhamburg.shared.math.AxisAlignedBoundingBox;
import edu.hawhamburg.shared.math.Vector;
import edu.hawhamburg.shared.misc.Constants;
import edu.hawhamburg.shared.rendering.Texture;
import edu.hawhamburg.shared.rendering.TextureManager;

/**
 * Implementation of a indexed vertex list triangle mesh.
 */
public class TriangleMesh implements ITriangleMesh {

    /**
     * Vertices.
     */
    private List<Vertex> vertices = new ArrayList<Vertex>();

    /**
     * Triangles.
     */
    private List<Triangle> triangles = new ArrayList<Triangle>();

    /**
     * Texture coordinates.
     */
    private List<Vector> textureCoordinates = new ArrayList<Vector>();

    /**
     * Texture object, leave null if no texture is used.
     */
    private String textureName = null;

    public TriangleMesh() {
    }

    public TriangleMesh(String textureName) {
        this.textureName = textureName;
    }

    /**
     * Copy constructor
     */
    public TriangleMesh(TriangleMesh mesh) {
        // Vertices
        for (int i = 0; i < mesh.getNumberOfVertices(); i++) {
            addVertex(new Vertex(mesh.getVertex(i)));
        }
        // Texture coordinates
        for (int i = 0; i < mesh.getNumberOfTextureCoordinates(); i++) {
            addTextureCoordinate(new Vector(mesh.getTextureCoordinate(i)));
        }
        // Triangles
        for (int i = 0; i < mesh.getNumberOfTriangles(); i++) {
            addTriangle(new Triangle(mesh.getTriangle(i)));
        }

    }

    @Override
    public void clear() {
        vertices.clear();
        triangles.clear();
        textureCoordinates.clear();
    }

    @Override
    public void addTriangle(int vertexIndex1, int vertexIndex2, int vertexIndex3) {
        triangles.add(new Triangle(vertexIndex1, vertexIndex2, vertexIndex3));
    }

    @Override
    public void addTriangle(AbstractTriangle t) {
        if (t instanceof Triangle) {
            triangles.add((Triangle) t);
        } else {
            throw new IllegalArgumentException("Can only add Triangle objects.");
        }
    }

    @Override
    public int addVertex(Vector position) {
        vertices.add(new Vertex(position));
        return vertices.size() - 1;
    }

    public int addVertex(Vertex vertex) {
        vertices.add(vertex);
        return vertices.size() - 1;
    }

    @Override
    public Vertex getVertex(int index) {
        return vertices.get(index);
    }

    @Override
    public int getNumberOfTriangles() {
        return triangles.size();
    }

    @Override
    public int getNumberOfVertices() {
        return vertices.size();
    }

    @Override
    public Vertex getVertex(AbstractTriangle triangle, int index) {

        if (!(triangle instanceof Triangle)) {
            return null;
        }
        Triangle t = (Triangle) triangle;
        return vertices.get(t.getVertexIndex(index));
    }

    @Override
    public Triangle getTriangle(int triangleIndex) {
        return triangles.get(triangleIndex);
    }

    @Override
    public Vector getTextureCoordinate(int texCoordIndex) {
        return textureCoordinates.get(texCoordIndex);
    }

    @Override
    public void computeTriangleNormals() {
        for (int triangleIndex = 0; triangleIndex < getNumberOfTriangles(); triangleIndex++) {
            Triangle t = triangles.get(triangleIndex);
            Vector a = vertices.get(t.getVertexIndex(0)).getPosition();
            Vector b = vertices.get(t.getVertexIndex(1)).getPosition();
            Vector c = vertices.get(t.getVertexIndex(2)).getPosition();
            Vector normal = b.subtract(a).cross(c.subtract(a));
            double norm = normal.getNorm();
            if (norm > 1e-5) {
                normal.multiply(1.0 / norm);
            }
            t.setNormal(normal);
        }
    }

    @Override
    public void addTextureCoordinate(Vector t) {
        textureCoordinates.add(t);
    }

    @Override
    public Texture getTexture() {
        return TextureManager.getInstance().getTexture(textureName);
    }

    @Override
    public void addTriangle(int vertexIndex1, int vertexIndex2, int vertexIndex3, int texCoordIndex1, int texCoordIndex2,
                            int texCoordIndex3) {
        triangles
                .add(new Triangle(vertexIndex1, vertexIndex2, vertexIndex3, texCoordIndex1, texCoordIndex2, texCoordIndex3));
    }

    @Override
    public void createShadowPolygons(Vector lightPosition, float extend, ITriangleMesh shadowPolygonMesh) {
        shadowPolygonMesh.clear();
        List<Edge> silhouetteEdges = getSilhouette(lightPosition);
        for (int i = 0; i < silhouetteEdges.size(); i++) {
            Edge edge = silhouetteEdges.get(i);
            Vector v0 = getVertex(edge.a).getPosition();
            Vector v1 = getVertex(edge.b).getPosition();
            Vector dv0 = v0.subtract(lightPosition).getNormalized().multiply(extend);
            Vector dv1 = v1.subtract(lightPosition).getNormalized().multiply(extend);
            Vector v0Dash = v0.add(dv0);
            Vector v1Dash = v1.add(dv1);
            int v0Index = shadowPolygonMesh.addVertex(v0);
            int v1Index = shadowPolygonMesh.addVertex(v1);
            int v0DashIndex = shadowPolygonMesh.addVertex(v0Dash);
            int v1DashIndex = shadowPolygonMesh.addVertex(v1Dash);
            Triangle t1 = new Triangle(v0Index, v0DashIndex, v1DashIndex);
            t1.setColor(new Vector(0.25, 0.025, 0.75, 0.5));
            Triangle t2 = new Triangle(v0Index, v1DashIndex, v1Index);
            t2.setColor(new Vector(0.25, 0.025, 0.75, 0.5));
            shadowPolygonMesh.addTriangle(t1);
            shadowPolygonMesh.addTriangle(t2);
        }
        shadowPolygonMesh.computeTriangleNormals();
        // System.out.println("Created " + shadowPolygonMesh.getNumberOfTriangles()
        // + " shadow triangles.");
    }

    @Override
    public int getNumberOfTextureCoordinates() {
        return textureCoordinates.size();
    }

    @Override
    public boolean hasTexture() {
        return textureName != null;
    }

    /**
     * Compute the silhouette (list of edges) for a given position
     */
    public List<Edge> getSilhouette(Vector position) {
        List<Edge> silhouetteEdges = new ArrayList<Edge>();
        Map<Edge, Integer> edge2FacetMap = new HashMap<Edge, Integer>();
        for (int triangleIndex = 0; triangleIndex < triangles.size(); triangleIndex++) {
            Triangle t = triangles.get(triangleIndex);
            for (int i = 0; i < 3; i++) {
                int a = t.getVertexIndex(i);
                int b = t.getVertexIndex((i + 1) % 3);
                Edge edge = new Edge(a, b);
                if (edge2FacetMap.containsKey(edge)) {
                    // Opposite egde found
                    int oppositeTriangle = edge2FacetMap.get(edge);
                    if (isSilhouetteEdge(position, triangleIndex, oppositeTriangle, edge)) {
                        silhouetteEdges.add(edge);
                    }
                    // Debugging: if edge map is empty in the end, then the surface is
                    // closed.
                    edge2FacetMap.remove(edge);
                } else {
                    // Opposite edge not yet found
                    edge2FacetMap.put(edge, triangleIndex);
                }
            }
        }
        // System.out
        // .println("Created " + silhouetteEdges.size() + " silhouette edges.");
        return silhouetteEdges;
    }

    /**
     * Returns true if the edge between the two given triangles is a silhouette
     * edge for the given position.
     */
    private boolean isSilhouetteEdge(Vector position, int triangle1Index, int triangle2Index, Edge edge) {
        Triangle t1 = triangles.get(triangle1Index);
        Triangle t2 = triangles.get(triangle2Index);
        Vertex v1 = vertices.get(t1.getVertexIndex(0));
        Vertex v2 = vertices.get(t2.getVertexIndex(0));
        double d1 = t1.getNormal().multiply(position) - t1.getNormal().multiply(v1.getPosition());
        double d2 = t2.getNormal().multiply(position) - t2.getNormal().multiply(v2.getPosition());
        if (d1 < 0) {
            edge.Flip();
        }
        return d1 * d2 < 0;
    }

    @Override
    public void setColor(Vector color) {
        if (color.getDimension() != 4) {
            Log.i(Constants.LOGTAG, "Color must be in RGBA format.");
            return;
        }
        for (Triangle triangle : triangles) {
            triangle.setColor(color);
        }
        for (Vertex vertex : vertices) {
            vertex.setColor(color);
        }
    }

    @Override
    public void setTransparency(double alpha) {
        for (Triangle triangle : triangles) {
            triangle.getColor().set(3, alpha);
        }
        for (Vertex vertex : vertices) {
            vertex.getColor().set(3, alpha);
        }
    }

    @Override
    public AxisAlignedBoundingBox getBoundingBox() {
        AxisAlignedBoundingBox bb = new AxisAlignedBoundingBox();
        for (Vertex v : vertices) {
            bb.add(v.getPosition());
        }
        return bb;
    }

    @Override
    public void setTextureName(String textureFilename) {
        this.textureName = textureFilename;
    }
}
