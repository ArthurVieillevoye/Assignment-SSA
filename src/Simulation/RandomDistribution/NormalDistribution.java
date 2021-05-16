package Simulation.RandomDistribution;

import java.util.Random;

public class NormalDistribution implements RandomDistribution{
    private final Random random;
    private final double mean;
    private final double standardDeviation;

    public NormalDistribution(double mean, double standardDeviation, long seed) {
        this.mean = mean;
        this.standardDeviation = standardDeviation;
        this.random = new Random(seed);
    }

    public NormalDistribution(double mean, double standardDeviation) {
        this(mean, standardDeviation, SeedGenrator.getseed());
    }

    //TODO Verify
    @Override
    public double executeRandom() {
        double results = random.nextGaussian()*standardDeviation+mean;
        if(results < 1) {
            return 1;
        }
        return results;
    }

    @Override
    public double getMean() {
        return mean;
    }
}
