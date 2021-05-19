package Simulation.RandomDistribution;

import java.util.Random;

public class SeedGenrator {
    private static Random rnd = new Random(10);

    public static long getseed(){
        return rnd.nextLong();
    }

    public static void newseed(int i){
        rnd = new Random(i);
    }
}
