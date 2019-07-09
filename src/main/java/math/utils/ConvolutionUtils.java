package math.utils;

public class ConvolutionUtils {


    /*
     * Kernel is the 'filter' and should have a center point (so NxN where N is an odd number)
     */
    public static double[][] convolution(double[][] sourceMatrix,
                                         double[][] kernel,
                                         int stride) {
        int resultMatrixSizeX = (sourceMatrix.length) / stride - 1;
        int resultMatrixSizeY = (sourceMatrix[0].length) / stride - 1;

        int kernelSize = kernel.length;

        double[][] result = new double[resultMatrixSizeX][resultMatrixSizeY];
        double[][] partToConvolve = new double[kernel.length][kernel[0].length];

        /*
         * i, j iterating over the source matrix
         */
        for (int i = 0; i < sourceMatrix.length - kernelSize; i++) {
            for (int j = 0; j < sourceMatrix[0].length - kernelSize; j++) {
                /*
                 * indexX,indexY iterating over the subMatrix which need to be used during the convolution
                 */
                for (int indexX = 0; indexX < kernelSize; indexX++) {
                    for (int indexY = 0; indexY < kernelSize; indexY++) {
                        partToConvolve[indexX][indexY] = sourceMatrix[i + indexX][j + indexY];
                    }
                }

                double[][] convolutionResult = MatrixUtils.dotProductElementwise(kernel, partToConvolve);

                double sum = 0;
                for (int indexX = 0; indexX < convolutionResult.length; indexX++) {
                    for (int indexY = 0; indexY < convolutionResult.length; indexY++) {
                        sum += convolutionResult[indexX][indexY];
                    }
                }
                //
                result[i][j] = sum;
            }
        }


        return result;
    }


}
