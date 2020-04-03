package cat.nyaa.fusion.ui;

import java.util.List;

public class MatrixCoordinate implements MatrixAccess{
    private List<Integer> rawSlots;
    private int rows;
    private int columns;

    public MatrixCoordinate(List<Integer> rawSlots, int rows, int columns) {
        this.rawSlots = rawSlots;
        this.rows = rows;
        this.columns = columns;
    }

    public int access(int row, int column) {
        return rawSlots.get(columns * row + column);
    }

    public int indexOf(int rawSlot){
        return rawSlots.indexOf(rawSlot);
    }

    @Override
    public int rows() {
        return rows;
    }

    @Override
    public int columns() {
        return columns;
    }

    @Override
    public int size(){
        return rows*columns;
    }
}
