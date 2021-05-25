/**
 *	Example program for using eventlists
 *	@author Joel Karel
 *	@version %I%, %G%
 */

package Simulation;

import Simulation.RandomDistribution.NonstationaryPoisson;
import Simulation.RandomDistribution.NormalDistribution;
import Simulation.RandomDistribution.Poisson;
import Simulation.RandomDistribution.SeedGenrator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Simulation {
    public static int seed = 10;
    public CEventList list;
    public Queue queue;
    public Source source;
    public Sink sink;
    public Machine mach;
    public static double tot = 0;
    public static FileWriter csvWriter;


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        csvWriter = new FileWriter("data.csv");
        for (int i=0; i <49;i++) {
            SeedGenrator.newseed(i);
            new Simulation().basesetting();
        }

        csvWriter.flush();
        csvWriter.close();
       // System.out.println(tot/(2*365*24));
    }

    Machine [] machines = new Machine[8];
    Queue [] queues = new Queue[8];

    public void basesetting() throws IOException {
        int time = (45*24*60);
        CEventList l = new CEventList();
        Sink si = new Sink("Sink 1");

        for (int i=0; i < 8 ;i++){
            queues[i] = new Queue();
        }
        
        Source s1 = new Source(l,"Regular task", new NonstationaryPoisson(30, .8, 24*60, time));
        Source s2 = new Source(l,"GPU task", new Poisson(2*60));

        // The GPU severs
        for (int i=0; i < 2; i++){
            s1.add(queues[i+6]);
            s2.add(queues[i+6]);
            machines[i+6] = new Machine(queues[i+6],si,l,"GPUs severs",new NormalDistribution(4*60, 50));
        }
        // The regular computation severs*/
        for (int i=0; i < 6; i++){
            machines[i] = new Machine(queues[i],si,l,"Regular computation severs",new NormalDistribution((2*60)+25, 42));
            s1.add(queues[i]);
        }

        l.start(time);
        ArrayList<String[]> data = new ArrayList<>();
        for(int i = 0; i < si.getEvents().length; i++){
            data.add(new String[]{si.getStations()[i], si.getEvents()[i], String.valueOf(si.getTimes()[i])});
        }
        for (String [] rowData : data) {
            csvWriter.append(String.join(",", new ArrayList<>(Arrays.asList(rowData))));
            csvWriter.append("\n");
        }
    }
}