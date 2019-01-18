package network.recurrent;

import network.vanilla.Neuron;

import java.util.ArrayList;
import java.util.List;

import static utils.NetworkUtils.HIDDEN_LAYER_SIZE;
import static utils.NetworkUtils.sigmoid;

public class RecurrentNeuron extends Neuron {

    private List<Double> hiddenStateWeights;
    private List<List<Double>> previousOutputs;

    public RecurrentNeuron() {
        super();
        this.hiddenStateWeights = new ArrayList<>();
        this.previousOutputs = new ArrayList<>();

        initializeHiddenWeights();
    }

    @Override
    public double calculateOutput() {
        output = incomingWeights.stream()
                .map(weight -> weight.getWeightenedOutput()) // Note: bias is included in here...
                .mapToDouble(weight -> weight)
                .sum();


        if (previousOutputs.size() > 1) { // If this is not the first iteration
            double hiddenStateSum = 0;
            for (int i = 0; i < hiddenStateWeights.size(); i++) {
                hiddenStateSum += hiddenStateWeights.get(i) * previousOutputs.get(previousOutputs.size() - 1).get(i);
            }
            output += hiddenStateSum;
        }

        output = sigmoid(output);
        return output;
    }

    public void addPreviousOutput(List<Double> previousOutput) {
        previousOutputs.add(previousOutput);
    }

    private void initializeHiddenWeights() {
        for (int i = 0; i < HIDDEN_LAYER_SIZE; i++) {
            hiddenStateWeights.add(new Double(0));
        }
    }
}
