package network.data;

import java.util.ArrayList;
import java.util.List;

public class BatchTrainingSample {

    private final List<TrainingSample> trainingSamples;

    public BatchTrainingSample() {
        this.trainingSamples = new ArrayList<>();
    }

    public BatchTrainingSample(List<TrainingSample> trainingSamples) {
        this.trainingSamples = trainingSamples;
    }

    public List<TrainingSample> getTrainingSamples() {
        return trainingSamples;
    }
}
