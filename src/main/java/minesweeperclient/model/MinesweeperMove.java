package minesweeperclient.model;

import minesweeperclient.model.CellChange;
import minesweeperclient.model.Minesweeper;

import java.util.List;

public class MinesweeperMove {

    private Minesweeper minesweeper;
    private List<CellChange> cellChanges;

    public Minesweeper getMinesweeper() {
        return minesweeper;
    }

    private void setMinesweeper(Minesweeper minesweeper) {
        this.minesweeper = minesweeper;
    }

    public List<CellChange> getCellChanges() {
        return cellChanges;
    }

    private void setCellChanges(List<CellChange> cellChanges) {
        this.cellChanges = cellChanges;
    }
}
