package network;

import network.data.BatchTrainingSample;
import network.data.TrainingSample;
import utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static utils.NetworkUtils.*;

public class NeuralNetwork {

    private final List<Neuron> inputLayer;
    private final List<Neuron> hiddenLayer;
    private final List<Neuron> outputLayer;

    public NeuralNetwork() {
        this.inputLayer = NetworkUtils.createNeurons(INPUT_LAYER_SIZE);
        this.hiddenLayer = NetworkUtils.createNeurons(HIDDEN_LAYER_SIZE);
        this.outputLayer = NetworkUtils.createNeurons(OUTPUT_LAYER_SIZE);

        NetworkUtils.buildConnectionBetweenNeurons(inputLayer, hiddenLayer);
        NetworkUtils.buildConnectionBetweenNeurons(hiddenLayer, outputLayer);
    }

    /*
     * Nothing fancy: feedforward the input, so neurons will have output/activation values
     * Returns the actual output
     */
    public List<Double> feedInputWithNetwork(List<Double> inputs) {
        if (INPUT_LAYER_SIZE != inputs.size()) throw new IllegalStateException("Inputs / Input layer size mismatch ");

        for (int i = 0; i < INPUT_LAYER_SIZE; i++) {
            inputLayer.get(i).setOutput(inputs.get(i));
        }

        hiddenLayer.stream().forEach(Neuron::calculateOutput);
        outputLayer.stream().forEach(Neuron::calculateOutput);

        return outputLayer.stream().map(Neuron::getOutput).collect(Collectors.toList());
    }

    /*
     * Returns the error
     */
    public Double trainNetworkInBatch(BatchTrainingSample batchTrainingSample) {
        double error = 0;
        for (TrainingSample trainingSample : batchTrainingSample.getTrainingSamples()) {
            List<Double> actualOutputs = trainNetwork(trainingSample);
            double currentError = Math.abs(calculateError(trainingSample, actualOutputs));
            error += currentError;
        }

        return error;
    }

    /*
     *  One iteration only, returns the output
     */
    public List<Double> trainNetwork(TrainingSample trainingSample) {
        if (INPUT_LAYER_SIZE != trainingSample.getInputSize())
            throw new IllegalStateException("Input layer size mismatch ");
        if (OUTPUT_LAYER_SIZE != trainingSample.getOutputSize())
            throw new IllegalStateException("Output layer size mismatch ");

        // Feed the input to activate neurons
        List<Double> actualOutputs = feedInputWithNetwork(trainingSample.getInputs());

        for (int i = 0; i < outputLayer.size(); i++) {
            Neuron outputNeuron = outputLayer.get(i);
            Double desiredOutput = trainingSample.getOutput(i);
            for (Weight weight : outputNeuron.getIncomingWeights()) {
                double actualOutput = outputNeuron.getOutput();
                double hiddenLayerNeuronOutput = weight.getFromNeuron().getOutput();
                double partialDerivative =
                        -actualOutput * (1.0 - actualOutput)   // ~ derivative of the sigmoid function
                                * hiddenLayerNeuronOutput               // ~ input from previous layer
                                * (desiredOutput - actualOutput);       // ~ error

                // not a real update, just set the gradient, and make the actual update after all gradients are calculated
                double deltaWeight = -LEARNING_RATE * partialDerivative;
                double newWeight = weight.getWeight() + deltaWeight;
                weight.setPreviousDelta(deltaWeight);

                weight.setNewWeight(newWeight + MOMENTUM * weight.getPreviousDelta());
            }
        }

        for (int i = 0; i < hiddenLayer.size(); i++) {
            Neuron hiddenNeuron = hiddenLayer.get(i);
            //Calculate backpropagated error
            double backpropagatedError = getBackpropagatedError(trainingSample.getExpectedOutputs(), hiddenNeuron);

            for (Weight weight : hiddenNeuron.getIncomingWeights()) {
                double hiddenLayerNeuronOutput = hiddenNeuron.getOutput();
                double previousLayerOutput = weight.getFromNeuron().getOutput();

                double partialDerivative =
                        hiddenLayerNeuronOutput * (1 - hiddenLayerNeuronOutput)
                                * previousLayerOutput
                                * backpropagatedError;  // instead of (desired - actual), here we calculate with a backprop. value

                double deltaWeight = -LEARNING_RATE * partialDerivative;
                double newWeight = weight.getWeight() + deltaWeight;
                weight.setPreviousDelta(deltaWeight);

                weight.setNewWeight(newWeight + MOMENTUM * weight.getPreviousDelta());
            }
        }

        /*
         * Update the weights with the gradients
         */
        outputLayer.stream()
                .flatMap(neuron -> neuron.getIncomingWeights().stream())
                .forEach(Weight::updateWeight);
        hiddenLayer.stream()
                .flatMap(neuron -> neuron.getIncomingWeights().stream())
                .forEach(Weight::updateWeight);

        return actualOutputs;
    }

    private double getBackpropagatedError(List<Double> expectedOutputs, Neuron hiddenNeuron) {
        double backpropagatedErrorFromOutputLayer = 0;
        for (int j = 0; j < outputLayer.size(); j++) {
            Neuron outputLayerNeuron = outputLayer.get(j);
            Weight weightBetweenHiddenAndOutputLayer = outputLayerNeuron.getConnectionFor(hiddenNeuron.getNeuronName());
            if (nonNull(weightBetweenHiddenAndOutputLayer)) {
                double desiredOutput = expectedOutputs.get(j);
                double actualOutput = outputLayerNeuron.getOutput();
                backpropagatedErrorFromOutputLayer += (-(desiredOutput - actualOutput)
                        * actualOutput * (1 - actualOutput)
                        * weightBetweenHiddenAndOutputLayer.getWeight());
            }
        }
        return backpropagatedErrorFromOutputLayer;
    }


    public double calculateError(TrainingSample trainingSample, List<Double> actualOutputs) {
        double totalError = 0;
        for (int j = 0; j < trainingSample.getOutputSize(); j++) {
            double localError = 0.5 * Math.pow(actualOutputs.get(j) - trainingSample.getOutput(j), 2);
            totalError += localError;
        }
        return totalError;
    }

    public double calculateError(BatchTrainingSample batchTrainingSample, List<List<Double>> actualOutputs) {
        double totalError = 0;
        for (int j = 0; j < batchTrainingSample.getTrainingSamples().size(); j++) {
            totalError += calculateError(batchTrainingSample.getTrainingSamples().get(j), actualOutputs.get(j));
        }
        return totalError;
    }


    public void printAllWeights() {
        System.out.println("Input layer weights : ");
        inputLayer.stream().flatMap(in -> in.getIncomingWeights().stream()).forEach(weight -> System.out.println("Weight : " + weight.getWeight() + " " + weight.getFromNeuron().getNeuronName()));
        System.out.println("Hidden layer weights : ");
        hiddenLayer.stream().flatMap(in -> in.getIncomingWeights().stream()).forEach(weight -> System.out.println("Weight : " + weight.getWeight() + " " + weight.getFromNeuron().getNeuronName()));
        System.out.println("Output layer weights : ");
        outputLayer.stream().flatMap(in -> in.getIncomingWeights().stream()).forEach(weight -> System.out.println("Weight : " + weight.getWeight() + " " + weight.getFromNeuron().getNeuronName()));
    }
}
