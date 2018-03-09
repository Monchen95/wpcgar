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

public class WorldSimulation2 {

    private final Vector WORLDNORMAL = new Vector(0,1,0);

    private final int AMOUNT;
    private Cell[] cells;
    private Matrix[] markerTransformation;
    private Vector[] markerPositionOnPlane;
    private int activeCell;
    private double minimalDistance = 0.15;
    private int directions = 4;

    public WorldSimulation2(int markerAmount){
        AMOUNT = markerAmount;
        cells = new Cell[AMOUNT];
        markerTransformation = new Matrix[AMOUNT];
        markerPositionOnPlane = new Vector[AMOUNT];

        for(int i=0;i<AMOUNT;i++){
            cells[i] = new Cell(null,null,null,null,i);
        }

        activeCell = 0;
    }

    private void setMarkerTransformations(Matrix[] markerTransformation) {
        this.markerTransformation = markerTransformation;
    }

    private void calcWorld(){
        for(int i=0;i<AMOUNT;i++){

        }
    }

    private void calcActiveNeighbours(){
        double ref_x = markerPositionOnPlane[activeCell].x();
        double ref_y = markerPositionOnPlane[activeCell].y();

        if(calcLeftNeighbour(ref_x)!=-1){
            cells[activeCell].setLeftCell(cells[calcLeftNeighbour(ref_x)]);
        } else {
            cells[activeCell].setLeftCell(null);
        }

        if(calcRightNeighbour(ref_x)!=-1){
            cells[activeCell].setRightCell(cells[calcRightNeighbour(ref_x)]);
        } else {
            cells[activeCell].setRightCell(null);
        }

        if(calcFrontNeighbour(ref_y)!=-1){
            cells[activeCell].setFrontCell(cells[calcFrontNeighbour(ref_y)]);
        } else {
            cells[activeCell].setFrontCell(null);
        }

        if(calcBackNeighbour(ref_y)!=-1){
            cells[activeCell].setBackCell(cells[calcBackNeighbour(ref_y)]);
        } else {
            cells[activeCell].setBackCell(null);
        }

    }

    private int calcLeftNeighbour(double x){
        double ref_x = x;
        int nearestMarker = -1;
        double nearestDistance=1000;                    //willkürlicher hoher wert, der in den Koordinaten nicht vorkommen kann

        for(int i=0;i<AMOUNT;i++){
            if(i!=activeCell){
                if(markerPositionOnPlane[i].x()<(markerPositionOnPlane[activeCell]).x()-minimalDistance){
                    if(markerPositionOnPlane[i].x()!=0) {
                        if (markerPositionOnPlane[i].x() < nearestDistance) {
                            nearestMarker = i;
                            nearestDistance = markerPositionOnPlane[i].x();
                        }
                    }
                }
            }
        }
        return nearestMarker;
    }

    private int calcRightNeighbour(double x){
        double ref_x = x;
        int nearestMarker = -1;
        double nearestDistance=-1000;                    //willkürlicher niedriger wert, der in den Koordinaten nicht vorkommen kann

        for(int i=0;i<AMOUNT;i++){
            if(i!=activeCell){
                if(markerPositionOnPlane[i].x()>(markerPositionOnPlane[activeCell]).x()+minimalDistance){
                    if(markerPositionOnPlane[i].x()!=0){
                        if(markerPositionOnPlane[i].x()>nearestDistance){
                            nearestMarker=i;
                            nearestDistance=markerPositionOnPlane[i].x();
                        }
                    }
                }
            }
        }
        return nearestMarker;
    }

    private int calcBackNeighbour(double y){
        double ref_y = y;
        int nearestMarker = -1;
        double nearestDistance= -1000;                    //willkürlicher niedriger wert, der in den Koordinaten nicht vorkommen kann

        for(int i=0;i<AMOUNT;i++){
            if(i!=activeCell){
                if(markerPositionOnPlane[i].y()>(markerPositionOnPlane[activeCell]).y()+minimalDistance){
                    if(markerPositionOnPlane[i].y()!=0) {
                        if (markerPositionOnPlane[i].y() > nearestDistance) {
                            nearestMarker = i;
                            nearestDistance = markerPositionOnPlane[i].y();
                        }
                    }
                }
            }
        }
        return nearestMarker;
    }

    private int calcFrontNeighbour(double y){
        double ref_y = y;
        int nearestMarker = -1;
        double nearestDistance= 1000;                    //willkürlicher hoher wert, der in den Koordinaten nicht vorkommen kann

        for(int i=0;i<AMOUNT;i++){
            if(i!=activeCell){
                if(markerPositionOnPlane[i].y()<(markerPositionOnPlane[activeCell]).y()-minimalDistance){
                    if(markerPositionOnPlane[i].y()!=0) {
                        if (markerPositionOnPlane[i].y() < nearestDistance) {
                            nearestMarker = i;
                            nearestDistance = markerPositionOnPlane[i].y();
                        }
                    }
                }
            }
        }
        return nearestMarker;
    }

    private void calcMarkerPositionsOnPlane(){
        for(int i=0;i<AMOUNT;i++){
            Vector tmpVec = markerTransformation[i].multiply(new Vector(0,0,0,1)).xyz();
            markerPositionOnPlane[i] = new Vector(tmpVec.x(),tmpVec.y(),0);

        }
        Log.d(Constants.LOGTAG,"Position in der Welt Marker Start: "+'\n'+markerPositionOnPlane[0].toString());
        Log.d(Constants.LOGTAG,"Position in der Welt Marker End: "+'\n'+markerPositionOnPlane[10].toString());
    }

    private boolean markerIsInWorld(Vector position){
        return true;
    }

    public void updateWorldSimulation(Matrix[] markerPositions){
        setMarkerTransformations(markerPositions);
        calcMarkerPositionsOnPlane();
        calcActiveNeighbours();

        Log.d(Constants.LOGTAG,"Aktiver Marker ist Marker: " + activeCell);

        if(cells[activeCell].getLeftCell()!=null){
            Log.d(Constants.LOGTAG,"Linker Nachbar ist Marker: " + cells[activeCell].getLeftCell().getAssignedMarker());
        }
        if(cells[activeCell].getRightCell()!=null){
            Log.d(Constants.LOGTAG,"Rechter Nachbar ist Marker: " + cells[activeCell].getRightCell().getAssignedMarker());
        }
        if(cells[activeCell].getBackCell()!=null){
            Log.d(Constants.LOGTAG,"Hinterer Nachbar ist Marker: " + cells[activeCell].getBackCell().getAssignedMarker());
        }
        if(cells[activeCell].getFrontCell()!=null){
            Log.d(Constants.LOGTAG,"Vorderer Nachbar ist Marker: " + cells[activeCell].getFrontCell().getAssignedMarker());
        }

        //Berechne den Aufbau des Spielfeldes

    }
}
