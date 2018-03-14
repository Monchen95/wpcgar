package edu.hawhamburg.shared.simulation;

import android.app.Activity;
import android.util.Log;

import edu.hawhamburg.shared.action.Direction;
import edu.hawhamburg.shared.characters.NPC;
import edu.hawhamburg.shared.characters.Pose;
import edu.hawhamburg.shared.datastructures.statusEffect.CharacterEffect;
import edu.hawhamburg.shared.dungeon.Cell;
import edu.hawhamburg.shared.math.Comparison;
import edu.hawhamburg.shared.math.Matrix;
import edu.hawhamburg.shared.math.Vector;
import edu.hawhamburg.shared.misc.Constants;
import edu.hawhamburg.shared.scenegraph.TransformationNode;

import static edu.hawhamburg.shared.characters.Pose.FIGHTING;
import static edu.hawhamburg.shared.characters.Pose.NORMAL;


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
    private double maximalDeviation = 0.15;
    private double obstacleDeviation = 0.5;
    private int directions = 4;
    private NPC playerCharacter;
    private NPC enemyCharacter; //auf zelle 6
    private NPC hostageCharacter; //auf zelle 10
    private Pose characterPose;
    int fighter=0;
    private TransformationNode charakterPosition;
    Activity vufAc;

    public WorldSimulation2(int markerAmount, int obstacleAmount){
        characterPose = NORMAL;
        MARKERAMOUNT = markerAmount;
        OBSTACLEAMOUNT = obstacleAmount;
        cells = new Cell[MARKERAMOUNT];
        markerTransformation = new Matrix[MARKERAMOUNT];
        markerPositionOnPlane = new Vector[MARKERAMOUNT];
        obstacleTransformation = new Matrix[OBSTACLEAMOUNT];
        obstaclePositionOnPlane = new Vector[OBSTACLEAMOUNT];

        playerCharacter=new NPC(100,20,"Hank");
        enemyCharacter=new NPC(50,10,"Bad Orc");
        hostageCharacter=new NPC(1000,2000,"Frank");


        for(int i = 0; i< MARKERAMOUNT; i++){
            cells[i] = new Cell(null,null,null,null,i);
        }

        cells[3].setCellEffect(CharacterEffect.HEAL);
        cells[5].setCellEffect(CharacterEffect.DEAL_DAMAGE);
        cells[8].setCellEffect(CharacterEffect.BUFF_WEAPON);
        cells[6].setNpc(enemyCharacter);
        cells[10].setHostage(hostageCharacter);

        activeCell = 0;


    }
    public String getPlayerHealth(){
        return String.valueOf(playerCharacter.getHealth());
    }

    public String getPlayerDamage(){
        return String.valueOf(playerCharacter.getDamage());
    }

    public Pose getPose(){
        return characterPose;
    }

    public void fightEnemy(){
        if(cells[activeCell].containsEnemy()){
            characterPose = FIGHTING;
            cells[activeCell].damageEnemy(playerCharacter.getDamage());
        }
    }

    private void setMarkerTransformations(Matrix[] markerTransformation) {
        this.markerTransformation = markerTransformation;
    }

    private void calcWorld(){
        for(int i = 0; i< MARKERAMOUNT; i++){

        }
    }

    public boolean winningCell(){
        return cells[activeCell].getHasHostage();
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
        if(cells[activeCell].hasEffects()){
            CharacterEffect tmpEffect = cells[activeCell].getCellEffect();
            if(tmpEffect==CharacterEffect.BUFF_WEAPON){
                playerCharacter.amplifyDamage(2);
            }
            if(tmpEffect==CharacterEffect.DEAL_DAMAGE){
                playerCharacter.afflictDamage(20);
            }
            if(tmpEffect==CharacterEffect.HEAL){
                playerCharacter.healUp(20);
            }
        }

        Log.d(Constants.LOGTAG,"Habe mich bewegt!");

        return activeCell;
    }


    private boolean checkFreeXAxis(Direction there){
        if(cells[activeCell].getCellInDirection(there)==null){
            return false;
        }

        double this_x = markerPositionOnPlane[activeCell].x();
        double this_y = markerPositionOnPlane[activeCell].y();
        double other_x = markerPositionOnPlane[cells[activeCell].getCellInDirection(there).getAssignedMarker()].x();
        double other_y = markerPositionOnPlane[cells[activeCell].getCellInDirection(there).getAssignedMarker()].y();

        for(int i=0;i<obstaclePositionOnPlane.length;i++){

            double obstacle_x=obstaclePositionOnPlane[i].x();
            double obstacle_y=obstaclePositionOnPlane[i].y();

            if(obstacle_x!=0.0) {
                if (!(obstacle_y > (this_y + obstacleDeviation) ||
                        obstacle_y < (this_y - obstacleDeviation))) {
                    if (there == Direction.LEFT) {
                        if (other_x<=obstacle_x && obstacle_x < this_x) {
                            return false;
                        }

                    } else if (there == Direction.RIGHT) {
                        if (other_x>=obstacle_x && obstacle_x > this_x) {
                            return false;
                        }
                    }

                }
            }
        }


        return true;
    }

    private boolean checkFreeYAxis(Direction there){
        if(cells[activeCell].getCellInDirection(there)==null){
            return false;
        }
        double this_x = markerPositionOnPlane[activeCell].x();
        double this_y = markerPositionOnPlane[activeCell].y();
        double other_x = markerPositionOnPlane[cells[activeCell].getCellInDirection(there).getAssignedMarker()].x();
        double other_y = markerPositionOnPlane[cells[activeCell].getCellInDirection(there).getAssignedMarker()].y();

        for(int i=0;i<obstaclePositionOnPlane.length;i++){

            double obstacle_x=obstaclePositionOnPlane[i].x();
            double obstacle_y=obstaclePositionOnPlane[i].y();
            if(obstacle_y!=0.0){
                if(!(obstacle_x>(this_x+obstacleDeviation) ||
                        obstacle_x<(this_x-obstacleDeviation))) {
                    if(there==Direction.BACK){
                        if(other_y<=obstacle_y && obstacle_y < this_y){
                            return false;
                        }

                    } else if(there==Direction.FRONT){
                        if(other_y>=obstacle_y && obstacle_y > this_y){
                            return false;
                        }
                    }
               }

            }
        }


        return true;
    }

    private boolean checkFree(Direction there){
        if(cells[activeCell].getCellInDirection(there)==null){
            return false;
        }

        boolean isFree = false;

        if(there==Direction.LEFT){
            isFree = checkFreeXAxis(there);
        }
        if(there==Direction.RIGHT){
            isFree = checkFreeXAxis(there);
        }
        if(there==Direction.FRONT){
            isFree = checkFreeYAxis(there);

        }
        if(there==Direction.BACK){
            isFree = checkFreeYAxis(there);

        }


        return isFree;
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
                if(!(markerPositionOnPlane[i].y()>(markerPositionOnPlane[activeCell].y()+maximalDeviation) ||
                        markerPositionOnPlane[i].y()<(markerPositionOnPlane[activeCell].y()-maximalDeviation))) {
                    if (markerPositionOnPlane[i].x() < (markerPositionOnPlane[activeCell]).x() - minimalDistance) {
                        if (markerPositionOnPlane[i].x() != 0) {
                            if (markerPositionOnPlane[i].x() > nearestDistance) {
                                nearestMarker = i;
                                nearestDistance = markerPositionOnPlane[i].x();
                            }
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
                if(!(markerPositionOnPlane[i].y()>(markerPositionOnPlane[activeCell].y()+maximalDeviation) ||
                        markerPositionOnPlane[i].y()<(markerPositionOnPlane[activeCell].y()-maximalDeviation))) {
                    if (markerPositionOnPlane[i].x() > (markerPositionOnPlane[activeCell]).x() + minimalDistance) {
                        if (markerPositionOnPlane[i].x() != 0) {
                            if (markerPositionOnPlane[i].x() < nearestDistance) {
                                nearestMarker = i;
                                nearestDistance = markerPositionOnPlane[i].x();
                            }
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
                if(!(markerPositionOnPlane[i].x()>(markerPositionOnPlane[activeCell].x()+maximalDeviation) ||
                        markerPositionOnPlane[i].x()<(markerPositionOnPlane[activeCell].x()-maximalDeviation))) {
                    if (markerPositionOnPlane[i].y() > (markerPositionOnPlane[activeCell]).y() + minimalDistance) {
                        if (markerPositionOnPlane[i].y() != 0) {
                            if (markerPositionOnPlane[i].y() < nearestDistance) {
                                nearestMarker = i;
                                nearestDistance = markerPositionOnPlane[i].y();
                            }
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
                if(!(markerPositionOnPlane[i].x()>(markerPositionOnPlane[activeCell].x()+maximalDeviation) ||
                        markerPositionOnPlane[i].x()<(markerPositionOnPlane[activeCell].x()-maximalDeviation))) {
                    if (markerPositionOnPlane[i].y() < (markerPositionOnPlane[activeCell]).y() - minimalDistance) {
                        if (markerPositionOnPlane[i].y() != 0) {
                            if (markerPositionOnPlane[i].y() > nearestDistance) {
                                nearestMarker = i;
                                nearestDistance = markerPositionOnPlane[i].y();
                            }
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

           // Log.d(Constants.LOGTAG,"Position in der Welt Marker: "+ i +'\n'+markerPositionOnPlane[i].toString());
        }
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

    private boolean enemyAlive(){
        return cells[activeCell].containsEnemy() && cells[activeCell].enemyAlive();
    }

    public void updateWorldSimulation(Matrix[] markerPositions, Matrix[] obstaclePositions){
        setMarkerTransformations(markerPositions);
        calcMarkerPositionsOnPlane();
        calcActiveNeighbours();
        setObestacleTransformations(obstaclePositions);
        calcObstaclePositionsOnPlane();


        if(enemyAlive()){
            characterPose = FIGHTING;
        } else if(winningCell()){
            characterPose=Pose.HAPPY;
            Log.d(Constants.LOGTAG,"Du hast Frank gerettet!");
        } else{
            characterPose = NORMAL;
        }

        //kampfloop zum drosseln -> in thread mit timer auslagern
        if(fighter>100 && enemyAlive()){
            playerCharacter.afflictDamage(cells[activeCell].getNpc().getDamage());
            fighter=0;
            Log.d(Constants.LOGTAG,"Gegner fügt: " + cells[activeCell].getNpc().getDamage() + " Schaden zu!");
        }
        fighter++;

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
