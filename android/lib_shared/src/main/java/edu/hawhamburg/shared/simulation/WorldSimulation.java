package edu.hawhamburg.shared.simulation;

import android.util.Log;

import edu.hawhamburg.shared.dungeon.Cell;
import edu.hawhamburg.shared.math.Matrix;
import edu.hawhamburg.shared.math.Vector;
import edu.hawhamburg.shared.misc.Constants;

/**
 * Created by Devran on 06.03.2018.
 */

/*
Ich weiß durch die Indizies des Array markerPositions, zu welchem Marker die Position gehört.
Ich baue also eine 1:1 Beziehung zwischen Zelle und Marker auf, verkettete diese dann anhand der
Position.

 */

public class WorldSimulation {

    private final Vector WORLDNORMAL = new Vector(0,1,0);

    private Cell[] cells;
    private Vector[] markerPositions;
    private Vector[] markerPositionOnNormal;

    public WorldSimulation(int markerPositionsLength){
        cells = new Cell[markerPositionsLength];
    }



    public void setMarkerPositions(Vector[] markerPositions) {
        this.markerPositions = markerPositions;
    }

    private boolean markerIsInWorld(Vector position){
        if(position.get(0)!=0 && position.get(1)!=0 && position.get(2)!=0){
            return true;
        } else {
            return false;
        }
    }

    public void updateWorldSimulation(Vector[] markerPositions){
        setMarkerPositions(markerPositions);

        //Rechne die Positionen auf die Ebene mit der Normale 0,1,0
      //  for(int i=0;i<markerPositions.length;i++){
     //       if(markerPositions[i]!=null){
                ///
                ///
      //          markerPositionOnNormal = null; //hier richtig zuweisen
      //      }
     //   }
        if(markerIsInWorld(markerPositions[0])){
          //  Log.d(Constants.LOGTAG,"Marker 1 Transformationsmatrix Invers: " +  '\n' + markerPositions[0].toString());
        }
        if(markerIsInWorld(markerPositions[1])){
         //   Log.d(Constants.LOGTAG,"Marker 2 Transformationsmatrix Invers: " +  '\n' + markerPositions[1].toString());
        }



        //Berechne den Aufbau des Spielfeldes

    }
}
