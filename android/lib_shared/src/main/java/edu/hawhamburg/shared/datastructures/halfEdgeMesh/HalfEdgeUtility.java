package edu.hawhamburg.shared.datastructures.halfEdgeMesh;

import edu.hawhamburg.shared.datastructures.mesh.ITriangleMesh;
import edu.hawhamburg.shared.datastructures.mesh.Triangle;
import edu.hawhamburg.shared.datastructures.mesh.TriangleMesh;
import edu.hawhamburg.shared.math.Vector;

/**
 * Created by Devran on 26.10.2017.
 */

public class HalfEdgeUtility {
    public static HalfEdgeTriangleMesh convert(ITriangleMesh tm){
        HalfEdgeTriangleMesh hEMesh = new HalfEdgeTriangleMesh();

        for(int i=0;i<tm.getNumberOfVertices();i++) {
            hEMesh.addVertex(tm.getVertex(i).getPosition());
        }

        for(int i=0;i<tm.getNumberOfTriangles();i++){
            Triangle oldTriangle = (Triangle) tm.getTriangle(i);
            int vertex1 = oldTriangle.getVertexIndex(0);
            int vertex2 = oldTriangle.getVertexIndex(1);
            int vertex3 = oldTriangle.getVertexIndex(2);

            HalfEdge h1 = new HalfEdge(hEMesh.vertexList.get(vertex1));
            HalfEdge h2 = new HalfEdge(hEMesh.vertexList.get(vertex2));
            HalfEdge h3 = new HalfEdge(hEMesh.vertexList.get(vertex3));

            hEMesh.vertexList.get(vertex1).setOutGoingHE(h1);
            hEMesh.vertexList.get(vertex2).setOutGoingHE(h2);
            hEMesh.vertexList.get(vertex3).setOutGoingHE(h3);

            h1.setSuccessorHE(h2);
            h2.setSuccessorHE(h3);
            h3.setSuccessorHE(h1);

            HalfEdgeTriangle triangle = new HalfEdgeTriangle(h1);

            h1.setFacet(triangle);
            h2.setFacet(triangle);
            h3.setFacet(triangle);

            hEMesh.halfEdgeList.add(h1);
            hEMesh.halfEdgeList.add(h2);
            hEMesh.halfEdgeList.add(h3);

            hEMesh.addTriangle(triangle);
        }





        System.out.println("test");

      /*  for(int i=0;i<tm.getNumberOfTriangles();i++){
            tempHETriangleMesh.addTriangle(tempHETriangleMesh.vertexList.indexOf(tm.getVertex(tm.getTriangle(i),0)),tempHETriangleMesh.vertexList.indexOf(tm.getVertex(tm.getTriangle(i),1)),tempHETriangleMesh.vertexList.indexOf(tm.getVertex(tm.getTriangle(i),2)));
        }
*/



        //tempHETriangleMesh.setAllOpposites();
    //    Vector colorVector = new Vector(1,128,0,0);

     //   hEMesh.setColor(colorVector);

        return hEMesh;
    }
}
