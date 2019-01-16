package utils;

import network.Neuron;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NetworkUtils {

    public static final int INPUT_LAYER_SIZE = 2;
    public static final int HIDDEN_LAYER_SIZE = 4;
    public static final int OUTPUT_LAYER_SIZE = 1;

    private static Neuron BIAS_NEURON;

    public static final double LEARNING_RATE = 0.9;
    public static final double MOMENTUM = 0.7;

    public static final Random RANDOM = new Random();

    public static Neuron getBias() {
        if (Objects.isNull(BIAS_NEURON)) {
            BIAS_NEURON = new Neuron();
            BIAS_NEURON.setOutput(-1);
        }
        return BIAS_NEURON;
    }


    public static List<Neuron> createNeurons(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> new Neuron())
                .collect(Collectors.toList());
    }

    public static void buildConnectionBetweenNeurons(List<Neuron> lowerLayer, List<Neuron> upperLayer) {
        for (Neuron upperLayerNeuron : upperLayer) {
            for (Neuron lowerLayerNeuron : lowerLayer) {
                upperLayerNeuron.addIncomingWeight(lowerLayerNeuron);
            }
            upperLayerNeuron.addBias();
        }

    }

    public static double sigmoid(double x) {
        return 1.0 / (1.0 +  (Math.exp(-x)));
    }


}
