package Simulation.RandomDistribution;

public class NonstationaryPoisson implements RandomDistribution{

    private final double mean;
    private final double amplitude;

    public NonstationaryPoisson(double mean, double amplitude) {
        this.mean = mean;
        this.amplitude = amplitude;
    }

    //TODO Implement the random generator
    @Override
    public double executeRandom() {
        return 0;
    }

    @Override
    public double getMean() {
        return mean;
    }
}
