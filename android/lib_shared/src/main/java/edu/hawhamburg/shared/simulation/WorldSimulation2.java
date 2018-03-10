package edu.hawhamburg.shared.simulation;

import android.util.Log;

import edu.hawhamburg.shared.action.Direction;
import edu.hawhamburg.shared.characters.NPC;
import edu.hawhamburg.shared.dungeon.Cell;
import edu.hawhamburg.shared.math.Comparison;
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

    private final int MARKERAMOUNT;
    private final int OBSTACLEAMOUNT;
    private Cell[] cells;
    private Matrix[] markerTransformation;
    private Matrix[] obstacleTransformation;
    private Vector[] markerPositionOnPlane;
    private Vector[] obstaclePositionOnPlane;
    private int activeCell;
    private double minimalDistance = 0.3;
    private int directions = 4;
    NPC playerCharacter;
    NPC enemyCharacter;
    NPC hostageCharacter;

    public WorldSimulation2(int markerAmount, int obstacleAmount){
        MARKERAMOUNT = markerAmount;
        OBSTACLEAMOUNT = obstacleAmount;
        cells = new Cell[MARKERAMOUNT];
        markerTransformation = new Matrix[MARKERAMOUNT];
        markerPositionOnPlane = new Vector[MARKERAMOUNT];
        obstacleTransformation = new Matrix[OBSTACLEAMOUNT];
        obstaclePositionOnPlane = new Vector[OBSTACLEAMOUNT];

        for(int i = 0; i< MARKERAMOUNT; i++){
            cells[i] = new Cell(null,null,null,null,i);
        }
        activeCell = 0;

        playerCharacter=new NPC(100,20,"Hank");
        enemyCharacter=new NPC(50,10,"Bad Orc");
        hostageCharacter=new NPC(1000,2000,"Frank");

    }

    private void setMarkerTransformations(Matrix[] markerTransformation) {
        this.markerTransformation = markerTransformation;
    }

    private void calcWorld(){
        for(int i = 0; i< MARKERAMOUNT; i++){

        }
    }

    public int move(Direction there){
        if(cells[activeCell].getCellInDirection(there)==null){
            Log.d(Constants.LOGTAG,"Keine Nachbarzelle!");
            return activeCell;
        }
        if(!checkFree(there)){
            Log.d(Constants.LOGTAG,"Da ist etwas im Weg!");
            return activeCell;
        }
        activeCell=cells[activeCell].getCellInDirection(there).getAssignedMarker();
        Log.d(Constants.LOGTAG,"Habe mich bewegt!");
        return activeCell;
    }

    private boolean checkFree(Direction there){
        if(cells[activeCell].getCellInDirection(there)==null){
            return false;
        }
        double other_x=0;
        double other_y=0;
        int otherCell;
        if(there==Direction.LEFT){
            otherCell=cells[activeCell].getLeftCell().getAssignedMarker();
            other_x=markerPositionOnPlane[otherCell].x();
            other_y=markerPositionOnPlane[otherCell].y();
        }
        if(there==Direction.RIGHT){
            otherCell=cells[activeCell].getRightCell().getAssignedMarker();
            other_x=markerPositionOnPlane[otherCell].x();
            other_y=markerPositionOnPlane[otherCell].y();
        }
        if(there==Direction.FRONT){
            otherCell=cells[activeCell].getFrontCell().getAssignedMarker();
            other_x=markerPositionOnPlane[otherCell].x();
            other_y=markerPositionOnPlane[otherCell].y();
        }
        if(there==Direction.BACK){
            otherCell=cells[activeCell].getBackCell().getAssignedMarker();
            other_x=markerPositionOnPlane[otherCell].x();
            other_y=markerPositionOnPlane[otherCell].y();
        }

        boolean isFree = calcFreeBetween(other_x,other_y);

        return isFree;
    }

    private boolean hasLeftNeighbour(){
        if(cells[activeCell].getLeftCell()==null){
            return false;
        } else {
            return true;
        }
    }
    private boolean hasRightNeighbour(){
        if(cells[activeCell].getRightCell()==null){
            return false;
        } else {
            return true;
        }
    }
    private boolean hasFrontNeighbour(){
        if(cells[activeCell].getFrontCell()==null){
            return false;
        } else {
            return true;
        }
    }
    private boolean hasBackNeighbour(){
        if(cells[activeCell].getBackCell()==null){
            return false;
        } else {
            return true;
        }
    }

    private boolean calcFreeBetween(double other_x, double other_y){
        Log.d(Constants.LOGTAG,"Nachbar x und y:" + other_x +"  " + other_y);
        if(other_x == 0.0 || other_y == 0.0){
            return false;
        }

        double this_x=markerPositionOnPlane[cells[activeCell].getAssignedMarker()].x();
        double this_y=markerPositionOnPlane[cells[activeCell].getAssignedMarker()].y();

        double obstacle_x;
        double obstacle_y;

        for(int i=0;i<OBSTACLEAMOUNT;i++){
            obstacle_x=obstaclePositionOnPlane[i].x();
            obstacle_y=obstaclePositionOnPlane[i].y();
            if(obstacle_x!=0 && obstacle_y!=0) {
                if (Comparison.LTLT(this_x, obstacle_x, other_x) || Comparison.LTLT(other_x, obstacle_x, this_x)) {
                    if (Comparison.GTGT(this_y + minimalDistance, obstacle_y, other_y - minimalDistance) || Comparison.GTGT(other_y + minimalDistance, obstacle_y, this_y - minimalDistance)) {
                        return false;
                    }
                }
            }
        }
        Log.d(Constants.LOGTAG,"Sowas von Frei!");
        return true;
    }

    public int aktiverMarker(){
        return activeCell;
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
        double nearestDistance=-1000;                    //willkürlicher niedriger wert, der in den Koordinaten nicht vorkommen kann

        for(int i = 0; i< MARKERAMOUNT; i++){
            if(i!=activeCell){
                if(markerPositionOnPlane[i].x()<(markerPositionOnPlane[activeCell]).x()-minimalDistance){
                    if(markerPositionOnPlane[i].x()!=0) {
                        if (markerPositionOnPlane[i].x() > nearestDistance) {
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
        double nearestDistance=1000;                    //willkürlicher hoher wert, der in den Koordinaten nicht vorkommen kann

        for(int i = 0; i< MARKERAMOUNT; i++){
            if(i!=activeCell){
                if(markerPositionOnPlane[i].x()>(markerPositionOnPlane[activeCell]).x()+minimalDistance){
                    if(markerPositionOnPlane[i].x()!=0){
                        if(markerPositionOnPlane[i].x()<nearestDistance){
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
        double nearestDistance= 1000;                    //willkürlicher hoher wert, der in den Koordinaten nicht vorkommen kann

        for(int i = 0; i< MARKERAMOUNT; i++){
            if(i!=activeCell){
                if(markerPositionOnPlane[i].y()>(markerPositionOnPlane[activeCell]).y()+minimalDistance){
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

    private int calcFrontNeighbour(double y){
        double ref_y = y;
        int nearestMarker = -1;
        double nearestDistance= -1000;                    //willkürlicher niedriger wert, der in den Koordinaten nicht vorkommen kann

        for(int i = 0; i< MARKERAMOUNT; i++){
            if(i!=activeCell){
                if(markerPositionOnPlane[i].y()<(markerPositionOnPlane[activeCell]).y()-minimalDistance){
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

    private void calcMarkerPositionsOnPlane(){
        for(int i = 0; i< MARKERAMOUNT; i++){
            Vector tmpVec = markerTransformation[i].multiply(new Vector(0,0,0,1)).xyz();
            markerPositionOnPlane[i] = new Vector(tmpVec.x(),tmpVec.y(),0);

        }
        Log.d(Constants.LOGTAG,"Position in der Welt Marker Start: "+'\n'+markerPositionOnPlane[0].toString());
        Log.d(Constants.LOGTAG,"Position in der Welt Marker End: "+'\n'+markerPositionOnPlane[10].toString());
    }

    private boolean markerIsInWorld(Vector position){
        return true;
    }

    private void setObestacleTransformations(Matrix[] obstacleTransformation){
        this.obstacleTransformation = obstacleTransformation;
    }

    private void calcObstaclePositionsOnPlane(){
        for(int i = 0; i< OBSTACLEAMOUNT; i++){
            Vector tmpVec = obstacleTransformation[i].multiply(new Vector(0,0,0,1)).xyz();
            obstaclePositionOnPlane[i] = new Vector(tmpVec.x(),tmpVec.y(),0);
        }
    }

    public void updateWorldSimulation(Matrix[] markerPositions, Matrix[] obstaclePositions){
        setMarkerTransformations(markerPositions);
        calcMarkerPositionsOnPlane();
        calcActiveNeighbours();
        setObestacleTransformations(obstaclePositions);
        calcObstaclePositionsOnPlane();

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
