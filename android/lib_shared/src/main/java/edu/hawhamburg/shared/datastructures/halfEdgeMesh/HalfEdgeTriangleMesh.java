package edu.hawhamburg.shared.datastructures.halfEdgeMesh;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import edu.hawhamburg.shared.datastructures.mesh.AbstractTriangle;
import edu.hawhamburg.shared.datastructures.mesh.ITriangleMesh;
import edu.hawhamburg.shared.datastructures.mesh.Vertex;
import edu.hawhamburg.shared.math.AxisAlignedBoundingBox;
import edu.hawhamburg.shared.math.Vector;
import edu.hawhamburg.shared.misc.Constants;
import edu.hawhamburg.shared.rendering.Texture;

/**
 * Created by Devran on 26.10.2017.
 */

public class HalfEdgeTriangleMesh implements ITriangleMesh{

    protected List<HalfEdge> halfEdgeList = new ArrayList<>();
    protected List<HalfEdgeVertex> vertexList = new ArrayList<>();
    protected List<HalfEdgeTriangle> triangleList = new ArrayList<>();
    protected List<AbstractTriangle> tList = new ArrayList<>();

    /**
     * Add a new vertex (given by position) to the vertex list. The new vertex is
     * appended to the end of the list.
     *
     * @param position
     */
    @Override
    public int addVertex(Vector position) {

        HalfEdgeVertex tempVertex = new HalfEdgeVertex(position,null);

        vertexList.add(tempVertex);

        return 0;
    }

    /**
     * Index in vertex list.
     *
     * @param index
     */
    @Override
    public Vertex getVertex(int index) {
        return vertexList.get(index);
    }

    /**
     * Index in triangle, must be in 0, 1, 2.
     *
     * @param triangle
     * @param index
     */
    @Override
    public Vertex getVertex(AbstractTriangle triangle, int index) {
        if(index==0){
            return triangleList.get(triangleList.indexOf(triangle)).getHalfEdge().getStartVertex();
        } else if(index==1) {
            return triangleList.get(triangleList.indexOf(triangle)).getHalfEdge().getSuccessorHE().getStartVertex();
        } else if(index==2) {
            return triangleList.get(triangleList.indexOf(triangle)).getHalfEdge().getSuccessorHE().getSuccessorHE().getStartVertex();
        }
        return null;
    }

    @Override
    public int getNumberOfVertices() {
        return vertexList.size();
    }

    /**
     * Add a new triangle to the mesh with the vertex indices a, b, c. The index
     * of the first vertex is 0.
     *
     * @param vertexIndex1
     * @param vertexIndex2
     * @param vertexIndex3
     */
    @Override
    public void addTriangle(int vertexIndex1, int vertexIndex2, int vertexIndex3) {

    }

    /**
     * Add a new triangle to the mesh with the vertex indices a, b, c. The index
     * of the first vertex is 0.
     *
     * @param t
     */
    @Override
    public void addTriangle(AbstractTriangle t) {
        triangleList.add((HalfEdgeTriangle) t);

    }

    public void addTriangle(HalfEdgeTriangle t){
        triangleList.add(t);
    }

    /**
     * Add triangle by vertex indices and corresponding texture coordinate
     * indices.
     *
     * @param vertexIndex1
     * @param vertexIndex2
     * @param vertexIndex3
     * @param texCoordIndex1
     * @param texCoordIndex2
     * @param texCoordIndex3
     */
    @Override
    public void addTriangle(int vertexIndex1, int vertexIndex2, int vertexIndex3, int texCoordIndex1, int texCoordIndex2, int texCoordIndex3) {

    }

    @Override
    public int getNumberOfTriangles() {
        return triangleList.size();
    }

    @Override
    public AbstractTriangle getTriangle(int triangleIndex) {
        return triangleList.get(triangleIndex);
    }

    /**
     * Clear mesh - remove all triangles and vertices.
     */
    @Override
    public void clear() {
        triangleList.clear();
        halfEdgeList.clear();
        vertexList.clear();
    }

    /**
     * Compute the triangles normals.
     */
    @Override
    public void computeTriangleNormals() {
        for (HalfEdgeTriangle triangle : triangleList) {

            //Drei Punkte des Dreicks extrahieren
            Vector a = triangle.getHalfEdge().getStartVertex().getPosition();
            Vector b = triangle.getHalfEdge().getSuccessorHE().getStartVertex().getPosition();
            Vector c = triangle.getHalfEdge().getSuccessorHE().getSuccessorHE().getStartVertex().getPosition();

            //Vektor ab und Vektor ac bilden
            Vector ab = b.subtract(a);
            Vector ac = c.subtract(a);

            //Kreuzprodukt von Vektor ab und Vektor ac --> Normalenvektor
            Vector n = ab.cross(ac);

            //Normalenvektor normieren
            n.normalize();
            Log.d(Constants.LOGTAG,"Triangle Normal: " + n.toString());
            triangle.setNormal(n);
        }
    }

    /**
     * Compute the vertex normals.
     */
    public void computeVertexNormals() {
        for (HalfEdgeVertex vertex : vertexList) {

            Vector n = new Vector(0, 0, 0);

            //Liste für alle Dreiecke, die den betreffenden Punkt beinhalten
            List<HalfEdgeTriangle> foundedTriangels = new ArrayList<>();

            for (HalfEdgeTriangle triangle : triangleList) {
                HalfEdgeVertex a = triangle.getHalfEdge().getStartVertex();
                HalfEdgeVertex b = triangle.getHalfEdge().getSuccessorHE().getStartVertex();
                HalfEdgeVertex c = triangle.getHalfEdge().getSuccessorHE().getSuccessorHE().getStartVertex();

                if (a == vertex || b == vertex || c == vertex) {
                    foundedTriangels.add(triangle);
                }

                //Die (bereits normierten) Normalen der gefundenen Dreiecke werden aufaddiert
                for (HalfEdgeTriangle foundedTriangle : foundedTriangels) {
                    n = n.add(foundedTriangle.getNormal());
                }
            }

            //Normalenvektor normieren
            n.normalize();
            Log.d(Constants.LOGTAG,"Vertex Normal: " + n.toString());
            vertex.setNormal(n);
        }
    }

    @Override
    public Vector getTextureCoordinate(int index) {
        return null;
    }

    /**
     * Add texture coordinate to mesh.
     *
     * @param t
     */
    @Override
    public void addTextureCoordinate(Vector t) {

    }

    @Override
    public Texture getTexture() {

        return null;
    }

    /**
     * Create a mesh of the shadow polygons.
     * <p>
     * lightPosition: Position of the light source. extend: Length of the polygons
     * shadowPolygonMesh: Result is put in there
     *
     * @param lightPosition
     * @param extend
     * @param shadowPolygonMesh
     */
    @Override
    public void createShadowPolygons(Vector lightPosition, float extend, ITriangleMesh shadowPolygonMesh) {

    }

    /**
     * Return the number of texture coordinates in the mesh.
     */
    @Override
    public int getNumberOfTextureCoordinates() {

        return 0;
    }

    /**
     * Returns true if the mesh has a texture assigned.
     */
    @Override
    public boolean hasTexture() {

        return false;
    }

    /**
     * Return the bounding box of the mesh.
     */
    @Override
    public AxisAlignedBoundingBox getBoundingBox() {

        return null;
    }

    @Override
    public void setTextureName(String textureFilename) {

    }

    /**
     * Set color to all triangles and all vertices of the mesh.
     *
     * @param color
     */
    @Override
    public void setColor(Vector color) {
        for(HalfEdgeTriangle t: triangleList){
            t.setColor(color);
        }
    }

    /**
     * Sets the alpha (blendding/trasparency) value for all triangles.
     *
     * @param alpha
     */
    @Override
    public void setTransparency(double alpha) {

    }
}
