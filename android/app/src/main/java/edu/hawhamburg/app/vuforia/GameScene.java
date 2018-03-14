package edu.hawhamburg.app.vuforia;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.annotation.StringDef;
import android.support.annotation.StyleRes;
import android.util.Log;
import android.view.Display;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import edu.hawhamburg.shared.characters.Pose;
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

    private ScaleNode scaleParticle;
    private TranslationNode translationParticle;
    private TransformationNode transformationParticle;
    private ITriangleMesh particle;
    private TriangleMeshNode particleMeshNode;
    private TriangleMeshNode meshNodeNormal;
    private TriangleMeshNode meshNodeHappy;
    private TriangleMeshNode meshNodeFighting;
    private TriangleMeshNode meshNodeJumping;
    private TriangleMeshNode meshNodeActive;

    private InnerNode playerChar = null;
   // protected INode floor1 = null;
   // protected INode floor2 = null;

    Button left;
    Button right;
    Button front;
    Button back;
    Button fight;
    Button turnLeft;
    Button turnRight;

    TextView playerHealth;
    TextView playerDamage;

    TextView enemeyHealth;
    TextView enemyDamage;

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

        fight = new Button("skeleton.png",-0.7,0.7,0.2, new ButtonHandler(){
            @Override
            public void handle(){
                sim.fightEnemy();
                Log.d(Constants.LOGTAG,"Kämpfe!!!");
            }
        });

        this.redraw();



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




        particle = new TriangleMesh();
        TriangleMeshFactory.createSphere(particle,1,7);
        particleMeshNode = new TriangleMeshNode(particle);

        scaleParticle.addChild(playerChar);
        transformationParticle.addChild(scaleParticle);
        translationParticle.addChild(transformationParticle);
        //markerStart.addChild(translationParticle);

        rootNode.addChild(translationParticle);

        for(int i=0;i<markerList.size();i++){
            rootNode.addChild(markerList.get(i));
        }

        for(int i=0;i<obstacleList.size();i++){
            rootNode.addChild(obstacleList.get(i));
        }

       // rootNode.addChild(markerStart);
       // rootNode.addChild(markerEnd);



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
                transformationParticle.setTransformation(markerList.get(charakterPosition).getTransformation());


            i=0;
        }
        i++;
    }
}
