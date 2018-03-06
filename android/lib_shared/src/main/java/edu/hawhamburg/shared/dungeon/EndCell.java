package edu.hawhamburg.shared.dungeon;

/**
 * Created by Devran on 06.03.2018.
 */

public class EndCell extends Cell {

    public EndCell(Cell leftCell, Cell rightCell, Cell frontCell, Cell backCell) {
        super(leftCell, rightCell, frontCell, backCell);
    }
}
