import network.objectbased.data.BatchTrainingSample;
import network.objectbased.data.TrainingSample;
import network.objectbased.vanilla.NeuralNetwork;

import java.util.List;

import static java.util.Arrays.asList;

public class Main {


    public static void main(String[] args) {
        NeuralNetwork network = new NeuralNetwork();

        TrainingSample traningSample1 = new TrainingSample(asList(0.0, 1.0), asList(1.0));
        TrainingSample traningSample2 = new TrainingSample(asList(1.0, 0.0), asList(1.0));
        TrainingSample traningSample3 = new TrainingSample(asList(1.0, 1.0), asList(0.0));
        TrainingSample traningSample4 = new TrainingSample(asList(0.0, 0.0), asList(0.0));
        BatchTrainingSample batchTraningSample = new BatchTrainingSample(asList(traningSample1, traningSample2, traningSample3, traningSample4));

        double error = 10;
        int i = 0;
        for (i = 0; i < 10000 && error > 0.0001; i++) {
            List<List<Double>> actualOutputs = network.trainNetwork(batchTraningSample);
            error = network.calculateError(batchTraningSample, actualOutputs);
        }

        System.out.println("Check results after iteration : " + i);
        for (TrainingSample trainingSample : batchTraningSample.getTrainingSamples()) {
            List<Double> output = network.feedInputWithNetwork(trainingSample.getInputs());
            System.out.println("Inputs : " + trainingSample.getInputs() + " Expected output : " + trainingSample.getExpectedOutputs() + " Actual : " + output);
        }

        network.printAllWeights();
    }
}
