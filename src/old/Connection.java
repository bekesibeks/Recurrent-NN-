package old;

public class Connection {
    double weight = 0;
    double prevDeltaWeight = 0; // for momentum
    double deltaWeight = 0;

    double previousWieght = 0;

    double adagradParameterWeight = 0;

    final Neuron leftNeuron;
    final Neuron rightNeuron;
    static int counter = 0;
    final public int id; // auto increment, starts at 0

    public Connection(Neuron fromN, Neuron toN) {
        leftNeuron = fromN;
        rightNeuron = toN;
        id = counter;
        counter++;
    }

    public void setAdagradParameterWeight(double delta) {
        adagradParameterWeight += delta * delta;
    }

    public double getAdagradParameterWeight() {
        return adagradParameterWeight;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double w) {
        previousWieght = weight;
        weight = w;
    }

    public double getPreviousWieght() {
        return previousWieght;
    }

    public void setPreviousWieght(double previousWieght) {
        this.previousWieght = previousWieght;
    }

    public void setDeltaWeight(double w) {
        prevDeltaWeight = deltaWeight;
        deltaWeight = w;
    }

    public double getPrevDeltaWeight() {
        return prevDeltaWeight;
    }

    public Neuron getFromNeuron() {
        return leftNeuron;
    }

    public Neuron getToNeuron() {
        return rightNeuron;
    }
}