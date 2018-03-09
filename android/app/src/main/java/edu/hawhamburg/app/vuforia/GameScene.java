package edu.hawhamburg.app.vuforia;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import edu.hawhamburg.shared.math.Matrix;
import edu.hawhamburg.shared.misc.Constants;
import edu.hawhamburg.shared.misc.Scene;
import edu.hawhamburg.shared.scenegraph.InnerNode;
import edu.hawhamburg.shared.simulation.WorldSimulation;
import edu.hawhamburg.vuforia.VuforiaMarkerNode;

/**
 * Created by Devran on 06.03.2018.
 */

public class GameScene extends Scene{

    private int i = 0;

    private final int MARKERPOOLSIZE = 11;

    private WorldSimulation sim;

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

    private void addMarkerPositionToArray(int arrayPosition, VuforiaMarkerNode marker){
            markerTransformationArray[arrayPosition] = marker.getTransformation();
    }

    private void clearMarkerPositionArray(){
        for(int i = 0; i< markerTransformationArray.length; i++){
            markerTransformationArray[i]=Matrix.createIdentityMatrix4();
        }
    }

    private void initWorld(){
        sim = new WorldSimulation(MARKERPOOLSIZE);
    }

    private void updateWorld(){
        for(int i=0;i<markerList.size();i++){
            addMarkerPositionToArray(i,markerList.get(i));
        }

        sim.updateWorldSimulation(markerTransformationArray);
        clearMarkerPositionArray();
    }

    @Override
    public void onSetup(InnerNode rootNode) {
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

        /*markerStart = new VuforiaMarkerNode("markerStart");
        markerEnd = new VuforiaMarkerNode("markerEnd");
        marker1 = new VuforiaMarkerNode("marker1");
        marker2 = new VuforiaMarkerNode("marker2");
        marker3 = new VuforiaMarkerNode("marker3");
        marker4 = new VuforiaMarkerNode("marker4");
        marker5 = new VuforiaMarkerNode("marker5");
        marker6 = new VuforiaMarkerNode("marker6");
        marker7 = new VuforiaMarkerNode("marker7");
        marker8 = new VuforiaMarkerNode("marker8");
        marker9 = new VuforiaMarkerNode("marker9");
        */

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

        rootNode.addChild(markerStart);
        rootNode.addChild(markerEnd);

        initWorld();

    }

    @Override
    public void onTimerTick(int counter) {

    }

    @Override
    public void onSceneRedraw() {
        if(i>150){
            Log.d(Constants.LOGTAG,"Updateworld");
            updateWorld();

            i=0;
        }
        i++;
    }
}
