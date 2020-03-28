package cat.nyaa.fusion.ui;

public class UiCoordinate {
    private int row;
    private int column;

    public UiCoordinate(int row, int column){
        this.row = row;
        this.column = column;
    }

    public int access(MatrixCoordinate matrixCoordinate){
        return matrixCoordinate.access(row, column);
    }
}
