package math.model;

public class Matrix {

    private final double[][] matrix;

    public Matrix(int sizeX, int sizeY) {
        matrix = new double[sizeX][sizeY];
    }

    public double[][] getMatrix() {
        return matrix;
    }
}
