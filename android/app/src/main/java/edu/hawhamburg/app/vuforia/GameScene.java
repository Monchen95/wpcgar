package edu.hawhamburg.app.vuforia;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import edu.hawhamburg.shared.characters.Pose;
import edu.hawhamburg.shared.datastructures.mesh.ITriangleMesh;
import edu.hawhamburg.shared.datastructures.mesh.ObjReader;
import edu.hawhamburg.shared.math.Matrix;
import edu.hawhamburg.shared.math.Vector;
import edu.hawhamburg.shared.misc.Button;
import edu.hawhamburg.shared.misc.ButtonHandler;
import edu.hawhamburg.shared.misc.Constants;
import edu.hawhamburg.shared.misc.Scene;
import edu.hawhamburg.shared.scenegraph.INode;
import edu.hawhamburg.shared.scenegraph.InnerNode;
import edu.hawhamburg.shared.scenegraph.ScaleNode;
import edu.hawhamburg.shared.scenegraph.TransformationNode;
import edu.hawhamburg.shared.scenegraph.TranslationNode;
import edu.hawhamburg.shared.scenegraph.TriangleMeshNode;
import edu.hawhamburg.shared.simulation.WorldSimulation;
import edu.hawhamburg.vuforia.VuforiaMarkerNode;

import static edu.hawhamburg.shared.action.Direction.BACK;
import static edu.hawhamburg.shared.action.Direction.FRONT;
import static edu.hawhamburg.shared.action.Direction.LEFT;
import static edu.hawhamburg.shared.action.Direction.RIGHT;

/**
 * Created by Devran on 06.03.2018.
 */

public class GameScene extends Scene{

    private int i = 0;

    private int enemyCellNumber = 6;
    private int hostageCellNumber = 10;


    private final int MARKERPOOLSIZE = 11;
    private final int OBSTACLEPOOLSIZE = 3;

    private WorldSimulation sim;
    private Pose characterPose;

    private VuforiaMarkerNode markerStart;
    private VuforiaMarkerNode markerEnd;
    private VuforiaMarkerNode marker1;
    private VuforiaMarkerNode marker2;
    private VuforiaMarkerNode marker3;
    private VuforiaMarkerNode marker4;
    private VuforiaMarkerNode marker5;
    private VuforiaMarkerNode marker6;
    private VuforiaMarkerNode marker7;
    private VuforiaMarkerNode marker8;
    private VuforiaMarkerNode marker9;
    private List<VuforiaMarkerNode> markerList = new ArrayList<>();
    private Matrix[] markerTransformationArray = new Matrix[MARKERPOOLSIZE];

    private VuforiaMarkerNode obstacle1;
    private VuforiaMarkerNode obstacle2;
    private VuforiaMarkerNode obstacle3;
    private List<VuforiaMarkerNode> obstacleList = new ArrayList<>();
    private Matrix[] obstacleTransformationArray = new Matrix[OBSTACLEPOOLSIZE];

    private ScaleNode scalePlayerChar;
    private TranslationNode translationParticle;
    private TransformationNode transformationPlayerChar;
    private ITriangleMesh meshPlayerChar;

    private ScaleNode scaleFrank;
    private TranslationNode translationFrank;
    private TransformationNode transformationFrank;

    private ScaleNode scaleEnemy;
    private TranslationNode translationEnemy;
    private TransformationNode transformationEnemy;

    private TriangleMeshNode meshNodeNormal;
    private TriangleMeshNode meshNodeHappy;
    private TriangleMeshNode meshNodeFighting;
    private TriangleMeshNode meshNodeJumping;
    private TriangleMeshNode meshNodeActive;
    private TriangleMeshNode meshNodeFrank;
    private TriangleMeshNode meshNodeEnemy;

    private InnerNode playerChar = null;
    private InnerNode frank = null;         //name des gefangenen freundes
    private InnerNode enemy = null;
    Button left;
    Button right;
    Button front;
    Button back;
    Button fight;

    int charakterPosition;



    public GameScene(){
        super(100, INode.RenderMode.REGULAR);

        charakterPosition=0;

        left = new Button("left.png",-0.7,-0.5,0.2, new ButtonHandler(){
            @Override
            public void handle(){
                charakterPosition=sim.move(LEFT);
                Log.d(Constants.LOGTAG,"Links!");
            }
        });

        right = new Button("right.png",-0.7,-0.9,0.2, new ButtonHandler(){
            @Override
            public void handle(){
                charakterPosition=sim.move(RIGHT);
                Log.d(Constants.LOGTAG,"Rechts!");
            }
        });

        front = new Button("up.png",-0.4,-0.7,0.2, new ButtonHandler(){
            @Override
            public void handle(){
                charakterPosition=sim.move(FRONT);
                Log.d(Constants.LOGTAG,"Vor!");
            }
        });

        back = new Button("down.png",-0.7,-0.7,0.2, new ButtonHandler(){
            @Override
            public void handle(){
                charakterPosition=sim.move(BACK);
                Log.d(Constants.LOGTAG,"Zurück!");
            }
        });

        fight = new Button("fight.png",-0.7,0.7,0.2, new ButtonHandler(){
            @Override
            public void handle(){
                if(sim.fightEnemy()==1){
                    enemy.removeChild(meshNodeEnemy);
                }
                Log.d(Constants.LOGTAG,"Kämpfe!!!");
            }
        });





        markerStart = new VuforiaMarkerNode("elphi");
        marker1 = new VuforiaMarkerNode("marker1");
        marker2 = new VuforiaMarkerNode("marker2");
        marker3 = new VuforiaMarkerNode("marker3");
        marker4 = new VuforiaMarkerNode("marker4");
        marker5 = new VuforiaMarkerNode("marker5");
        marker6 = new VuforiaMarkerNode("marker6");
        marker7 = new VuforiaMarkerNode("marker7");
        marker8 = new VuforiaMarkerNode("marker8");
        marker9 = new VuforiaMarkerNode("marker9");
        markerEnd = new VuforiaMarkerNode("campus");

        markerList.add(markerStart);
        markerList.add(marker1);
        markerList.add(marker2);
        markerList.add(marker3);
        markerList.add(marker4);
        markerList.add(marker5);
        markerList.add(marker6);
        markerList.add(marker7);
        markerList.add(marker8);
        markerList.add(marker9);
        markerList.add(markerEnd);

        obstacle1 = new VuforiaMarkerNode("obstacle1");
        obstacle2 = new VuforiaMarkerNode("obstacle2");
        obstacle3 = new VuforiaMarkerNode("obstacle3");

        obstacleList.add(obstacle1);
        obstacleList.add(obstacle2);
        obstacleList.add(obstacle3);

        addButton(left);
        addButton(right);
        addButton(front);
        addButton(back);
        addButton(fight);

        scalePlayerChar = new ScaleNode(0.02);
        translationParticle = new TranslationNode(new Vector(0,-0.08,-0.1,1));
        transformationPlayerChar = new TransformationNode();


        scaleFrank = new ScaleNode(0.01);
        translationFrank = new TranslationNode(new Vector(0,0.1,0.05,1));
        transformationFrank = new TransformationNode(Matrix.createRotationMatrix4(new Vector(0,1,0,1),30));

        scaleEnemy = new ScaleNode(0.1);
        translationEnemy = new TranslationNode(new Vector(0,-0.3,0,1));
        transformationEnemy = new TransformationNode();

        initWorld();
    }

    private void addMarkerPositionToArray(int arrayPosition, VuforiaMarkerNode marker){
            markerTransformationArray[arrayPosition] = marker.getTransformation();
    }

    private void addObstacleTransformationToArray(int arrayPosition, VuforiaMarkerNode obstacle){
        obstacleTransformationArray[arrayPosition] = obstacle.getTransformation();
    }

    private void clearMarkerPositionArray(){
        for(int i = 0; i< markerTransformationArray.length; i++){
            markerTransformationArray[i]=Matrix.createIdentityMatrix4();
        }
    }

    private void initWorld(){
        sim = new WorldSimulation(MARKERPOOLSIZE,OBSTACLEPOOLSIZE);
    }

    private void updateWorld(){
        for(int i=0;i<markerList.size();i++){
            addMarkerPositionToArray(i,markerList.get(i));
        }
        for(int i=0;i<obstacleList.size();i++){
            addObstacleTransformationToArray(i,obstacleList.get(i));
        }

        sim.updateWorldSimulation(markerTransformationArray,obstacleTransformationArray);
        clearMarkerPositionArray();
    }





    @Override
    public void onSetup(InnerNode rootNode) {


        ObjReader reader = new ObjReader();
        List<ITriangleMesh> playerCharMeshNormal = reader.read("meshes/Character1.obj");
        playerChar = new InnerNode();
        for (ITriangleMesh mesh : playerCharMeshNormal) {
            meshNodeNormal = new TriangleMeshNode(mesh);
        }

        List<ITriangleMesh> playerCharMeshHappy = reader.read("meshes/Character1Happy.obj");
        playerChar = new InnerNode();
        for (ITriangleMesh mesh : playerCharMeshHappy) {
            meshNodeHappy = new TriangleMeshNode(mesh);
        }

        List<ITriangleMesh> playerCharMeshFighting = reader.read("meshes/Character1Fighting.obj");
        playerChar = new InnerNode();
        for (ITriangleMesh mesh : playerCharMeshFighting) {
            meshNodeFighting = new TriangleMeshNode(mesh);
        }

        List<ITriangleMesh> playerCharMeshJumping = reader.read("meshes/Character1Jumping.obj");
        playerChar = new InnerNode();
        for (ITriangleMesh mesh : playerCharMeshJumping) {
            meshNodeJumping = new TriangleMeshNode(mesh);
        }

        meshNodeActive = meshNodeNormal;
        playerChar.addChild(meshNodeActive);

        List<ITriangleMesh> frankMesh = reader.read("meshes/deer.obj");
        frank = new InnerNode();
        for (ITriangleMesh mesh : frankMesh) {
            meshNodeFrank = new TriangleMeshNode(mesh);
        }
        frank.addChild(meshNodeFrank);

        List<ITriangleMesh> enemyMesh = reader.read("meshes/max_planck.obj");
        enemy = new InnerNode();
        for (ITriangleMesh mesh : enemyMesh) {
            meshNodeEnemy = new TriangleMeshNode(mesh);
        }
        enemy.addChild(meshNodeEnemy);

         for(int i=0;i<markerList.size();i++) {
            List<ITriangleMesh> floorMesh = reader.read("meshes/Floor.obj");
            InnerNode floor = new InnerNode();
            for (ITriangleMesh mesh : floorMesh) {
                TriangleMeshNode meshNode = new TriangleMeshNode(mesh);
                floor.addChild(meshNode);
            }

            TranslationNode floorTranslate = new TranslationNode(new Vector(0,0,0,1));
            TransformationNode floorTransform = new TransformationNode();

            ScaleNode scaleNode = new ScaleNode(0.1);

            scaleNode.addChild(floor);
            floorTransform.addChild(scaleNode);
            floorTranslate.addChild(floorTransform);
            markerList.get(i).addChild(floorTranslate);
          }

        for(int i=0;i<obstacleList.size();i++) {
            List<ITriangleMesh> wallMesh = reader.read("meshes/Walls.obj");
            InnerNode wall = new InnerNode();
            for (ITriangleMesh mesh : wallMesh) {
                TriangleMeshNode meshNode = new TriangleMeshNode(mesh);
                wall.addChild(meshNode);
            }

            TranslationNode wallTranslate = new TranslationNode(new Vector(0,0,0,1));
            TransformationNode wallTransform = new TransformationNode();

            ScaleNode scaleNode = new ScaleNode(0.1);

            scaleNode.addChild(wall);
            wallTransform.addChild(scaleNode);
            wallTranslate.addChild(wallTransform);
            obstacleList.get(i).addChild(wallTranslate);
        }


        scaleFrank.addChild(frank);
        transformationFrank.addChild(scaleFrank);
        translationFrank.addChild(transformationFrank);
        markerEnd.addChild(translationFrank);

        scaleEnemy.addChild(enemy);
        transformationEnemy.addChild(scaleEnemy);
        translationEnemy.addChild(transformationEnemy);
        markerList.get(enemyCellNumber).addChild(translationEnemy);

        scalePlayerChar.addChild(playerChar);
        transformationPlayerChar.addChild(scalePlayerChar);
        translationParticle.addChild(transformationPlayerChar);
        rootNode.addChild(translationParticle);

        for(int i=0;i<markerList.size();i++){
            rootNode.addChild(markerList.get(i));
        }

        for(int i=0;i<obstacleList.size();i++){
            rootNode.addChild(obstacleList.get(i));
        }

    }

    private void changePose(Pose pose){
        if(pose== Pose.NORMAL){
            changePoseToNormal();
        }
        if(pose== Pose.FIGHTING){
            changePoseToFighting();
        }
        if(pose== Pose.HAPPY){
            changePoseToHappy();
        }
        if(pose== Pose.JUMPING){
            changePoseToJumping();
        }
    }

    public void changePoseToNormal(){
        playerChar.removeChild(meshNodeActive);
        meshNodeActive=meshNodeNormal;
        playerChar.addChild(meshNodeActive);
    }

    public void changePoseToFighting(){
        playerChar.removeChild(meshNodeActive);
        meshNodeActive=meshNodeFighting;
        playerChar.addChild(meshNodeActive);
    }

    public void changePoseToHappy(){
        playerChar.removeChild(meshNodeActive);
        meshNodeActive=meshNodeHappy;
        playerChar.addChild(meshNodeActive);
    }

    public void changePoseToJumping(){
        playerChar.removeChild(meshNodeActive);
        meshNodeActive=meshNodeJumping;
        playerChar.addChild(meshNodeActive);
    }

    @Override
    public void onTimerTick(int counter) {

    }

    @Override
    public void onSceneRedraw() {
        if(i>0){
            Log.d(Constants.LOGTAG,"Updateworld");
            updateWorld();


            if(sim.getPose()!=characterPose){
                changePose(sim.getPose());
            }

                transformationPlayerChar.setTransformation(markerList.get(charakterPosition).getTransformation());


            i=0;
        }
        i++;
    }
}
