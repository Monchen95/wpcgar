package edu.hawhamburg.shared.datastructures.halfEdgeMesh;

import android.util.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.hawhamburg.shared.datastructures.mesh.ITriangleMesh;
import edu.hawhamburg.shared.datastructures.mesh.Triangle;
import edu.hawhamburg.shared.datastructures.mesh.TriangleMesh;
import edu.hawhamburg.shared.math.Vector;

/**
 * Created by Devran on 26.10.2017.
 */

public class HalfEdgeUtility {
    public static HalfEdgeTriangleMesh convert(ITriangleMesh tm){
        Map<HalfEdge,HalfEdgeMapEntry> oppositeMap = new HashMap<>();

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

            oppositeMap.put(h1,new HalfEdgeMapEntry(hEMesh.vertexList.indexOf(h1.getStartVertex()),hEMesh.vertexList.indexOf(h1.getSuccessorHE().getStartVertex())));
            oppositeMap.put(h2,new HalfEdgeMapEntry(hEMesh.vertexList.indexOf(h2.getStartVertex()),hEMesh.vertexList.indexOf(h2.getSuccessorHE().getStartVertex())));
            oppositeMap.put(h3,new HalfEdgeMapEntry(hEMesh.vertexList.indexOf(h3.getStartVertex()),hEMesh.vertexList.indexOf(h3.getSuccessorHE().getStartVertex())));
        }

        for(Map.Entry<HalfEdge, HalfEdgeMapEntry> h1: oppositeMap.entrySet()){
            for(Map.Entry<HalfEdge, HalfEdgeMapEntry> h2: oppositeMap.entrySet()){
                if(!(h1.getKey().equals(h2.getKey()))){
                    if(h1.getValue().equals(h2.getValue())){
                       int tmpIdx1 = hEMesh.halfEdgeList.indexOf(h1.getKey());
                       int tmpIdx2 = hEMesh.halfEdgeList.indexOf(h2.getKey());
                        hEMesh.halfEdgeList.get(tmpIdx1).setOppositeHE(hEMesh.halfEdgeList.get(tmpIdx2));
                        hEMesh.halfEdgeList.get(tmpIdx2).setOppositeHE(hEMesh.halfEdgeList.get(tmpIdx1));
                    }
                }
            }
        }

        hEMesh.computeTriangleNormals();
        hEMesh.computeVertexNormals();

        Vector color = new Vector(1, 0.25, 0.25, 1);

        hEMesh.setColor(color);

        return hEMesh;
    }
}
