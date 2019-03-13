package edu.hawhamburg.shared.importer.skeleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.hawhamburg.shared.datastructures.mesh.Triangle;
import edu.hawhamburg.shared.datastructures.mesh.TriangleMesh;
import edu.hawhamburg.shared.datastructures.mesh.Vertex;
import edu.hawhamburg.shared.math.Matrix;
import edu.hawhamburg.shared.math.Vector;

public class SimpleTestFactory {
    public static SkeletalAnimatedMesh createSuperSimpleSkeletalAnimatedMesh(){
        TriangleMesh mesh = new TriangleMesh();
        Vertex v0 = new Vertex(new Vector(2,3,4,1));
        Vertex v1 = new Vertex(new Vector(2,3,4,1));
        mesh.addVertex(v0);
        mesh.addVertex(v1);

        Matrix rootLocalBindMatrix = new Matrix(
                0,-1,0,0,
                1,0,0,0,
                0,0,1,0,
                0,0,0,1
        );
        Matrix childLocalBindMatrix = new Matrix(
                1,0,0,0,
                0,0,-1,0,
                0,1,0,1,
                0,0,0,1
        );
        Joint rootJoint = new Joint("root",rootLocalBindMatrix);
        Joint childJoint = new Joint("child",childLocalBindMatrix);
        childJoint.setParentJoint(rootJoint);

        rootJoint.addKeyFrame(0.0,new Matrix(
                0,1,0,0,
                1,0,0,0,
                0,0,-1,0,
                0,0,0,1
        ));
        childJoint.addKeyFrame(0.0,new Matrix(
                1,0,0,0.5,
                0,-1,-0,0,
                0,0,-1,1,
                0,0,0,1
        ));

        rootJoint.setInversBindMatrix(rootLocalBindMatrix.getInverse());
        childJoint.setInversBindMatrix(rootJoint.getInversBindMatrix().multiply(childLocalBindMatrix.getInverse()));
        List<Joint> jointList = new ArrayList<>();
        jointList.add(rootJoint);
        jointList.add(childJoint);
        Skeleton skeleton = new Skeleton(rootJoint,jointList);

        SkeletonAnimationController skeletonAnimationController = new SkeletonAnimationController(2);
        List<Integer> vertex1AffectList = new ArrayList<>();
        vertex1AffectList.add(1);
        List<Float> vertex1WeightList = new ArrayList<>();
        vertex1WeightList.add(1f);
        skeletonAnimationController.vertexWeightControllers[0] = new VertexWeightController(vertex1AffectList,vertex1WeightList);
        List<Integer> vertex2AffectList = new ArrayList<>();
        vertex1AffectList.add(0);
        vertex1AffectList.add(1);
        List<Float> vertex2WeightList = new ArrayList<>();
        vertex1WeightList.add(0.5f);
        vertex1WeightList.add(0.5f);

        skeletonAnimationController.vertexWeightControllers[1] = new VertexWeightController(vertex2AffectList,vertex2WeightList);

        SkeletalAnimatedMesh skeletalAnimatedMesh = new SkeletalAnimatedMesh(mesh,skeleton,skeletonAnimationController);

        return skeletalAnimatedMesh;
    }
}
