package edu.hawhamburg.app.vuforia;

import android.util.Log;

import java.util.List;

import edu.hawhamburg.shared.datastructures.mesh.AbstractTriangle;
import edu.hawhamburg.shared.datastructures.mesh.ITriangleMesh;
import edu.hawhamburg.shared.datastructures.mesh.ObjReader;
import edu.hawhamburg.shared.datastructures.mesh.TriangleMesh;
import edu.hawhamburg.shared.datastructures.mesh.TriangleMeshFactory;
import edu.hawhamburg.shared.datastructures.mesh.Vertex;
import edu.hawhamburg.shared.datastructures.particles.fireParticle;
import edu.hawhamburg.shared.math.AxisAlignedBoundingBox;
import edu.hawhamburg.shared.math.Matrix;
import edu.hawhamburg.shared.math.Vector;
import edu.hawhamburg.shared.misc.Button;
import edu.hawhamburg.shared.misc.ButtonHandler;
import edu.hawhamburg.shared.misc.Constants;
import edu.hawhamburg.shared.misc.Scene;
import edu.hawhamburg.shared.scenegraph.BoundingBoxNode;
import edu.hawhamburg.shared.scenegraph.INode;
import edu.hawhamburg.shared.scenegraph.InnerNode;
import edu.hawhamburg.shared.scenegraph.ScaleNode;
import edu.hawhamburg.shared.scenegraph.TransformationNode;
import edu.hawhamburg.shared.scenegraph.TranslationNode;
import edu.hawhamburg.shared.scenegraph.TriangleMeshNode;
import edu.hawhamburg.vuforia.VuforiaMarkerNode;

/**
 * Created by abm510 on 10.01.2018.
 */

public class ParticleScene extends Scene {

    private VuforiaMarkerNode markerCannon;
    private VuforiaMarkerNode markerTarget;

    private Vector positionCannon;
    private Vector positionTarget;

    private TranslationNode translationCannon;
    private TranslationNode translationTarget;
    private TranslationNode translationParticle;

    private TransformationNode transformationParticle;
    private TransformationNode transformationCannon;

    private TriangleMeshNode particleMeshNode;

    private ScaleNode scaleCannon;
    private ScaleNode scaleTarget;
    private ScaleNode scaleParticle;

    private INode cannon;
    private INode target;
    private ITriangleMesh particle;

    private fireParticle cannonBall;

    private Vector force;
    private Vector velocity;
    private double mass;
    private double delta;
    private double radius;

    private boolean shoot = false;

    public ParticleScene() {
        super(100, INode.RenderMode.REGULAR);

        force = new Vector(0,-9.81,0);
        velocity = new Vector(0,0,0);
        //velocity = new Vector(-0.5,2,0);
        mass = 5;
        radius = 1;
        delta = 0.1;
        positionCannon = new Vector(0,0,0,1);
        positionTarget = new Vector(0,0,0,1);

        markerCannon = new VuforiaMarkerNode("campus");
        markerTarget = new VuforiaMarkerNode("elphi");

        translationCannon = new TranslationNode(positionCannon);
        translationTarget = new TranslationNode(positionTarget);
        transformationParticle = new TransformationNode();
        transformationCannon = new TransformationNode();
    }

    @Override
    public void onSetup(InnerNode rootNode) {

        Button button = new Button("skeleton.png",
                -0.7, -0.7, 0.2, new ButtonHandler() {
            @Override
            public void handle() {
                initialize();
                  //moveNext();
                //Log.d(Constants.LOGTAG,"button pressed!!");
            }
        });
        addButton(button);

        particle = new TriangleMesh();
        TriangleMeshFactory.createSphere(particle,radius,7);
        particleMeshNode = new TriangleMeshNode(particle);
        translationParticle = new TranslationNode(new Vector(2,2,0,0));
//        translationParticle = new TranslationNode(positionCannon);
        scaleParticle = new ScaleNode(0.09);

        scaleParticle.addChild(particleMeshNode);
        transformationParticle.addChild(scaleParticle);
        translationParticle.addChild(transformationParticle);
        markerCannon.addChild(translationParticle);

        ObjReader reader = new ObjReader();

        List<ITriangleMesh> meshesO = reader.read("meshes/cannon.obj");
        InnerNode cannonNodeO = new InnerNode();

        //achtung
        TriangleMeshNode meshNodeCannon = null;
        for (ITriangleMesh mesh : meshesO) {
            meshNodeCannon = new TriangleMeshNode(mesh);
            cannonNodeO.addChild(meshNodeCannon);
        }


        scaleCannon = new ScaleNode(0.2);
        scaleCannon.addChild(cannonNodeO);
        cannon = scaleCannon;

        List<ITriangleMesh> meshesT = reader.read("meshes/chest.obj");
        InnerNode targetNodeO = new InnerNode();
        TriangleMeshNode meshNodeTarget = null;
        for (ITriangleMesh mesh : meshesT) {
            meshNodeTarget = new TriangleMeshNode(mesh);
            targetNodeO.addChild(meshNodeTarget);
        }

        scaleTarget= new ScaleNode(0.2);
        scaleTarget.addChild(targetNodeO);
        target = scaleTarget;

        markerTarget.addChild(translationTarget);
        translationTarget.addChild(target);

        rootNode.addChild(markerTarget);

        transformationCannon.addChild(cannon);
        translationCannon.addChild(transformationCannon);
        markerCannon.addChild(translationCannon);

        rootNode.addChild(markerCannon);

        cannonBall = new fireParticle(positionCannon.xyz(),mass,velocity,force);
        Matrix rotationMatrix = new Matrix(0,0,1,0,1,0,-1,0,0);
        rotationMatrix = Matrix.makeHomogenious(rotationMatrix);
        transformationParticle.setTransformation(rotationMatrix);

        transformationCannon.setTransformation(rotationMatrix);

    }



    private void initialize(){
        cannonBall.reset();
        cannonBall.setPosition(positionCannon.xyz());
        cannonBall.setVelocity(new Vector(-0.5,2,0));
        shoot = true;
        //Log.d(Constants.LOGTAG,"Reinitialized Cannonball!");
    }



    boolean collide(AxisAlignedBoundingBox bbox1, Matrix transformation1,
                    AxisAlignedBoundingBox bbox2, Matrix transformation2){

        Vector bbox1URHom = Vector.makeHomogenious(bbox1.getUR());
        Vector bbox2URHom = Vector.makeHomogenious(bbox2.getUR());
        Vector bbox1LLHom = Vector.makeHomogenious(bbox1.getLL());
        Vector bbox2LLHom = Vector.makeHomogenious(bbox2.getLL());

        //Log.d(Constants.LOGTAG, "UR1: before" + bbox1URHom);
        //Log.d(Constants.LOGTAG, "UR2: before" + bbox2URHom);
        //Log.d(Constants.LOGTAG, "LL1: before" + bbox1LLHom);
        //Log.d(Constants.LOGTAG, "LL2: before" + bbox2LLHom);

        Matrix transformationToWorld2 = transformation1.multiply(transformation2.getInverse());

        Vector ur1 = transformationToWorld2.multiply(bbox1URHom).xyz();
        Vector ur2 = bbox2URHom.xyz();
        Vector ll1 = transformationToWorld2.multiply(bbox1LLHom).xyz();
        Vector ll2 = bbox2LLHom.xyz();

       // Log.d(Constants.LOGTAG, "UR1 after: " + ur1);
       // Log.d(Constants.LOGTAG, "UR2 after: " + ur2);
       // Log.d(Constants.LOGTAG, "LL1 after: " + ll1);
       // Log.d(Constants.LOGTAG, "LL2 after: " + ll2);

        //bbox1.transform(transformation1) alternativ

        if( ur1.x()>ll2.x() && ur2.x()>ll1.x()){
          //  Log.d(Constants.LOGTAG, "Calcuate x");

            if( ur1.y()>ll2.y() && ur2.y()>ll1.y()){
           //     Log.d(Constants.LOGTAG, "Calcuate y");

                if( ur1.z()>ll2.z() && ur2.z()>ll1.z()){
            //        Log.d(Constants.LOGTAG, "Calcuate z");

                    return true;
                }
            }
        }
        return false;
    }



    private void moveNext(){
        cannonBall.calcNewPosition(delta);
        cannonBall.calcNewVelocity(delta);

        Log.d(Constants.LOGTAG, "Position: " + cannonBall.getPosition());
        Log.d(Constants.LOGTAG, "Velocity: " + cannonBall.getVelocity());

        Vector newPosition = Vector.makeHomogenious(cannonBall.getPosition());

        translationParticle.setTranslation(newPosition);
    }



    @Override
    public void onTimerTick(int counter) {

    }



    @Override
    public void onSceneRedraw() {

            if(shoot) {
                //calc position
                cannonBall.calcNewPosition(delta);
                //calc velocity
                cannonBall.calcNewVelocity(delta);
            }
            //Log.d(Constants.LOGTAG, "Position: " + cannonBall.getPosition());
            //Log.d(Constants.LOGTAG, "Velocity: " + cannonBall.getVelocity());

            Vector newPosition = Vector.makeHomogenious(cannonBall.getPosition());

            translationParticle.setTranslation(newPosition);


            if (collide(particleMeshNode.getBoundingBox(),
                    particleMeshNode.getTransformation(),
                    target.getBoundingBox(),
                    target.getTransformation())) {

                    Log.d(Constants.LOGTAG,"Nice Shot!!");
                cannonBall.setPosition(positionCannon.xyz()); //wohin auch immer
                cannonBall.setVelocity(new Vector(0.0,0,0));
                shoot = false;
            }


    }
}

