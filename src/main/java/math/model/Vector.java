package math.model;

public class Vector {

    private final double[] vector;

    public Vector(double[] vector) {
        this.vector = vector;
    }

    public Vector(int size) {
        this.vector = new double[size];
    }

    public double[] getVector() {
        return vector;
    }
}
