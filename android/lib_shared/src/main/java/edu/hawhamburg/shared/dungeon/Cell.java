package edu.hawhamburg.shared.dungeon;

import edu.hawhamburg.shared.action.Direction;
import edu.hawhamburg.shared.characters.NPC;
import edu.hawhamburg.shared.datastructures.statusEffect.CharacterEffect;

/**
 * Created by Devran on 06.03.2018.
 */

public class Cell {

    private int assignedMarker;
    private CharacterEffect cellEffect;
    private boolean hasEnemy;
    private NPC npc;

    private Cell leftCell;
    private Cell rightCell;
    private Cell frontCell;
    private Cell backCell;
    private boolean blocked;
    private boolean hasNPC;
    private boolean hasHostage;

    public void setHostage(NPC hostage){
        npc = hostage;
        hasHostage = true;
    }

    public boolean getHasHostage(){
        return hasHostage;
    }

    public void damageEnemy(int amount){
        if(npc != null) {
            npc.afflictDamage(amount);
        }
    }

    public boolean enemyAlive(){
        if(npc ==null){
            return false;
        }
        if(npc.isAlive()) {
            return true;
        } else {
            npc =null;
            return false;
        }
    }

    public NPC getNpc() {
        return npc;
    }

    public void setNpc(NPC npc) {
        hasEnemy = true;
        this.npc = npc;
    }



    public boolean hasEffects(){
        if(cellEffect!=null){
          return true;
        }
        else {
            return false;
        }
    }

    public boolean containsEnemy(){
        return hasEnemy;
    }

    public int getAssignedMarker() {
        return assignedMarker;
    }

    public void setAssignedMarker(int assignedMarker) {
        this.assignedMarker = assignedMarker;
    }

    public CharacterEffect getCellEffect() {
        return cellEffect;
    }

    public void setCellEffect(CharacterEffect cellEffect) {
        this.cellEffect = cellEffect;
    }

    public Cell getLeftCell() {
        return leftCell;
    }

    public void setLeftCell(Cell leftCell) {
        this.leftCell = leftCell;
    }

    public Cell getRightCell() {
        return rightCell;
    }

    public void setRightCell(Cell rightCell) {
        this.rightCell = rightCell;
    }

    public Cell getFrontCell() {
        return frontCell;
    }

    public void setFrontCell(Cell frontCell) {
        this.frontCell = frontCell;
    }

    public Cell getBackCell() {
        return backCell;
    }

    public void setBackCell(Cell backCell) {
        this.backCell = backCell;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public boolean isHasNPC() {
        return hasNPC;
    }

    public void setHasNPC(boolean hasNPC) {
        this.hasNPC = hasNPC;
    }

    public Cell getCellInDirection(Direction there){
        if(there==Direction.LEFT){
            return leftCell;
        }
        if(there==Direction.RIGHT){
            return rightCell;
        }
        if(there==Direction.FRONT){
            return frontCell;
        }
        if(there==Direction.BACK){
            return backCell;
        }
        return null;
    }





    public Cell(Cell leftCell, Cell rightCell, Cell frontCell,Cell backCell){
        this.leftCell=leftCell;
        this.rightCell=rightCell;
        this.frontCell=frontCell;
        this.backCell=backCell;
    }

    public Cell(Cell leftCell, Cell rightCell, Cell frontCell,Cell backCell,int assignedMarker){
        this.leftCell=leftCell;
        this.rightCell=rightCell;
        this.frontCell=frontCell;
        this.backCell=backCell;
        this.assignedMarker=assignedMarker;
        hasEnemy=false;
    }

}
