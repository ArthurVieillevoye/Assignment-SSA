package Simulation.RandomDistribution;

import java.util.Random;

public class UniformDistribution implements RandomDistribution{
    private final Random random;
    private double mean;

    public UniformDistribution(double mean, long seed) {
        this.mean = mean;
        this.random = new Random(seed);
    }

    public UniformDistribution(double mean) {
        this(mean, SeedGenrator.getseed());
    }

    @Override
    public double executeRandom() {
        // draw a [0,1] uniform distributed number
        double u = random.nextDouble();
        // Convert it into a exponentially distributed random variate with mean 33
        double res = -mean*Math.log(u);
        return res;
    }

    @Override
    public double getMean() {
        return mean;
    }
}
