package edu.hawhamburg.app.vuforia;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import edu.hawhamburg.shared.datastructures.mesh.ITriangleMesh;
import edu.hawhamburg.shared.datastructures.mesh.ObjReader;
import edu.hawhamburg.shared.importer.skeleton.AnimationConfig;
import edu.hawhamburg.shared.importer.skeleton.SkeletalAnimatedMesh;
import edu.hawhamburg.shared.importer.skeleton.SkeletonLock;
import edu.hawhamburg.shared.math.Matrix;
import edu.hawhamburg.shared.math.Vector;
import edu.hawhamburg.shared.misc.Button;
import edu.hawhamburg.shared.misc.ButtonHandler;
import edu.hawhamburg.shared.misc.Scene;
import edu.hawhamburg.shared.scenegraph.InnerNode;
import edu.hawhamburg.shared.scenegraph.ScaleNode;
import edu.hawhamburg.shared.scenegraph.TransformationNode;
import edu.hawhamburg.shared.scenegraph.TranslationNode;
import edu.hawhamburg.shared.scenegraph.TriangleMeshNode;
import edu.hawhamburg.vuforia.VuforiaMarkerNode;


public class SkeletalAnimatedScene extends Scene {


    private VuforiaMarkerNode characterMarker;
    TriangleMeshNode mNode = null;
    ScaleNode scaleNode = new ScaleNode(0.1);
    SkeletalAnimatedMesh skeletalAnimatedMesh;


    TranslationNode characterTranslation;

    TransformationNode characterTransformation;
    TransformationNode correctionTransformation;

    Vector targetVector;
    Vector characterVector;


    Matrix originMatrix;

    double xm=190.0;
    double ym=330.0;
    double zm=300.0;

    int frames=0;

    boolean updateVBO = true;

    boolean followCamera = true;

    public void speedUp(){
        AnimationConfig.speedUp();
    }

    public void slowDown(){
        AnimationConfig.slowDown();
    }

    public void togglefollowCamera(){
        followCamera=!followCamera;
    }

    public void toggleUpdateVBO(){
        updateVBO=!updateVBO;
    }

    public void toggleAnimation(){
        AnimationConfig.startStopAnimation();
    }

    public void toggleBlendMode(){
        AnimationConfig.toggleSkinning();
    }


    public void resetAnimation(){

    }

    @Override
    public void onSetup(InnerNode rootNode) {

        Button speedUp = new Button("fast.png",
                -0.4, -0.4, 0.2, new ButtonHandler() {
            @Override
            public void handle() {
                speedUp();
            }
        });
        addButton(speedUp);

        Button slowDown = new Button("slow.png",
                -0.7, -0.4, 0.2, new ButtonHandler() {
            @Override
            public void handle() {
                slowDown();
            }
        });
        addButton(slowDown);

        Button toggleUpdateVBO = new Button("stop.png",
                -0.7, -0.7, 0.2, new ButtonHandler() {
            @Override
            public void handle() {
                toggleUpdateVBO();
            }
        });
        addButton(toggleUpdateVBO);

        Button toggleAnimation = new Button("play.png",
                -0.4, -0.7, 0.2, new ButtonHandler() {
            @Override
            public void handle() {
                toggleAnimation();
            }
        });
        addButton(toggleAnimation);

        Button toggleBlendMode = new Button("toggle.png",
                -0.1, -0.7, 0.2, new ButtonHandler() {
            @Override
            public void handle() {
                toggleBlendMode();
            }
        });
        addButton(toggleBlendMode);

        Button togglefollowCamera = new Button("eye.png",
                0.2, -0.7, 0.2, new ButtonHandler() {
            @Override
            public void handle() {
                togglefollowCamera();
            }
        });
        addButton(togglefollowCamera);






        ObjReader reader = new ObjReader();



        //read skeletal object
        skeletalAnimatedMesh = reader.readDae("meshes/cowboy.dae");
        ITriangleMesh mesh = skeletalAnimatedMesh.getMesh();
        mesh.setColor(new Vector(1, 1, 1, 1));
        mesh.computeTriangleNormals();
        mNode = new TriangleMeshNode(mesh);
        characterTransformation = new TransformationNode();
        correctionTransformation = new TransformationNode();

        characterMarker = new VuforiaMarkerNode("elphi");

        characterVector = new Vector(0,0,0,1);
        targetVector = new Vector(0,0,0,1);
        characterTranslation = new TranslationNode(characterVector);

        scaleNode.addChild(mNode);
        characterTransformation.addChild(scaleNode);
        characterTranslation.addChild(characterTransformation);
        correctionTransformation.addChild(characterTranslation);
        characterMarker.addChild(correctionTransformation);

        getRoot().addChild(characterMarker);
        skeletalAnimatedMesh.start();
        originMatrix = new Matrix(1,0,0,0,
                0,1,0,0,
                0,0,1,0,
                0,0,0,1);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("FPS: "+frames + " " + AnimationConfig.skinning);
                frames=0;
            }
        },0,1000);

    }

    @Override
    public void onTimerTick(int counter) {

    }

    @Override
    public void onSceneRedraw() {
        frames++;
        if(updateVBO){
            SkeletonLock.mutex.lock();
            mNode.updateVbo();
            SkeletonLock.mutex.unlock();
        }
        if(followCamera){
            characterTransformation.setTransformation(getTransformationSightLine(findSightLine()));
        }


    }




    protected Vector findSightLine(){
        Matrix tmp = characterMarker.getTransformation().getInverse().multiply(originMatrix);
        Vector pTargetOView = tmp.multiply(targetVector);

        Vector sightLine = pTargetOView.subtract(characterVector);
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
        retMatrix=retMatrix.multiply(Matrix.createRotationMatrix4(new Vector(1,0,0),xm));
        retMatrix=retMatrix.multiply(Matrix.createRotationMatrix4(new Vector(0,1,0),ym));
        retMatrix=retMatrix.multiply(Matrix.createRotationMatrix4(new Vector(0,0,1),zm));
        return retMatrix;
    }

}
