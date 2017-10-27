package edu.hawhamburg.shared.datastructures.mesh;

import android.util.Log;

import java.util.Arrays;
import java.util.List;

import edu.hawhamburg.shared.math.Vector;
import edu.hawhamburg.shared.misc.Constants;

/**
 * Collection of tools to work on triangle meshes.
 *
 * @author Philipp Jenke
 */
public class TriangleMeshTools {

    /**
     * Recale mesh to max dimension 1 and center at origin.
     */
    public static void fitToUnitBox(List<ITriangleMesh> meshes) {
        Vector ll = new Vector(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        Vector ur = new Vector(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);

        for (ITriangleMesh mesh : meshes) {
            for (int i = 0; i < mesh.getNumberOfVertices(); i++) {
                Vector pos = mesh.getVertex(i).getPosition();
                for (int j = 0; j < 3; j++) {
                    if (pos.get(j) < ll.get(j)) {
                        ll.set(j, pos.get(j));
                    }
                    if (pos.get(j) > ur.get(j)) {
                        ur.set(j, pos.get(j));
                    }
                }
            }
        }

        Vector center = ll.add(ur).multiply(0.5);
        Vector diag = ur.subtract(ll);
        double scale = Math.max(diag.x(), Math.max(diag.y(), diag.z()));

        for (ITriangleMesh mesh : meshes) {
            for (int i = 0; i < mesh.getNumberOfVertices(); i++) {
                Vertex vertex = mesh.getVertex(i);
                vertex.getPosition().copy((vertex.getPosition().subtract(center)).multiply(1.0 / scale));
            }
            mesh.computeTriangleNormals();
        }
        Log.i(Constants.LOGTAG, "Successfully fit mesh into unit box.");
    }

    /**
     * Recale mesh to max dimension 1 and center at origin.
     */
    public static void fitToUnitBox(ITriangleMesh mesh) {
        Vector ll = new Vector(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        Vector ur = new Vector(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
        for (int i = 0; i < mesh.getNumberOfVertices(); i++) {
            Vector pos = mesh.getVertex(i).getPosition();
            for (int j = 0; j < 3; j++) {
                if (pos.get(j) < ll.get(j)) {
                    ll.set(j, pos.get(j));
                }
                if (pos.get(j) > ur.get(j)) {
                    ur.set(j, pos.get(j));
                }
            }
        }

        Vector center = ll.add(ur).multiply(0.5);
        Vector diag = ur.subtract(ll);
        double scale = Math.max(diag.x(), Math.max(diag.y(), diag.z()));

        for (int i = 0; i < mesh.getNumberOfVertices(); i++) {
            Vertex vertex = mesh.getVertex(i);
            vertex.getPosition().copy((vertex.getPosition().subtract(center)).multiply(1.0 / scale));
        }
        mesh.computeTriangleNormals();
        Log.i(Constants.LOGTAG, "Successfully fit mesh into unit box.");
    }

    /**
     * Create a unified mesh from all meshes in the list. Not tested for meshes using textures.
     */
    public static TriangleMesh unite(List<ITriangleMesh> meshes) {
        TriangleMesh mesh = new TriangleMesh();

        int vertexOffset = 0;
        int texCoordOffset = 0;
        for (ITriangleMesh m : meshes) {
            // Vertices
            for (int i = 0; i < m.getNumberOfVertices(); i++) {
                mesh.addVertex(new Vertex(m.getVertex(i)));
            }
            for (int i = 0; i < m.getNumberOfTextureCoordinates(); i++) {
                mesh.addTextureCoordinate(new Vector(m.getTextureCoordinate(i)));
            }
            for (int i = 0; i < m.getNumberOfTriangles(); i++) {
                Triangle t = new Triangle((Triangle) m.getTriangle(i));
                t.addVertexIndexOffset(vertexOffset);
                t.addTexCoordOffset(texCoordOffset);
                mesh.addTriangle(t);
            }

            vertexOffset += m.getNumberOfVertices();
            texCoordOffset += m.getNumberOfTextureCoordinates();
        }

        return mesh;
    }

    /**
     * The minimum y-value is 0.
     */
    public static void placeOnXZPlane(ITriangleMesh mesh) {
        placeOnXZPlane(Arrays.asList(mesh));
    }

    /**
     * The minimum y-value is 0.
     */
    public static void placeOnXZPlane(List<ITriangleMesh> meshes) {
        double yMin = Double.POSITIVE_INFINITY;

        for (ITriangleMesh mesh : meshes) {
            for (int i = 0; i < mesh.getNumberOfVertices(); i++) {
                Vector pos = mesh.getVertex(i).getPosition();
                if (pos.y() < yMin) {
                    yMin = pos.y();
                }
            }
        }

        for (ITriangleMesh mesh : meshes) {
            for (int i = 0; i < mesh.getNumberOfVertices(); i++) {
                Vertex vertex = mesh.getVertex(i);
                vertex.getPosition().set(1, vertex.getPosition().y() - yMin);
            }
            mesh.computeTriangleNormals();
        }
        Log.i(Constants.LOGTAG, "Successfully placed object on x-z-plane.");
    }
}
