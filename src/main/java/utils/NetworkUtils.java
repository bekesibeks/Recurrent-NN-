package utils;

import network.objectbased.vanilla.Neuron;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Objects.isNull;

public class NetworkUtils {

    public static final int INPUT_LAYER_SIZE = 2;
    public static final int HIDDEN_LAYER_SIZE = 4;
    public static final int OUTPUT_LAYER_SIZE = 1;

    private static Neuron BIAS_NEURON;
    private static final double BIAS_OUTPUT = -1;

    public static final double LEARNING_RATE = 0.9;
    public static final double MOMENTUM = 0.7;

    public static final Random RANDOM = new Random();


    // lol, that singleton
    public static Neuron getBias() {
        if (isNull(BIAS_NEURON)) {
            BIAS_NEURON = new Neuron();
            BIAS_NEURON.setOutput(BIAS_OUTPUT);
        }
        return BIAS_NEURON;
    }


    public static List<Neuron> createNeurons(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> new Neuron())
                .collect(Collectors.toList());
    }

    public static void buildConnectionBetweenNeurons(List<? extends Neuron> lowerLayer,
                                                     List<? extends Neuron> upperLayer) {
        for (Neuron upperLayerNeuron : upperLayer) {
            for (Neuron lowerLayerNeuron : lowerLayer) {
                upperLayerNeuron.addIncomingWeight(lowerLayerNeuron);
            }
            upperLayerNeuron.addBias();
        }

    }

    public static double sigmoid(double x) {
        return 1.0 / (1.0 + (Math.exp(-x)));
    }


}
