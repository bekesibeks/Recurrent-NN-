package network;

import static utils.NetworkUtils.RANDOM;

public class Weight {

    private final Neuron fromNeuron;
    private final Neuron toNeuron;

    private double weight;

    // For Adagram/Momentum or other optimization algorithms, keep the values in memory
    private double newWeight;
    private double previousDelta;
    private double adagradDelta;

    public Weight(Neuron fromNeuron, Neuron toNeuron) {
        this.fromNeuron = fromNeuron;
        this.toNeuron = toNeuron;

        weight = (RANDOM.nextDouble() * 2) - 1;
        previousDelta = 0;
    }

    public void updateWeight(){
        this.weight = newWeight;
    }

    public double getPreviousDelta() {
        return previousDelta;
    }

    public void setPreviousDelta(double previousDelta) {
        this.previousDelta = previousDelta;
    }

    public double getWeightenedOutput() {
        return weight * fromNeuron.getOutput();
    }

    public Neuron getFromNeuron() {
        return fromNeuron;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getNewWeight() {
        return newWeight;
    }

    public void setNewWeight(double newWeight) {
        this.newWeight = newWeight;
    }

    public double getAdagradDelta() {
        return adagradDelta;
    }

    public void setAdagradDelta(double adagradDelta) {
        this.adagradDelta = adagradDelta;
    }
}
