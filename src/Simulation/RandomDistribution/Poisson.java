package Simulation.RandomDistribution;

import java.util.Random;

public class Poisson implements RandomDistribution{
    private final Random random;
    private final double mean;

    public Poisson(double mean, long seed) {
        this.mean = mean;
        this.random = new Random(seed);
    }

    public Poisson(double mean) {
        this(mean, SeedGenrator.getseed());
    }

    //TODO Implement the random generator
    @Override
    public double executeRandom() {
        double r = 0;
        double a = random.nextDouble();
        double p = Math.exp(-mean);

        while (a > p) {
            r += 1;
            a = a - p;
            p = p * mean / r;
        }
        return r;
    }

    @Override
    public double getMean() {
        return mean;
    }
}
