package network.objectbased.vanilla;

import static utils.NetworkUtils.LEARNING_RATE;
import static utils.NetworkUtils.RANDOM;

public class Weight {

    private final Neuron fromNeuron;
    private final Neuron toNeuron;

    private double weight;

    // For Adagram/Momentum or other optimization algorithms, keep the values in memory
    private double newWeight;
    private double previousDelta;
    private double adagradDelta;
    private double delta;

    public Weight(Neuron fromNeuron, Neuron toNeuron) {
        this.fromNeuron = fromNeuron;
        this.toNeuron = toNeuron;

        this.weight = (RANDOM.nextDouble() * 2) - 1; //  (-1, 1)
        this.previousDelta = 0;
        this.delta = 0;
    }

    // In case online training is used, this method should be called in each iteration after gradients are calculated
    public void updateWeightOnlineTraining() {
        this.weight = newWeight;
    }

    // In case batch training is used, this method should be called, after each batch
    public void updateWeightBatchTraining() {
        double batchDelta = -LEARNING_RATE * delta;

        double newWeight = weight + batchDelta;
        // Notice, MOMENTUM or ADAGRAD is not used here,
        // TODO -> implement them later if the performance is bad
        weight = newWeight;

        delta = 0;
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

    public double getDelta() {
        return delta;
    }

    public void addDelta(double delta) {
        this.delta += delta;
    }
}
