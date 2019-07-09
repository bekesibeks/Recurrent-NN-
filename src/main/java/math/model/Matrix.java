package math.model;

import java.util.function.DoubleFunction;

import static utils.NetworkUtils.RANDOM;

public class Matrix {

    private final double[][] matrix;

    public Matrix(int sizeX, int sizeY) {
        matrix = new double[sizeX][sizeY];
    }

    public Matrix(double[][] matrix) {
        this.matrix = matrix;
    }

    public Matrix transpose() {
        double[][] transposed = new double[matrix[0].length][matrix.length];

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                transposed[j][i] = matrix[i][j];
            }
        }
        return new Matrix(transposed);
    }

    public void initializeElementsRandomly() {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                matrix[i][j] = (RANDOM.nextDouble() * 2) - 1;
            }
        }
    }

    public void applyToElements(DoubleFunction<Double> function) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                matrix[i][j] = function.apply(matrix[i][j]);
            }
        }
    }

    public double[][] getMatrix() {
        return matrix;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("-----");
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                stringBuilder.append(matrix[i][j]).append(" , ");
            }
            stringBuilder.append(System.getProperty("line.separator"));
        }

        return stringBuilder.toString();
    }
}
