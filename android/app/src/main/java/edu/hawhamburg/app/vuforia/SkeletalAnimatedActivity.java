package edu.hawhamburg.app.vuforia;

import com.vuforia.CameraCalibration;
import com.vuforia.CameraDevice;
import com.vuforia.Device;
import com.vuforia.Matrix34F;
import com.vuforia.RenderingPrimitives;
import com.vuforia.State;
import com.vuforia.Vec4F;
import com.vuforia.ViewList;
import com.vuforia.Vuforia;

import java.util.List;

import edu.hawhamburg.app.opengl.AnimationConfig;
import edu.hawhamburg.shared.datastructures.mesh.ITriangleMesh;
import edu.hawhamburg.shared.datastructures.mesh.ObjReader;
import edu.hawhamburg.shared.datastructures.mesh.TriangleMesh;
import edu.hawhamburg.shared.datastructures.mesh.TriangleMeshFactory;
import edu.hawhamburg.shared.importer.skeleton.SkeletalAnimatedMesh;
import edu.hawhamburg.shared.math.Matrix;
import edu.hawhamburg.shared.math.Vector;
import edu.hawhamburg.shared.misc.Button;
import edu.hawhamburg.shared.misc.ButtonHandler;
import edu.hawhamburg.shared.misc.Scene;
import edu.hawhamburg.shared.scenegraph.INode;
import edu.hawhamburg.shared.scenegraph.InnerNode;
import edu.hawhamburg.shared.scenegraph.ScaleNode;
import edu.hawhamburg.shared.scenegraph.TransformationNode;
import edu.hawhamburg.shared.scenegraph.TranslationNode;
import edu.hawhamburg.shared.scenegraph.TriangleMeshNode;
import edu.hawhamburg.vuforia.VuforiaMarkerNode;


public class SkeletalAnimatedActivity extends Scene {


        private VuforiaMarkerNode characterMarker;
        private VuforiaMarkerNode targetMarker;
        TriangleMeshNode mNode = null;
        ScaleNode scaleNode = new ScaleNode(0.1);
        SkeletalAnimatedMesh skeletalAnimatedMesh;

        int t = 0;
        double progression =0;

        TranslationNode characterTranslation;
       TranslationNode targetTranslation;

        TransformationNode characterTransformation;
        TransformationNode correctionTransformation;

        Vector targetVector;
        Vector characterVector;
        protected INode targetNode = null;

        Vector cubeVector;
        TranslationNode cubeTranslation;
        TransformationNode cubeTransformation;
    private TriangleMeshNode cube;
    private ITriangleMesh cubeMesh;
    private ScaleNode scaleCube;
    boolean animate = false;
    boolean isResetted = false;
    //double xm=0;
    //double ym=0;
    //double zm=0;

    double xm=190.0;
    double ym=330.0;
    double zm=300.0;


    public void startAnimation(){
        animate=!animate;
    }
    public void turnX(){
        xm=xm+10;
        if(xm==360){
            xm=0;
        }
        System.out.println("xm: "+xm);
        System.out.println("ym: "+ym);
        System.out.println("zm: "+zm);

    }

    public void turnY(){
        ym=ym+10;
        if(ym==360){
            ym=0;
        }
        System.out.println("xm: "+xm);
        System.out.println("ym: "+ym);
        System.out.println("zm: "+zm);
    }

    public void turnZ(){
        zm=zm+10;
        if(zm==360){
            zm=0;
        }
        System.out.println("xm: "+xm);
        System.out.println("ym: "+ym);
        System.out.println("zm: "+zm);
    }

    public void resetAnimation(){


        isResetted=!isResetted;
    }

    @Override
    public void onSetup(InnerNode rootNode) {

        Button animate = new Button("skeleton.png",
                -0.7, -0.7, 0.2, new ButtonHandler() {
            @Override
            public void handle() {
                turnX();
            }
        });
        addButton(animate);



        Button loadNext = new Button("lego.png",
                -0.5, -0.7, 0.2, new ButtonHandler() {
            @Override
            public void handle() {
                turnY();
                //moveNext();
                //Log.d(Constants.LOGTAG,"button pressed!!");
            }
        });
        addButton(loadNext);

        Button loadPrevious = new Button("ground.png",
                -0.3, -0.7, 0.2, new ButtonHandler() {
            @Override
            public void handle() {
                turnZ();
                //moveNext();
                //Log.d(Constants.LOGTAG,"button pressed!!");
            }
        });
        addButton(loadPrevious);

        cubeVector = new Vector(0.5,0.5,0.5,1);
        cubeTranslation = new TranslationNode(cubeVector);
        cubeTransformation = new TransformationNode();
        scaleCube=new ScaleNode(0.5);

        cubeMesh = new TriangleMesh();
        TriangleMeshFactory.createCube(cubeMesh);
        cube = new TriangleMeshNode(cubeMesh);


        scaleCube.addChild(cube);

        cubeTranslation.addChild(scaleCube);
        cubeTransformation.addChild(cubeTranslation);
getRoot().addChild(cubeTransformation);


        ObjReader reader = new ObjReader();

        List<ITriangleMesh> meshesO = reader.read("meshes/max_planck.obj");
        InnerNode targetInnerNode = new InnerNode();
        for (ITriangleMesh mesh2 : meshesO) {
            TriangleMeshNode meshNode = new TriangleMeshNode(mesh2);
            targetInnerNode.addChild(meshNode);
        }


        ScaleNode scaleO = new ScaleNode(0.3);
        scaleO.addChild(targetInnerNode);
        targetNode = scaleO;

        targetVector = new Vector(0,0,0,1);
        targetMarker = new VuforiaMarkerNode("campus");
        targetTranslation = new TranslationNode(targetVector);
        targetTranslation.addChild(targetNode);
        targetMarker.addChild(targetTranslation);
        getRoot().addChild(targetMarker);

        //read skeletal object
        skeletalAnimatedMesh = reader.readDae("meshes/cowboy.dae");
        ITriangleMesh mesh = skeletalAnimatedMesh.getMesh();
        mesh.setColor(new Vector(1, 1, 1, 1));
        mesh.computeTriangleNormals();
        mNode = new TriangleMeshNode(mesh);
        characterTransformation = new TransformationNode();
        correctionTransformation = new TransformationNode();
        //correctionTransformation.setTransformation(characterTransformation.getTransformation().multiply(Matrix.createRotationMatrix4(new Vector(0,1,0),90)));
        //correctionTransformation.setTransformation(characterTransformation.getTransformation().multiply(Matrix.createRotationMatrix4(new Vector(1,0,0),90)));

        characterMarker = new VuforiaMarkerNode("elphi");
        characterVector = new Vector(0,0,0,1);
        characterTranslation = new TranslationNode(characterVector);

        //scaleNode.addChild(targetNode);
        scaleNode.addChild(mNode);
        characterTransformation.addChild(scaleNode);
        characterTranslation.addChild(characterTransformation);
        correctionTransformation.addChild(characterTranslation);
        characterMarker.addChild(correctionTransformation);

        getRoot().addChild(characterMarker);

    }

    @Override
    public void onTimerTick(int counter) {

    }
    boolean animNow=true;
    @Override
    public void onSceneRedraw() {
        if(animNow){
            animate();
            skeletalAnimatedMesh.animate2(progression,AnimationConfig.skinning);
            mNode.updateVbo();
        }

        characterTransformation.setTransformation(getTransformationSightLine(findSightLine()));
/*
        if (animate) {

            TriangleMesh animatedMesh = skeletalAnimatedMesh.animate(progression);
            progression = progression + 0.05f;
                if(progression>0.8333f){
                progression=0.01f;
                }

            scaleNode.removeChild(mNode);
            mNode = new TriangleMeshNode(animatedMesh);
            scaleNode.addChild(mNode);
        } else {
            resetAnimation();
        }

        characterTransformation.setTransformation(getTransformationSightLine(findSightLine()));

        RenderingPrimitives r = Device.getInstance().getRenderingPrimitives();
        Matrix34F matrix34F = r.getEyeDisplayAdjustmentMatrix(0);
        Matrix34F matrix34F2 = r.getProjectionMatrix(0,0);
        float[] f = matrix34F.getData();
        for(int i=0;i<f.length;i++){
            System.out.println(i);
            System.out.println(f[i]);
        }*/

    }

        Matrix originMatrix2 = new Matrix(0,0,0,20,
                                         0,0,0,0,
                                         0,0,0,0,
                                         0,0,0,1);
    Matrix originMatrix = new Matrix(1,0,0,0,
            0,1,0,0,
            0,0,1,0,
            0,0,0,1);

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
        //retMatrix=retMatrix.multiply(Matrix.createRotationMatrix4(new Vector(0,1,0),90));
        //retMatrix=retMatrix.multiply(Matrix.createRotationMatrix4(new Vector(1,0,0),-20));
        retMatrix=retMatrix.multiply(Matrix.createRotationMatrix4(new Vector(1,0,0),xm));
        retMatrix=retMatrix.multiply(Matrix.createRotationMatrix4(new Vector(0,1,0),ym));
        retMatrix=retMatrix.multiply(Matrix.createRotationMatrix4(new Vector(0,0,1),zm));
        return retMatrix;
    }
    public double animate(){
        if(!AnimationConfig.animate){
            return progression;
        }
        progression += 0.01;
        if(progression>1){
            progression=0.0;
        }
        return progression;
    }
}
