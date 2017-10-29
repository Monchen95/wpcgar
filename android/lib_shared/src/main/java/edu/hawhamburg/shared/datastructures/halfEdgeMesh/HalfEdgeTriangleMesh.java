package edu.hawhamburg.shared.datastructures.halfEdgeMesh;

import java.util.ArrayList;
import java.util.List;

import edu.hawhamburg.shared.datastructures.mesh.AbstractTriangle;
import edu.hawhamburg.shared.datastructures.mesh.ITriangleMesh;
import edu.hawhamburg.shared.datastructures.mesh.Vertex;
import edu.hawhamburg.shared.math.AxisAlignedBoundingBox;
import edu.hawhamburg.shared.math.Vector;
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

        //eine contains triangle methode schreiben, um herauszufinden ob opposite gesetzt werden kann

        if(vertexList.get(vertexIndex1).getOutGoingHE().getSuccessorHE()!=null){
            HalfEdge tempHalfEdge1 = new HalfEdge(vertexList.get(vertexIndex1));
            vertexList.get(vertexIndex1).setOutGoingHE(tempHalfEdge1);
            halfEdgeList.add(tempHalfEdge1);
        }

        if(vertexList.get(vertexIndex2).getOutGoingHE().getSuccessorHE()!=null){
            HalfEdge tempHalfEdge2 = new HalfEdge(vertexList.get(vertexIndex2));
            vertexList.get(vertexIndex2).setOutGoingHE(tempHalfEdge2);
            halfEdgeList.add(tempHalfEdge2);
        }

        if(vertexList.get(vertexIndex3).getOutGoingHE().getSuccessorHE()!=null){
            HalfEdge tempHalfEdge3 = new HalfEdge(vertexList.get(vertexIndex3));
            vertexList.get(vertexIndex3).setOutGoingHE(tempHalfEdge3);
            halfEdgeList.add(tempHalfEdge3);
        }

        vertexList.get(vertexIndex1).getOutGoingHE().setSuccessorHE(vertexList.get(vertexIndex2).getOutGoingHE());
        vertexList.get(vertexIndex2).getOutGoingHE().setSuccessorHE(vertexList.get(vertexIndex3).getOutGoingHE());
        vertexList.get(vertexIndex3).getOutGoingHE().setSuccessorHE(vertexList.get(vertexIndex1).getOutGoingHE());

        HalfEdgeTriangle tempTriangle = new HalfEdgeTriangle(vertexList.get(vertexIndex1).getOutGoingHE());

        vertexList.get(vertexIndex1).getOutGoingHE().setFacet(tempTriangle);
        vertexList.get(vertexIndex2).getOutGoingHE().setFacet(tempTriangle);
        vertexList.get(vertexIndex3).getOutGoingHE().setFacet(tempTriangle);

        triangleList.add(tempTriangle);

/*
        //opposite setzen
        for(int i=0;i<getNumberOfTriangles();i++){

            int otherIndex1 = vertexList.indexOf(getVertex(triangleList.get(i),0));
            int otherIndex2 = vertexList.indexOf(getVertex(triangleList.get(i),1));
            int otherIndex3 = vertexList.indexOf(getVertex(triangleList.get(i),2));

            List<Integer> tempOtherList = new ArrayList<>();
            tempOtherList.add(otherIndex1);
            tempOtherList.add(otherIndex2);
            tempOtherList.add(otherIndex3);

            List<Integer> tempThisList = new ArrayList<>();
            tempThisList.add(vertexIndex1);
            tempThisList.add(vertexIndex2);
            tempThisList.add(vertexIndex3);

            int newVertex1Memory = 0;
            int newVertex2Memory = 0;

            int sameVertex = 0;
            int vertex1Memory = 0;
            int vertex2Memory = 0;
            HalfEdgeTriangle matchingTriangle = triangleList.get(i);

            for(int j=0;j<3;j++){
                for(int k=0;k<3;k++){
                    if(sameVertex==0&&(tempOtherList.get(j)==tempThisList.get(k))){
                        vertex1Memory = tempOtherList.get(j);
                        newVertex1Memory = tempThisList.get(k);
                        sameVertex ++;
                    }
                    if(sameVertex==1&&(tempOtherList.get(j)==tempThisList.get(k))){
                        vertex2Memory = tempOtherList.get(j);
                        newVertex2Memory = tempThisList.get(k);
                        sameVertex ++;
                    }
                    if(sameVertex==2){

                        if(matchingTriangle.getHalfEdge().getStartVertex().equals(vertexList.get(vertex1Memory))){
                            if(matchingTriangle.getHalfEdge().getSuccessorHE().getStartVertex().equals(vertexList.get(vertex2Memory))){
                                matchingTriangle.getHalfEdge().setOppositeHE(vertexList.get(newVertex2Memory).getOutGoingHE());
                                vertexList.get(newVertex2Memory).getOutGoingHE().setOppositeHE(matchingTriangle.getHalfEdge());
                            }
                            if(matchingTriangle.getHalfEdge().getSuccessorHE().getSuccessorHE().getStartVertex().equals(vertexList.get(vertex2Memory))){
                                matchingTriangle.getHalfEdge().getSuccessorHE().getSuccessorHE().setOppositeHE(vertexList.get(newVertex1Memory).getOutGoingHE());
                                vertexList.get(newVertex1Memory).getOutGoingHE().setOppositeHE(matchingTriangle.getHalfEdge().getSuccessorHE().getSuccessorHE());
                            }

                        }
                        if(matchingTriangle.getHalfEdge().getStartVertex().equals(vertexList.get(vertex2Memory))){
                            if(matchingTriangle.getHalfEdge().getSuccessorHE().getStartVertex().equals(vertexList.get(vertex1Memory))){
                                matchingTriangle.getHalfEdge().getSuccessorHE().setOppositeHE(vertexList.get(newVertex1Memory).getOutGoingHE());
                                vertexList.get(newVertex1Memory).getOutGoingHE().setOppositeHE(matchingTriangle.getHalfEdge());
                            }
                            if(matchingTriangle.getHalfEdge().getSuccessorHE().getSuccessorHE().getStartVertex().equals(vertexList.get(vertex1Memory))){
                                matchingTriangle.getHalfEdge().getSuccessorHE().getSuccessorHE().setOppositeHE(vertexList.get(newVertex2Memory).getOutGoingHE());
                                vertexList.get(newVertex2Memory).getOutGoingHE().setOppositeHE(matchingTriangle.getHalfEdge().getSuccessorHE().getSuccessorHE());
                            }
                        }
                        if(matchingTriangle.getHalfEdge().getSuccessorHE().getStartVertex().equals(vertexList.get(vertex1Memory))){

                            if(matchingTriangle.getHalfEdge().getSuccessorHE().getSuccessorHE().getStartVertex().equals(vertexList.get(vertex2Memory))){
                                matchingTriangle.getHalfEdge().getSuccessorHE().setOppositeHE(vertexList.get(newVertex2Memory).getOutGoingHE());
                                vertexList.get(newVertex2Memory).getOutGoingHE().setOppositeHE(matchingTriangle.getHalfEdge().getSuccessorHE());
                            }
                        }
                        if(matchingTriangle.getHalfEdge().getSuccessorHE().getStartVertex().equals(vertexList.get(vertex2Memory))){

                            if(matchingTriangle.getHalfEdge().getSuccessorHE().getSuccessorHE().getStartVertex().equals(vertexList.get(vertex1Memory))){
                                matchingTriangle.getHalfEdge().getSuccessorHE().setOppositeHE(vertexList.get(newVertex1Memory).getOutGoingHE());
                                vertexList.get(newVertex1Memory).getOutGoingHE().setOppositeHE(matchingTriangle.getHalfEdge().getSuccessorHE());
                            }
                        }

                    }
                }
            }

        }
*/
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
        //textur, egal
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

            vertex.setNormal(n);
        }
    }

    @Override
    public Vector getTextureCoordinate(int index) {
        //textur, egal
        return null;
    }

    /**
     * Add texture coordinate to mesh.
     *
     * @param t
     */
    @Override
    public void addTextureCoordinate(Vector t) {
        //textur, egal

    }

    @Override
    public Texture getTexture() {
        //textur, egal

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
        //textur, egal

    }

    /**
     * Return the number of texture coordinates in the mesh.
     */
    @Override
    public int getNumberOfTextureCoordinates() {
        //textur, egal

        return 0;
    }

    /**
     * Returns true if the mesh has a texture assigned.
     */
    @Override
    public boolean hasTexture() {
        //textur, egal

        return false;
    }

    /**
     * Return the bounding box of the mesh.
     */
    @Override
    public AxisAlignedBoundingBox getBoundingBox() {
        //textur, egal

        return null;
    }

    @Override
    public void setTextureName(String textureFilename) {
        //textur, egal

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
        //todo, wenn rest fertig

    }
}
