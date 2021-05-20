package Simulation.RandomDistribution;

import java.util.ArrayList;
import java.util.Random;

public class NonstationaryPoisson implements RandomDistribution{

    private Random random;
    private final double mean;
    private final double amplitude;
    private final double period;
    private final double time;
    private Double [] interarrival;
    private int index;

    public NonstationaryPoisson(double mean, double amplitude, double period, double time){
        this(mean, amplitude, period, time, SeedGenrator.getseed());
    }

    public NonstationaryPoisson(double mean, double amplitude, double period, double time, long seed) {
        this.mean = mean;
        this.amplitude = amplitude;
        this.period = period;
        this.random = new Random(seed);
        this.time = time;
        generateInterval(time);
    }

    public Double[] getInterarrival() {
        return interarrival;
    }

    private void generateInterval(double T){
        index = 0;
        double max = amplitude*2;
        int m = 0;
        int n = 0;
        ArrayList<Double> s = new ArrayList<>();
        ArrayList<Double> t = new ArrayList<>();
        s.add(0.);
        t.add(0.);
        while(s.get(m) <= T){
            double w = -Math.log(random.nextDouble())/max;
            s.add(s.get(m)+w);
            double d = random.nextDouble();
            if(d <= formula(s.get(m+1))/(max*24)){
                t.add(s.get(m+1));
            }
            m+=1;
        }
        t.remove(0);
        interarrival = new Double[t.size()];
        for(int i=0; i < t.size() - 1; i++){
            interarrival[t.size() - (1+i)] = t.get(t.size() - (1+i)) - t.get(t.size() - (2+i));
        }
        interarrival[0] = 0.;
        //interarrival = t.toArray(interarrival);
    }

    private double formula(double x){
        return (amplitude* Math.sin(x * ((2*Math.PI)/period)))+amplitude;
    }

    //TODO Implement the random generator
    @Override
    public double executeRandom() {
//        System.out.println(index + " vs "+interarrival.length);
        if(index == interarrival.length) return time;
        return interarrival[index++];
    }

    @Override
    public double getMean() {
        return mean;
    }
}
