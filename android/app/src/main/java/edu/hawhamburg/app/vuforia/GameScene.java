package edu.hawhamburg.app.vuforia;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import edu.hawhamburg.shared.datastructures.mesh.ITriangleMesh;
import edu.hawhamburg.shared.datastructures.mesh.ObjReader;
import edu.hawhamburg.shared.datastructures.mesh.TriangleMesh;
import edu.hawhamburg.shared.datastructures.mesh.TriangleMeshFactory;
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
import edu.hawhamburg.shared.simulation.WorldSimulation2;
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

    private final int MARKERPOOLSIZE = 11;
    private final int OBSTACLEPOOLSIZE = 3;

    private WorldSimulation2 sim;


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

    private ScaleNode scaleParticle;
    private TranslationNode translationParticle;
    private TransformationNode transformationParticle;
    private ITriangleMesh particle;
    private TriangleMeshNode particleMeshNode;

    //protected INode playerChar = null;
   // protected INode floor1 = null;
   // protected INode floor2 = null;

    Button left;
    Button right;
    Button front;
    Button back;
    Button fight;
    Button turnLeft;
    Button turnRight;

    int charakterPosition;




    public GameScene(){
        super(100, INode.RenderMode.REGULAR);
        charakterPosition=0;

        left = new Button("skeleton.png",-0.7,-0.5,0.2, new ButtonHandler(){
            @Override
            public void handle(){
                charakterPosition=sim.move(LEFT);
                Log.d(Constants.LOGTAG,"Links!");
            }
        });

        right = new Button("skeleton.png",-0.7,-0.9,0.2, new ButtonHandler(){
            @Override
            public void handle(){
                charakterPosition=sim.move(RIGHT);
                Log.d(Constants.LOGTAG,"Rechts!");
            }
        });

        front = new Button("skeleton.png",-0.4,-0.7,0.2, new ButtonHandler(){
            @Override
            public void handle(){
                charakterPosition=sim.move(FRONT);
                Log.d(Constants.LOGTAG,"Vor!");
            }
        });

        back = new Button("skeleton.png",-0.7,-0.7,0.2, new ButtonHandler(){
            @Override
            public void handle(){
                charakterPosition=sim.move(BACK);
                Log.d(Constants.LOGTAG,"Zurück!");
            }
        });

        fight = new Button("skeleton.png",-0.7,-0.3,0.2, new ButtonHandler(){
            @Override
            public void handle(){
                sim.fightEnemy();
                Log.d(Constants.LOGTAG,"Kämpfe!!!");
            }
        });

        turnLeft = new Button("skeleton.png",-0.7,-0.3,0.2, new ButtonHandler(){
            @Override
            public void handle(){
                sim.fightEnemy();
                Log.d(Constants.LOGTAG,"Nach links gedreht!!!");
            }
        });

        turnRight = new Button("skeleton.png",-0.7,-0.3,0.2, new ButtonHandler(){
            @Override
            public void handle(){
                sim.fightEnemy();
                Log.d(Constants.LOGTAG,"Nach rechts gedreht!!!");
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

        scaleParticle = new ScaleNode(0.02);
        translationParticle = new TranslationNode(new Vector(0,-0.08,-0.1,1));
        transformationParticle = new TransformationNode();

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
        sim = new WorldSimulation2(MARKERPOOLSIZE,OBSTACLEPOOLSIZE);
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
        List<ITriangleMesh> playerCharMesh = reader.read("meshes/Character1.obj");
        InnerNode playerChar = new InnerNode();
        for (ITriangleMesh mesh : playerCharMesh) {
            TriangleMeshNode meshNode = new TriangleMeshNode(mesh);
            playerChar.addChild(meshNode);
        }

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



        particle = new TriangleMesh();
        TriangleMeshFactory.createSphere(particle,1,7);
        particleMeshNode = new TriangleMeshNode(particle);

        scaleParticle.addChild(playerChar);
        transformationParticle.addChild(scaleParticle);
        translationParticle.addChild(transformationParticle);
        //markerStart.addChild(translationParticle);

        rootNode.addChild(translationParticle);
        rootNode.addChild(markerStart);
        rootNode.addChild(markerEnd);



    }

    @Override
    public void onTimerTick(int counter) {

    }

    @Override
    public void onSceneRedraw() {
        if(i>0){
            Log.d(Constants.LOGTAG,"Updateworld");
            updateWorld();

                transformationParticle.setTransformation(markerList.get(charakterPosition).getTransformation());


            i=0;
        }
        i++;
    }
}
