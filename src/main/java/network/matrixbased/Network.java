package network.matrixbased;

import math.model.Matrix;
import math.utils.MatrixUtils;
import utils.NetworkUtils;

import static utils.NetworkUtils.*;

public class Network {

    private final Matrix weightsHiddenLayer;
    private final Matrix weightsOutputLayer;

    private final Matrix biasHiddenLayer;
    private final Matrix biasOutputLayer;


    public Network() {
        weightsHiddenLayer = new Matrix(HIDDEN_LAYER_SIZE, INPUT_LAYER_SIZE);
        weightsOutputLayer = new Matrix(OUTPUT_LAYER_SIZE, HIDDEN_LAYER_SIZE);

        weightsHiddenLayer.initializeElementsRandomly();
        weightsOutputLayer.initializeElementsRandomly();

        biasOutputLayer = new Matrix(1, OUTPUT_LAYER_SIZE);
        biasHiddenLayer = new Matrix(1, HIDDEN_LAYER_SIZE);
    }

    public Matrix feedInput(Matrix input) {
        Matrix hiddenLayerOutput = MatrixUtils.dotProduct(input, weightsHiddenLayer);
        // TODO -> Note bias should be added here
        hiddenLayerOutput.applyToElements(NetworkUtils::sigmoid);

        Matrix outputLayerOutput = MatrixUtils.dotProduct(hiddenLayerOutput.transpose(), weightsOutputLayer);
        // TODO -> Note bias should be added here
        outputLayerOutput.applyToElements(NetworkUtils::sigmoid);

        return outputLayerOutput;
    }

    public static void main(String[] args) {
        Network network = new Network();
        Matrix input = new Matrix(2, 1);

        Matrix output = network.feedInput(input);

        System.out.println(output);
    }

}
