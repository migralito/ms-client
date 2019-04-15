package minesweeperclient.model;

public class CellChange {

    private Coordinates coordinates;
    private String cell;

    public Coordinates getCoordinates() {
        return coordinates;
    }

    private void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public String getCell() {
        return cell;
    }

    private void setCell(String cell) {
        this.cell = cell;
    }
}
