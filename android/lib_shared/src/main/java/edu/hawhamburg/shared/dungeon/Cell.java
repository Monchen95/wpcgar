package edu.hawhamburg.shared.dungeon;

import edu.hawhamburg.shared.datastructures.statusEffect.StatusEffect;

/**
 * Created by Devran on 06.03.2018.
 */

public class Cell {

    private int assignedMarker;
    private StatusEffect cellEffect;
    private Cell leftCell;
    private Cell rightCell;
    private Cell frontCell;
    private Cell backCell;

    public Cell(Cell leftCell, Cell rightCell, Cell frontCell,Cell backCell){
        this.leftCell=leftCell;
        this.rightCell=rightCell;
        this.frontCell=frontCell;
        this.backCell=backCell;
    }

}
