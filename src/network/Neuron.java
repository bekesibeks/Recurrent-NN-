package network;

import utils.NetworkUtils;

import java.util.*;

import static utils.NetworkUtils.sigmoid;

public class Neuron {

    private static int NEURON_COUNTER = 1;

    /*
        Only to help debugging, can be removed later on
     */
    private final String neuronName;
    private final Map<String, Weight> weightsLookup;

    private final List<Weight> incomingWeights;

    /*
       Maybe bias doesnt need to be a separated weight
     */
    private Weight bias;
    private double output;

    public Neuron() {
        incomingWeights = new ArrayList<>();
        weightsLookup = new HashMap<>();
        neuronName = ++NEURON_COUNTER + "#";
    }

    public void calculateOutput() {
        output = incomingWeights.stream()
                .map(weight -> weight.getWeightenedOutput()) // Notice bias is included in here
                .mapToDouble(weight -> weight)
                .sum();

        output = sigmoid(output);
    }

    public void addBias() {
        bias = new Weight(NetworkUtils.getBias(), this);
        incomingWeights.add(bias);
    }

    public void addIncomingWeight(Neuron inputNeuron) {
        Weight weight = new Weight(inputNeuron, this);
        incomingWeights.add(weight);
        weightsLookup.put(inputNeuron.getNeuronName(), weight);
    }

    public List<Weight> getIncomingWeights() {
        return incomingWeights;
    }

    public Weight getBias() {
        return bias;
    }

    public void setBias(Weight bias) {
        this.bias = bias;
    }

    public double getOutput() {
        return output;
    }

    public void setOutput(double output) {
        this.output = output;
    }

    public String getNeuronName() {
        return neuronName;
    }

    public Weight getConnectionFor(Object key) {
        return weightsLookup.get(key);
    }
}
