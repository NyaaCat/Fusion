package cat.nyaa.fusion.ui;

import java.util.List;

public class MatrixCoordinate {
    private List<Integer> rawSlots;
    private int rows;
    private int columns;

    public MatrixCoordinate(List<Integer> rawSlots, int rows, int columns) {
        this.rawSlots = rawSlots;
        this.rows = rows;
        this.columns = columns;
    }

    public int access(int row, int column) {
        return rawSlots.get(rows * row + column);
    }

    public int indexOf(int rawSlot){
        return rawSlots.indexOf(rawSlot);
    }
}
