package edu.hawhamburg.shared.misc;

import android.util.Log;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.hawhamburg.shared.datastructures.mesh.ITriangleMesh;
import edu.hawhamburg.shared.datastructures.mesh.ObjReader;
import edu.hawhamburg.shared.datastructures.mesh.TriangleMesh;
import edu.hawhamburg.shared.datastructures.mesh.TriangleMeshFactory;
import edu.hawhamburg.shared.datastructures.mesh.TriangleMeshTools;
import edu.hawhamburg.shared.datastructures.skeleton.Bone;
import edu.hawhamburg.shared.datastructures.skeleton.Skeleton;
import edu.hawhamburg.shared.datastructures.skeleton.SkeletonNode;
import edu.hawhamburg.shared.math.Matrix;
import edu.hawhamburg.shared.math.Vector;
import edu.hawhamburg.shared.scenegraph.InnerNode;
import edu.hawhamburg.shared.scenegraph.TriangleMeshNode;

/**
 * Test scene for sceleton applications.
 *
 * @author Philipp Jenke
 */

public class SkeletonScene extends Scene {
    private double animationAlpha = 0;

    private Bone trunkBone;
    private Bone cylinderBottom;
    private Bone cylinderMiddle;
    private Bone cylinderTop;
    private TriangleMeshNode meshNode;
    private ShowBones showBones = ShowBones.BONES_ONLY;
    private SkeletonNode skeletonNode;
    private TriangleMesh mesh;

    //meine veränderung
    private TriangleMesh copyMesh;

    boolean weighted = true;

    private double[] boneArray1;
    private double[] boneArray2;
    private double[] boneArray3;
    private double[] boneArray4;

    private double abwBone1;
    private double abwBone2;
    private double abwBone3;
    private double abwBone4;

    private double wd1;
    private double wd2;
    private double wd3;
    private double wd4;




    private int[] vertexToBoneAssignment;



    public enum ShowBones {
        BONES_ONLY, MESH_AND_BONES, MESH_ONLY;

        public ShowBones next() {
            int index = ordinal() + 1;
            if (index >= values().length) {
                index = 0;
            }
            return values()[index];
        }
    }

    public SkeletonScene() {
    }

    @Override
    public void onSetup(InnerNode rootNode) {

        Button button = new Button("skeleton.png",
                -0.7, -0.7, 0.2, new ButtonHandler() {
            @Override
            public void handle() {
                showBones = showBones.next();
                updateRenderSettings();
            }
        });
        addButton(button);

        // Skeleton
        Skeleton skeleton = new Skeleton();
        trunkBone = new Bone(skeleton, 0.2);
        trunkBone.setRotation(Matrix.createRotationMatrix4(new Vector(0, 0, 1), 90 * Math.PI / 180.0));
        cylinderBottom = new Bone(trunkBone, 0.26);
        cylinderMiddle = new Bone(cylinderBottom, 0.26);
        cylinderTop = new Bone(cylinderMiddle, 0.26);

        // Preserve the current state of all bones as the rest state
        skeleton.setRestState();
        skeletonNode = new SkeletonNode(skeleton);
        getRoot().addChild(skeletonNode);

        // Plane
        ITriangleMesh planeMesh = new TriangleMesh();
        TriangleMeshFactory.createPlane(planeMesh, new Vector(0, 0, 0), new Vector(0, 1, 0), 1);
        rootNode.addChild(new TriangleMeshNode(planeMesh));


        // Pine-Tree
        ObjReader reader = new ObjReader();
        List<ITriangleMesh> meshes = reader.read("meshes/pinetree.obj");
        mesh = (TriangleMesh) TriangleMeshTools.unite(meshes);




        // Cylinder
        //mesh = new TriangleMesh();
        //TriangleMeshFactory.createCylinder(mesh, 0.025, 1, 8, 20);


        copyMesh = new TriangleMesh(mesh);

        meshNode = new TriangleMeshNode(mesh);
        rootNode.addChild(meshNode);

        boneArray1 = new double[mesh.getNumberOfVertices()];
        boneArray2 = new double[mesh.getNumberOfVertices()];
        boneArray3 = new double[mesh.getNumberOfVertices()];
        boneArray4 = new double[mesh.getNumberOfVertices()];
        vertexToBoneAssignment = new int[mesh.getNumberOfVertices()];
        assignVertexIndexToBones(mesh);





        updateRenderSettings();
        getRoot().setLightPosition(new Vector(1, 2, 1));

        Log.d(Constants.LOGTAG,"Assignment Array: " + Arrays.toString(vertexToBoneAssignment));

        double smallest1=0;
        double smallest2=0;
        double smallest3=0;
        double smallest4=0;

        for(int i=1;i<boneArray1.length;i++){
            smallest1 = Math.min(boneArray1[i-1],boneArray1[i]);
            smallest2 = Math.min(boneArray2[i-1],boneArray2[i]);
            smallest3 = Math.min(boneArray3[i-1],boneArray3[i]);
            smallest4 = Math.min(boneArray4[i-1],boneArray4[i]);

        }

        //abwBone1=smallest1;
        //abwBone2=smallest2;
        //abwBone3=smallest3;
        //abwBone4=smallest4;

        abwBone1=0.1;
        abwBone2=0.1;
        abwBone3=0.1;
        abwBone4=0.1;



    }

    private double weightFunction(double distance, double abw){

        double abwQrd = Math.pow(abw,2);
        double zaehler = 1;
        double nenner = Math.sqrt(2*Math.PI*abwQrd);

        double exponentEzaehler = -1*Math.pow(distance,2);
        double exponentEnenner = 2 * abwQrd;

        double exponentE = exponentEzaehler/exponentEnenner;

        double wd = (zaehler / nenner) * Math.pow(Math.E,exponentE);

        return wd;
    }

    private void updateRenderSettings() {
        switch (showBones) {
            case MESH_ONLY:
                skeletonNode.setActive(false);
                meshNode.setTransparency(1.0);
                meshNode.setActive(true);
                break;
            case MESH_AND_BONES:
                skeletonNode.setActive(true);
                meshNode.setTransparency(0.5);
                meshNode.setActive(true);
                break;
            case BONES_ONLY:
                skeletonNode.setActive(true);
                meshNode.setTransparency(0.5);
                meshNode.setActive(false);
                break;
        }
    }

    private void assignVertexIndexToBones(TriangleMesh mesh){

        double distance1;
        double distance2;
        double distance3;
        double distance4;

        for(int i=0;i<mesh.getNumberOfVertices();i++){
            distance1 = calculateDistance(trunkBone,mesh.getVertex(i).getPosition());
            distance2 = calculateDistance(cylinderBottom,mesh.getVertex(i).getPosition());
            distance3 = calculateDistance(cylinderMiddle,mesh.getVertex(i).getPosition());
            distance4 = calculateDistance(cylinderTop,mesh.getVertex(i).getPosition());

            boneArray1[i] = distance1;
            boneArray2[i] = distance2;
            boneArray3[i] = distance3;
            boneArray4[i] = distance4;

            Log.d(Constants.LOGTAG,"Für Vertex: " + i);
            Log.d(Constants.LOGTAG,"Distanz 1: " + distance1);
            Log.d(Constants.LOGTAG,"Distanz 2: " + distance2);
            Log.d(Constants.LOGTAG,"Distanz 3: " + distance3);
            Log.d(Constants.LOGTAG,"Distanz 4: " + distance4);


            if(distance1<distance2 && distance1<distance3 && distance1<distance4){
                vertexToBoneAssignment[i]=1;
            }else if(distance2<distance3 && distance2<distance4 && distance2<distance1){
                vertexToBoneAssignment[i]=2;
            }else if(distance3<distance1 && distance3 < distance2 && distance3<distance4){
                vertexToBoneAssignment[i]=3;
            } else {
                vertexToBoneAssignment[i]=4;
            }

        }


    }

    /**
     * Calculates the distance between a bone and a point.
     */
    private double calculateDistance(Bone bone, Vector point) {


       // Log.d(Constants.LOGTAG,"Bone Length: " + bone.getLength());

       // Log.d(Constants.LOGTAG,"Vector 1: " + point.toString());


        double r = 0.0;
        double distance = 0.0;

        Vector tmp;
        Vector xStroke;

        // Point x
        Vector x = point;

        // Line g: p + r * v
        Vector p1 = bone.getStart();
        Vector p2 = bone.getEnd();
        Vector p1p2 = p2.subtract(p1);
        Vector v = p1p2.getNormalized();
      //  Log.d(Constants.LOGTAG,"Bone Start : " + p1);
      //  Log.d(Constants.LOGTAG,"Bone End: " + p2);

      //  Log.d(Constants.LOGTAG,"Bone Direction: " + p1p2);
      //  Log.d(Constants.LOGTAG,"Bone Normalized: " + v);

        // r' = (x - p) * v
        tmp = x.subtract(p1);
        r = tmp.multiply(v);

      //  Log.d(Constants.LOGTAG,"Bone R: " + r);


        // x' = p + r' * v
        tmp = v.multiply(r);

       // Log.d(Constants.LOGTAG,"Bone v*r': " + tmp);

        xStroke = p1.add(tmp);

       // Log.d(Constants.LOGTAG,"Bone xStroke: " + xStroke);


        // x' = ||x - x'||
        tmp = x.subtract(xStroke);
        distance = tmp.getNorm();





        Vector lengthToStartVec = xStroke.subtract(p1);
        double lengthToStart = lengthToStartVec.getNorm();

        Vector lengthToEndVec = xStroke.subtract(p2);
        double lengthToEnd = lengthToEndVec.getNorm();

        Log.d(Constants.LOGTAG,"-------------------------");
        Log.d(Constants.LOGTAG,"Distanz 1 Result: " + distance);
        Log.d(Constants.LOGTAG,"Länge zu Start Result: " + lengthToStart);
        Log.d(Constants.LOGTAG,"Länge zu End Result: " + lengthToEnd);
        Log.d(Constants.LOGTAG,"Länge Knochen Result: " + bone.getLength());
        Log.d(Constants.LOGTAG,"-------------------------");


        if(lengthToStart<bone.getLength() && lengthToEnd<bone.getLength()){
            return distance;
        }

        Vector distanceStart = point.subtract(p1);
        Vector distanceEnd = point.subtract(p2);

        double distanceStartLength = distanceStart.getNorm();
        double distanceEndLength = distanceEnd.getNorm();

        double tmpSmall = Math.max(distanceStartLength,distanceEndLength);
        return tmpSmall;
    }



    @Override
    public void onTimerTick(int counter) {

    }

    @Override
    public void onSceneRedraw() {
        // Animate skeleton
        double DELTA_ANIMATION_ALPHA = 0.03;
        animationAlpha += DELTA_ANIMATION_ALPHA;
        double angle = Math.cos(animationAlpha) * Math.PI / 180.0 * 8;
        trunkBone.setRotation(Matrix.createRotationMatrix4(new Vector(0, 0, 1), 90 * Math.PI / 180.0));
        cylinderTop.setRotation(Matrix.createRotationMatrix4(new Vector(0, 1, 0), angle));
        cylinderMiddle.setRotation(Matrix.createRotationMatrix4(new Vector(0, 1, 0), 2 * angle));
        cylinderBottom.setRotation(Matrix.createRotationMatrix4(new Vector(0, 1, 0), 3 * angle));

        // Update mesh based on skeleton
        // TODO

        //gucken wie sich die knochen bewegt haben, bottom->middle->top relationen beachten ???

        //get corresponding bone to every vertex, update its position
        for(int i=0;i<mesh.getNumberOfVertices();i++) {

            if (weighted) {

                Log.d(Constants.LOGTAG,"verrücktes experiment");

                wd1 = weightFunction(boneArray1[i],abwBone1);
                wd2 = weightFunction(boneArray2[i],abwBone2);
                wd3 = weightFunction(boneArray3[i],abwBone3);
                wd4 = weightFunction(boneArray4[i],abwBone4);
                double wdgesamt = wd1+wd2+wd3+wd4;

                Vector currentPosition = copyMesh.getVertex(i).getPosition();
                currentPosition = Vector.makeHomogenious(currentPosition);





                Vector deltaTrunk = trunkBone.getRestStateTransformationAtStart().getInverse().multiply(currentPosition);
                Vector newPosTrunk = trunkBone.getTransformationAtStart().multiply(deltaTrunk);
                newPosTrunk = newPosTrunk.multiply(wd1);

                Vector deltaBottom = cylinderBottom.getRestStateTransformationAtStart().getInverse().multiply(currentPosition);
                Vector newPosBottom = cylinderBottom.getTransformationAtStart().multiply(deltaBottom);
                newPosBottom = newPosBottom.multiply(wd2);

                Vector deltaMiddle = cylinderMiddle.getRestStateTransformationAtStart().getInverse().multiply(currentPosition);
                Vector newPosMiddle = cylinderMiddle.getTransformationAtStart().multiply(deltaMiddle);
                newPosMiddle = newPosMiddle.multiply(wd3);

                Vector deltaTop = cylinderTop.getRestStateTransformationAtStart().getInverse().multiply(currentPosition);
                Vector newPosTop = cylinderTop.getTransformationAtStart().multiply(deltaTop);
                newPosTop = newPosTop.multiply(wd4);

                Vector newPos = newPosTrunk.add(newPosBottom).add(newPosMiddle).add(newPosTop);

                newPos = newPos.multiply(Math.pow(wdgesamt,-1));

                newPos = newPos.xyz();

                mesh.getVertex(i).getPosition().copy(newPos);




            } else {
                Vector currentPosition = copyMesh.getVertex(i).getPosition();
                int correspondingBone = vertexToBoneAssignment[i];

                Bone tmpBone = null;

                if (correspondingBone == 1) {
                    tmpBone = trunkBone;

                } else if (correspondingBone == 2) {
                    tmpBone = cylinderBottom;

                } else if (correspondingBone == 3) {
                    tmpBone = cylinderMiddle;

                } else if (correspondingBone == 4) {
                    tmpBone = cylinderTop;

                }

                currentPosition = Vector.makeHomogenious(currentPosition);

                Vector delta = tmpBone.getRestStateTransformationAtStart().getInverse().multiply(currentPosition);
                Vector newPos = tmpBone.getTransformationAtStart().multiply(delta);

                newPos = newPos.xyz();
                mesh.getVertex(i).getPosition().copy(newPos);

            }

            mesh.computeTriangleNormals();

            meshNode.updateVbo();
        }
    }
}
