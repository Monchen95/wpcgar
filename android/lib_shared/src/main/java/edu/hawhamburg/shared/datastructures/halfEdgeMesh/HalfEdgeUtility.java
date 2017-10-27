package edu.hawhamburg.shared.datastructures.halfEdgeMesh;

import edu.hawhamburg.shared.datastructures.mesh.ITriangleMesh;
import edu.hawhamburg.shared.datastructures.mesh.TriangleMesh;
import edu.hawhamburg.shared.math.Vector;

/**
 * Created by Devran on 26.10.2017.
 */

public class HalfEdgeUtility {
    public static HalfEdgeTriangleMesh convert(ITriangleMesh tm){
        HalfEdgeTriangleMesh tempHETriangleMesh = new HalfEdgeTriangleMesh();

        for(int i=0;i<tm.getNumberOfVertices();i++) {
            tempHETriangleMesh.addVertex(tm.getVertex(i).getPosition());
        }

        for(int i=0;i<tm.getNumberOfTriangles();i++){
            tempHETriangleMesh.addTriangle(tempHETriangleMesh.vertexList.indexOf(tm.getVertex(tm.getTriangle(i),0)),tempHETriangleMesh.vertexList.indexOf(tm.getVertex(tm.getTriangle(i),1)),tempHETriangleMesh.vertexList.indexOf(tm.getVertex(tm.getTriangle(i),2)));
        }



        //tempHETriangleMesh.setAllOpposites();
        Vector colorVector = new Vector(1,128,0,0);

        tempHETriangleMesh.setColor(colorVector);

        return tempHETriangleMesh;
    }
}
