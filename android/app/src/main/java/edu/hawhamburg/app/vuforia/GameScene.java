package edu.hawhamburg.app.vuforia;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import edu.hawhamburg.shared.datastructures.mesh.ITriangleMesh;
import edu.hawhamburg.shared.datastructures.mesh.TriangleMesh;
import edu.hawhamburg.shared.datastructures.mesh.TriangleMeshFactory;
import edu.hawhamburg.shared.math.Matrix;
import edu.hawhamburg.shared.math.Vector;
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

/**
 * Created by Devran on 06.03.2018.
 */

public class GameScene extends Scene{

    private int i = 0;

    private final int MARKERPOOLSIZE = 11;

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

    private ScaleNode scaleParticle;
    private TranslationNode translationParticle;
    private TransformationNode transformationParticle;
    private ITriangleMesh particle;
    private TriangleMeshNode particleMeshNode;

    private void addMarkerPositionToArray(int arrayPosition, VuforiaMarkerNode marker){
            markerTransformationArray[arrayPosition] = marker.getTransformation();
    }

    private void clearMarkerPositionArray(){
        for(int i = 0; i< markerTransformationArray.length; i++){
            markerTransformationArray[i]=Matrix.createIdentityMatrix4();
        }
    }

    private void initWorld(){
        sim = new WorldSimulation2(MARKERPOOLSIZE);
    }

    private void updateWorld(){
        for(int i=0;i<markerList.size();i++){
            addMarkerPositionToArray(i,markerList.get(i));
        }

        sim.updateWorldSimulation(markerTransformationArray);
        clearMarkerPositionArray();
    }

    public GameScene(){
        super(100, INode.RenderMode.REGULAR);

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

        scaleParticle = new ScaleNode(0.09);
        translationParticle = new TranslationNode(new Vector(0,0,0,1));
        transformationParticle = new TransformationNode();

        initWorld();
    }


    @Override
    public void onSetup(InnerNode rootNode) {

        particle = new TriangleMesh();
        TriangleMeshFactory.createSphere(particle,1,7);
        particleMeshNode = new TriangleMeshNode(particle);

        scaleParticle.addChild(particleMeshNode);
        transformationParticle.addChild(scaleParticle);
        translationParticle.addChild(transformationParticle);

        rootNode.addChild(translationParticle);
        rootNode.addChild(markerStart);
        rootNode.addChild(markerEnd);



    }

    @Override
    public void onTimerTick(int counter) {

    }

    @Override
    public void onSceneRedraw() {
        if(i>50){
            Log.d(Constants.LOGTAG,"Updateworld");
            updateWorld();
            //transformationParticle.setTransformation(markerEnd.getTransformation());
            //transformationParticle.setTransformation(markerStart.getTransformation());
           // translationParticle.setTranslation(sim.giveCharacterPosition());

            i=0;
        }
        i++;
    }
}
