package edu.hawhamburg.shared.dungeon;

import edu.hawhamburg.shared.datastructures.statusEffect.StatusEffect;

/**
 * Created by Devran on 06.03.2018.
 */

public class Cell {

    private int assignedMarker;
    private StatusEffect cellEffect;

    public int getAssignedMarker() {
        return assignedMarker;
    }

    public void setAssignedMarker(int assignedMarker) {
        this.assignedMarker = assignedMarker;
    }

    public StatusEffect getCellEffect() {
        return cellEffect;
    }

    public void setCellEffect(StatusEffect cellEffect) {
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

    private Cell leftCell;
    private Cell rightCell;
    private Cell frontCell;
    private Cell backCell;
    private boolean blocked;
    private boolean hasNPC;

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
    }

}
