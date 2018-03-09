package edu.hawhamburg.shared.simulation;

import android.util.Log;
import android.view.VelocityTracker;

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
    private Matrix refMarkerTransformation;

    private Cell[] cells;
    private Matrix[] markerTransformation;
    private Vector[] markerPositionInRefCoord;
    private Vector[] markerPositionOnPlane;

    public WorldSimulation(int markerAmount){
        cells = new Cell[markerAmount];
        markerTransformation = new Matrix[markerAmount];
        markerPositionInRefCoord = new Vector[markerAmount];
        markerPositionOnPlane = new Vector[markerAmount];
    }

    public Vector giveCharacterPosition(){
        return Vector.makeHomogenious(markerPositionInRefCoord[10]);
    }

    private void setMarkerTransformations(Matrix[] markerTransformation) {
        this.markerTransformation = markerTransformation;
        refMarkerTransformation = markerTransformation[0].getInverse();
    }

    private void calcMarkerCoordsInRef(){
        for(int i=0;i<markerPositionInRefCoord.length;i++){
            Matrix transformationInRef = markerTransformation[i].multiply(refMarkerTransformation);
            markerPositionInRefCoord[i] = transformationInRef.multiply(new Vector(0,0,0,1)).xyz();
        }
        Log.d(Constants.LOGTAG,"Vektor " +10+ " im Ref Coordsys " + '\n' + markerPositionInRefCoord[10].toString());
    }
    
    private void calcMarkerPositionsOnPlane(){
        
    }

    private boolean markerIsInWorld(Vector position){
        return true;
    }

    public void updateWorldSimulation(Matrix[] markerPositions){
        setMarkerTransformations(markerPositions);
        calcMarkerCoordsInRef();

        //Rechne die Positionen auf die Ebene mit der Normale 0,1,0
      //  for(int i=0;i<markerPositions.length;i++){
     //       if(markerPositions[i]!=null){
                ///
                ///
      //          markerPositionOnNormal = null; //hier richtig zuweisen
      //      }
     //   }


        //Berechne den Aufbau des Spielfeldes

    }
}
