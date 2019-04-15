package minesweeperclient;

import minesweeperclient.model.Minesweeper;

import java.util.List;

public class MinesweeperGame {

    private final MinesweeperConnector connector;
    private Minesweeper minesweeper;

    MinesweeperGame(boolean https, String host) {
        this.connector = new MinesweeperConnector(https, host);
        this.minesweeper = connector.createGame();
    }

    public List<List<String>> getField() {
        return minesweeper.getField();
    }

    public void shovel(int x, int y) {
        this.minesweeper = connector.shovel(minesweeper.getId(), x, y).getMinesweeper();
    }

    public void markBomb(int x, int y) {
        this.minesweeper = connector.mark(minesweeper.getId(), x, y, false).getMinesweeper();
    }

    public void markQuestion(int x, int y) {
        this.minesweeper = connector.mark(minesweeper.getId(), x, y, true).getMinesweeper();
    }

    public void removeMark(int x, int y) {
        this.minesweeper = connector.unmark(minesweeper.getId(), x, y).getMinesweeper();
    }
}
