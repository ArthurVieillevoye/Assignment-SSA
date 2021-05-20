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

public class Simulation {
    public static int seed = 10;
    public CEventList list;
    public Queue queue;
    public Source source;
    public Sink sink;
    public Machine mach;
    public static double tot = 0;


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        for (int i=0; i <10;i++) {
            SeedGenrator.newseed(i);
            new Simulation().basesetting();
        }
        System.out.println(tot/(10*4*24));
    }

    Machine [] machines = new Machine[8];
    Queue [] queues = new Queue[8];

    public void basesetting(){
        int time = 4*24*60;
        CEventList l = new CEventList();
        Sink si = new Sink("Sink 1");

        for (int i=0; i < 8 ;i++){
            queues[i] = new Queue();
        }
        // TODO Implement the Nonstationary poisson process

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
        tot += si.getEvents().length/3.;
        for(int i=0; i < si.getEvents().length; i+=3){
            System.out.println(si.getTimes()[i]+"     "+si.getEvents()[i]+"    "+ si.getStations()[i]);
        }
    }
}