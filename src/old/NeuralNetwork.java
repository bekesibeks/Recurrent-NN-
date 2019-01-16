package old;

import java.text.*;
import java.util.*;

public class NeuralNetwork {
    static {
        Locale.setDefault(Locale.ENGLISH);
    }

    final boolean isTrained = false;
    final DecimalFormat df;
    final Random rand = new Random(1);
    final ArrayList<Neuron> inputLayer = new ArrayList<Neuron>();
    final ArrayList<Neuron> hiddenLayer = new ArrayList<Neuron>();
    final ArrayList<Neuron> outputLayer = new ArrayList<Neuron>();
    final Neuron bias = new Neuron();
    final int[] layers;
    final int randomWeightMultiplier = 1;

    final double epsilon = 0.00000000001;

    final double learningRate = 0.9f;
    final double momentum = 0.7f;

    // Inputs for xor problem
    final double inputs[][] = {{1, 1}, {1, 0}, {0, 1}, {0, 0}};

    // Corresponding outputs, xor training data
    final double expectedOutputs[][] = {{0}, {1}, {1}, {0}};
    double resultOutputs[][] = {{-1}, {-1}, {-1}, {-1}}; // dummy init
    double output[];

    // for weight update all
    final HashMap<String, Double> weightUpdate = new HashMap<String, Double>();

    public static void main(String[] args) {



        double sum = 0;

            NeuralNetwork nn = new NeuralNetwork(2, 4, 1);
            int maxRuns = 100000;
            double minErrorCondition = 0.001;

            nn.run(maxRuns, minErrorCondition);




       // System.out.println("Total avarage "+(sum/50));
    }

    public NeuralNetwork(int input, int hidden, int output) {
        this.layers = new int[]{input, hidden, output};
        df = new DecimalFormat("#.0#");

        /**
         * Create all neurons and connections Connections are created in the
         * neuron class
         */
        for (int i = 0; i < layers.length; i++) {
            if (i == 0) { // input layer
                for (int j = 0; j < layers[i]; j++) {
                    Neuron neuron = new Neuron();
                    inputLayer.add(neuron);
                }
            } else if (i == 1) { // hidden layer
                for (int j = 0; j < layers[i]; j++) {
                    Neuron neuron = new Neuron();
                    neuron.addInConnectionsS(inputLayer);
                    neuron.addBiasConnection(bias);
                    hiddenLayer.add(neuron);
                }
            } else if (i == 2) { // output layer
                for (int j = 0; j < layers[i]; j++) {
                    Neuron neuron = new Neuron();
                    neuron.addInConnectionsS(hiddenLayer);
                    neuron.addBiasConnection(bias);
                    outputLayer.add(neuron);
                }
            } else {
                System.out.println("!Error old.NeuralNetwork init");
            }
        }

        // initialize random weights
        for (Neuron neuron : hiddenLayer) {
            ArrayList<Connection> connections = neuron.getAllInConnections();
            for (Connection conn : connections) {
                double newWeight = getRandom();
                conn.setWeight(newWeight);
            }
        }
        for (Neuron neuron : outputLayer) {
            ArrayList<Connection> connections = neuron.getAllInConnections();
            for (Connection conn : connections) {
                double newWeight = getRandom();
                conn.setWeight(newWeight);
            }
        }

        // reset id counters
        Neuron.counter = 0;
        Connection.counter = 0;

        if (isTrained) {
            trainedWeights();
            updateAllWeights();
        }
    }

    // random
    double getRandom() {

        return  (rand.nextDouble() * 2 - 1); // [-1;1[

    }

    /**
     * @param inputs There is equally many neurons in the input layer as there are
     *               in input variables
     */
    public void setInput(double inputs[]) {
        for (int i = 0; i < inputLayer.size(); i++) {
            inputLayer.get(i).setOutput(inputs[i]);
        }
    }

    public double[] getOutput() {
        double[] outputs = new double[outputLayer.size()];
        for (int i = 0; i < outputLayer.size(); i++)
            outputs[i] = outputLayer.get(i).getOutput();
        return outputs;
    }

    /**
     * Calculate the output of the neural network based on the input The forward
     * operation
     */
    public void activate() {
        for (Neuron n : hiddenLayer)
            n.calculateOutput();
        for (Neuron n : outputLayer)
            n.calculateOutput();
    }

    /**
     * all output propagate back
     *
     * @param expectedOutput first calculate the partial derivative of the error with
     *                       respect to each of the weight leading into the output neurons
     *                       bias is also updated here
     */
    public void applyBackpropagation(double expectedOutput[]) {

        // error check, normalize value ]0;1[
        for (int i = 0; i < expectedOutput.length; i++) {
            double d = expectedOutput[i];
            if (d < 0 || d > 1) {
                if (d < 0)
                    expectedOutput[i] = 0 + epsilon;
                else
                    expectedOutput[i] = 1 - epsilon;
            }
        }

        int i = 0;
        for (Neuron outputNeuron : outputLayer) {
            ArrayList<Connection> connections = outputNeuron.getAllInConnections();
            for (Connection con : connections) {
                double outputLayerNeuronOutput = outputNeuron.getOutput();
                double hiddenLayerNeuronOutput = con.leftNeuron.getOutput();
                double desiredOutput = expectedOutput[i];

                //  delta(o1) * out(h1)
                double partialDerivative =
                        -outputLayerNeuronOutput * (1.0 - outputLayerNeuronOutput) // Derivate of the sigmoid function
                                * hiddenLayerNeuronOutput   // ~ input
                                * (desiredOutput - outputLayerNeuronOutput); // looks like the error itself


                double deltaWeight = -learningRate * partialDerivative;

                double newWeight = con.getWeight() + deltaWeight;
                con.setDeltaWeight(deltaWeight);
                con.setWeight(newWeight + momentum * con.getPrevDeltaWeight());
            }
            i++;
        }

        // update weights for the hidden layer
        for (Neuron hiddelLayerNeuron : hiddenLayer) {
            ArrayList<Connection> connections = hiddelLayerNeuron.getAllInConnections();
            for (Connection con : connections) {

                double backpropagatedErrorFromOutputLayer = 0;
                int j = 0;
                for (Neuron outputLayerNeuron : outputLayer) {
                    double weightBetweenHiddenAndOutputLayer = outputLayerNeuron.getConnection(hiddelLayerNeuron.id).getPreviousWieght();
                    double desiredOutput = (double) expectedOutput[j];
                    double acutalOutput = outputLayerNeuron.getOutput();
                    j++;
                    backpropagatedErrorFromOutputLayer += (-(desiredOutput - acutalOutput)
                            * acutalOutput * (1 - acutalOutput)
                            * weightBetweenHiddenAndOutputLayer);
                }


                double hiddenLayerNeuronOutput = hiddelLayerNeuron.getOutput();
                double previousLayerOutput = con.leftNeuron.getOutput();

                double partialDerivative = hiddenLayerNeuronOutput * (1 - hiddenLayerNeuronOutput)
                        * previousLayerOutput
                        * backpropagatedErrorFromOutputLayer;

                double deltaWeight = -learningRate * partialDerivative;
                double newWeight = con.getWeight() + deltaWeight;
                con.setDeltaWeight(deltaWeight);
                con.setWeight(newWeight + momentum * con.getPrevDeltaWeight());
            }
        }
    }

    double run(int maxSteps, double minError) {
        int i;
        // Train neural network until minError reached or maxSteps exceeded
        double error = 1;
        for (i = 0; i < maxSteps && error > minError; i++) {
            error = 0;
            for (int p = 0; p < inputs.length; p++) {
                setInput(inputs[p]);

                activate();

                output = getOutput();
                resultOutputs[p] = output;

                for (int j = 0; j < expectedOutputs[p].length; j++) {
                    double err = 0.5 * Math.pow(output[j] - expectedOutputs[p][j], 2);
                    error += err;
                }

                applyBackpropagation(expectedOutputs[p]);
            }
        }

        printResult();

        System.out.println("Sum of squared errors = " + error);
        System.out.println("##### EPOCH " + i + "\n");
        if (i == maxSteps) {
            System.out.println("!Error training try again");
        } else {
            // printAllWeights();
            // printWeightUpdate();
        }

        return i;
    }

    void printResult() {
        System.out.println("NN example with xor training");
        for (int p = 0; p < inputs.length; p++) {
            System.out.print("INPUTS: ");
            for (int x = 0; x < layers[0]; x++) {
                System.out.print(inputs[p][x] + " ");
            }

            System.out.print("EXPECTED: ");
            for (int x = 0; x < layers[2]; x++) {
                System.out.print(expectedOutputs[p][x] + " ");
            }

            System.out.print("ACTUAL: ");
            for (int x = 0; x < layers[2]; x++) {
                System.out.print(resultOutputs[p][x] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    String weightKey(int neuronId, int conId) {
        return "N" + neuronId + "_C" + conId;
    }

    /**
     * Take from hash table and put into all weights
     */
    public void updateAllWeights() {
        // update weights for the output layer
        for (Neuron n : outputLayer) {
            ArrayList<Connection> connections = n.getAllInConnections();
            for (Connection con : connections) {
                String key = weightKey(n.id, con.id);
                double newWeight = weightUpdate.get(key);
                con.setWeight(newWeight);
            }
        }
        // update weights for the hidden layer
        for (Neuron n : hiddenLayer) {
            ArrayList<Connection> connections = n.getAllInConnections();
            for (Connection con : connections) {
                String key = weightKey(n.id, con.id);
                double newWeight = weightUpdate.get(key);
                con.setWeight(newWeight);
            }
        }
    }

    // trained data
    void trainedWeights() {
        weightUpdate.clear();

        weightUpdate.put(weightKey(3, 0), 0.4617563814065817);
        weightUpdate.put(weightKey(3, 1), -0.9877656354684774);
        weightUpdate.put(weightKey(3, 2), 0.9274095940464153);
        weightUpdate.put(weightKey(4, 3),-0.17983837701559668);
        weightUpdate.put(weightKey(4, 4), 0.8797307775638197);
        weightUpdate.put(weightKey(4, 5), 0.8943898353263877);
        weightUpdate.put(weightKey(5, 6), -0.5845703173805659);
        weightUpdate.put(weightKey(5, 7), 0.8741642977919393);
        weightUpdate.put(weightKey(5, 8), -0.2056513156305888);
        weightUpdate.put(weightKey(6, 9), -0.33456588808097765);
        weightUpdate.put(weightKey(6, 10), -0.3049639415937795);
        weightUpdate.put(weightKey(6, 11), -0.41188593599192647);
        weightUpdate.put(weightKey(7, 12), 0.9355118188482414);
        weightUpdate.put(weightKey(7, 13), 0.012967254652470173);
        weightUpdate.put(weightKey(7, 14), -0.7680658239346845);
        weightUpdate.put(weightKey(7, 15), 0.5410717601583555);
        weightUpdate.put(weightKey(7, 16), 0.31978541738683997);
    }

    public void printWeightUpdate() {
        System.out.println("printWeightUpdate, put this i trainedWeights() and set isTrained to true");
        // weights for the hidden layer
        for (Neuron n : hiddenLayer) {
            ArrayList<Connection> connections = n.getAllInConnections();
            for (Connection con : connections) {
                String w = df.format(con.getWeight());
                System.out.println("weightUpdate.put(weightKey(" + n.id + ", "
                        + con.id + "), " + w + ");");
            }
        }
        // weights for the output layer
        for (Neuron n : outputLayer) {
            ArrayList<Connection> connections = n.getAllInConnections();
            for (Connection con : connections) {
                String w = df.format(con.getWeight());
                System.out.println("weightUpdate.put(weightKey(" + n.id + ", "
                        + con.id + "), " + w + ");");
            }
        }
        System.out.println();
    }

    public void printAllWeights() {
        System.out.println("printAllWeights");
        // weights for the hidden layer
        for (Neuron n : hiddenLayer) {
            ArrayList<Connection> connections = n.getAllInConnections();
            for (Connection con : connections) {
                double w = con.getWeight();
                System.out.println("n=" + n.id + " c=" + con.id + " w=" + w);
            }
        }
        // weights for the output layer
        for (Neuron n : outputLayer) {
            ArrayList<Connection> connections = n.getAllInConnections();
            for (Connection con : connections) {
                double w = con.getWeight();
                System.out.println("n=" + n.id + " c=" + con.id + " w=" + w);
            }
        }
        System.out.println();
    }
}