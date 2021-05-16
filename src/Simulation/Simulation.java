/**
 *	Example program for using eventlists
 *	@author Joel Karel
 *	@version %I%, %G%
 */

package Simulation;

import Simulation.RandomDistribution.NormalDistribution;
import Simulation.RandomDistribution.Poisson;

public class Simulation {
    public static int seed = 10;
    public CEventList list;
    public Queue queue;
    public Source source;
    public Sink sink;
    public Machine mach;


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        new Simulation().basesetting();
    }

    Machine [] machines = new Machine[8];
    Queue [] queues = new Queue[8];

    public void basesetting(){
        CEventList l = new CEventList();
        Sink si = new Sink("Sink 1");


        for (int i=0; i < 8 ;i++){
            queues[i] = new Queue();
        }
        // TODO Implement the Nonstationary poisson process
        Source s1 = new Source(l,"Regular task", new Poisson(30));
        Source s2 = new Source(l,"GPU task", new Poisson(5*60));

        // The regular computation severs
        for (int i=0; i < 6; i++){
            machines[i] = new Machine(queues[i],si,l,"Regular computation severs",new NormalDistribution((2*60)+25, 42));
            s1.add(queues[i]);
        }

        for (int i=0; i < 2; i++){
            s1.add(queues[i+6]);
            s2.add(queues[i+6]);
            machines[i+6] = new Machine(queues[i+6],si,l,"GPUs severs",new NormalDistribution(4*60, 50));
        }
        // The GPU severs



        l.start(12*60); // 2000 is maximum time
        System.out.println();
    }
}