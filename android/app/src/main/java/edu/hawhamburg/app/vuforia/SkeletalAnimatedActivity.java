package edu.hawhamburg.app.vuforia;

import java.util.ArrayList;
import java.util.List;

import edu.hawhamburg.shared.datastructures.mesh.ITriangleMesh;
import edu.hawhamburg.shared.datastructures.mesh.ObjReader;
import edu.hawhamburg.shared.datastructures.mesh.TriangleMesh;
import edu.hawhamburg.shared.importer.math.Quaternion;
import edu.hawhamburg.shared.importer.skeleton.AnimationHelper;
import edu.hawhamburg.shared.importer.skeleton.SkeletalAnimatedMesh;
import edu.hawhamburg.shared.math.Matrix;
import edu.hawhamburg.shared.math.Vector;
import edu.hawhamburg.shared.misc.Scene;
import edu.hawhamburg.shared.scenegraph.InnerNode;
import edu.hawhamburg.shared.scenegraph.ScaleNode;
import edu.hawhamburg.shared.scenegraph.TransformationNode;
import edu.hawhamburg.shared.scenegraph.TriangleMeshNode;
import edu.hawhamburg.vuforia.VuforiaMarkerNode;

public class SkeletalAnimatedActivity extends Scene {


        private VuforiaMarkerNode characterMarker;
        TransformationNode meshTransformationNode;
        TriangleMesh mesh = null;
        TriangleMeshNode mNode = null;
        boolean b = true;
        InnerNode node = new InnerNode();
        ScaleNode scaleNode = new ScaleNode(0.1);
        SkeletalAnimatedMesh skeletalAnimatedMesh;
        List<TransformationNode> jointNodesAnimated = new ArrayList<>();
        List<TransformationNode> jointNodesBindPose = new ArrayList<>();
        List<TransformationNode> boneNodes = new ArrayList<>();
        int t = 0;
        float progression =0;
        Matrix initMatrix;

    @Override
    public void onSetup(InnerNode rootNode) {



        characterMarker = new VuforiaMarkerNode("elphi");


        ObjReader reader = new ObjReader();

        skeletalAnimatedMesh = reader.readDae("meshes/cowboy.dae");
        ITriangleMesh mesh = skeletalAnimatedMesh.getMesh();
        mesh.setColor(new Vector(1, 1, 1, 1));
        mesh.computeTriangleNormals();
        mNode = new TriangleMeshNode(mesh);
        meshTransformationNode = new TransformationNode();
        meshTransformationNode.setTransformation(meshTransformationNode.getTransformation().multiply(Matrix.createRotationMatrix4(new Vector(1,0,0),-90)));
        meshTransformationNode.addChild(mNode);
        initMatrix=meshTransformationNode.getTransformation();

        scaleNode.addChild(meshTransformationNode);
        node.addChild(scaleNode);
        //getRoot().addChild(node);
        characterMarker.addChild(node);
        getRoot().addChild(characterMarker);

    }

    @Override
    public void onTimerTick(int counter) {

    }

    @Override
    public void onSceneRedraw() {

        t=0;

        TriangleMesh animatedMesh = skeletalAnimatedMesh.animate(progression);
        progression = progression + 0.05f;
        if(progression>0.8333f){
            progression=0.01f;
        }

        Matrix rotationOfMarker = new Matrix(characterMarker.getTransformation().get(0,0),characterMarker.getTransformation().get(0,1),characterMarker.getTransformation().get(0,2),
                characterMarker.getTransformation().get(1,0),characterMarker.getTransformation().get(1,1),characterMarker.getTransformation().get(1,2),
                characterMarker.getTransformation().get(2,0),characterMarker.getTransformation().get(2,1),characterMarker.getTransformation().get(2,2));

        Matrix rotationOfMarkerInvers = rotationOfMarker.getInverse();
        Matrix rotationOfMarkerInversHomogen = Matrix.makeHomogenious(rotationOfMarkerInvers);

        meshTransformationNode.removeChild(mNode);
        mNode = new TriangleMeshNode(animatedMesh);
        meshTransformationNode.addChild(mNode);

        meshTransformationNode.setTransformation(rotationOfMarkerInversHomogen.multiply(initMatrix));
        //meshTransformationNode.setTransformation(initMatrix.multiply(rotationOfMarkerInversHomogen));

        //meshTransformationNode.setTransformation(getTransformationSightLine(findSightLine()));
        //meshTransformationNode.setTransformation(meshTransformationNode.getTransformation().multiply(Matrix.createRotationMatrix4(new Vector(1,0,0),-90)));

        t++;

    }


    protected Vector findSightLine(){

        Vector lookAt = new Vector(0,0,0,1);
        Matrix tmp = characterMarker.getTransformation().getInverse().multiply(Matrix.createIdentityMatrix4());
        Vector pTargetOView = tmp.multiply(lookAt);

        Vector sightLine = pTargetOView.subtract(lookAt);

        sightLine.normalize();

        return sightLine;
    }

    protected Matrix getTransformationSightLine(Vector sightLine){
        Vector x = sightLine.xyz();
        Vector z = x.cross(new Vector(0,1,0));
        Vector y = z.cross(x);

        z.normalize();
        y.normalize();

        Matrix transMatrix = new Matrix(x,y,z);
        Matrix retMatrix = Matrix.makeHomogenious(transMatrix);

        return retMatrix;
    }
}
