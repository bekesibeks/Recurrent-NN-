package network.data;

import java.util.ArrayList;
import java.util.List;

public class TrainingSample {

    private final List<Double> inputs;
    private final List<Double> expectedOutputs;

    public TrainingSample() {
        inputs = new ArrayList<>();
        expectedOutputs = new ArrayList<>();
    }

    public TrainingSample(List<Double> inputs, List<Double> expectedOutputs) {
        this.inputs = inputs;
        this.expectedOutputs = expectedOutputs;
    }

    public Double getInput(int index) {
        return inputs.get(index);
    }

    public Double getOutput(int index) {
        return expectedOutputs.get(index);
    }

    public int getOutputSize() {
        return expectedOutputs.size();
    }

    public int getInputSize() {
        return inputs.size();
    }

    public List<Double> getInputs() {
        return inputs;
    }

    public List<Double> getExpectedOutputs() {
        return expectedOutputs;
    }
}
