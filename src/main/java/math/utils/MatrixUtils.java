package math.utils;

public class MatrixUtils {

    /*
     * Multiply every element in the matrix with a const value
     */
    public static double[][] dotProduct(double[][] matrix, double multiplier) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                matrix[i][j] *= multiplier;
            }
        }
        return matrix;
    }

    /*
     * Dot product of two matrices
     */
    public static double[][] dotProduct(double[][] leftMatrix, double[][] rightMatrix) {
        if (leftMatrix.length != rightMatrix[0].length)
            throw new IllegalStateException("Matrix dimensions are not match. ");

        double[][] product = new double[leftMatrix.length][rightMatrix[0].length];
        int leftMatrixDimensionX = leftMatrix.length;
        int leftMatrixDimensionY = leftMatrix[0].length;
        int rightMatrixDimensionY = rightMatrix[0].length;

        for (int i = 0; i < leftMatrixDimensionX; i++) {
            for (int j = 0; j < rightMatrixDimensionY; j++) {
                for (int k = 0; k < leftMatrixDimensionY; k++) {
                    product[i][j] += leftMatrix[i][k] * rightMatrix[k][j];
                }
            }
        }
        return product;
    }

    public static double[][] add(double[][] matrix, double constant) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                matrix[i][j] += constant;
            }
        }
        return matrix;
    }

    /*
     * TODO -> write unit tests instead
     */
    public static void main(String[] args) {
        double[][] left = {{3, -2, 5}, {3, 0, 4}};
        double[][] right = {{2, 3}, {-9, 0}, {0, 4}};

        double[][] result = dotProduct(left, right);

        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[0].length; j++) {
                System.out.print(result[i][j] + " ");
            }
            System.out.println();
        }
    }


}
