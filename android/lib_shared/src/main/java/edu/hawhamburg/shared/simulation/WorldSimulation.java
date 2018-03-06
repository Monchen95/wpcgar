package edu.hawhamburg.shared.simulation;

import edu.hawhamburg.shared.dungeon.Cell;
import edu.hawhamburg.shared.math.Matrix;
import edu.hawhamburg.shared.math.Vector;

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
    private Matrix[] markerPositions;
    private Matrix[] markerPositionOnNormal;

    public WorldSimulation(Matrix[] markerPositions){
        cells = new Cell[markerPositions.length];
        setMarkerPositions(markerPositions);
    }

    public void setMarkerPositions(Matrix[] markerPositions) {
        this.markerPositions = markerPositions;
    }

    public void updateWorldSimulation(Matrix[] markerPositions){
        setMarkerPositions(markerPositions);

        //Rechne die Positionen auf die Ebene mit der Normale 0,1,0
        for(int i=0;i<markerPositions.length;i++){
            if(markerPositions[i]!=null){
                ///
                ///
                markerPositionOnNormal = null; //hier richtig zuweisen
            }
        }

        //Berechne den Aufbau des Spielfeldes

    }
}
